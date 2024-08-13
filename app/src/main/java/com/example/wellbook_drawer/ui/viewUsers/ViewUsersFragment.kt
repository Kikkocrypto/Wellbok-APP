package com.example.wellbook_drawer.ui.viewUsers

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wellbook_drawer.Database.Database_wellApp
import com.example.wellbook_drawer.Database.UserDB.User
import com.example.wellbook_drawer.Database.UserDB.UserDAO
import com.example.wellbook_drawer.R
import com.example.wellbook_drawer.databinding.FragmentAggiungiutenteBinding
import com.example.wellbook_drawer.placeholder.PlaceholderContent
import com.example.wellbook_drawer.ui.aggiungiUtente.AddUser_ViewModel
import com.example.wellbook_drawer.ui.aggiungiUtente.UserRecyclerViewAdapter
import com.example.wellbook_drawer.ui.aggiungiUtente.UserVMFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A fragment representing a list of Items.
 */
class ViewUsersFragment : Fragment() {
    private lateinit var userDao: UserDAO
    private lateinit var userRecyclerViewAdapter: UserRecyclerViewAdapter
    private lateinit var viewModel: AddUser_ViewModel

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        val userDatabase = Database_wellApp.getInstance(requireContext())
        userDao = userDatabase.userDAO()

        val factory = UserVMFactory(userDao)
        viewModel = ViewModelProvider(this, factory).get(AddUser_ViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_users_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.users_list)

        userRecyclerViewAdapter = UserRecyclerViewAdapter(
            clickListener = { user ->
                Toast.makeText(context, "Clicked: ${user.name}", Toast.LENGTH_SHORT).show()
            },
            deleteClickListener = { user ->
                deleteUser(user)
            },
            editClickListener = { user ->
                showEditDialog(user)
            }
        )

        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        recyclerView.adapter = userRecyclerViewAdapter

        loadUsersFromDB()

        return view
    }


    private fun loadUsersFromDB() {
        userDao.getAllUsers().observe(viewLifecycleOwner) { users ->
            userRecyclerViewAdapter.setList(users)
        }
    }

    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                userDao.deleteUser(user)
            }
            loadUsersFromDB() // Refresh the list after deletion
        }
    }

    private fun updateUser(user: User) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                userDao.updateUser(user)
            }
            loadUsersFromDB()
        }
    }


    private fun showEditDialog(user: User) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_user, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etLastName = dialogView.findViewById<EditText>(R.id.etLastName)
        val etPhone = dialogView.findViewById<EditText>(R.id.etPhone)
        val btnEdit = dialogView.findViewById<Button>(R.id.btnEdit)
        val btnDismiss = dialogView.findViewById<Button>(R.id.btnDismiss)

        etName.setText(user.name)
        etLastName.setText(user.lastName)
        etPhone.setText(user.phone)


        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Modifica Utente")
        val dialog = dialogBuilder.create()
        dialog.show()

        btnEdit.setOnClickListener {
            val updatedUser = user.copy(
                name = etName.text.toString().uppercase(),
                lastName = etLastName.text.toString().uppercase(),
                phone = etPhone.text.toString()
            )
            updateUser(updatedUser)
            dialog.dismiss()
        }

        btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ViewUsersFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }


}
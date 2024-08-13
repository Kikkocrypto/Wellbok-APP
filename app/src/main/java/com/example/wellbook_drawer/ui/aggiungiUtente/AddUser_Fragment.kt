package com.example.wellbook_drawer.ui.aggiungiUtente

import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.compose.ui.window.application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wellbook_drawer.Database.Database_wellApp
import com.example.wellbook_drawer.Database.UserDB.User
import com.example.wellbook_drawer.R
import com.example.wellbook_drawer.databinding.FragmentAggiungiutenteBinding

class AddUser_Fragment : Fragment() {

    private var _binding: FragmentAggiungiutenteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView
    private val binding get() = _binding!!
    private var viewModel: AddUser_ViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val dao = Database_wellApp.getInstance(requireContext()).userDAO()
        val factory = UserVMFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(AddUser_ViewModel::class.java)


        _binding = FragmentAggiungiutenteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // inserisci utente
        binding.apply{
            btnSend.setOnClickListener{
                saveUser()
                val navController = findNavController()
                navController.navigate(R.id.view_Users)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


     // function used to save user in database
    private fun saveUser() {
         binding.apply {
             val name = etNome.text.toString().uppercase()
             val lastName = etCognome.text.toString().uppercase()
             val phone = etTelefono.text.toString().uppercase()

             if (isPhoneNumberValid(phone)) {
                 val user = User(
                     0,
                     name,
                     lastName,
                     phone
                 )
                 viewModel?.addUser(user)
                 Toast.makeText(requireContext(), "Utente inserito", Toast.LENGTH_SHORT).show()
                 etNome.text.clear()
                 etCognome.text.clear()
                 etTelefono.text.clear()
             }else {
                 Toast.makeText(requireContext(), "Numero di telefono non valido", Toast.LENGTH_SHORT).show()
                 etTelefono.text.clear()
                 return
             }
         }
     }

    private fun isPhoneNumberValid(phone: String): Boolean {
        return phone.length == 10
    }


}
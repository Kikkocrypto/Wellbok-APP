package com.example.wellbook_drawer.ui.aggiungiUtente

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.wellbook_drawer.Database.UserDB.User
import com.example.wellbook_drawer.R
import com.example.wellbook_drawer.databinding.FragmentViewUsersBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserRecyclerViewAdapter(private val clickListener: (User) -> Unit,
    private val deleteClickListener: (User) -> Unit,
    private val editClickListener: (User) -> Unit): RecyclerView.Adapter<UserViewHolder>() {

    private val users_list = ArrayList<User>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val binding = FragmentViewUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return users_list.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // collega i dati alle view del ViewHolder e gestisce i clic sui singoli elementi della lista
        holder.bind(users_list[position], clickListener, deleteClickListener, editClickListener)
    }

    // aggiorna la lista di utenti e notifica l'adapter della modifica
    fun setList(users: List<User>){
        users_list.clear()
        users_list.addAll(users)
        notifyDataSetChanged()
    }
}


// ViewHolder per ogni elemento della lista di utenti nella RecyclerView
class UserViewHolder(private val binding: FragmentViewUsersBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(user: User, clickListener: (User) -> Unit,
             deleteClickListener: (User) -> Unit,
             editClickListener: (User) -> Unit) {

        binding.apply {
            tvName.text = user.name
            tvLastName.text = user.lastName
            tvPhone.text = user.phone

            // gestisce il clic su un elemento della lista
            fabDelete.setOnClickListener { deleteClickListener(user) }
            fabEdit.setOnClickListener { editClickListener(user) }

            // Gestione del click su tutto l'elemento
            root.setOnClickListener { clickListener(user) }
        }
    }
}
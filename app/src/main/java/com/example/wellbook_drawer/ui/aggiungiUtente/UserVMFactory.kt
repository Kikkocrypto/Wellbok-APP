package com.example.wellbook_drawer.ui.aggiungiUtente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wellbook_drawer.Database.UserDB.UserDAO

class UserVMFactory (private val dao: UserDAO): ViewModelProvider.Factory{
    // crea un oggetto PersonaViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddUser_ViewModel::class.java)){ // se il viewModel è PersonaVieweModel, viene creato un viewModel con i dati DAO
            return AddUser_ViewModel(dao) as T
        }
        throw  IllegalArgumentException("Unknown View Model Class")
    }

}
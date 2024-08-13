package com.example.wellbook_drawer.ui.aggiungiPrenotazione

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wellbook_drawer.Database.ReservationDB.ReservationDAO
import com.example.wellbook_drawer.Database.UserDB.UserDAO
import com.example.wellbook_drawer.ui.aggiungiUtente.AddUser_ViewModel

class ReservationVMFactory (private val dao: ReservationDAO): ViewModelProvider.Factory{
    // crea un oggetto PersonaViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddReservation_ViewModel::class.java)){ // se il viewModel è PersonaVieweModel, viene creato un viewModel con i dati DAO
            return AddReservation_ViewModel(dao) as T
        }
        throw  IllegalArgumentException("Unknown View Model Class")
    }

}
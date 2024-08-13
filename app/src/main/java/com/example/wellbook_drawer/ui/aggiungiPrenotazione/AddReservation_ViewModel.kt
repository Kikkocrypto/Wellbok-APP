package com.example.wellbook_drawer.ui.aggiungiPrenotazione

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellbook_drawer.Database.ReservationDB.Reservation
import com.example.wellbook_drawer.Database.ReservationDB.ReservationDAO
import com.example.wellbook_drawer.Database.UserAndReservation
import com.example.wellbook_drawer.Database.UserDB.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddReservation_ViewModel(private val dao: ReservationDAO) : ViewModel() {


    fun addReservation(reservation: Reservation) = viewModelScope.launch{
        withContext(Dispatchers.IO){
            dao.insertReservation(reservation)
        }
    }

    fun updateReservation(reservation: Reservation) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            dao.updateReservation(reservation)
        }
    }

    fun deleteReservation(reservation: Reservation) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            dao.deleteReservation(reservation)
        }
    }

    fun getAllReservations(): LiveData<List<Reservation>> {
        return dao.getAllReservations()
    }

    fun getAllUsers(): LiveData<List<User>> {
        return dao.getAllUsers()
    }

    fun getAllReservationsWithUsers(): LiveData<List<UserAndReservation>> {
        return dao.getUserWithReservation()
    }

}
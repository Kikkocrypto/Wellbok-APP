package com.example.wellbook_drawer.Database.ReservationDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.wellbook_drawer.Database.UserAndReservation
import com.example.wellbook_drawer.Database.UserDB.User

@Dao
interface ReservationDAO {

    @Insert
    suspend fun insertReservation(reservation: Reservation)

    @Update
    suspend fun updateReservation(reservation: Reservation)

    @Delete
    suspend fun deleteReservation(reservation: Reservation)

    @Query("SELECT * FROM reservation_table")
    fun getAllReservations(): LiveData<List<Reservation>>

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<User>>

    @Transaction
    @Query("SELECT * FROM reservation_table")
    fun getUserWithReservation(): LiveData<List<UserAndReservation>>

}
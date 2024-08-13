package com.example.wellbook_drawer.Database

import androidx.room.Embedded
import androidx.room.Relation
import com.example.wellbook_drawer.Database.ReservationDB.Reservation
import com.example.wellbook_drawer.Database.UserDB.User

data class UserAndReservation(
    @Embedded val reservation: Reservation,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    val user: User
)


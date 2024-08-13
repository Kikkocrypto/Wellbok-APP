package com.example.wellbook_drawer.Database.ReservationDB

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.wellbook_drawer.Database.UserDB.User

@Entity(
    tableName = "reservation_table",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["user_id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reservation_id")
    val id: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "reservation_startTime")
    val startTtime: String,
    @ColumnInfo(name = "reservation_finishTime")
    val finishTtime: String,
    @ColumnInfo(name = "reservation_day")
    val day: String
)

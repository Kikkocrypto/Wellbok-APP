package com.example.wellbook_drawer.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wellbook_drawer.Database.ReservationDB.Reservation
import com.example.wellbook_drawer.Database.ReservationDB.ReservationDAO
import com.example.wellbook_drawer.Database.UserDB.User
import com.example.wellbook_drawer.Database.UserDB.UserDAO

@Database(entities = [User::class, Reservation:: class], version = 2, exportSchema = false)
abstract class  Database_wellApp : RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun reservationDAO(): ReservationDAO

    companion object{
        @Volatile
        private var INSTANCE: Database_wellApp? = null

        fun getInstance(contex: Context):Database_wellApp {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        contex.applicationContext,
                        Database_wellApp::class.java,
                        name = "wellApp_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }

    }


}
package com.example.wellbook_drawer.Database.UserDB
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table",
        indices = [Index(value = ["user_name", "user_lastName"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Int,
    @ColumnInfo(name = "user_name")
    var name: String,
    @ColumnInfo(name = "user_lastName")
    var lastName: String,
    @ColumnInfo(name = "user_phone")
    var phone: String
)

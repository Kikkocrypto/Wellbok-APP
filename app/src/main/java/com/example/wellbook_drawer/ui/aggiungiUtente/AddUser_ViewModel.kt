package com.example.wellbook_drawer.ui.aggiungiUtente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellbook_drawer.Database.UserDB.User
import com.example.wellbook_drawer.Database.UserDB.UserDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddUser_ViewModel(private val dao: UserDAO) : ViewModel() {

    val user_list = dao.getAllUsers()

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is gallery Fragment"
//    }
//    val text: LiveData<String> = _text


    fun addUser(user: User) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            dao.insertUser(user)
        }
    }

    fun updateUser(user: User) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            dao.updateUser(user)
        }
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            dao.deleteUser(user)
        }
    }



}
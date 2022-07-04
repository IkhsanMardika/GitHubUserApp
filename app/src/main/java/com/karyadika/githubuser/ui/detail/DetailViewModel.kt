package com.karyadika.githubuser.ui.detail


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.karyadika.githubuser.data.UserRepository
import com.karyadika.githubuser.data.model.Users
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    suspend fun getDetailUsers(username: String) = repository.getDetailUsers(username)

    fun saveAsFavorite(user: Users) = viewModelScope.launch {
        repository.saveAsFavorite(user)
    }

    fun deleteFromFavorite(user: Users) = viewModelScope.launch {
        repository.deleteFromFavorite(user)
    }

}
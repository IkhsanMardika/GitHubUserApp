package com.karyadika.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.karyadika.githubuser.data.UserRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    suspend fun getAllFavoriteUsers() = repository.getAllFavoriteList()

}
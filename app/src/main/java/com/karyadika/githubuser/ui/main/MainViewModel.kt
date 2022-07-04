package com.karyadika.githubuser.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.karyadika.githubuser.data.UserRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    fun searchUsers(query: String) = repository.searchUsers(query)

}


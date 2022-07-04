package com.karyadika.githubuser.ui.follow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.karyadika.githubuser.data.UserRepository

class FollowersViewModel(application: Application) : AndroidViewModel(application)  {

    private val repository = UserRepository(application)

    fun getUserFollowers(username: String) = repository.getFollowers(username)
}
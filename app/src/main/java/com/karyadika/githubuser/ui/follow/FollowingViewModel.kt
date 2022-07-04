package com.karyadika.githubuser.ui.follow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.karyadika.githubuser.data.UserRepository

class FollowingViewModel(application: Application) : AndroidViewModel(application)  {

    private val repository = UserRepository(application)

    fun getUserFollowing(username: String) = repository.getFollowing(username)

}
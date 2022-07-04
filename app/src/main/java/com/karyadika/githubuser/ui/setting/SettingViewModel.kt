package com.karyadika.githubuser.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.karyadika.githubuser.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    fun saveThemeSetting(darkModeState: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(darkModeState)
        }
    }

    fun getThemeSetting() = repository.getThemeSetting().asLiveData(Dispatchers.IO)

}
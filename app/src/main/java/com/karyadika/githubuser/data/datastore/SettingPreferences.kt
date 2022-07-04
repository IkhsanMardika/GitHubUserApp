package com.karyadika.githubuser.data.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences (private val context: Context){

    private val Context.userPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATA_STORE_NAME
    )

    suspend fun saveThemeSetting(darkModeState: Boolean) {
        context.userPreferenceDataStore.edit { preferences ->
            preferences[THEME_KEY] = darkModeState
        }
    }

    fun getThemeSetting(): Flow<Boolean> =
        context.userPreferenceDataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }


    companion object {
        const val DATA_STORE_NAME = "USER_DATASTORE"
        private val THEME_KEY = booleanPreferencesKey("theme_setting")

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SettingPreferences? = null
        fun getInstance(context: Context): SettingPreferences =
            INSTANCE ?: synchronized(this){
                val instance = INSTANCE ?: SettingPreferences(context).also { INSTANCE = it }
                instance
            }
    }

}
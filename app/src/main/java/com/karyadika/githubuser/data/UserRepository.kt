package com.karyadika.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karyadika.githubuser.BuildConfig
import com.karyadika.githubuser.data.datastore.SettingPreferences
import com.karyadika.githubuser.data.local.UserDao
import com.karyadika.githubuser.data.local.UserDatabase
import com.karyadika.githubuser.data.model.Users
import com.karyadika.githubuser.data.remote.response.UsersItemResponse
import com.karyadika.githubuser.data.remote.retrofit.ApiConfig
import com.karyadika.githubuser.data.remote.retrofit.ApiService
import com.karyadika.githubuser.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository (private val application: Application) {

    private val apiService: ApiService
    private val userDao: UserDao
    private val preferences: SettingPreferences

    init {
        apiService = ApiConfig.getApiService()
        val db: UserDatabase = UserDatabase.getDatabase(application)
        userDao = db.userDao()
        preferences = SettingPreferences.getInstance(application)
    }

    //Remote
    fun searchUsers(query: String): LiveData<Result<List<Users>>> {
        val listUser = MutableLiveData<Result<List<Users>>>()

        listUser.postValue(Result.Loading())
        apiService.getUsersData(API_KEY, query).enqueue(object : Callback<UsersItemResponse>{
            override fun onResponse(call: Call<UsersItemResponse>, response: Response<UsersItemResponse>) {
                val list = response.body()?.items
                if (list.isNullOrEmpty()) {
                    listUser.postValue(Result.Error(null))
                } else {
                    listUser.postValue(Result.Success(list))
                }
            }
            override fun onFailure(call: Call<UsersItemResponse>, t: Throwable) {
                listUser.postValue(Result.Error(t.message))
            }
        })
        return listUser
    }

    suspend fun getDetailUsers(username: String): LiveData<Result<Users>>{
        val userDetail = MutableLiveData<Result<Users>>()
        if (userDao.getFavoriteUser(username) != null) {
            userDetail.postValue(Result.Success(userDao.getFavoriteUser(username)))
        } else {
            apiService.getUsersDetail(API_KEY, username).enqueue(object : Callback<Users>{
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    val result = response.body()
                    userDetail.postValue(Result.Success(result))
                }
                override fun onFailure(call: Call<Users>, t: Throwable) {
                    userDetail.postValue(Result.Error(t.message))
                }
            })
        }
        return userDetail
    }

    fun getFollowing(username: String) : LiveData<Result<List<Users>>>{
        val listUser = MutableLiveData<Result<List<Users>>>()
        listUser.postValue(Result.Loading())
        apiService.getFollowing(API_KEY, username).enqueue(object : Callback<List<Users>>{
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                val list = response.body()
                if (list.isNullOrEmpty()){
                    listUser.postValue(Result.Error(null))
                } else {
                    listUser.postValue(Result.Success(list))
                }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                listUser.postValue(Result.Error(t.message))
            }
        })
        return listUser
    }

    fun getFollowers(username: String) : LiveData<Result<List<Users>>> {
        val listUsers = MutableLiveData<Result<List<Users>>>()

        listUsers.postValue(Result.Loading())
        apiService.getFollowers(API_KEY, username).enqueue(object : Callback<List<Users>>{
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    listUsers.postValue(Result.Error(null))
                } else {
                    listUsers.postValue(Result.Success(list))
                }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                listUsers.postValue(Result.Error(t.message))
            }
        })

        return listUsers
    }

    //Local
    suspend fun getAllFavoriteList(): LiveData<Result<List<Users>>> {
        val listFavorite = MutableLiveData<Result<List<Users>>>()
        listFavorite.postValue(Result.Loading())
        if (userDao.getAllFavoriteListUser().isNullOrEmpty()) {
            listFavorite.postValue(Result.Error(null))
        } else {
            listFavorite.postValue(Result.Success(userDao.getAllFavoriteListUser()))
        }
        return listFavorite
    }

    suspend fun saveAsFavorite(user: Users) = userDao.insert(user)

    suspend fun deleteFromFavorite(user: Users) = userDao.delete(user)

    //DataStore
    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        preferences.saveThemeSetting(isDarkModeActive)
    }

    fun getThemeSetting() = preferences.getThemeSetting()


    companion object {
        const val API_KEY = "Bearer ${BuildConfig.API_KEY}"
    }

}
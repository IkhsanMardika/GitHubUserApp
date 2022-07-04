package com.karyadika.githubuser.data.remote.retrofit

import com.karyadika.githubuser.data.model.Users
import com.karyadika.githubuser.data.remote.response.UsersItemResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("search/users?")
    fun getUsersData(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Call<UsersItemResponse>

    @GET("users/{username}")
    fun getUsersDetail(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<Users>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<List<Users>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<List<Users>>

}
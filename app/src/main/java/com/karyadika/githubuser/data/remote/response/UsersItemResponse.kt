package com.karyadika.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName
import com.karyadika.githubuser.data.model.Users

data class UsersItemResponse(
    @field:SerializedName("items")
    val items: List<Users>
)


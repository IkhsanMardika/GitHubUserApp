package com.karyadika.githubuser.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "data_users")
data class Users(

    @field:SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int? = 0,

    @field:SerializedName("login")
    @ColumnInfo(name = "username")
    val username: String? = "",

    @field:SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String? = "",

    @field:SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = "",

    @field:SerializedName("html_url")
    @ColumnInfo(name = "html_url")
    var htmlUrl: String? = "",

    @field:SerializedName("company")
    @ColumnInfo(name = "company")
    val company: String? = "",

    @field:SerializedName("location")
    @ColumnInfo(name = "location")
    val location: String? = "",

    @field:SerializedName("followers_url")
    @ColumnInfo(name = "followers_url")
    val followersUrl: String,

    @field:SerializedName("following_url")
    @ColumnInfo(name = "following_url")
    val followingUrl: String,

    @field:SerializedName("created_at")
    @ColumnInfo(name = "create")
    val createdAt: String? = "",

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean? = false

)














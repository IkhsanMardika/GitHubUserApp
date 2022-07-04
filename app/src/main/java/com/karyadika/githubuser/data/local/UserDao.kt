package com.karyadika.githubuser.data.local

import androidx.room.*
import com.karyadika.githubuser.data.model.Users

@Dao
interface UserDao {

    @Query("SELECT * FROM data_users ORDER BY username ASC")
    suspend fun getAllFavoriteListUser(): List<Users>

    @Query("SELECT * FROM data_users WHERE username = :username")
    suspend fun getFavoriteUser(username: String): Users?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: Users)

    @Delete
    suspend fun delete(users: Users)

}




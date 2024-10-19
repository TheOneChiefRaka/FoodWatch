package com.example.foodwatch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.foodwatch.data.entities.User

@Dao
interface UserDao {

    // Add user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    // Get by ID
    @Query("SELECT * FROM users WHERE pkUserId = :userId")
    suspend fun getUserByID(userId: Long): User?

    // Delete user
    @Delete
    suspend fun delete(user: User)
}
package com.example.foodwatch.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.foodwatch.database.entities.Reaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ReactionDao {
    @Query("SELECT * FROM reaction")
    fun getAll(): Flow<List<Reaction>>

    @Query("SELECT * FROM reaction WHERE date LIKE :date")
    suspend fun findReactionsByDate(date: String): List<Reaction>

    @Insert
    suspend fun insert(reaction: Reaction)

    @Delete
    suspend fun delete(reaction: Reaction)
}
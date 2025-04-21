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

    @Query("SELECT * FROM reaction WHERE reactionTime >= :reactionDate || \" 00:00\" AND reactionTime <= :reactionDate || \" 23:59\" ORDER BY reactionTime")
    suspend fun findReactionsByDate(reactionDate: String): List<Reaction>

    @Query("SELECT * FROM reaction WHERE :min <= reactionTime AND reactionTime <= :max")
    suspend fun findReactionsByTimeRange(min: String, max: String): List<Reaction>

    @Query("UPDATE reaction SET reactionTime = :reactionTime, severity = :severity WHERE reactionId = :reactionId")
    suspend fun updateReactionById(reactionTime: String, severity: String, reactionId: Int)

    @Query("SELECT * FROM reaction WHERE reactionId = :id")
    suspend fun findReactionById(id: Int): Reaction

    @Insert
    suspend fun insert(reaction: Reaction): Long

    @Delete
    suspend fun delete(reaction: Reaction)
}
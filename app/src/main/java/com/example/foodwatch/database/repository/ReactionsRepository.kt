package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import com.example.foodwatch.database.dao.ReactionDao
import com.example.foodwatch.database.entities.Reaction
import kotlinx.coroutines.flow.Flow

class ReactionsRepository(private val reactionDao: ReactionDao) {
    val allReactions: Flow<List<Reaction>> = reactionDao.getAll()

    @WorkerThread
    suspend fun findReactionsByDate(date: String): List<Reaction> {
        return reactionDao.findReactionsByDate(date)
    }

    @WorkerThread
    suspend fun findReactionsByTimeRange(min: String, max: String): List<Reaction> {
        return reactionDao.findReactionsByTimeRange(min, max)
    }

    @WorkerThread
    suspend fun insert(reaction: Reaction): Long {
        return reactionDao.insert(reaction)
    }
}
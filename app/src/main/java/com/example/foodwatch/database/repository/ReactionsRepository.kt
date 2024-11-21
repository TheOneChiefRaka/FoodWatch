package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import com.example.foodwatch.database.dao.ReactionDao
import com.example.foodwatch.database.entities.Reaction
import kotlinx.coroutines.flow.Flow

class ReactionsRepository(private val reactionDao: ReactionDao) {
    val allReactions: Flow<List<Reaction>> = reactionDao.getAll()

    @WorkerThread
    suspend fun findReactionsByTime(time: String): List<Reaction> {
        return reactionDao.findReactionsByTime(time)
    }

    @WorkerThread
    suspend fun insert(reaction: Reaction) {
        reactionDao.insert(reaction)
    }
}
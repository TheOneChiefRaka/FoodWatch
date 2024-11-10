package com.example.foodwatch

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ReactionsRepository(private val reactionDao: ReactionDao) {
    val allReactions: Flow<List<Reaction>> = reactionDao.getAll()

    @WorkerThread
    suspend fun findReactionsByDate(date: String): List<Reaction> {
        return reactionDao.findReactionsByDate(date)
    }

    @WorkerThread
    suspend fun insert(reaction: Reaction) {
        reactionDao.insert(reaction)
    }
}
package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import com.example.foodwatch.database.dao.ReactionDao
import com.example.foodwatch.database.entities.Reaction
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

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
    suspend fun findReactionsByYearMonth(yearMonth: YearMonth): List<List<Reaction>> {
        var monthString = yearMonth.month.value.toString()
        if(yearMonth.month.value < 10)
            monthString = "0${yearMonth.month.value}"
        val yearMonthString = "${yearMonth.year}-${monthString}"

        val reactions = findReactionsByTimeRange("$yearMonthString-01", "$yearMonthString-31")
        val reactionsList = mutableListOf<MutableList<Reaction>>()
        for(i in 0..30)
            reactionsList.add(mutableListOf())
        //count meals eaten on a given day
        for(reaction in reactions) {
            reactionsList[reaction.reactionTime.slice(8..9).toInt()-1].add(reaction)
        }
        return reactionsList
    }

    @WorkerThread
    suspend fun updateReactionById(reactionTime: String, severity: String, reactionId: Int) {
        return reactionDao.updateReactionById(reactionTime, severity, reactionId)
    }

    @WorkerThread
    suspend fun findReactionById(id: Int): Reaction {
        return reactionDao.findReactionById(id)
    }

    @WorkerThread
    suspend fun deleteReaction(reaction: Reaction) {
        return reactionDao.delete(reaction)
    }

    @WorkerThread
    suspend fun insert(reaction: Reaction): Long {
        return reactionDao.insert(reaction)
    }

/*
    @WorkerThread
    suspend fun insert(reaction: Reaction) {
        reactionDao.insert(reaction)
    }

    @WorkerThread
    suspend fun delete(reactionId: Int) {
        reactionDao.delete(reactionId)
    }

    @WorkerThread
    fun findReactionsByDateRange(startDate: String, endDate: String): List<Reaction> {
        return reactionDao.findReactionsByDateRange(startDate, endDate)
    }

    @WorkerThread
    suspend fun insert(reaction: Reaction) {
        reactionDao.insert(reaction)
    }
*/
}
package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.map
//import androidx.paging.filter
//import androidx.paging.map
import com.example.foodwatch.database.dao.ReactionDao
import com.example.foodwatch.database.entities.Reaction
import com.example.foodwatch.database.entities.ReactionIngredientResult
import com.example.foodwatch.database.entities.ReactionWithIngredients
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import kotlinx.coroutines.flow.map

class ReactionsRepository(private val reactionDao: ReactionDao) {
    val allReactionsWithoutIngredients: Flow<List<Reaction>> = reactionDao.getAll()

    fun getAllReactionsWithIngredients(): LiveData<List<ReactionWithIngredients>> {
        return reactionDao.getAllReactionsWithIngredients()
    }

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

        for(i in 0..30) {
            reactionsList.add(mutableListOf())
        }

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

    fun getIngredientReactionCountsForReaction(reactionId: Int): Map<String, Int> {
        return reactionDao.getIngredientReactionCountsForReaction(reactionId)
            .associate { it.ingredientName to it.count }
    }

    fun getAllReactions(): Flow<List<ReactionWithIngredients>> {
        return reactionDao.getAllReactions().map { rawResults ->
            // Process the raw results into ReactionWithIngredients
            val groupedByReaction = rawResults.groupBy { it.reactionId }

            groupedByReaction.map { (_, reactions) ->
                val severity = reactions.first().severity ?: ""
                val ingredientCounts = reactions
                    .filter { it.ingredientName != null }
                    .groupBy { it.ingredientName!! }
                    .mapValues { it.value.size }

                ReactionWithIngredients(severity, ingredientCounts)
            }
        }
    }

    fun getReactionsWithIngredientsByDate(date: String): Flow<List<ReactionWithIngredients>> {
        return reactionDao.getReactionsWithIngredientsByDate(date).map { rawResults ->
            // Process the raw results into ReactionWithIngredients
            val groupedByReaction = rawResults.groupBy { it.reactionId }

            groupedByReaction.map { (_, reactions) ->
                val severity = reactions.first().severity ?: ""
                val ingredientCounts = reactions
                    .filter { it.ingredientName != null }
                    .groupBy { it.ingredientName!! }
                    .mapValues { it.value.size }

                ReactionWithIngredients(severity, ingredientCounts)
            }
        }
    }

    private fun transformToReactionWithIngredients(reactionIngredientResults: List<ReactionIngredientResult>): List<ReactionWithIngredients> {
        val grouped = reactionIngredientResults.groupBy { it.reactionId }
        return grouped.map { (reactionId, results) ->
            val reaction = results.first()
            val ingredientCounts = results.groupingBy { it.ingredientName }
                .eachCount()
                .filterKeys { it != null }
                .mapKeys { it.key!! }
            ReactionWithIngredients(
                severity = reaction.severity ?: "",
                ingredientCounts = ingredientCounts
            )
        }
    }

//    class ReactionsRepository(private val reactionDao: ReactionDao) {
//        val allReactions: Flow<List<Reaction>> = reactionDao.getAll()
//
//        @WorkerThread
//        suspend fun findReactionsByDate(date: String): MutableList<Reaction> {
//            return reactionDao.findReactionsByDate(date)
//        }
//
//        @WorkerThread
//        suspend fun findReactionsByTimeRange(min: String, max: String): List<Reaction> {
//            return reactionDao.findReactionsByTimeRange(min, max)
//        }
//
//        @WorkerThread
//        suspend fun findReactionsByYearMonth(yearMonth: YearMonth): List<List<Reaction>> {
//            var monthString = yearMonth.month.value.toString()
//            if (yearMonth.month.value < 10)
//                monthString = "0${yearMonth.month.value}"
//            val yearMonthString = "${yearMonth.year}-${monthString}"
//
//            val reactions = findReactionsByTimeRange("$yearMonthString-01", "$yearMonthString-31")
//            val reactionsList = mutableListOf<MutableList<Reaction>>()
//
//            for (i in 0..30) {
//                reactionsList.add(mutableListOf())
//            }
//
//            //count meals eaten on a given day
//            for (reaction in reactions) {
//                reactionsList[reaction.reactionTime.slice(8..9).toInt() - 1].add(reaction)
//            }
//            return reactionsList
//        }
//
//        @WorkerThread
//        suspend fun updateReactionById(reactionTime: String, severity: String, reactionId: Int) {
//            return reactionDao.updateReactionById(reactionTime, severity, reactionId)
//        }
//
//        @WorkerThread
//        suspend fun findReactionById(id: Int): Reaction {
//            return reactionDao.findReactionById(id)
//        }
//
//        @WorkerThread
//        suspend fun deleteReaction(reaction: Reaction) {
//            return reactionDao.delete(reaction)
//        }
//
//        @WorkerThread
//        suspend fun insert(reaction: Reaction): Long {
//            return reactionDao.insert(reaction)
//        }
//
//        fun getIngredientReactionCountsForReaction(reactionId: Int): Map<String, Int> {
//            return reactionDao.getIngredientReactionCountsForReaction(reactionId)
//                .associate { it.ingredientName to it.count }
//        }
//
//        fun getReactionsWithIngredientsByDate(date: String): Flow<List<ReactionWithIngredients>> {
//            return reactionDao.getReactionsWithIngredientsByDate(date).map { rawResults ->
//                // Process the raw results into ReactionWithIngredients
//                val groupedByReaction = rawResults.groupBy { it.reactionId }
//
//                groupedByReaction.map { (_, reactions) ->
//                    val severity = reactions.first().severity ?: ""
//                    val ingredientCounts = reactions
//                        .filter { it.ingredientName != null }
//                        .groupBy { it.ingredientName!! }
//                        .mapValues { it.value.size }
//
//                    ReactionWithIngredients(severity, ingredientCounts)
//                }
//            }
//        }
//
//        private fun transformToReactionWithIngredients(reactionIngredientResults: List<ReactionIngredientResult>): List<ReactionWithIngredients> {
//            val grouped = reactionIngredientResults.groupBy { it.reactionId }
//            return grouped.map { (reactionId, results) ->
//                val reaction = results.first()
//                val ingredientCounts = results.groupingBy { it.ingredientName }
//                    .eachCount()
//                    .filterKeys { it != null }
//                    .mapKeys { it.key!! }
//                ReactionWithIngredients(
//                    severity = reaction.severity ?: "",
//                    ingredientCounts = ingredientCounts
//                )
//            }
//        }
//    }
//

}
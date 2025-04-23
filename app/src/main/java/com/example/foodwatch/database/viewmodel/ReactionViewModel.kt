package com.example.foodwatch.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodwatch.database.repository.ReactionsRepository
import com.example.foodwatch.database.entities.Reaction
import com.example.foodwatch.database.entities.ReactionIngredientResult
import com.example.foodwatch.database.entities.ReactionWithIngredients
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.YearMonth

class ReactionViewModel(private val repository: ReactionsRepository) : ViewModel() {

    // Using LiveData and caching what allMeals returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allReactions: LiveData<List<Reaction>> = repository.allReactionsWithoutIngredients.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun getAllReactions(): LiveData<List<ReactionWithIngredients>> {
        return repository.getAllReactionsWithIngredients() // Assuming you have this method in your repository
    }

    fun insert(reaction: Reaction) = viewModelScope.async {
        repository.insert(reaction)
    }
    fun findReactionsByDate(date: String) = viewModelScope.async {
        repository.findReactionsByDate(date).toMutableList()
    }

    fun findReactionsByTimeRange(min: String, max: String) = viewModelScope.async {
        repository.findReactionsByTimeRange(min, max)
    }

    fun findReactionsByYearMonth(yearMonth: YearMonth) = viewModelScope.async {
        repository.findReactionsByYearMonth(yearMonth)
    }

    fun findReactionById(id: Int) = viewModelScope.async {
        repository.findReactionById(id)
    }

    fun updateReactionById(reactionTime: String, severity: String, reactionId: Int) = viewModelScope.launch {
        repository.updateReactionById(reactionTime, severity, reactionId)
    }

    fun deleteReaction(reaction: Reaction) = viewModelScope.launch {
        repository.deleteReaction(reaction)
    }

    suspend fun getIngredientReactionCounts(reactionId: Int): Map<String, Int> {
        return repository.getIngredientReactionCountsForReaction(reactionId).filterKeys { it != null }.mapKeys { it.key!! }
    }


//    fun processReactionIngredients(rawData: List<ReactionIngredientResult>): List<ReactionWithIngredients> {
//        val groupedByReaction = rawData.groupBy { it.reactionId }
//
//        return groupedByReaction.map { (_, reactions) ->
//            val severity = reactions.first().severity
//            val ingredientCounts = reactions
//                .groupBy { it.ingredientName }
//                .mapValues { it.value.size }
//
//            ReactionWithIngredients(severity, ingredientCounts)
//        }
//    }

    fun getReactionsWithIngredientsByDate(date: String?): LiveData<List<ReactionWithIngredients>> {
        return if (date.isNullOrEmpty()) {
            MutableLiveData(emptyList())
        } else {
            repository.getReactionsWithIngredientsByDate(date).asLiveData()
        }
    }

}

class ReactionViewModelFactory(private val repository: ReactionsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.foodwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReactionViewModel(private val repository: ReactionsRepository) : ViewModel() {

    // Using LiveData and caching what allMeals returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allReactions: LiveData<List<Reaction>> = repository.allReactions.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(reaction: Reaction) = viewModelScope.launch {
        repository.insert(reaction)
    }
    fun findReactionsByDate(date: String) = viewModelScope.async {
        repository.findReactionsByDate(date)
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
package com.example.foodwatch.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.repository.IngredientsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class IngredientViewModel(private val repository: IngredientsRepository) : ViewModel() {
    val allIngredients: LiveData<List<Ingredient>> = repository.allIngredients.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(ingredient: Ingredient) = viewModelScope.launch {
        repository.insert(ingredient)
    }
}

class IngredientViewModelFactory(private val repository: IngredientsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
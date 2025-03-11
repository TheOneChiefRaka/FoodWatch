package com.example.foodwatch.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.repository.ReactionsRepository
import com.example.foodwatch.database.entities.Reaction
import com.example.foodwatch.database.entities.relations.MealIngredientCrossRef
import com.example.foodwatch.database.repository.MealIngredientRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MealIngredientViewModel(private val repository: MealIngredientRepository) : ViewModel() {

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun getMealWithIngredientsById(mealId: Int) = viewModelScope.async {
        repository.getMealWithIngredientsById(mealId)
    }

    fun insertIngredientsList(ingredients: List<MealIngredientCrossRef>) = viewModelScope.launch {
        repository.insertIngredientsList(ingredients)
    }

    fun deleteIngredientsByMealId(mealId: Int) = viewModelScope.launch {
        repository.deleteIngredientsByMealId(mealId)
    }
}

class MealIngredientViewModelFactory(private val repository: MealIngredientRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealIngredientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealIngredientViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
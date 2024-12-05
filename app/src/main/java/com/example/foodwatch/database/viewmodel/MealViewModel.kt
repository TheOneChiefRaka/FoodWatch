package com.example.foodwatch.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodwatch.database.repository.MealsRepository
import com.example.foodwatch.database.entities.Meal
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealsRepository) : ViewModel() {

    // Using LiveData and caching what allMeals returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMeals: LiveData<List<Meal>> = repository.allMeals.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun addMeal(meal: Meal, onSuccess: (Long) -> Unit) = viewModelScope.launch {
        val mealId = repository.addMeal(meal)
        onSuccess(mealId)
    }

    fun getMealById(mealId: Int, onResult: (Meal) -> Unit) = viewModelScope.launch {
        val meal = repository.getMealById(mealId)
        onResult(meal)
    }

    fun findMealsByTimeRange(min: String, max: String) = viewModelScope.async {
        repository.findMealsByTimeRange(min, max)
    }

    fun findMealsByDate(date: String) = viewModelScope.async {
        repository.findMealsByDate(date)
    }
}

class MealViewModelFactory(private val repository: MealsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
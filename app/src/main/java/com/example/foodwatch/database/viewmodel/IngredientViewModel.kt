package com.example.foodwatch.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.repository.IngredientsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class IngredientViewModel(private val repository: IngredientsRepository) : ViewModel() {
    val allIngredients: LiveData<List<Ingredient>> = repository.allIngredients.asLiveData()

    //running the function asynchronously so we can get a result from it
    fun findIngredientsByName(ingredientNames: List<String>) = viewModelScope.async {
        repository.findIngredientsByName(ingredientNames)
    }

    fun findIngredientById(ingredientId: Int) = viewModelScope.async {
        repository.findIngredientById(ingredientId)
    }

/*
    fun findAllPossibleAllergens() = viewModelScope.async {
        repository.findAllPossibleAllergens()
    }
*/

    fun insert(ingredient: Ingredient) = viewModelScope.launch {
        repository.insert(ingredient)
    }

    fun addOrUpdateIngredients(name: String) = viewModelScope.launch {
        repository.addOrUpdateIngredient(name)
    }

    fun getIngredientIdByName(name: String) = viewModelScope.async{
        repository.getIngredientIdByName(name)
    }

    fun getAllIngredientNames() = viewModelScope.async {
        repository.getAllIngredientNames()
    }

    fun getIngredientNames() = viewModelScope.async{
        repository.getIngredientNames()
    }

    fun getIngredientData() = viewModelScope.async {
        repository.getIngredientData()
    }

    fun getIngredientDataTimeRange(startDate: String, endDate: String) = viewModelScope.async {
        repository.getIngredientDataTimeRange(startDate, endDate)
    }

    fun deleteIngredient(ingredient: Ingredient) = viewModelScope.launch{
        repository.deleteIngredient(ingredient)
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
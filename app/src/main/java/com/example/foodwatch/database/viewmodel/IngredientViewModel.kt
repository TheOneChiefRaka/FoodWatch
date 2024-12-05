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

    //running the function asynchronously so we can get a result from it
    fun findIngredientsByName(ingredientNames: List<String>) = viewModelScope.async {
        repository.findIngredientsByName(ingredientNames)
    }

    //launching the function because we don't care about its result
    fun addIngredientsReactionMild(ingredientIds: List<Int>) = viewModelScope.launch {
        repository.addIngredientsReactionMild(ingredientIds)
    }

    fun addIngredientsReactionMedium(ingredientIds: List<Int>) = viewModelScope.launch {
        repository.addIngredientsReactionMedium(ingredientIds)
    }

    fun addIngredientsReactionSevere(ingredientIds: List<Int>) = viewModelScope.launch {
        repository.addIngredientsReactionSevere(ingredientIds)
    }

    fun findAllPossibleAllergens() = viewModelScope.async {
        repository.findAllPossibleAllergens()
    }

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

    //running the function asynchronously so we can get a result from it
    fun findIngredientsByName(ingredientNames: List<String>) = viewModelScope.async {
        repository.findIngredientsByName(ingredientNames)
    }

    //launching the function because we don't care about its result
    fun addIngredientsReactionMild(ingredientIds: List<Int>) = viewModelScope.launch {
        repository.addIngredientsReactionMild(ingredientIds)
    }

    fun addIngredientsReactionMedium(ingredientIds: List<Int>) = viewModelScope.launch {
        repository.addIngredientsReactionMedium(ingredientIds)
    }

    fun addIngredientsReactionSevere(ingredientIds: List<Int>) = viewModelScope.launch {
        repository.addIngredientsReactionSevere(ingredientIds)
    }

    fun addIngredientsToTable(name: String){
        val ingredient = Ingredient(name = name)
    }

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
package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import com.example.foodwatch.database.dao.IngredientDao
import com.example.foodwatch.database.entities.Ingredient
import kotlinx.coroutines.flow.Flow

class IngredientsRepository(private val ingredientDao: IngredientDao) {
    //
    val allIngredients: Flow<List<Ingredient>> = ingredientDao.getAll()

    @WorkerThread
    suspend fun findIngredientsByName(ingredientNames: List<String>): List<Ingredient> {
        return ingredientDao.findIngredientsByName(ingredientNames)
    }

    @WorkerThread
    suspend fun addIngredientsReactionMild(ingredientIds: List<Int>) {
        ingredientDao.addIngredientsReactionMild(ingredientIds)
    }

    @WorkerThread
    suspend fun addIngredientsReactionMedium(ingredientIds: List<Int>) {
        ingredientDao.addIngredientsReactionMedium(ingredientIds)
    }

    @WorkerThread
    suspend fun addIngredientsReactionSevere(ingredientIds: List<Int>) {
        ingredientDao.addIngredientsReactionSevere(ingredientIds)
    }

    @WorkerThread
    suspend fun insert(ingredient: Ingredient) {
        ingredientDao.insert(ingredient)
    }
}
package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import com.example.foodwatch.database.dao.IngredientDao
import com.example.foodwatch.database.entities.Ingredient
import kotlinx.coroutines.flow.Flow

class IngredientsRepository(private val ingredientDao: IngredientDao) {
    //
    val allIngredients: Flow<List<Ingredient>> = ingredientDao.getAll()

    @WorkerThread
    suspend fun insert(ingredient: Ingredient) {
        ingredientDao.insert(ingredient)
    }
}
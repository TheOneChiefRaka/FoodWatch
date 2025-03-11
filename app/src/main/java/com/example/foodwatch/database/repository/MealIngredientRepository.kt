package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import com.example.foodwatch.database.dao.MealIngredientCrossRefDao
import com.example.foodwatch.database.entities.relations.MealWithIngredients

class MealIngredientRepository(private val mealIngredientCrossRefDao: MealIngredientCrossRefDao) {
    //
    @WorkerThread
    suspend fun getMealWithIngredientsById(mealId: Int): MealWithIngredients {
        return mealIngredientCrossRefDao.getMealWithIngredientsById(mealId)
    }
}
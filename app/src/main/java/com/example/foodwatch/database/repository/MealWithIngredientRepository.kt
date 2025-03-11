package com.example.foodwatch.database.repository

import com.example.foodwatch.database.dao.MealIngredientCrossRefDao
import com.example.foodwatch.database.entities.relations.IngredientWithMeal
import com.example.foodwatch.database.entities.relations.MealWithIngredients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealWithIngredientRepository(private val mealIngredientCrossRefDao: MealIngredientCrossRefDao) {

    // Function to get all meals with their ingredients
    suspend fun getMealWithIngredients(): List<MealWithIngredients> {
        return withContext(Dispatchers.IO) {
            mealIngredientCrossRefDao.getMealWithIngredients()
        }
    }

    // Function to get all ingredients with their meals
    suspend fun getIngredientWithMeals(): List<IngredientWithMeal> {
        return withContext(Dispatchers.IO) {
            mealIngredientCrossRefDao.getIngredientWithMeals()
        }
    }
}
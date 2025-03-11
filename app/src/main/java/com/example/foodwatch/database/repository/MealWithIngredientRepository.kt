package com.example.foodwatch.database.repository

import com.example.foodwatch.database.dao.MealIngredientCrossRefDao
import com.example.foodwatch.database.entities.relations.MealWithIngredients

class MealWithIngredientRepository(private val mealIngredientCrossRefDao: MealIngredientCrossRefDao) {
    //
    val allIngredientsWithMeals: Flow<List<MealWithIngredients>> =
}
package com.example.foodwatch.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.foodwatch.database.entities.relations.IngredientWithMeal
import com.example.foodwatch.database.entities.relations.MealWithIngredients

@Dao
interface MealIngredientCrossRefDao {
    @Transaction
    @Query("SELECT * FROM Meal")
    fun getMealWithIngredients(): List<MealWithIngredients>

    @Transaction
    @Query("SELECT * FROM Ingredient")
    fun getIngredientWithMeals(): List<IngredientWithMeal>
}
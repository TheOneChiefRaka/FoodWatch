package com.example.foodwatch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.foodwatch.database.entities.relations.IngredientWithMeal
import com.example.foodwatch.database.entities.relations.MealIngredientCrossRef
import com.example.foodwatch.database.entities.relations.MealWithIngredients

@Dao
interface MealIngredientCrossRefDao {
    @Transaction
    @Query("SELECT * FROM Meal")
    suspend fun getMealWithIngredients(): List<MealWithIngredients>

    @Transaction
    @Query("SELECT * FROM Meal WHERE mealId = :mealId")
    suspend fun getMealWithIngredientsById(mealId: Int): MealWithIngredients

    @Transaction
    @Query("DELETE FROM MealIngredientCrossRef WHERE mealId = :mealId")
    suspend fun deleteIngredientsByMealId(mealId: Int)

    @Transaction
    @Insert
    suspend fun insertIngredients(ingredients: List<MealIngredientCrossRef>)

    @Transaction
    @Query("SELECT * FROM Ingredient")
    suspend fun getIngredientWithMeals(): List<IngredientWithMeal>
}
package com.example.foodwatch.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.foodwatch.database.entities.Meal
import kotlinx.coroutines.flow.Flow

// All function needed to modify meal table go here

@Dao
interface MealDao {
    @Query("SELECT * FROM meal")
    fun getAll(): Flow<List<Meal>>

    @Query("SELECT * FROM meal WHERE timeEaten >= :dateEaten || \" 00:00\" AND timeEaten <= :dateEaten || \" 23:59\" ORDER BY timeEaten")
    suspend fun findMealsByDate(dateEaten: String): List<Meal>

    @Query("SELECT * FROM meal WHERE timeEaten >= :min AND timeEaten <= :max")
    suspend fun findMealsByTimeRange(min: String, max: String): List<Meal>

    @Query("SELECT * FROM meal WHERE id = :mealId")
    suspend fun getMealById(mealId: Int): Meal

    @Query("DELETE FROM meal WHERE id = :mealId")
    suspend fun deleteMealById(mealId: Int)

    @Insert
    suspend fun addMeal(meal: Meal): Long

    @Delete
    suspend fun delete(meal: Meal)
}
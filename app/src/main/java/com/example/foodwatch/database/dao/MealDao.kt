package com.example.foodwatch.database.dao

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

    @Query("SELECT * FROM meal WHERE timeEaten LIKE :timeEaten")
    suspend fun findMealsByTime(timeEaten: String): List<Meal>

    @Insert
    suspend fun insert(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)
}
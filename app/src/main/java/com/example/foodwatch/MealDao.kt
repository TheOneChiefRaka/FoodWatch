package com.example.foodwatch

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// All function needed to modify meal table go here
@Dao
interface MealDao {

    // Upsert or Insert can be used. Upsert is combination of insert and update
    // Inserts a new meal ticket
    @Insert
    suspend fun insertMeal(meal: Meal)

    // Delete meal ticket
    @Delete
    suspend fun deleteMeal(meal: Meal)

    // Query for all records
    @Query("SELECT * FROM meal")
    fun getAll(): List<Meal>
}
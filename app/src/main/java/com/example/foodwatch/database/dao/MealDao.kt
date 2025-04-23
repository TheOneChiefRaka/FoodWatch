package com.example.foodwatch.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.entities.ReactionWithMeal
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

    @Query("SELECT * FROM meal WHERE mealId = :mealId")
    suspend fun getMealById(mealId: Int): Meal

    @Query("UPDATE meal SET timeEaten = :timeEaten, name = :name, reactionId = :reactionId WHERE mealId = :id")
    suspend fun updateMealById(timeEaten: String, name: String, id: Int, reactionId: Int?)

    @Query("UPDATE meal SET reactionId = NULL WHERE reactionId = :reactionId")
    suspend fun removeReactionFromMeals(reactionId: Int)

    @Query("DELETE FROM meal WHERE mealId = :mealId")
    suspend fun deleteMealById(mealId: Int)

    @Insert
    suspend fun addMeal(meal: Meal): Long

    @Delete
    suspend fun delete(meal: Meal)

/*
    @Transaction
    @Query("SELECT * FROM Meal")
    fun getReactionsWithMeals(): List<ReactionWithMeal>
*/
}
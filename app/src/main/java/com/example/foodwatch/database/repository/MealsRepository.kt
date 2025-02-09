package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import com.example.foodwatch.database.dao.MealDao
import com.example.foodwatch.database.entities.Meal
import kotlinx.coroutines.flow.Flow

class MealsRepository(private val mealDao: MealDao) {
    //
    val allMeals: Flow<List<Meal>> = mealDao.getAll()

    @WorkerThread
    suspend fun findMealsByDate(dateEaten: String): List<Meal> {
        return mealDao.findMealsByDate(dateEaten)
    }

    @WorkerThread
    suspend fun findMealsByTimeRange(min: String, max: String): List<Meal> {
        return mealDao.findMealsByTimeRange(min, max)
    }

    @WorkerThread
    suspend fun addMeal(meal: Meal): Long{
        return mealDao.addMeal(meal)
    }

    @WorkerThread
    suspend fun getMealById(mealId: Int): Meal{
        return mealDao.getMealById(mealId)
    }
}
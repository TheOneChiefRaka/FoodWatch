package com.example.foodwatch

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class MealsRepository(private val mealDao: MealDao) {
    //
    val allMeals: Flow<List<Meal>> = mealDao.getAll()

    @WorkerThread
    suspend fun findMealsByDate(date: String): List<Meal> {
        return mealDao.findMealsByDate(date)
    }

    @WorkerThread
    suspend fun insert(meal: Meal) {
        mealDao.insert(meal)
    }
}
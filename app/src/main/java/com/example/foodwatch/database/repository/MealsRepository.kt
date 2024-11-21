package com.example.foodwatch.database.repository

import androidx.annotation.WorkerThread
import com.example.foodwatch.database.dao.MealDao
import com.example.foodwatch.database.entities.Meal
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
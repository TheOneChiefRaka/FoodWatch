package com.example.foodwatch.database.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.collection.intListOf
import androidx.lifecycle.LiveData
import com.example.foodwatch.database.dao.MealDao
import com.example.foodwatch.database.entities.Meal
import com.kizitonwose.calendar.core.CalendarMonth
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

class MealsRepository(private val mealDao: MealDao) {
    val allMeals: Flow<List<Meal>> = mealDao.getAll()


    @WorkerThread
    suspend fun findMealsByDate(dateEaten: String): List<Meal> {
        return mealDao.findMealsByDate(dateEaten)
    }

    @WorkerThread
    suspend fun countMealsEatenByYearMonth(yearMonth: YearMonth): List<Int> {
        var monthString = yearMonth.month.value.toString()
        if(yearMonth.month.value < 10)
            monthString = "0${yearMonth.month.value}"
        val yearMonthString = "${yearMonth.year}-${monthString}"
        val meals = findMealsByTimeRange("$yearMonthString-01", "$yearMonthString-31")
        val count = mutableListOf<Int>()
        for(i in 0..30)
            count.add(0)
        //count meals eaten on a given day
        for(meal in meals) {
//            Log.i("TEST", "countMealsEatenByYearMonth: ${meal.timeEaten.slice(8..9).toInt()-1}")
            count[meal.timeEaten.slice(8..9).toInt()-1]++
        }
        return count
    }

    @WorkerThread
    suspend fun findMealsByTimeRange(min: String, max: String): List<Meal> {
        return mealDao.findMealsByTimeRange(min, max)
    }

    @WorkerThread
    suspend fun addMeal(meal: Meal): Long {
        return mealDao.addMeal(meal)
    }

    @WorkerThread
    suspend fun getMealById(mealId: Int): Meal {
        return mealDao.getMealById(mealId)
    }

    @WorkerThread
    suspend fun updateMealById(meal: Meal) {
        return mealDao.updateMealById(meal.timeEaten, meal.name, meal.mealId, meal.reactionId)
    }

    @WorkerThread
    suspend fun removeReactionFromMeals(reactionId: Int) {
        return mealDao.removeReactionFromMeals(reactionId)
    }

    @WorkerThread
    suspend fun deleteMealById(mealId: Int) {
        return mealDao.deleteMealById(mealId)
    }

    fun getAll(): Flow<List<Meal>> = mealDao.getAll()
}
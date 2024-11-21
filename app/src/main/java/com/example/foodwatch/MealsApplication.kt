package com.example.foodwatch

import android.app.Application
import com.example.foodwatch.database.MealsDatabase
import com.example.foodwatch.database.repository.IngredientsRepository
import com.example.foodwatch.database.repository.MealsRepository
import com.example.foodwatch.database.repository.ReactionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MealsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { MealsDatabase.getDatabase(this, applicationScope) }
    val meals_repository by lazy { MealsRepository(database.mealDao()) }
    val reactions_repository by lazy { ReactionsRepository(database.reactionDao()) }
    val ingredients_repository by lazy { IngredientsRepository(database.ingredientDao()) }
}
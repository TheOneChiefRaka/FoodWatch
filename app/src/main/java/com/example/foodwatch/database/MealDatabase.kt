package com.example.foodwatch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodwatch.database.dao.IngredientDao
import com.example.foodwatch.database.dao.MealDao
import com.example.foodwatch.database.dao.MealIngredientCrossRefDao
import com.example.foodwatch.database.dao.ReactionDao
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.entities.Reaction
import com.example.foodwatch.database.entities.relations.MealIngredientCrossRef
import kotlinx.coroutines.CoroutineScope


@Database(entities = [Meal::class, Reaction::class, Ingredient::class, MealIngredientCrossRef::class], version = 3)
abstract class MealsDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun reactionDao(): ReactionDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun mealIngredientDao(): MealIngredientCrossRefDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MealsDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MealsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealsDatabase::class.java,
                    "meal_database"
                ).createFromAsset("ingredient_initialization.db")
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
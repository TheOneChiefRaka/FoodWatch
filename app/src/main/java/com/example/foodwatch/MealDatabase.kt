package com.example.foodwatch

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Meal::class, Reaction::class], version = 1)
abstract class MealsDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun reactionDao(): ReactionDao

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
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
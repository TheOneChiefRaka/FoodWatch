package com.example.foodwatch

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import java.time.LocalDate

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val date: Long,
    @ColumnInfo val name: String,
)

@Database(entities = [Meal::class], version = 1)
abstract class MealsDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}

@Dao
interface MealDao {
    @Query("SELECT * FROM meal")
    fun getAll(): List<Meal>

    @Query("SELECT * FROM meal WHERE date LIKE :date")
    fun findByDate(date: Long): Meal

    @Insert
    fun insertAll(vararg meals: Meal)

    @Delete
    fun delete(meal: Meal)
}
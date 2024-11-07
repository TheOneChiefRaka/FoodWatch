package com.example.foodwatch

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalTime

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val date: String,
    @ColumnInfo val name: String,
    @ColumnInfo val time: String,
)

@Database(entities = [Meal::class], version = 1)
abstract class MealsDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao

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
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}

@Dao
interface MealDao {
    @Query("SELECT * FROM meal")
    fun getAll(): Flow<List<Meal>>

    @Query("SELECT * FROM meal WHERE date LIKE :date")
    suspend fun findMealsByDate(date: String): List<Meal>

    @Insert
    suspend fun insert(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)
}

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

class MealViewModel(private val repository: MealsRepository) : ViewModel() {

    // Using LiveData and caching what allMeals returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMeals: LiveData<List<Meal>> = repository.allMeals.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(meal: Meal) = viewModelScope.launch {
        repository.insert(meal)
    }
    fun findMealsByDate(date: String) = viewModelScope.async {
        repository.findMealsByDate(date)
    }
}

class MealViewModelFactory(private val repository: MealsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
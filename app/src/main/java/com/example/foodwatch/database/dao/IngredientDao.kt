package com.example.foodwatch.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.entities.IngredientData
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredient")
    fun getAll(): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredient WHERE name IN (:ingredientNames)")
    suspend fun findIngredientsByName(ingredientNames: List<String>): List<Ingredient>

    @Query("SELECT * FROM Ingredient WHERE name = :name LIMIT 1")
    suspend fun checkForIngredient(name: String): Ingredient?

    @Query("SELECT ingredientId FROM Ingredient WHERE name = :name LIMIT 1")
    suspend fun getIngredientIdByName(name: String): Long?

    @Query("SELECT name FROM Ingredient")
    suspend fun getAllIngredientNames(): List<String>

    @Query("SELECT * FROM Ingredient WHERE ingredientId = :id")
    suspend fun findIngredientById(id: Int): Ingredient

    @Query("SELECT * FROM Ingredient ORDER BY name ASC")
    suspend fun getIngredientNames(): List<Ingredient>

    @Query("SELECT i.name, COUNT(*) AS timesEaten, COUNT(CASE r.severity WHEN 'Mild' THEN 1 ELSE NULL END) AS mild, COUNT(CASE r.severity WHEN 'Medium' THEN 1 ELSE NULL END) AS medium, COUNT(CASE r.severity WHEN 'Severe' THEN 1 ELSE NULL END) AS severe\n" +
            "FROM ingredient AS i INNER JOIN mealingredientcrossref AS mi ON i.ingredientId = mi.ingredientId\n" +
            "INNER JOIN meal AS m ON mi.mealId = m.mealId\n" +
            "LEFT OUTER JOIN reaction AS r ON m.reactionId = r.reactionId\n" +
            "GROUP BY i.name\n" +
            "HAVING mild + medium + severe > 0\n" +
            "ORDER BY ((mild + medium + severe)) DESC, timesEaten DESC")
    suspend fun getIngredientData(): List<IngredientData>

    @Query("SELECT i.name, COUNT(*) AS timesEaten, COUNT(CASE r.severity WHEN 'Mild' THEN 1 ELSE NULL END) AS mild, COUNT(CASE r.severity WHEN 'Medium' THEN 1 ELSE NULL END) AS medium, COUNT(CASE r.severity WHEN 'Severe' THEN 1 ELSE NULL END) AS severe\n" +
            "FROM ingredient AS i INNER JOIN mealingredientcrossref AS mi ON i.ingredientId = mi.ingredientId\n" +
            "INNER JOIN meal AS m ON mi.mealId = m.mealId\n" +
            "LEFT OUTER JOIN reaction AS r ON m.reactionId = r.reactionId\n" +
            "WHERE :startDate || \" 00:00\" <= m.timeEaten AND m.timeEaten <= :endDate || \" 23:59\"\n" +
            "GROUP BY i.name\n" +
            "HAVING mild + medium + severe > 0\n" +
            "ORDER BY ((mild + medium + severe)) DESC, timesEaten DESC")
    suspend fun getIngredientDataTimeRange(startDate: String, endDate: String): List<IngredientData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredientToTable(ingredient: Ingredient): Long

    @Insert
    suspend fun insert(ingredient: Ingredient): Long

    @Query("SELECT * FROM ingredient ORDER BY name ASC")
    fun getAllIngredients(): LiveData<List<Ingredient>>

    @Delete
    suspend fun delete(ingredient: Ingredient)
}
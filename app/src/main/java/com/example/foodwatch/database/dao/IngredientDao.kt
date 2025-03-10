package com.example.foodwatch.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodwatch.database.entities.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM Ingredient")
    fun getAll(): Flow<List<Ingredient>>

    @Query("SELECT * FROM Ingredient WHERE name IN (:ingredientNames)")
    suspend fun findIngredientsByName(ingredientNames: List<String>): List<Ingredient>

    @Query("SELECT * FROM Ingredient WHERE name = :name LIMIT 1")
    suspend fun checkForIngredient(name: String): Ingredient?


    @Query("SELECT pk_i_id FROM Ingredient WHERE name = :name LIMIT 1")
    suspend fun getIngredientIdByName(name: String): Int?

    @Query("SELECT name FROM Ingredient")
    suspend fun getAllIngredientNames(): List<String>

    @Query("SELECT * FROM Ingredient WHERE pk_i_id = :id")
    suspend fun findIngredientById(id: Int): Ingredient

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredientToTable(ingredient: Ingredient)

    @Insert
    suspend fun insert(ingredient: Ingredient)

    @Delete
    suspend fun delete(ingredient: Ingredient)
}
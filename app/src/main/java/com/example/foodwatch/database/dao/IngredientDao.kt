package com.example.foodwatch.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.foodwatch.database.entities.Ingredient
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredient")
    fun getAll(): Flow<List<Ingredient>>

    @Query("SELECT * FROM ingredient WHERE name IN (:ingredientNames)")
    suspend fun findIngredientsByName(ingredientNames: List<String>): List<Ingredient>

    @Query("UPDATE ingredient SET mildReactions = mildReactions + 1 WHERE id IN (:ingredientIds)" )
    suspend fun addIngredientsReactionMild(ingredientIds: List<Int>)

    @Query("UPDATE ingredient SET mediumReactions = mediumReactions + 1 WHERE id IN (:ingredientIds)" )
    suspend fun addIngredientsReactionMedium(ingredientIds: List<Int>)

    @Query("UPDATE ingredient SET severeReactions = severeReactions + 1 WHERE id IN (:ingredientIds)" )
    suspend fun addIngredientsReactionSevere(ingredientIds: List<Int>)

    @Query("SELECT * FROM Ingredient WHERE (mildReactions + mediumReactions + severeReactions) > 0")
    suspend fun findAllPossibleAllergens(): List<Ingredient>

    @Insert
    suspend fun insert(ingredient: Ingredient)

    @Delete
    suspend fun delete(ingredient: Ingredient)
}
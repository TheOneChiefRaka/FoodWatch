package com.example.foodwatch.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.foodwatch.database.entities.Reaction
import kotlinx.coroutines.flow.Flow
import com.example.foodwatch.database.entities.ReactionWithIngredients
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.entities.ReactionIngredientResult
import com.example.foodwatch.database.entities.relations.MealIngredientCrossRef

@Dao
interface ReactionDao {
    @Query("SELECT * FROM reaction")
    fun getAll(): Flow<List<Reaction>>

    @Transaction
    @Query("SELECT * FROM reaction")
    fun getAllReactionsWithIngredients(): LiveData<List<ReactionWithIngredients>>

    @Query("SELECT * FROM reaction WHERE reactionTime >= :reactionDate || \" 00:00\" AND reactionTime <= :reactionDate || \" 23:59\" ORDER BY reactionTime")
    suspend fun findReactionsByDate(reactionDate: String): List<Reaction>

    @Query("SELECT * FROM reaction WHERE :min <= reactionTime AND reactionTime <= :max")
    suspend fun findReactionsByTimeRange(min: String, max: String): List<Reaction>

    @Query("UPDATE reaction SET reactionTime = :reactionTime, severity = :severity WHERE reactionId = :reactionId")
    suspend fun updateReactionById(reactionTime: String, severity: String, reactionId: Int)

    @Query("SELECT * FROM reaction WHERE reactionId = :id")
    suspend fun findReactionById(id: Int): Reaction

    @Query("""
        SELECT Ingredient.name AS ingredientName, COUNT(*) AS count
        FROM Reaction
        JOIN Meal ON Reaction.reactionID = Meal.reactionId
        JOIN MealIngredientCrossRef ON Meal.mealId = MealIngredientCrossRef.mealId
        JOIN Ingredient ON MealIngredientCrossRef.ingredientId = Ingredient.ingredientId
        WHERE Reaction.reactionId = :reactionId
        Group By Ingredient.ingredientId
    """)
    fun getIngredientReactionCountsForReaction(reactionId: Int): List<IngredientCount>

    @Query("""
        SELECT 
            Reaction.reactionId,
            Reaction.reactionTime,
            Reaction.severity,
            Ingredient.ingredientId,
            Ingredient.name AS ingredientName
        FROM reaction
        JOIN Meal ON Reaction.reactionId = Meal.reactionId
        JOIN MealIngredientCrossRef ON Meal.mealId = MealIngredientCrossRef.mealId
        JOIN Ingredient ON MealIngredientCrossRef.ingredientId = Ingredient.ingredientId
        WHERE Reaction.reactionTime >= :date || " 00:00" AND Reaction.reactionTime <= :date || " 23:59"
    """)
    fun getReactionsWithIngredientsByDate(date: String): Flow<List<ReactionIngredientResult>>

    @Query("""
        SELECT
            Reaction.reactionId,
            Reaction.reactionTime,
            Reaction.severity,
            Ingredient.name AS ingredientName
        FROM reaction
        JOIN Meal ON Reaction.reactionId = Meal.reactionId
        JOIN MealIngredientCrossRef ON Meal.mealId = MealIngredientCrossRef.mealId
        JOIN Ingredient ON MealIngredientCrossRef.ingredientId = Ingredient.ingredientId
    """)
    fun getAllReactions(): Flow<List<ReactionIngredientResult>>

    @Insert
    suspend fun insert(reaction: Reaction): Long

    @Delete
    suspend fun delete(reaction: Reaction)
}

data class IngredientCount(
    val ingredientName: String,
    val count: Int
)
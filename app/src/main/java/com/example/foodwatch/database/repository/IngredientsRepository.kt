package com.example.foodwatch.database.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.foodwatch.database.dao.IngredientDao
import com.example.foodwatch.database.entities.Ingredient
import kotlinx.coroutines.flow.Flow

class IngredientsRepository(private val ingredientDao: IngredientDao) {
    val allIngredients: Flow<List<Ingredient>> = ingredientDao.getAll()

    @WorkerThread
    suspend fun findIngredientsByName(ingredientNames: List<String>): List<Ingredient> {
        return ingredientDao.findIngredientsByName(ingredientNames)
    }

    @WorkerThread
    suspend fun findIngredientById(ingredientId: Int) : Ingredient {
        return ingredientDao.findIngredientById(ingredientId)
    }
    /*
    @WorkerThread
    suspend fun addIngredientsReactionMild(ingredientIds: List<Int>) {
        ingredientDao.addIngredientsReactionMild(ingredientIds)
    }

    @WorkerThread
    suspend fun addIngredientsReactionMedium(ingredientIds: List<Int>) {
        ingredientDao.addIngredientsReactionMedium(ingredientIds)
    }

    @WorkerThread
    suspend fun addIngredientsReactionSevere(ingredientIds: List<Int>) {
        ingredientDao.addIngredientsReactionSevere(ingredientIds)
    }
    @WorkerThread
    suspend fun findAllPossibleAllergens(): List<Ingredient> {
        return ingredientDao.findAllPossibleAllergens()
    }
    */
    @WorkerThread
    suspend fun insert(ingredient: Ingredient) {
        ingredientDao.insert(ingredient)
    }

    // First check if ingredient is already in table
    @WorkerThread
    suspend fun addOrUpdateIngredient(name: String): Int? {
        var ingredientId = ingredientDao.getIngredientIdByName(name)
        Log.i("TEST", "$ingredientId")
        if (ingredientId != null){ // ingredient exists
            //ingredientDao.incrementTimesEaten(name)
            return ingredientId
        }
        else { //Otherwise ingredient was not found and needs to be added to table
            //insert into ingredients table
            ingredientDao.insertIngredientToTable(Ingredient(name = name))
            ingredientId = getIngredientIdByName(name)
            return ingredientId
        }
    }

    // Gets ingredient ID
    @WorkerThread
    suspend fun getIngredientIdByName(name: String): Int? {
        return ingredientDao.getIngredientIdByName(name)
    }

    suspend fun getAllIngredientNames(): List<String>{
        return ingredientDao.getAllIngredientNames()
    }

    suspend fun getIngredientNames(): List<Ingredient>{
        return ingredientDao.getIngredientNames()
    }

    suspend fun deleteIngredient(ingredient: Ingredient){
        ingredientDao.delete(ingredient)
    }
}
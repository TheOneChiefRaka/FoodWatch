package com.example.foodwatch.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodwatch.data.entities.Allergen

@Dao
interface AllergenDao {
    @Insert
    suspend fun insertAllergen(allergen: Allergen)

    @Query("SELECT * FROM allergens")
    suspend fun getAllAllergens(): List<Allergen>
}
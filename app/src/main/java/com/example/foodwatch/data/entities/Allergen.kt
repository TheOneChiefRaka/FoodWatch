package com.example.foodwatch.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allergens")
data class Allergen(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val species: String,
    val commonName: String,
    val iuisName: String,
    val type: String,
    val group: String,
    val allergenicity: String
)
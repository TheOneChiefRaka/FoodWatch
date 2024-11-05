package com.example.foodwatch

import androidx.room.*

// This defines the meal table columns
@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val mealName: String,
    val mealTime: String
    )

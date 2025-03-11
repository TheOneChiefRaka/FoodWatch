package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val ingredientId: Int = 0,
    @ColumnInfo val name: String
)
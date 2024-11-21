package com.example.foodwatch.database.entities

import androidx.room.*

// This defines the meal table columns
@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val date: String,
    @ColumnInfo val name: String,
    @ColumnInfo val time: String,
)
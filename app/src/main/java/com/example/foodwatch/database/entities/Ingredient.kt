package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String,
    @ColumnInfo val timesEaten: Int = 0,
    @ColumnInfo val mildReactions: Int = 0,
    @ColumnInfo val mediumReactions: Int = 0,
    @ColumnInfo val severeReactions: Int = 0
)
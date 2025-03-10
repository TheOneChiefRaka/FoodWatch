package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val pk_i_id: Int = 0,
    @ColumnInfo val name: String,       //ingredient name
)
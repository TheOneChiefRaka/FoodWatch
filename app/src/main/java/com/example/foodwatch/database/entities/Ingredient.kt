package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String,                       //ingredient name
    @ColumnInfo val timesEaten: Int = 0,                //number of meals that contain this ingredient
    @ColumnInfo val mildReactions: Int = 0,             //number of meals which happened 3 hours before a mild reaction
    @ColumnInfo val mediumReactions: Int = 0,           //number of meals which happened 3 hours before a medium reaction
    @ColumnInfo val severeReactions: Int = 0            //number of meals which happened 3 hours before a severe reaction
)
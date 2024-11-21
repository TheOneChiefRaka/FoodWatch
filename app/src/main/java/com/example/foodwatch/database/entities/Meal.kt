package com.example.foodwatch.database.entities

import androidx.room.*
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson(value: List<Int>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()
}

// This defines the meal table columns
@Entity
@TypeConverters(Converters::class)
data class Meal(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val timeEaten: String,
    @ColumnInfo val name: String,
    @ColumnInfo val ingredients: List<Int>,
)
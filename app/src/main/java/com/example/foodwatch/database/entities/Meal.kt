package com.example.foodwatch.database.entities

import androidx.room.*
import com.google.gson.Gson

//converters to convert an array of integers to a json array; used to store ingredients lists into the table (hopefully)
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

    @PrimaryKey(autoGenerate = true) val meal_id: Int = 0,
    @ColumnInfo val timeEaten: String,                  //format of yyyy-MM-dd HH:mm so that it can be sorted into chronological order
    @ColumnInfo val name: String,                       //name of the meal
)
package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//class Converters {
//    @TypeConverter
//    fun fromStringList(value: List<String>?): String {
//        if (value == null) {
//            return ""
//        }
//        val gson = Gson()
//        return gson.toJson(value)
//    }
//
//    @TypeConverter
//    fun toStringList(value: String): List<String> {
//        if (value.isEmpty()) {
//            return emptyList()
//        }
//        val gson = Gson()
//        val listType = object : TypeToken<List<String>>() {}.type
//        return gson.fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromStringToIntMap(value: String): Map<String, Int> {
//        if (value.isEmpty()) {
//            return emptyMap()
//        }
//        val gson = Gson()
//        val mapType = object : TypeToken<Map<String, Int>>() {}.type
//        return gson.fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromMapToString(map: Map<String, Int>): String {
//        val gson = Gson()
//        return gson.toJson(map)
//    }
//}

@Entity
data class Reaction(
    @PrimaryKey(autoGenerate = true) val reactionId: Int = 0,
    @ColumnInfo val reactionTime: String,               //format of yyyy-MM-dd HH:mm so that it can be sorted into chronological order
    @ColumnInfo val severity: String,                   //severity of the reaction: mild, medium, or severe
)
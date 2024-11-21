package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val date: String,
    @ColumnInfo val time: String,
    @ColumnInfo val severity: String,
)
package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reaction(
    @PrimaryKey(autoGenerate = true) val reaction_id: Int = 0,
    @ColumnInfo val reactionTime: String,               //format of yyyy-MM-dd HH:mm so that it can be sorted into chronological order
    @ColumnInfo val severity: String,                   //severity of the reaction: mild, medium, or severe
)
package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val symptoms: String,                   //unused currently, but is planned to track the specific symptoms experienced, to compare with common reactions to certain allergens
    @ColumnInfo val reactionTime: String,               //format of yyyy-MM-dd HH:mm so that it can be sorted into chronological order
    @ColumnInfo val severity: String,                   //severity of the reaction: mild, medium, or severe
)
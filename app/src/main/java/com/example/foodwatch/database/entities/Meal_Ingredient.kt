package com.example.foodwatch.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "meal_ingredient",
    foreignKeys = [
        ForeignKey(
            entity = Ingredient::class,
            parentColumns = ["pk_i_id"],
            childColumns = ["fk_i_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["pk_m_id"],
            childColumns = ["fk_m_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MealIngredient(
    @ColumnInfo(name = "fk_m_id") val fkMid: Int,
    @ColumnInfo(name = "fk_i_id") val fkIid: Int
)
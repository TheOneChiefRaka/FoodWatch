package com.example.foodwatch.database.entities.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.entities.Meal

@Entity(primaryKeys = ["mealId", "ingredientId"])
data class MealIngredientCrossRef (
    val mealId: Int,
    val ingredientId: Int
)

data class MealWithIngredients(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "ingredientId",
        associateBy = Junction(MealIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)

data class IngredientWithMeal(
    @Embedded val ingredient: Ingredient,
    @Relation(
        parentColumn = "ingredientId",
        entityColumn = "mealId",
        associateBy = Junction(MealIngredientCrossRef::class)
    )
    val meals: List<Meal>
)
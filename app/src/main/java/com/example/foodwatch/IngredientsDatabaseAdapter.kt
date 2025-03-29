package com.example.foodwatch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.entities.Ingredient

class IngredientsDatabaseAdapter(private val onDeleteClicked: (Ingredient) -> Unit) : RecyclerView.Adapter<IngredientsDatabaseAdapter.IngredientViewHolder>() {

    private val ingredients = mutableListOf<Ingredient>()

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientTitle: TextView = itemView.findViewById(R.id.ingredientTitle)
        val deleteButton: Button = itemView.findViewById(R.id.deleteIngredientDatabase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_database_names, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientTitle.text = ingredient.name

        holder.deleteButton.setOnClickListener{
            deleteIngredient(ingredient)
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun updateIngredients(newIngredients: List<Ingredient>) {
        ingredients.clear()
        ingredients.addAll(newIngredients)
        notifyDataSetChanged()
    }

    fun deleteIngredient(ingredient: Ingredient) {
        Log.d("IngredientsTab", "I'm going to delete ${ingredient.name}!")
    }
}

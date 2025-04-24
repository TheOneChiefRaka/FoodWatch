package com.example.foodwatch

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.entities.Ingredient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class IngredientsDatabaseAdapter(private val lifecycleScope: CoroutineScope) : RecyclerView.Adapter<IngredientsDatabaseAdapter.IngredientViewHolder>() {

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
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete ${ingredient.name}?")
                .setPositiveButton("Yes"){ dialog, _ ->
                    lifecycleScope.launch {
                        val ingredientDao = (holder.itemView.context.applicationContext as MealsApplication).database.ingredientDao()
                        ingredientDao.delete(ingredient)
                        deleteIngredient(ingredient)
                }
            }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
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
        val index = ingredients.indexOfFirst { it.name == ingredient.name }

        if (index >= 0){
            ingredients.removeAt(index)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, ingredients.size)
        }

    }
}

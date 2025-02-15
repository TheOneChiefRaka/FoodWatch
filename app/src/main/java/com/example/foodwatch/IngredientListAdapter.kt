package com.example.foodwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientListAdapter(
    var ingredients: List<Ingredient>
) : RecyclerView.Adapter<IngredientListAdapter.AddedIngredientViewHolder>() {

    inner class AddedIngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedIngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_list, parent, false)
        return AddedIngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddedIngredientViewHolder, position: Int) {
        holder.itemView.apply{
            findViewById<TextView>(R.id.ingredientTitle).text = ingredients[position].title
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }
}
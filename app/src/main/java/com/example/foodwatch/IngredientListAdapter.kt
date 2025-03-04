package com.example.foodwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientListAdapter(
    private var ingredients: MutableList<Ingredient>
) : RecyclerView.Adapter<IngredientListAdapter.AddedIngredientViewHolder>() {

    inner class AddedIngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ingredientTitle: TextView = itemView.findViewById(R.id.ingredientTitle)
        val deleteButton: Button = itemView.findViewById(R.id.deleteIngredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedIngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_list, parent, false)
        return AddedIngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddedIngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientTitle.text = ingredient.title

        holder.deleteButton.setOnClickListener{
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun removeItem(position: Int){
        ingredients.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, ingredients.size)
    }

    fun updateIngredients(newIngredients: List<Ingredient>){
        ingredients.clear()
        ingredients.addAll(newIngredients)
        notifyDataSetChanged()
    }

    fun getIngredients(): List<Ingredient>{
        return ingredients
    }

}
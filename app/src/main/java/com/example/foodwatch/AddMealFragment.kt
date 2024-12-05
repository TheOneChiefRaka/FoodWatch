package com.example.foodwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodwatch.database.repository.IngredientsRepository
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory

class AddMealFragment : Fragment(R.layout.fragment_typemeals) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_typemeals, container, false)

        val dao = (requireActivity().application as MealsApplication).database.ingredientDao()
        val repository = IngredientsRepository(dao)
        var viewModel = ViewModelProvider(
            this,
            IngredientViewModelFactory(repository)
        ).get(IngredientViewModel::class.java)

        var mealNameInput = view.findViewById<EditText>(R.id.mealName) // Meal name
        val ingredientInput = view.findViewById<EditText>(R.id.mealIngredientInput) // Ingredient name
        val enterMealButton = view.findViewById<Button>(R.id.addMealToTableButton) // Add meal button
        val enterIngredientButton = view.findViewById<Button>(R.id.addIngredientButton) // Add ingredient button
        val ingredients = mutableListOf<String>() // List of ingredients

        enterIngredientButton.setOnClickListener() {
            val ingredientText = ingredientInput.text.toString().trim()
            if (ingredientText.isNotEmpty()) {
                ingredients.add(ingredientText)
                ingredientInput.text.clear()
                Toast.makeText(requireContext(), "Ingredient added: $ingredientText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter an ingredient", Toast.LENGTH_SHORT).show()
            }
        }

        enterMealButton.setOnClickListener() {
            if (ingredients.isEmpty()){
                Toast.makeText(requireContext(), "Please enter ingredients before submitting!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                val mealName = mealNameInput.text.toString().trim()
                println("Adding $mealName to meal table. List of ingredients: ${ingredients.joinToString(", ")}")

                viewModel.addIngredientsToTable(ingredients.toString())

                Toast.makeText(requireContext(), "Ingredients saved to table!", Toast.LENGTH_SHORT).show()
                ingredients.clear()
            }

        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

package com.example.foodwatch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import kotlinx.coroutines.launch


class IngredientsTab : Fragment(R.layout.fragment_ingredients_tab) {

    private val ingredientViewModel: IngredientViewModel by viewModels {
        IngredientViewModelFactory((activity?.application as MealsApplication).ingredients_repository)
    }

    private lateinit var adapter: IngredientsDatabaseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("IngredientsTab", "onViewCreated called")

        val recyclerView = view.findViewById<RecyclerView>(R.id.ingredientsRecyclerView)
        val adapter = IngredientsDatabaseAdapter { ingredientToDelete -> viewLifecycleOwner.lifecycleScope.launch {
            ingredientViewModel.deleteIngredient(ingredientToDelete)
            //adapter.deleteIngredient(ingredientToDelete)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch{
            val ingredients = ingredientViewModel.getIngredientNames().await()
            adapter.updateIngredients(ingredients)
        }

    }
}

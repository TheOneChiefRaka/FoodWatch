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

class ReactionsTab : Fragment(R.layout.fragment_reactions_tab) {

    private val ingredientViewModel: IngredientViewModel by viewModels {
        IngredientViewModelFactory((activity?.application as MealsApplication).ingredients_repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ReactionsTab", "onViewCreated called")

        val recyclerView = view.findViewById<RecyclerView>(R.id.reportsRecyclerList)
        val adapter = ReportListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        /*
        lifecycleScope.launch{
            val ingredients = ingredientViewModel.findAllPossibleAllergens().await()
            Log.d("ReactionsTab", "Loaded ${ingredients.size} ingredients")

            ingredients.forEach{
                Log.d("ReactionsTab", "Ingredient: ${it.name}, timesEaten=${it.timesEaten}")
            }
            adapter.submitList(ingredients)
        }
         */
    }
}
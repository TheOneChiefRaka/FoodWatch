package com.example.foodwatch

import android.os.Bundle
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.ingredientsRecyclerView)
        val adapter = IngredientsDatabaseAdapter(viewLifecycleOwner.lifecycleScope)

        val ingredientViewModel: IngredientViewModel by viewModels {
            IngredientViewModelFactory((activity?.application as MealsApplication).ingredients_repository)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch{
            val ingredients = ingredientViewModel.getIngredientNames().await()
            adapter.updateIngredients(ingredients)
        }

    }
}

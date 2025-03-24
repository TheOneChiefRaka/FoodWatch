package com.example.foodwatch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory


class IngredientsTab : Fragment(R.layout.fragment_ingredients_tab) {

    private val ingredientViewModel: IngredientViewModel by viewModels {
        IngredientViewModelFactory((activity?.application as MealsApplication).ingredients_repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("IngredientsTab", "onViewCreated called")
    }
}

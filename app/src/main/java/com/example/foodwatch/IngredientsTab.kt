package com.example.foodwatch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import kotlinx.coroutines.launch


class IngredientsTab : Fragment(R.layout.fragment_ingredients_tab) {

    private var fullIngredientList: List<Ingredient> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val recyclerView = view.findViewById<RecyclerView>(R.id.ingredientsRecyclerView)
        val adapter = IngredientsDatabaseAdapter(viewLifecycleOwner.lifecycleScope)

        val ingredientViewModel: IngredientViewModel by viewModels {
            IngredientViewModelFactory((activity?.application as MealsApplication).ingredients_repository)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch{
            val ingredients = ingredientViewModel.getIngredientNames().await()
            fullIngredientList = ingredients
            adapter.updateIngredients(ingredients)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){
                val query = s.toString().trim()
                val filteredList = if (query.isEmpty()){
                    fullIngredientList
                } else {
                    fullIngredientList.filter { it.name.contains(query, ignoreCase = true) }
                }
                adapter.updateIngredients(filteredList)
            }
        })
    }
}

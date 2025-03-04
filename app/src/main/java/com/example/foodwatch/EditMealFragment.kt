package com.example.foodwatch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.MealViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditMealFragment : Fragment(R.layout.fragment_editmeal) {
    val args: EditMealFragmentArgs by navArgs()

    val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealsApplication).meals_repository)
    }

    val ingredientViewModel: IngredientViewModel by viewModels {
        IngredientViewModelFactory((activity?.application as MealsApplication).ingredients_repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate view
        val view: View = inflater.inflate(R.layout.fragment_editmeal, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get nav args
        val mealId = args.mealId
        val mealNameView = view.findViewById<EditText>(R.id.mealName)
        val mealTimeView = view.findViewById<EditText>(R.id.mealTime)
        val mealDateView = view.findViewById<EditText>(R.id.mealDate)
        val ingredientsList = view.findViewById<RecyclerView>(R.id.ingredientList)
        val enterIngredientButton = view.findViewById<Button>(R.id.addIngredientButton)
        val ingredientInput = view.findViewById<EditText>(R.id.mealIngredientInput)
        val saveButton = view.findViewById<Button>(R.id.saveEditsButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        val ingredientMutableList = mutableListOf<Ingredient>()
        val ingredients = mutableListOf<String>() // List of ingredients

        val adapter = IngredientListAdapter(ingredientMutableList)
        ingredientsList.adapter = adapter
        ingredientsList.layoutManager = LinearLayoutManager(this.context)

        //launch asynchronous database query
        lifecycleScope.launch {
            val meal = mealViewModel.getMealById(mealId).await()
            mealNameView.setText(meal.name)
            mealTimeView.setText(meal.timeEaten.takeLast(5))
            mealDateView.setText(meal.timeEaten.take(10))
            for(ingredientId in meal.ingredients) {
                val ingredient = ingredientViewModel.findIngredientById(ingredientId).await()
                ingredientMutableList.add(Ingredient(ingredient.name))
                adapter.notifyItemInserted(ingredientMutableList.size)
                ingredients.add(ingredient.name)
            }
            Log.i("TEST", ingredientMutableList.toString())
        }

        enterIngredientButton.setOnClickListener {
            val title = ingredientInput.text.toString().trim()
            val ingredientToAdd = Ingredient(title)
            val ingredientText = ingredientInput.text.toString().trim()
            if (ingredientText.isNotEmpty()) {
                ingredientMutableList.add(ingredientToAdd)
                adapter.notifyItemInserted(ingredientMutableList.size)
                ingredients.add(ingredientText)
                ingredientInput.text.clear()
                Toast.makeText(requireContext(), "Ingredient added: $ingredientText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter an ingredient", Toast.LENGTH_SHORT).show()
            }
        }

        suspend fun submitMeal() {
            val mealName = mealNameView.text.toString().trim()
            val mealTime = mealTimeView.text.toString().trim()
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date: String = LocalDateTime.now().format(dateFormat)

            val updatedIngredients = adapter.getIngredients()

            if (updatedIngredients.isEmpty()){
                Toast.makeText(requireContext(), "No ingredients to save!", Toast.LENGTH_SHORT).show()
                return
            }

            val ingredientIds = mutableListOf<Int>()

            for (ingredient in updatedIngredients){
                ingredientViewModel.addOrUpdateIngredients(ingredient.title).join()
                val ingredientId = ingredientViewModel.getIngredientIdByName(ingredient.title).await()
                if(ingredientId != null){
                    ingredientIds.add(ingredientId)
                }
                else{
                    Toast.makeText(requireContext(), "Error finding ingredientID!", Toast.LENGTH_LONG).show()
                }
            }

            val meal = Meal(
                id = mealId,
                name = mealName,
                timeEaten = "$date $mealTime",
                ingredients = ingredientIds
            )

            mealViewModel.updateMealById(meal) {
                Toast.makeText(requireContext(), "Meal edited!", Toast.LENGTH_SHORT).show()
            }
        }

        saveButton.setOnClickListener {
            if (ingredients.isEmpty()){
                Toast.makeText(requireContext(), "Please enter ingredients before submitting!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if (mealTimeView.text.isEmpty()){
                Toast.makeText(requireContext(), "Please enter a time before submitting!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if (mealNameView.text.isEmpty()){
                Toast.makeText(requireContext(), "Please enter the meal name before submitting!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                lifecycleScope.launch { submitMeal() }
            }

        }

        deleteButton.setOnClickListener {
            mealViewModel.deleteMealById(mealId)
            findNavController().navigateUp()
        }
    }
}
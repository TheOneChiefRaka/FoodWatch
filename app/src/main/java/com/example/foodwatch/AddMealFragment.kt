package com.example.foodwatch

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.repository.IngredientsRepository
import com.example.foodwatch.database.repository.MealsRepository
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.MealViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddMealFragment : Fragment(R.layout.fragment_typemeals) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_typemeals, container, false)

        val ingredientDao = (requireActivity().application as MealsApplication).database.ingredientDao()
        val ingredientRepository = IngredientsRepository(ingredientDao)
        var ingredientViewModel = IngredientViewModel(ingredientRepository)

        val mealDao = (requireActivity().application as MealsApplication).database.mealDao()
        val mealRepository = MealsRepository(mealDao)
        var mealViewModel = MealViewModel(mealRepository)

        var mealNameInput = view.findViewById<EditText>(R.id.mealName) // Meal name
        var timeInput = view.findViewById<EditText>(R.id.mealTime) // Time of meal eaten
        val ingredientInput = view.findViewById<EditText>(R.id.mealIngredientInput) // Ingredient name
        val enterMealButton = view.findViewById<Button>(R.id.addMealToTableButton) // Add meal button
        val enterIngredientButton = view.findViewById<Button>(R.id.addIngredientButton) // Add ingredient button
        val ingredients = mutableListOf<String>() // List of ingredients
        val ingredientIds = mutableListOf<Int>() // List of ingredient IDs
        val mealDate = view.findViewById<EditText>(R.id.mealDate) // Date of meal eaten

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        timeInput.setText(String.format("%02d:%02d", hour, minute))

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Update time with user selected time
            timeInput.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, true)

        timeInput.setOnClickListener {
            timePickerDialog.show()
        }

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

        suspend fun submitMeal() {
            val mealName = mealNameInput.text.toString().trim()
            val mealTime = timeInput.text.toString().trim()
            val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var date: String = LocalDateTime.now().format(dateFormat)
            var ingredientName = ""

            var position = 0 // Counter for inserting ingredients into table
            for (ingredient in ingredients){
                ingredientViewModel.addOrUpdateIngredients(ingredients[position]).join()
                position++
            }
            position = 0


            for (ingredient in ingredients) {
                ingredientName = ingredients[position]
                val ingredientId = ingredientViewModel.getIngredientIdByName(ingredientName).await()
                if (ingredientId != null) {
                    ingredientIds.add(ingredientId)
                } else {
                    Toast.makeText(requireContext(), "Error finding ingredient ID!", Toast.LENGTH_LONG).show()
                }

                position++
            }
            position = 0

            val meal = Meal(
                name = mealName,
                timeEaten = "$date $mealTime",
                ingredients = ingredientIds
            )

            mealViewModel.addMeal(meal) { mealId ->
                Toast.makeText(requireContext(), "Meal added!", Toast.LENGTH_SHORT).show()
            }

            ingredients.clear()
            timeInput.text.clear()
            mealNameInput.text.clear()
        }

        enterMealButton.setOnClickListener() {
            if (ingredients.isEmpty()){
                Toast.makeText(requireContext(), "Please enter ingredients before submitting!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if (timeInput.text.isEmpty()){
                Toast.makeText(requireContext(), "Please enter a time before submitting!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if (mealNameInput.text.isEmpty()){
                Toast.makeText(requireContext(), "Please enter the meal name before submitting!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else {
                lifecycleScope.launch { submitMeal() }
            }

        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

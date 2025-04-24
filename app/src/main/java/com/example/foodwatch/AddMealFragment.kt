package com.example.foodwatch

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.entities.relations.MealIngredientCrossRef
import com.example.foodwatch.database.repository.IngredientsRepository
import com.example.foodwatch.database.repository.MealIngredientRepository
import com.example.foodwatch.database.repository.MealsRepository
import com.example.foodwatch.database.repository.ReactionsRepository
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.MealIngredientViewModel
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.ReactionViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class AddMealFragment : Fragment(R.layout.fragment_addmeal) {

    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_addmeal, container, false)

        val ingredientDao = (requireActivity().application as MealsApplication).database.ingredientDao()
        val ingredientRepository = IngredientsRepository(ingredientDao)
        var ingredientViewModel = IngredientViewModel(ingredientRepository)

        val mealDao = (requireActivity().application as MealsApplication).database.mealDao()
        val mealRepository = MealsRepository(mealDao)
        var mealViewModel = MealViewModel(mealRepository)

        val mealIngredientCrossRefDao = (requireActivity().application as MealsApplication).database.mealIngredientDao()
        val mealIngredientRepository = MealIngredientRepository(mealIngredientCrossRefDao)
        val mealIngredientViewModel = MealIngredientViewModel(mealIngredientRepository)

        val reactionDao = (requireActivity().application as MealsApplication).database.reactionDao()
        val reactionsRepository = ReactionsRepository(reactionDao)
        val reactionViewModel = ReactionViewModel(reactionsRepository)

        var mealNameInput = view.findViewById<EditText>(R.id.mealName) // Meal name
        var timeInput = view.findViewById<EditText>(R.id.mealTime) // Time of meal eaten
        val ingredientInput = view.findViewById<AutoCompleteTextView>(R.id.mealIngredientInput) // Ingredient name
        val enterMealButton = view.findViewById<Button>(R.id.addMealToTableButton) // Add meal button
        val enterIngredientButton = view.findViewById<Button>(R.id.addIngredientButton) // Add ingredient button


        val ingredients = mutableListOf<String>() // List of ingredients


        val ingredientIds = mutableListOf<Int>() // List of ingredient IDs
        val mealDate = view.findViewById<EditText>(R.id.mealDate) // Date of meal eaten
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        mealDate.setText(LocalDate.now().format(dateFormat).toString())

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        timeInput.setText(String.format("%02d:%02d", hour, minute))

        // Date picker
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            var viewDay = selectedDay.toString()
            var viewMonth = (selectedMonth+1).toString()
            if(selectedDay < 10) {
                viewDay = "0$viewDay"
            }
            if(selectedMonth < 9) {
                viewMonth = "0$viewMonth"
            }
            mealDate.setText("$selectedYear-${viewMonth}-$viewDay")
        }, year, month, day)

        mealDate.setOnClickListener {
            datePickerDialog.show()
        }

        // Time picker
        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Update time with user selected time
            timeInput.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, true)

        timeInput.setOnClickListener {
            timePickerDialog.show()
        }

        // Add ingredient
        val adapter = IngredientListAdapter(ingredients)
        //ingredientList.adapter = adapter
        view.findViewById<RecyclerView>(R.id.ingredientList).adapter = adapter
        view.findViewById<RecyclerView>(R.id.ingredientList).layoutManager = LinearLayoutManager(this.context)

        lifecycleScope.launch {
            val ingredientNames: List<String> = ingredientViewModel.getAllIngredientNames().await()
            setupAutoComplete(ingredientInput, ingredientNames)
        }


        enterIngredientButton.setOnClickListener {
            val title = ingredientInput.text.toString().trim()
            val ingredientText = ingredientInput.text.toString().trim()
            //no empty ingredient names, no duplicates
            if (ingredientText.isNotEmpty() && !adapter.getIngredients().contains(title)) {
                ingredients.add(ingredientText)
                adapter.notifyItemInserted(ingredients.size)
                ingredientInput.text.clear()
                Toast.makeText(requireContext(), "Ingredient added: $ingredientText", Toast.LENGTH_SHORT).show()
            } else {
                if(!adapter.getIngredients().contains(title))
                    Toast.makeText(requireContext(), "Ingredient already entered", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), "Please enter an ingredient", Toast.LENGTH_SHORT).show()
            }
        }

        //executes when autocomplete option is clicked
        ingredientInput.setOnItemClickListener {listAdapter, view, pos, id ->
            val title = listAdapter.getItemAtPosition(pos).toString()
            val ingredientText = ingredientInput.text.toString().trim()
            //no empty ingredient names, no duplicates
            if (ingredientText.isNotEmpty() && !adapter.getIngredients().contains(title)) {
                ingredients.add(ingredientText)
                adapter.notifyItemInserted(ingredients.size)
                ingredientInput.text.clear()
                Toast.makeText(requireContext(), "Ingredient added: $ingredientText", Toast.LENGTH_SHORT).show()
            } else {
                if(adapter.getIngredients().contains(title))
                    Toast.makeText(requireContext(), "Ingredient already entered", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), "Please enter an ingredient", Toast.LENGTH_SHORT).show()
            }
        }

        // This is called when the submit meal button is pressed
        suspend fun submitMeal() {
            val mealName = mealNameInput.text.toString().trim()
            val mealTime = timeInput.text.toString().trim()
            var date: String = mealDate.text.toString()
            if(date == "")
                date = LocalDate.now().format(dateFormat)

            val updatedIngredients = adapter.getIngredients()

            // If the user did not enter any ingredients show an error
            if (updatedIngredients.isEmpty()){
                Toast.makeText(requireContext(), "No ingredients to save!", Toast.LENGTH_SHORT).show()
                return
            }

            val ingredientIds = mutableListOf<Int>()

            for (ingredient in updatedIngredients){
                ingredientViewModel.addOrUpdateIngredients(ingredient).join()
                val ingredientId = ingredientViewModel.getIngredientIdByName(ingredient).await()
                if(ingredientId != null){
                    ingredientIds.add(ingredientId.toInt())
                }
                else{
                    Toast.makeText(requireContext(), "Error finding ingredientID!", Toast.LENGTH_LONG).show()
                }
            }

            //check if there is already a reaction within 3 hours of the meal
            var reactionId: Int? = null
            val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val maxTimestamp = LocalDateTime.parse("$date $mealTime", dateTimeFormat).toEpochSecond(ZoneOffset.UTC) + 3600 * 2
            val maxTime = LocalDateTime.ofEpochSecond(maxTimestamp, 0, ZoneOffset.UTC)
            val futureReactions = reactionViewModel.findReactionsByTimeRange("$date $mealTime", maxTime.format(dateTimeFormat)).await()
            if(futureReactions.isNotEmpty()) {
                //there should only be one
                reactionId = futureReactions[0].reactionId
            }

            val meal = Meal(
                name = mealName,
                timeEaten = "$date $mealTime",
                reactionId = reactionId
            )

            var newMealId = 0

            mealViewModel.addMeal(meal) { mealId ->
                Toast.makeText(requireContext(), "Meal added!", Toast.LENGTH_SHORT).show()
                newMealId = mealId.toInt()
            }.await()


            val mealIngredientList = mutableListOf<MealIngredientCrossRef>()

            for(ingredient in ingredientIds) {
                var newIngredient = MealIngredientCrossRef(newMealId, ingredient)
                mealIngredientList.add(newIngredient)
            }
            //insert list of references
            mealIngredientViewModel.insertIngredientsList(mealIngredientList)

            adapter.updateIngredients(emptyList())
            timeInput.text.clear()
            mealNameInput.text.clear()
            findNavController().navigateUp()
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

    private fun setupAutoComplete(ingredientInput: AutoCompleteTextView, ingredientNames: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ingredientNames)
        ingredientInput.setAdapter(adapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

package com.example.foodwatch

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.Date
import java.util.Locale

class ReactionsTab : Fragment(R.layout.fragment_reactions_tab) {

    private val ingredientViewModel: IngredientViewModel by viewModels {
        IngredientViewModelFactory((activity?.application as MealsApplication).ingredients_repository)
    }

    private var startDate: Date? = null
    private var endDate: Date? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ReactionsTab", "onViewCreated called")

        // Added buttons
        val startDateButton = view.findViewById<Button>(R.id.btn_start)
        val endDateButton = view.findViewById<Button>(R.id.btn_end)

        // Setting click listener for start date button
        startDateButton.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                startDate = selectedDate
                // Update the button text with new date
                startDateButton.text = SimpleDateFormat("d MMM yy", Locale.ENGLISH).format(selectedDate)
            }
        }

        // Setting click listener for end date button
        endDateButton.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                endDate = selectedDate
                // Update the button text with new date
                endDateButton.text = SimpleDateFormat("d MMM yy", Locale.ENGLISH).format(selectedDate)
            }
        }

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

    private fun showDatePickerDialog(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
            onDateSelected(selectedDate)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }
}
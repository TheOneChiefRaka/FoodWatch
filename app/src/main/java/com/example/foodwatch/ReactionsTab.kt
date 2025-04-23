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
import com.example.foodwatch.database.viewmodel.ReactionViewModel
import com.example.foodwatch.database.viewmodel.ReactionViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.DatePicker
import com.example.foodwatch.database.entities.Reaction
import java.util.Date
import java.util.Locale
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory


class ReactionsTab : Fragment(R.layout.fragment_reactions_tab) {

    private val reactionViewModel: ReactionViewModel by viewModels {
        ReactionViewModelFactory((activity?.application as MealsApplication).reactions_repository)
    }

    private val ingredientViewModel: IngredientViewModel by viewModels {
        IngredientViewModelFactory((activity?.application as MealsApplication).ingredients_repository)
    }

    private lateinit var reportListAdapter: ReportListAdapter
    private var startDate: Date? = null
    private var endDate: Date? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
//    private val currentDate = Date()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ReactionsTab", "onViewCreated called")

        // Added buttons
        val startDateButton = view.findViewById<Button>(R.id.btn_start)
        val endDateButton = view.findViewById<Button>(R.id.btn_end)

        // Load all reactions
        loadReportData()

        // Setting click listener for start date button
        startDateButton.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                startDate = selectedDate
                // Update the button text with new date
                startDateButton.text = SimpleDateFormat("dd-MM-yy", Locale.ENGLISH).format(selectedDate)
                updateReactionsList()
            }
        }

        // Setting click listener for end date button
        endDateButton.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                endDate = selectedDate
                // Update the button text with new date
                endDateButton.text = SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH).format(selectedDate)
                updateReactionsList()
            }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.reportsRecyclerList)
        reportListAdapter = ReportListAdapter()
        recyclerView.adapter = reportListAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateReactionsList() {
        if (startDate == null && endDate == null) {
            // Load all reactions
            loadReportData()
        }
        else if (startDate != null && endDate != null) {
            val startDateString = dateFormat.format(startDate!!)
            val endDateString = dateFormat.format(endDate!!)
            // Only one day
            if (startDateString == endDateString) {
                //loadReactionsForDate(startDateString)
            } else {
                //loadReactionsForTimeRange("$startDateString 00:00", "$endDateString 23:59")
            }
        }
    }

    private fun loadReportData() {
        lifecycleScope.launch { reportListAdapter.submitList(ingredientViewModel.getIngredientData().await()) }
    }

    private fun showDatePickerDialog(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(requireContext(), { _: DatePicker, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
            onDateSelected(selectedDate)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }
}
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
import com.example.foodwatch.database.entities.ReactionWithIngredients
import java.util.Date
import java.util.Locale
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe


class ReactionsTab : Fragment(R.layout.fragment_reactions_tab) {

    private val reactionViewModel: ReactionViewModel by viewModels {
        ReactionViewModelFactory((activity?.application as MealsApplication).reactions_repository)
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
        loadAllReactions()

        // Setting click listener for start date button
        startDateButton.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                startDate = selectedDate
                // Update the button text with new date
                startDateButton.text = SimpleDateFormat("d MMM yy", Locale.ENGLISH).format(selectedDate)
                updateReactionsList()
            }
        }

        // Setting click listener for end date button
        endDateButton.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                endDate = selectedDate
                // Update the button text with new date
                endDateButton.text = SimpleDateFormat("d MMM yy", Locale.ENGLISH).format(selectedDate)
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
            loadAllReactions()
        }
        else if (startDate != null && endDate != null) {
            val startDateString = dateFormat.format(startDate!!)
            val endDateString = dateFormat.format(endDate!!)
            // Only one day
            if (startDateString == endDateString) {
                loadReactionsForDate(startDateString)
            } else {
                loadReactionsForTimeRange("$startDateString 00:00", "$endDateString 23:59")
            }
        }
    }

//    private fun updateReactionsList() {
//        if (startDate != null && endDate != null) {
//            val startDateString = dateFormat.format(startDate!!)
//            val endDateString = dateFormat.format(endDate!!)
//
//            // Only one day
//            if (startDateString == endDateString) {
//                loadReactionsForDate(startDateString)
//            } else {
//                lifecycleScope.launch {
//                    val reactions = reactionViewModel.findReactionsByTimeRange(
//                        "$startDateString 00:00",
//                        "$endDateString 23:59"
//                    ).await()
//
//                    val reactionsWithIngredients = mutableListOf<ReactionWithIngredients>()
//                    for (reaction in reactions) {
//                        val ingredientCounts = reactionViewModel.getIngredientReactionCounts(reaction.reactionId)
//                        reactionsWithIngredients.add(
//                            ReactionWithIngredients(
//                                reaction.severity,
//                                ingredientCounts
//                            )
//                        )
//                    }
//                    reportListAdapter.submitList(reactionsWithIngredients)
//                }
//            }
//        }
//    }
    private fun loadAllReactions() {
        reactionViewModel.getAllReactions().observe(viewLifecycleOwner, Observer { reactionsWithIngredients ->
            reportListAdapter.submitList(reactionsWithIngredients)
        })
    }

    private fun loadReactionsForDate(date: String) {
        lifecycleScope.launch {
            reactionViewModel.getReactionsWithIngredientsByDate(date).observe(
                viewLifecycleOwner
            ) { reactionsWithIngredients ->
                reportListAdapter.submitList(reactionsWithIngredients)
            }
        }
    }

    private fun loadReactionsForTimeRange(min: String, max: String) {
        lifecycleScope.launch {
            val reactions = reactionViewModel.findReactionsByTimeRange(min, max).await()

            val reactionsWithIngredients = mutableListOf<ReactionWithIngredients>()
            for (reaction in reactions) {
                val ingredientCounts = reactionViewModel.getIngredientReactionCounts(reaction.reactionId)
                reactionsWithIngredients.add(
                    ReactionWithIngredients(
                        reaction.severity,
                        ingredientCounts
                    )
                )
            }
            reportListAdapter.submitList(reactionsWithIngredients)
        }
    }

//    private fun updateReactionsList() {
//        if (startDate != null && endDate != null) {
//            val startDateString = dateFormat.format(startDate!!)
//            val endDateString = dateFormat.format(endDate!!)
//
//            // If only one day is selected
//            if (startDateString == endDateString) {
//                loadReactionsForDate(startDateString)
//            } else {
//                lifecycleScope.launch {
//                    val reactions = reactionViewModel.findReactionsByTimeRange (
//                        "$startDateString 00:00",
//                        "$endDateString 23:59"
//                    ).await()
//
//                    val reactionsWithIngredients = mutableListOf<ReactionWithIngredients>()
//                    for (reaction in reactions) {
//                        val ingredientCounts =
//                            reactionViewModel.getIngredientReactionCounts(reaction.reactionId)
//                        reactionsWithIngredients.add(
//                            ReactionWithIngredients(
//                                reaction.severity,
//                                ingredientCounts
//                            )
//                        )
//                    }
//                }
//            }
//        }
//    }

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
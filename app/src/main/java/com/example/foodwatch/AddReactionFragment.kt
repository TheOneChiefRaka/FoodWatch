package com.example.foodwatch

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentController
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.entities.Reaction
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.MealViewModelFactory
import com.example.foodwatch.database.viewmodel.ReactionViewModel
import com.example.foodwatch.database.viewmodel.ReactionViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MealNotFoundDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder.setMessage("A reaction cannot be more than 3 hours after a meal. Please add a meal or correct the date and time fields.")
                .setPositiveButton("Add Meal") { dialog, id ->
                    //navigate to addmeal page
                    this.findNavController().navigate(R.id.addReaction_to_addMeal)
                }
                .setNegativeButton("Close") { dialog, id ->
                    //close dialog
                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class AddReactionFragment : Fragment() {

    val reactionViewModel: ReactionViewModel by viewModels {
        ReactionViewModelFactory((activity?.application as MealsApplication).reactions_repository)
    }

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
        val view: View = inflater.inflate(R.layout.fragment_addreaction, container, false)

        //get navFragment
        val navFragment = activity?.supportFragmentManager?.findFragmentById(R.id.navFragment) as NavHostFragment

        //get form fields
        val reactionDate = view.findViewById<EditText>(R.id.reactionDate)
        val addReactionButton = view.findViewById<Button>(R.id.addReactionButton)
        val reactionTimeField = view.findViewById<EditText>(R.id.reactionTime)
        val reactionSeverityField = view.findViewById<Spinner>(R.id.reactionSeverity)

        val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        reactionDate.setText(LocalDate.now().format(dateFormat).toString())

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        reactionTimeField.setText(String.format("%02d:%02d", hour, minute))

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
            reactionDate.setText("$selectedYear-${viewMonth}-$viewDay")
        }, year, month, day)

        reactionDate.setOnClickListener {
            datePickerDialog.show()
        }

        // Time picker
        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Update time with user selected time
            reactionTimeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, true)

        reactionTimeField.setOnClickListener {
            timePickerDialog.show()
        }

        suspend fun updateMeals() {
            //get the timestamp 3 hours before the reaction
            val reactionTime = LocalDateTime.parse("${reactionDate.text} ${reactionTimeField.text}", dateTimeFormat).toEpochSecond(ZoneOffset.UTC)
            val minimumTimestamp = reactionTime - 3600 * 2 //3 hours before reaction
            val minTime = LocalDateTime.ofEpochSecond(minimumTimestamp, 0, ZoneOffset.UTC)

            //check whether any meals are present in the last 3 hours
            val recentMeals = mealViewModel.findMealsByTimeRange(minTime.format(dateTimeFormat), "${reactionDate.text} ${reactionTimeField.text}").await()
            if(recentMeals.isEmpty()) {
                MealNotFoundDialogFragment().show(parentFragmentManager, "MEAL_NOT_FOUND")
                return
            }

            val newReaction =
                Reaction(0, "${reactionDate.text} ${reactionTimeField.text}", reactionSeverityField.selectedItem.toString())
            val newReactionId = reactionViewModel.insert(newReaction).await()

            val updatedRecentMeals = mutableListOf<Meal>()

            //add reaction id to all meals within the last 3 hours
            for(meal in recentMeals) {
                mealViewModel.updateMealById(Meal(meal.mealId, newReactionId.toInt(), meal.timeEaten, meal.name), {})
            }
            navFragment.navController.navigate(R.id.to_home)
        }

        addReactionButton.setOnClickListener {
            if(reactionSeverityField.selectedItem.toString() != "" || reactionTimeField.text.toString() != "") {
                lifecycleScope.launch { updateMeals() }
            }
            else {
                //insert some error displaying code here
            }
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
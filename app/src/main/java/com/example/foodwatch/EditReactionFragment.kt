package com.example.foodwatch

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
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
import androidx.navigation.fragment.navArgs
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

class EditReactionFragment : Fragment() {
    val args: EditReactionFragmentArgs by navArgs()

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
        val view: View = inflater.inflate(R.layout.fragment_editreaction, container, false)

        //get navFragment
        val navFragment = activity?.supportFragmentManager?.findFragmentById(R.id.navFragment) as NavHostFragment

        //get form fields
        val reactionId = args.reactionId
        val reactionDate = view.findViewById<EditText>(R.id.reactionDate)
        val editReactionButton = view.findViewById<Button>(R.id.editReactionButton)
        val reactionTimeField = view.findViewById<EditText>(R.id.reactionTime)
        val reactionSeverityField = view.findViewById<Spinner>(R.id.reactionSeverity)
        val deleteReactionButton = view.findViewById<Button>(R.id.deleteReactionButton)

        //prevents manually editing
        reactionDate.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP)
                view.performClick()
            else
                false
        }
        reactionTimeField.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP)
                view.performClick()
            else
                false
        }

        //query reaction data
        var originalTime: String? = null

        lifecycleScope.launch {
            val reaction = reactionViewModel.findReactionById(reactionId).await()
            originalTime = reaction.reactionTime
            reactionTimeField.setText(reaction.reactionTime.takeLast(5))
            reactionDate.setText(reaction.reactionTime.take(10))
            reactionSeverityField.setSelection(
                when(reaction.severity) {
                    "Mild" -> 0
                    "Medium" -> 1
                    "Severe" -> 2
                    else -> 0
                }
            )
        }

        val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

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
            //if the time hasn't been changed, no reason to update meals
            if("${reactionDate.text} ${reactionTimeField.text}" != originalTime) {
                //remove the reaction from all meals currently
                mealViewModel.removeReactionFromMeals(reactionId).join()

                //get the timestamp 3 hours before the reaction
                val reactionTime = LocalDateTime.parse("${reactionDate.text} ${reactionTimeField.text}", dateTimeFormat).toEpochSecond(ZoneOffset.UTC)
                val minimumTimestamp = reactionTime - 3600 * 2 //3 hours before reaction
                val minTime = LocalDateTime.ofEpochSecond(minimumTimestamp, 0, ZoneOffset.UTC)

                //check whether any meals are present in the last 3 hours
                val recentMeals = mealViewModel.findMealsByTimeRange(minTime.format(dateTimeFormat), "${reactionDate.text} ${reactionTimeField.text}").await()
//                if(recentMeals.isEmpty()) {
//                    MealNotFoundDialogFragment().show(parentFragmentManager, "MEAL_NOT_FOUND")
//                    return
//                }

                //add reaction id to all meals within the last 3 hours
                for(meal in recentMeals) {
                    mealViewModel.updateMealById(Meal(meal.mealId, reactionId, meal.timeEaten, meal.name), {})
                }
            }

            //update reaction
            reactionViewModel.updateReactionById("${reactionDate.text} ${reactionTimeField.text}", reactionSeverityField.selectedItem.toString(), reactionId)

            navFragment.navController.navigateUp()
        }

        deleteReactionButton.setOnClickListener {
            reactionViewModel.deleteReaction(Reaction(reactionId, "${reactionDate.text} ${reactionTimeField.text}", reactionSeverityField.selectedItem.toString()))
            navFragment.navController.navigateUp()
        }

        editReactionButton.setOnClickListener {
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
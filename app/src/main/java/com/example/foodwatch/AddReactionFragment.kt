package com.example.foodwatch

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.entities.Reaction
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.MealViewModelFactory
import com.example.foodwatch.database.viewmodel.ReactionViewModel
import com.example.foodwatch.database.viewmodel.ReactionViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class AddReactionFragment : Fragment() {

    private lateinit var calendarField: CalendarView
    private lateinit var reactionTimeField: EditText

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
        val calendarField = view.findViewById<CalendarView>(R.id.calendarField)
        val addReactionButton = view.findViewById<Button>(R.id.toAddReactionButton)
        val reactionTimeField = view.findViewById<EditText>(R.id.reactionTime)
        val reactionSeverityField = view.findViewById<Spinner>(R.id.reactionSeverity)

        calendarField.maxDate = System.currentTimeMillis()
        val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var date: String = LocalDateTime.now().format(dateFormat)
        var time: String = LocalTime.now().toString().take(5)
        reactionTimeField.setText(time, TextView.BufferType.EDITABLE)

        //Log.i("TEST", LocalDateTime.parse("2024-12-02 15:00", formatter).toEpochSecond(ZoneOffset.UTC).toString())

        // TimePicker
        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Update time with user selected time
            reactionTimeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, LocalTime.now().hour, LocalTime.now().minute, true)

        // Show TimePicker
        reactionTimeField.setOnClickListener {
            timePickerDialog.show()
        }


        calendarField.setOnDateChangeListener { calendar, year, month, day ->
            var newDate = "$year"
            if(month < 10) {
                newDate += "-0${month+1}"
            }
            else {
                newDate += "-${month+1}"
            }
            if(day < 10) {
                newDate += "-0${day}"
            }
            else {
                newDate += "-${day}"
            }
            date = newDate
        }

        suspend fun updateMeals() {
            val newReaction =
                Reaction(0, "$date ${reactionTimeField.text}", reactionSeverityField.selectedItem.toString())
            val newReactionId = reactionViewModel.insert(newReaction).await()

            val reactionTime = LocalDateTime.parse("$date ${reactionTimeField.text}", dateTimeFormat).toEpochSecond(ZoneOffset.UTC)
            val minimumTimestamp = reactionTime - 3600 * 3 //3 hours before reaction
            val minTime = LocalDateTime.ofEpochSecond(minimumTimestamp, 0, ZoneOffset.UTC)
            val recentMeals = mealViewModel.findMealsByTimeRange(minTime.format(dateTimeFormat), "$date ${reactionTimeField.text}").await()
            val updatedRecentMeals = mutableListOf<Meal>()

            //add reaction id to all meals within the last 3 hours
            for(meal in recentMeals) {
                mealViewModel.updateMealById(Meal(meal.mealId, newReactionId.toInt(), meal.timeEaten, meal.name), {})
            }
        }

        addReactionButton.setOnClickListener {
            if(reactionSeverityField.selectedItem.toString() != "" || reactionTimeField.text.toString() != "") {
                lifecycleScope.launch { updateMeals() }
                navFragment.navController.navigate(R.id.to_home)
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

package com.example.foodwatch

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
import com.example.foodwatch.database.entities.Reaction
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.MealViewModelFactory
import com.example.foodwatch.database.viewmodel.ReactionViewModel
import com.example.foodwatch.database.viewmodel.ReactionViewModelFactory
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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

        suspend fun updateIngredients() {
            val newReaction =
                Reaction(0, "", "$date ${reactionTimeField.text}", reactionSeverityField.selectedItem.toString())
            reactionViewModel.insert(newReaction)

            val reactionTime = LocalDateTime.parse("$date ${reactionTimeField.text}", dateTimeFormat).toEpochSecond(ZoneOffset.UTC)
            val minimumTimestamp = reactionTime - 3600 * 3 //3 hours before reaction
            val minTime = LocalDateTime.ofEpochSecond(minimumTimestamp, 0, ZoneOffset.UTC)
            val recentMeals = mealViewModel.findMealsByTimeRange(minTime.format(dateTimeFormat), "$date ${reactionTimeField.text}").await()

            val recentIngredients = mutableSetOf<Int>()
            for(meal in recentMeals) {
                recentIngredients.addAll(meal.ingredients)
            }
            Log.i("TEST", recentIngredients.toString())
            when(reactionSeverityField.selectedItem.toString()) {
                "Mild" -> ingredientViewModel.addIngredientsReactionMild(recentIngredients.toList())
                "Medium" -> ingredientViewModel.addIngredientsReactionMedium(recentIngredients.toList())
                "Severe" -> ingredientViewModel.addIngredientsReactionSevere(recentIngredients.toList())
                else -> Log.e("FOODWATCH_ERR", "Received unexpected severity ${reactionSeverityField.selectedItem}")
            }
        }

        addReactionButton.setOnClickListener {
            if(reactionSeverityField.selectedItem.toString() != "" || reactionTimeField.text.toString() != "") {
                lifecycleScope.launch { updateIngredients() }
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

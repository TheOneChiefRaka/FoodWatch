package com.example.foodwatch

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddMealFragment : Fragment() {

    val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate view
        val view: View = inflater.inflate(R.layout.fragment_addmeal, container, false)


        //get navFragment
        val navFragment = activity?.supportFragmentManager?.findFragmentById(R.id.navFragment) as NavHostFragment

        //get form fields
        val calendarField = view.findViewById<CalendarView>(R.id.calendarField)
        val mealNameField = view.findViewById<EditText>(R.id.mealNameField)
        val addMealButton = view.findViewById<Button>(R.id.addMealButton)

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        var date: String = LocalDateTime.now().format(formatter)

        calendarField.setOnDateChangeListener { calendar, year, month, day ->
            date = "${month + 1}/$day/$year"
        }

        addMealButton.setOnClickListener {
            val newMeal = Meal(0, date, mealNameField.text.toString())
            mealViewModel.insert(newMeal)

            navFragment.navController.navigate(R.id.to_home)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

package com.example.foodwatch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddMealFragment : Fragment() {

    val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealsApplication).meals_repository)
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
        val addMealButton = view.findViewById<Button>(R.id.toAddReactionButton)
        val timeEatenField = view.findViewById<EditText>(R.id.reactionTime)

        calendarField.maxDate = System.currentTimeMillis()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var date: String = LocalDateTime.now().format(formatter)
        var time: String = LocalTime.now().toString().take(5)
        timeEatenField.setText(time, TextView.BufferType.EDITABLE)

        calendarField.setOnDateChangeListener { calendar, year, month, day ->
            date = "$year"
            if(month < 10) {
                date += "-0${month+1}"
            }
            else {
                date += "-${month+1}"
            }
            if(day < 10) {
                date += "-0${day}"
            }
            else {
                date += "-${day}"
            }
            Log.d("DATE", date)
        }

        addMealButton.setOnClickListener {
            if(mealNameField.text.toString() != "" || timeEatenField.text.toString() != "") {
                val newMeal =
                    Meal(0, date, mealNameField.text.toString(), timeEatenField.text.toString())
                mealViewModel.insert(newMeal)

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

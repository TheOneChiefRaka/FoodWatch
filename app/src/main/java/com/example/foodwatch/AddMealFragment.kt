package com.example.foodwatch

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.foodwatch.MealsDatabase
import java.time.LocalDate

class AddMealFragment : Fragment() {
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

        addMealButton.setOnClickListener {
            val newMeal = Meal(calendarField.date, mealNameField.text.toString())

            navFragment.navController.navigate(R.id.to_home)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

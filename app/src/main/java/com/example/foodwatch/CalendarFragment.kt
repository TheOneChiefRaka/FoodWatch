package com.example.foodwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.runBlocking

class CalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate view
        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)

        val mealViewModel: MealViewModel by viewModels()

        //get navFragment
        val navFragment = activity?.supportFragmentManager?.findFragmentById(R.id.navFragment) as NavHostFragment
        val mealText = view.findViewById<TextView>(R.id.dayMealText)
        val calendar = view.findViewById<CalendarView>(R.id.calendar)
        var meal = mealViewModel.findByDate("10/16/2024")


        mealText.setText(meal.toString())

        calendar.setOnDateChangeListener { calendar, year, month, day ->

        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
package com.example.foodwatch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {

    val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate view
        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.dailyListRecycler)
        val adapter = CalendarListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)




        //get navFragment
        val navFragment = activity?.supportFragmentManager?.findFragmentById(R.id.navFragment) as NavHostFragment
        val mealText = view.findViewById<TextView>(R.id.dayMealText)
        val calendar = view.findViewById<CalendarView>(R.id.calendar)

        suspend fun updateMeal(date: String) {
            Log.d("DATE TEST", date)
            val meals = mealViewModel.findMealsByDate(date).await()
            val sortedMeals = meals.sortedBy { it.time }
            adapter.submitList(sortedMeals)
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var date: String = LocalDateTime.now().format(formatter)
        lifecycleScope.launch { updateMeal(date) }

        calendar.setOnDateChangeListener { calendar, year, month, day ->
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
            lifecycleScope.launch { updateMeal(date) }
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

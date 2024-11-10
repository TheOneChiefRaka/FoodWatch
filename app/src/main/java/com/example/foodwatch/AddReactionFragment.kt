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
import androidx.navigation.fragment.NavHostFragment
import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AddReactionFragment : Fragment() {

    val reactionViewModel: ReactionViewModel by viewModels {
        ReactionViewModelFactory((activity?.application as MealsApplication).reactions_repository)
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
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var date: String = LocalDateTime.now().format(formatter)
        var time: String = LocalTime.now().toString().take(5)
        reactionTimeField.setText(time, TextView.BufferType.EDITABLE)

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

        addReactionButton.setOnClickListener {
            if(reactionSeverityField.selectedItem.toString() != "" || reactionTimeField.text.toString() != "") {
                val newReaction =
                    Reaction(0, date, reactionTimeField.text.toString(), reactionSeverityField.selectedItem.toString())
                reactionViewModel.insert(newReaction)

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

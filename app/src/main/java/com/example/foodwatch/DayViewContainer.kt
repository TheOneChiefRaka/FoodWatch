package com.example.foodwatch

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val dayOfMonth = view.findViewById<TextView>(R.id.calendarDayText)
    val reactionDotRecycler = view.findViewById<RecyclerView>(R.id.reactionDotRecycler)

    fun setHeight(height: Int) {
        val params = view.layoutParams
        params.height = height
        view.layoutParams = params
    }

    fun setWidth(width: Int) {
        val params = view.layoutParams
        params.width = width
        view.layoutParams = params
    }
}
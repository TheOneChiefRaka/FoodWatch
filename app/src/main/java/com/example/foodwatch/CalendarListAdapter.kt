package com.example.foodwatch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class CalendarListAdapter : ListAdapter<CalendarListObject, CalendarListAdapter.MealViewHolder>(calendarListObjectsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.text, current.time)
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?, time: String?) {
            val newTime = time?.takeLast(5)
            wordItemView.text = "$newTime: $text"
        }

        companion object {
            fun create(parent: ViewGroup): MealViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendarlist_item, parent, false)
                return MealViewHolder(view)
            }
        }
    }

    class calendarListObjectsComparator : DiffUtil.ItemCallback<CalendarListObject>() {
        override fun areItemsTheSame(oldItem: CalendarListObject, newItem: CalendarListObject): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CalendarListObject, newItem: CalendarListObject): Boolean {
            return oldItem.text == newItem.text
        }
    }
}

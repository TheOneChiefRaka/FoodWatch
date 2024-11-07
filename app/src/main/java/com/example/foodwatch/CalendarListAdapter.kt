package com.example.foodwatch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class CalendarListAdapter : ListAdapter<Meal, CalendarListAdapter.MealViewHolder>(MealsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.time)
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?, time: String?) {
            wordItemView.text = "$time: $text"
        }

        companion object {
            fun create(parent: ViewGroup): MealViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendarlist_item, parent, false)
                return MealViewHolder(view)
            }
        }
    }

    class MealsComparator : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.name == newItem.name
        }
    }
}

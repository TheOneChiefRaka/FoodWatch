package com.example.foodwatch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.foodwatch.database.entities.Meal

class MealListAdapter : ListAdapter<Meal, MealListAdapter.MealViewHolder>(mealListObjectsComparator()) {
    var navcontroller: NavController? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        navcontroller = parent.findNavController()
        return MealViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            val action = CalendarFragmentDirections.calendarToEditMeal(current.mealId)
            navcontroller?.navigate(action)
        }
        holder.bind(current.name, current.timeEaten)
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(name: String?, time: String?) {
            val newTime = time?.takeLast(5)
            listItemView.text = "$newTime: $name"
        }

        companion object {
            fun create(parent: ViewGroup): MealViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendarlist_item, parent, false)
                return MealViewHolder(view)
            }
        }
    }

    class mealListObjectsComparator : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
}

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
import com.example.foodwatch.database.entities.Reaction

class ReactionListAdapter : ListAdapter<Reaction, ReactionListAdapter.ReactionViewHolder>(reactionListObjectsComparator()) {
    var navcontroller: NavController? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        navcontroller = parent.findNavController()
        return ReactionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            val action = CalendarFragmentDirections.calendarToEditMeal(current.reactionId)
            //navcontroller?.navigate(action)
        }
        holder.bind(current.severity, current.reactionTime)
    }

    class ReactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(severity: String?, time: String?) {
            val newTime = time?.takeLast(5)
            listItemView.text = "$newTime: $severity"
        }

        companion object {
            fun create(parent: ViewGroup): ReactionViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendarlist_item, parent, false)
                return ReactionViewHolder(view)
            }
        }
    }

    class reactionListObjectsComparator : DiffUtil.ItemCallback<Reaction>() {
        override fun areItemsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
            return oldItem == newItem
        }
    }
}
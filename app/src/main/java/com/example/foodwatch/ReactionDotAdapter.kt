package com.example.foodwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.entities.Reaction

class ReactionDotAdapter : ListAdapter<Reaction, ReactionDotAdapter.ReactionDotViewHolder>(ReactionListObjectsComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionDotViewHolder {
        return ReactionDotViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ReactionDotViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.severity)
    }

    class ReactionDotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(severity: String?) {

            when(severity) {
                "Mild" -> itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.reaction_dot_mild)
                "Medium" -> itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.reaction_dot_medium)
                "Severe" -> itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.reaction_dot_severe)
                else -> itemView.background = ContextCompat.getDrawable(itemView.context, R.drawable.reaction_dot_mild)
            }
            itemView.requestLayout()
        }

        companion object {
            fun create(parent: ViewGroup): ReactionDotViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.reaction_dot_item, parent, false)
                return ReactionDotViewHolder(view)
            }
        }
    }

    class ReactionListObjectsComparator : DiffUtil.ItemCallback<Reaction>() {
        override fun areItemsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
            return oldItem == newItem
        }
    }
}
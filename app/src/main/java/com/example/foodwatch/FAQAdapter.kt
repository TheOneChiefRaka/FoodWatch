package com.example.foodwatch

import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FAQAdapter(private val items: List<FAQItem>) : RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    inner class FAQViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val q: TextView = view.findViewById(R.id.tvQuestion)
        val a: TextView = view.findViewById(R.id.tvAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.faq_item, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val item = items[position]
        holder.q.text = item.question
        holder.a.text = item.answer

        // Show or hide the answer based on isExpanded
        holder.a.visibility = if (item.isExpanded) View.VISIBLE else View.GONE

        // Toggle expand/collapse on row click
        holder.itemView.setOnClickListener {
            item.isExpanded = !item.isExpanded
            // Animate the change (optional)
            TransitionManager.beginDelayedTransition(holder.itemView as ViewGroup)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = items.size
}

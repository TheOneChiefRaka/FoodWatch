package com.example.foodwatch

import android.renderscript.Sampler.Value
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.foodwatch.database.entities.Ingredient
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


class ReportListAdapter : ListAdapter<Ingredient, ReportListAdapter.IngredientViewHolder>(IngredientsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.timesEaten, current.mildReactions, current.mediumReactions, current.severeReactions)
    }

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.ingredientName)
        private val mealsEatenView: TextView = itemView.findViewById(R.id.mealsEaten)
        private val pieChart: PieChart = itemView.findViewById(R.id.pieChart)

        fun bind(name: String, timesEaten: Int, mild: Int, medium: Int, severe: Int) {
            nameView.text = "$name:"
            mealsEatenView.text = "$timesEaten meals eaten"

            val entries = mutableListOf<PieEntry>()
            val reportColors = mutableListOf<Int>()
            val noReaction = timesEaten.toFloat() - mild.toFloat() - medium.toFloat() - severe.toFloat()
            if(noReaction > 0) {
                entries.add(PieEntry(noReaction / timesEaten, "No Reaction"))
                reportColors.add(R.color.noReaction)
            }

            if(mild > 0) {
                entries.add(PieEntry(mild.toFloat() / timesEaten, "Mild"))
                reportColors.add(R.color.mildReaction)
            }

            if(medium > 0) {
                entries.add(PieEntry(medium.toFloat() / timesEaten, "Medium"))
                reportColors.add(R.color.mediumReaction)
            }

            if(severe > 0) {
                entries.add(PieEntry(severe.toFloat() / timesEaten, "Severe"))
                reportColors.add(R.color.severeReaction)
            }

            val dataSet = PieDataSet(entries, "Categories")
            dataSet.setColors(reportColors.toIntArray(), itemView.context)
            val pieData = PieData(dataSet)
            pieData.setValueFormatter(PercentFormatter(pieChart))

            pieChart.data = pieData
            pieChart.setUsePercentValues(true) //display percents instead of floats
            pieChart.setEntryLabelColor(R.color.black)
            pieChart.data.setValueTextSize(13f)
            pieChart.data.setValueTextColor(R.color.black)
            pieChart.invalidate() // Refresh the chart
            pieChart.description.isEnabled = false // Disable description
            pieChart.legend.isEnabled = false // Enable legend
            pieChart.animateY(1400) // Animate the chart
        }

        companion object {
            fun create(parent: ViewGroup): IngredientViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.reportlist_item, parent, false)
                return IngredientViewHolder(view)
            }
        }
    }

    class IngredientsComparator : DiffUtil.ItemCallback<Ingredient>() {
        override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
            return     oldItem.id == newItem.id
                    && oldItem.name == newItem.name
                    && oldItem.timesEaten == newItem.timesEaten
                    && oldItem.mildReactions == newItem.mildReactions
                    && oldItem.mediumReactions == newItem.mediumReactions
                    && oldItem.severeReactions == newItem.severeReactions
        }
    }
}
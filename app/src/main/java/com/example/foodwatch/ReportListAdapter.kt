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
import com.example.foodwatch.ReactionListAdapter.ReactionViewHolder
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.entities.IngredientData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.compareTo
import kotlin.text.toFloat


class ReportListAdapter : ListAdapter<IngredientData, ReportListAdapter.ReactionViewHolder>(ReactionsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        return ReactionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current.name, current.timesEaten, current.mild, current.medium, current.severe)
    }

    class ReactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

            pieChart.setDrawEntryLabels(false) // Get rid of the labels
            pieChart.isDrawHoleEnabled = false
            pieChart.isRotationEnabled = false
            pieChart.data = pieData
            pieChart.setUsePercentValues(true) // Display percents instead of floats
            pieChart.setEntryLabelColor(R.color.black)
            pieChart.data.setValueTextSize(13f)
            pieChart.data.setValueTextColor(R.color.black)
            pieChart.invalidate() // Refresh the chart
            pieChart.description.isEnabled = false // Disable description
            pieChart.legend.isEnabled = false // Enable legend
            pieChart.animateXY(0, 0) // Animate the chart
        }

        companion object {
            fun create(parent: ViewGroup): ReactionViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_reactions_tab, parent, false)
                return ReactionViewHolder(view)
            }
        }
    }

    class ReactionsComparator : DiffUtil.ItemCallback<IngredientData>() {
        override fun areItemsTheSame(oldItem: IngredientData, newItem: IngredientData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: IngredientData, newItem: IngredientData): Boolean {
            return  oldItem.name == newItem.name &&
                    oldItem.timesEaten == newItem.timesEaten &&
                    oldItem.mild == newItem.mild &&
                    oldItem.medium == newItem.medium &&
                    oldItem.severe == newItem.severe
        }
    }
}
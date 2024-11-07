package com.example.foodwatch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class ReportsFragment : Fragment(R.layout.fragment_reports) {

    private lateinit var pieChartA: PieChart
    private lateinit var pieChartB: PieChart
    private lateinit var pieChartC: PieChart
    private lateinit var pieChartD: PieChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChartA = view.findViewById( R.id.pieChartA)
        pieChartB = view.findViewById(R.id.pieChartB)
        pieChartC = view.findViewById(R.id.pieChartC)
        pieChartD = view.findViewById(R.id.pieChartD)

        setPieChartData(pieChartA, listOf(PieEntry(57f, "Category A"), PieEntry(5f, "Other")))
        setPieChartData(pieChartB, listOf(PieEntry(47f, "Category B"), PieEntry(15f, "Other")))
        setPieChartData(pieChartC, listOf(PieEntry(31f, "Category A"), PieEntry(27f, "Other")))
        setPieChartData(pieChartD, listOf(PieEntry(480f, "Category B"), PieEntry(52f, "Other")))
    }

    private fun setPieChartData(pieChart: PieChart, entries: List<PieEntry>) {
        val dataSet = PieDataSet(entries, "Categories")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList() // Use Material colors
        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.invalidate() // Refresh the chart
        pieChart.description.isEnabled = false // Disable description
        pieChart.legend.isEnabled = false // Enable legend
        pieChart.animateY(1400) // Animate the chart
    }
}
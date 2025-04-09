package com.example.foodwatch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReportsFragment : Fragment(R.layout.fragment_reports) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        /*

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter = ReportsPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position->
            tab.text = when (position){
                0 -> "Reactions"
                1 -> "Ingredients"
                2 -> "Meals"
                else -> ""
            }
        }.attach()
        */
    }
}
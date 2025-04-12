package com.example.foodwatch

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ReportsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> ReactionsTab()
            1 -> IngredientsTab()
            else -> throw IllegalArgumentException("Invalid tab!")
        }
    }
}
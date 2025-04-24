package com.example.foodwatch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FAQFragment : Fragment(R.layout.fragment_faq) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val faqs = listOf(
            FAQItem(
                question = "How do I add a meal?",
                answer   = "In the home screen, tap on \"Add Meal\" and fill out the information about the meal you had. After filling out your information tap \"Add Meal\" to save your meal."
            ),
            FAQItem(
                question = "How do I edit a meal I have already submitted?",
                answer = "From the Calendar screen, find your meal on the calendar by tapping the date of it's entry. Then tap the meal you wish to enter. After making changes to your meal tap \"Save Meal\" and your meal changes will be saved to the database."
            ),
            FAQItem(
                question = "How do I delete a meal?",
                answer = "From the Calendar screen, find your meal on the calendar by tapping the date of it's entry. Then tap on the meal you wish to delete, afterwards tap on the \"Delete\" button."
            ),
            FAQItem(
                question = "How do I export my data?",
                answer   = "In the Options screen, tap \"Export Data\". This will create a CSV file of all the ingredients you have entered but will only show ingredients if they are suspected of causing allergic reactions. You can then find this file in your device's \"Downloads\" folder."
            )
        )

        val rv = view.findViewById<RecyclerView>(R.id.faqRecyclerView)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = FAQAdapter(faqs)
    }
}

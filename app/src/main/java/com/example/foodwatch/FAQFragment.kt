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
                answer   = "In the home screen, tap on \"Add Meal\"."
            ),
            FAQItem(
                question = "How do I export data?",
                answer   = "In the Options screen, tap \"Export\" and check your Downloads folder."
            ),
            FAQItem(
                question = "How are you?",
                answer = "I'm doing good!"
            )
        )

        val rv = view.findViewById<RecyclerView>(R.id.faqRecyclerView)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = FAQAdapter(faqs)
    }
}

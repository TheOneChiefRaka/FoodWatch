package com.example.foodwatch

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button Variables
        val addMealButton = view.findViewById<Button>(R.id.addMealButton);
        val recipesButton = view.findViewById<Button>(R.id.recipes);
        val addReactionButton = view.findViewById<Button>(R.id.addReaction);

        // On CLick Listeners
        addMealButton.setOnClickListener {
            Toast.makeText(requireContext(), "Meal Added!", Toast.LENGTH_SHORT).show();
        }
        recipesButton.setOnClickListener {
            Toast.makeText(requireContext(), "Recipes!", Toast.LENGTH_SHORT).show();
        }
        addReactionButton.setOnClickListener {
            Toast.makeText(requireContext(), "Add Reaction!", Toast.LENGTH_SHORT).show();
        }
    }
}
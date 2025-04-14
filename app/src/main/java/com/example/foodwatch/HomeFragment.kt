package com.example.foodwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate view
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        //get navFragment
        val navFragment = activity?.supportFragmentManager?.findFragmentById(R.id.navFragment) as NavHostFragment
        val addMealButton = view.findViewById<Button>(R.id.addMealButton)
        val addReactionButton = view.findViewById<Button>(R.id.addReaction)
        val recipesButton = view.findViewById<Button>(R.id.recipes)

        //add button to navigate from home to add meal page
        addMealButton.setOnClickListener {
            navFragment.navController.navigate(R.id.home_to_addmeal)
        }
        addReactionButton.setOnClickListener {
            navFragment.navController.navigate(R.id.home_to_addreaction)
        }
        recipesButton.setOnClickListener {
            Toast.makeText(requireContext(), "Recipes!", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
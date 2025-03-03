package com.example.foodwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.MealViewModelFactory
import kotlinx.coroutines.launch

class EditMealFragment : Fragment(R.layout.fragment_editmeal) {
    val args: EditMealFragmentArgs by navArgs()

    val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealsApplication).meals_repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate view
        val view: View = inflater.inflate(R.layout.fragment_editmeal, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get nav args
        val mealId = args.mealId
        val mealNameView = view.findViewById<TextView>(R.id.mealNameView)
        val mealTimeView = view.findViewById<TextView>(R.id.mealTimeView)
        //val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        //launch asynchronous database query
        lifecycleScope.launch {
            val meal = mealViewModel.getMealById(mealId).await()
            mealNameView.text = meal.name
            mealTimeView.text = meal.timeEaten.takeLast(5)
        }

        /*deleteButton.setOnClickListener {
            mealViewModel.deleteMealById(mealId)
            findNavController().navigateUp()
        }*/
    }
}
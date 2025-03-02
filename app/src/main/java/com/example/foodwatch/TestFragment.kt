package com.example.foodwatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodwatch.database.entities.Meal
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.MealViewModelFactory
import kotlinx.coroutines.launch


class TestFragment : Fragment(R.layout.fragment_test) {
    val args: TestFragmentArgs by navArgs()

    val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealsApplication).meals_repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate view
        val view: View = inflater.inflate(R.layout.fragment_test, container, false)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealId = args.mealID
        val mealNameView = view.findViewById<TextView>(R.id.mealNameTextView)
        val mealIdView = view.findViewById<TextView>(R.id.idTextView)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)

        lifecycleScope.launch {
            val meal = mealViewModel.getMealById(mealId).await()
            mealNameView.text = meal.name
        }

        deleteButton.setOnClickListener {
            mealViewModel.deleteMealById(mealId)
            findNavController().navigateUp()
        }

        mealIdView.text = mealId.toString()
    }
}
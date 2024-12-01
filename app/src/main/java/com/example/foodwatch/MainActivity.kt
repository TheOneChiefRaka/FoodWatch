package com.example.foodwatch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.MealViewModelFactory
import com.example.foodwatch.database.viewmodel.ReactionViewModel
import com.example.foodwatch.database.viewmodel.ReactionViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((application as MealsApplication).meals_repository)
    }

    val reactionViewModel: ReactionViewModel by viewModels {
        ReactionViewModelFactory((application as MealsApplication).reactions_repository)
    }

    val ingredientViewModel: IngredientViewModel by viewModels {
        IngredientViewModelFactory((application as MealsApplication).ingredients_repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //find navController, used to navigate to different fragments
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment
        val navController = navHostFragment.navController

        //find bottom navigation bar
        val navBar = findViewById<BottomNavigationView>(R.id.navBar) as BottomNavigationView
        navBar.selectedItemId = R.id.homeButton

        //set listener for when a button is selected
        navBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.accountButton -> navController.navigate(R.id.to_account)
                R.id.calendarButton -> navController.navigate(R.id.to_calendar)
                R.id.homeButton -> navController.navigate(R.id.to_home)
                R.id.reportsButton -> navController.navigate(R.id.to_reports)
                R.id.optionsButton -> navController.navigate(R.id.to_options)
            }
            true
        }

    }
}
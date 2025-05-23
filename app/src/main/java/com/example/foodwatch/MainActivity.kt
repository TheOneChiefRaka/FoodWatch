package com.example.foodwatch

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getBoolean("darkMode", false)
        if (nightMode) {
            // Dark mode is enabled
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Dark mode is disabled
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        //find navController, used to navigate to different fragments
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment
        val navController = navHostFragment.navController

        //find bottom navigation bar
        val navBar = findViewById<BottomNavigationView>(R.id.navBar)
        navBar.selectedItemId = R.id.homeButton

        //set listener for when a button is selected
        navBar.setOnItemSelectedListener { item ->
            val view = navBar.findViewById<View>(item.itemId)
            view?.animate()
                /// Jumps up
                ?.translationY(-30f)
                ?.scaleY(1.2f)
                ?.scaleX(0.65f)
                ?.setDuration(200)
                ?.setInterpolator(AccelerateInterpolator())
                ?.withEndAction {
                    /// Goes down
                    view.animate()
                        .translationY(0f)
                        .setDuration(100)
                        .withEndAction {
                            /// Hits ground
                            view.animate()
                                .scaleX(1.25f)
                                .scaleY(0.5f)
                                .setDuration(100)
                                .withEndAction {
                                    /// Goes back to normal
                                    view.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(100)
                                        .setInterpolator(DecelerateInterpolator())
                                }
                        }
            }

            // Handle navigation
            when (item.itemId) {
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
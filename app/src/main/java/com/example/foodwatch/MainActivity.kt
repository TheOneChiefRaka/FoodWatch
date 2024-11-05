package com.example.foodwatch

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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

        //find navController, used to navigate to different fragments
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment
        val navController = navHostFragment.navController

        //find bottom navigation bar
        val navBar = findViewById<BottomNavigationView>(R.id.navBar) as BottomNavigationView
        navBar.selectedItemId = R.id.homeButton

        //set listener for when a button is selected
        navBar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.accountButton -> navController.navigate(R.id.to_account)
                R.id.calendarButton -> navController.navigate(R.id.to_calendar)
                R.id.homeButton -> navController.navigate(R.id.to_home)
                R.id.reportsButton -> navController.navigate(R.id.to_reports)
                R.id.optionsButton -> navController.navigate(R.id.to_options)
            }
            true
        }

        val button = findViewById<Button>(R.id.addMealButton)

        button?.setOnClickListener{
            Toast.makeText(this@MainActivity, R.string.pass_test, Toast.LENGTH_LONG).show()
        }
    }
}
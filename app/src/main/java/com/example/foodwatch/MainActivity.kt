package com.example.foodwatch

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button;


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

        /*
        Example of how to create a button and have it change activities(pages)
        */

        // Assign calendarButton value to be listened for
        val calButton = findViewById<Button>(R.id.calendarButton)

        // Listen for button to be clicked
        calButton.setOnClickListener{
            val screen = Intent(this,Calendar::class.java) // Get activity to go to
            startActivity(screen) // Activate activity
        }
    }
}
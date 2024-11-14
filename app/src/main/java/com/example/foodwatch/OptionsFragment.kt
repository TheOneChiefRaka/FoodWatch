package com.example.foodwatch

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class OptionsFragment : Fragment(R.layout.fragment_options) {

    private var isDarkTheme = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button using findViewById
        val buttonToggleTheme: Button = view.findViewById(R.id.buttonToggleTheme)

        buttonToggleTheme.setOnClickListener {
            toggleTheme()
        }
    }

    private fun toggleTheme() {
        activity?.let { activity ->
            // Check if theme is dark and toggle
            if (isDarkTheme) {
                activity.setTheme(R.style.Theme_FoodWatch_Light)
            } else {
                activity.setTheme(R.style.Theme_FoodWatch_Dark)
            }

            // Ensure that the activity context is valid before recreating
            if (!activity.isFinishing) {
                activity.recreate()  // Recreate activity to apply the new theme
                isDarkTheme = !isDarkTheme  // Toggle theme flag
            }
        }
    }
}

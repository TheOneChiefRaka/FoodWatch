package com.example.foodwatch

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment

class OptionsFragment : Fragment(R.layout.fragment_options) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val darkModeSwitch = view.findViewById<SwitchCompat>(R.id.darkModeSwitch)
        val nightMode = sharedPreferences.getBoolean("darkMode", false)
        if (nightMode) {
            // Dark mode is enabled
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            darkModeSwitch.isChecked = true
        } else {
            // Dark mode is disabled
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            darkModeSwitch.isChecked = false
        }
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Handle dark mode switch state change
            if (isChecked) {
                // Dark mode is enabled
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("darkMode", true)
                editor.apply()
            } else {
                // Dark mode is disabled
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("darkMode", false)
                editor.apply()
            }
        }

        /// Notifications and sounds
        val notificationSwitch = view.findViewById<SwitchCompat>(R.id.notificationSwitch)
        notificationSwitch.setOnCheckedChangeListener { _, isChecked -> {}
            // Handle notification switch state change
        }
        val soundSwitch = view.findViewById<SwitchCompat>(R.id.soundSwitch)
        soundSwitch.setOnCheckedChangeListener { _, isChecked ->{}}


        return view
    }
}
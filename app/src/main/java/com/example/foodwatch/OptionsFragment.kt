package com.example.foodwatch

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.entities.IngredientData
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import kotlinx.coroutines.launch
import java.io.File

class OptionsFragment : Fragment(R.layout.fragment_options) {

    private val viewModel: IngredientViewModel by viewModels { IngredientViewModelFactory((requireActivity().application as MealsApplication).ingredients_repository) }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_options, container, false)
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val darkModeSwitch = view.findViewById<SwitchCompat>(R.id.darkModeSwitch)
        val nightMode = sharedPreferences.getBoolean("darkMode", false)
        val exportButton = view.findViewById<Button>(R.id.exportDataButton)

        exportButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val stats = viewModel.getIngredientData().await()
                if (stats.isEmpty()) {
                    Toast.makeText(requireContext(),
                        "No ingredient data to export",
                        Toast.LENGTH_SHORT).show()
                } else {
                    exportStatsCsv(stats)
                }
            }

        }

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

        val notificationSwitch = view.findViewById<SwitchCompat>(R.id.notificationSwitch)
        notificationSwitch.setOnCheckedChangeListener { _, isChecked -> {} }
        val soundSwitch = view.findViewById<SwitchCompat>(R.id.soundSwitch)
        soundSwitch.setOnCheckedChangeListener { _, isChecked ->{}}

        return view
    }

    // Export ingredient information to CSV file for download
    private fun exportStatsCsv(stats: List<IngredientData>) {
        val header = listOf("name","timesEaten","mild","medium","severe")
            .joinToString(",") + "\n"

        val rows = stats.joinToString("\n") { s ->
            listOf(
                "\"${s.name.replace("\"","\"\"")}\"",
                s.timesEaten.toString(),
                s.mild.toString(),
                s.medium.toString(),
                s.severe.toString()
            ).joinToString(",")
        }

        val csv = header + rows

        // Save CSV file to downloads folder on device
        val resolver = requireContext().contentResolver
        val cv = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "ingredient_stats.csv")
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = resolver.insert(
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
            cv
        ) ?: run {
            Toast.makeText(requireContext(), "Export failed", Toast.LENGTH_SHORT).show()
            return
        }

        resolver.openOutputStream(uri)?.use { it.write(csv.toByteArray()) }

        Toast.makeText(
            requireContext(),
            "Exported ${stats.size} rows to Downloads/ingredient_stats.csv",
            Toast.LENGTH_LONG
        ).show()
    }
}
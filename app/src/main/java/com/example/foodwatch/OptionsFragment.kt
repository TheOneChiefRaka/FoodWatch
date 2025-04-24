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
import com.example.foodwatch.database.entities.Ingredient
import com.example.foodwatch.database.viewmodel.IngredientViewModel
import com.example.foodwatch.database.viewmodel.IngredientViewModelFactory
import java.io.File

class OptionsFragment : Fragment(R.layout.fragment_options) {

    private val ingredientViewModel: IngredientViewModel by viewModels {
        IngredientViewModelFactory((requireActivity().application as MealsApplication).ingredients_repository)
    }

    private var latestIngredients: List<Ingredient> = emptyList()

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
        val exportButton = view.findViewById<Button>(R.id.exportData)

        ingredientViewModel.allIngredients.observe(viewLifecycleOwner) { list ->
            latestIngredients = list
        }

        view.findViewById<Button>(R.id.exportData).setOnClickListener {
            if (latestIngredients.isEmpty()) {
                Toast.makeText(requireContext(), "No ingredients to export", Toast.LENGTH_SHORT)
                    .show()
            } else {
                exportIngredientsCSV(latestIngredients)
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

    private fun exportIngredientsCSV(ingredients: List<Ingredient>) {
        val resolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "ingredients_export.csv")
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = resolver.insert(
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
            contentValues
        ) ?: return

        val header = listOf(
            "id",
            "name",
            "timesEaten",
            "mildReactions",
            "mediumReactions",
            "severeReactions"
        ).joinToString(",") + "\n"

        val rows = ingredients.joinToString("\n") { ing ->
            val safeName = "\"${ing.name.replace("\"", "\"\"")}\""
            listOf(
                ing.ingredientId.toString(),
                safeName
            ).joinToString(",")
        }

        val csvText = header + rows

        resolver.openOutputStream(uri)?.use { out ->
            out.write(csvText.toByteArray())

            Toast.makeText(
                requireContext(),
                "Exported ${ingredients.size} ingredients to Downloads",
                Toast.LENGTH_LONG
            ).show()


        }
    }
}
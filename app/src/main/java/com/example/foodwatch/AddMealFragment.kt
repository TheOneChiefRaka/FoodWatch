package com.example.foodwatch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class AddMealFragment : Fragment(R.layout.fragment_addmeal) {

    override fun onViewCreated(view: View, savedInstanceState:Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.ingredient_listview) // Display the ingredient_listview xml

        listView.adapter = CustomAdapter(this.requireContext())

    }

    private class CustomAdapter(context: Context): BaseAdapter(){

        private val mContext: Context

        // This is the array of ingredient names. Needs to be an array of names pulled from a database
        private val names = arrayListOf<String>(
            "Banana", "Beef", "Bread", "Chicken", "Dairy", "Fish", "Olive Oil", "Shellfish", "Yogurt"
        )

        init{
            mContext = context
        }

        override fun getCount(): Int {
            return names.size
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getItem(p0: Int): Any {
            return "TEST STRING"
        }

        // This creates the view that goes into each listView position
        override fun getView(position: Int, p1: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.row_ingredient, viewGroup, false) // Inflate the row_ingredient xml file onto screen

            val ingredientTextView = rowMain.findViewById<TextView>(R.id.ingredient_textView)
            ingredientTextView.text = names.get(position) // Get the name of the ingredient at the corresponding position count

            return rowMain
        }
    }

}
package com.example.foodwatch

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class AddMealFragment : Fragment(R.layout.fragment_addmeal) {

    override fun onViewCreated(view: View, savedInstanceState:Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.ingredient_listview)

        listView.adapter = CustomAdapter(this)

    }

    private class CustomAdapter(context: Context): BaseAdapter(){

        private val mContext: Context

        init{
            mContext = context
        }

        override fun getCount(): Int {
            TODO("Not yet implemented")
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getItem(p0: Int): Any {
            return "TEST STRING"
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val textView = TextView(mContext)
            textView.text = "HERE is my ROW for my LISTVIEW"
            return textView
        }
    }

}
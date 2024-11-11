package com.example.foodwatch

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddMealFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addmeal)

        val listView = findViewById<ListView>(R.id.main_listview)

        listView.adapter = MyCustomAdapter(this) // this needs to be my custom adapter telling my list what to render
    }

    private class MyCustomAdapter(context: Context): BaseAdapter(){

        private val mContext: Context

        init{
            this.mContext = context
        }
        // Responsible for how many rows in my list
        override fun getCount(): Int {
            return 5
        }

        override fun getItemId(p0: Int): Long {
            TODO("Not yet implemented")
        }

        override fun getItem(p0: Int): Any {
            TODO("Not yet implemented")
        }

        // Renders each row
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val textView = TextView(mContext)
            textView.text = "Here is my Row for my ListView"
            return textView
        }
    }
}
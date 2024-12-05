package com.example.foodwatch

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class AddMealFragmentCheckboxes : Fragment(R.layout.fragment_addmeal) {

    override fun onViewCreated(view: View, savedInstanceState:Bundle?) {
    super.onViewCreated(view, savedInstanceState)


    }
}
        /*
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.ingredient_listview) // Display the ingredient_listview xml

        listView.adapter = CustomAdapter(this.requireContext())

        val addMeal = view.findViewById<Button>(R.id.toAddMealButton)

        var position = 0
        addMeal.setOnClickListener{
            println("Adding meal with ingredients.")
            val mealName = view.findViewById<EditText>(R.id.mealName)
            println(mealName.text.toString())
            for (views in listView){
                val mCheckBox = views.findViewById<CheckBox>(R.id.ingredientCheckbox)
                if(mCheckBox.isChecked){
                    println(listView.getItemAtPosition(position))
                }
                position++
            }
            position = 0
            println()
        }


    }

    public class MyAdapter: ArrayAdapter<String>() {

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
            return names[p0]
        }

        // This creates the view that goes into each listView position
        override fun getView(position: Int, p1: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val rowMain = p1 ?: layoutInflater.inflate(R.layout.row_ingredient, viewGroup, false) // Inflate the row_ingredient xml file onto screen

            val ingredientTextView = rowMain.findViewById<TextView>(R.id.ingredient_textView)
            //val ingredientCheckbox = rowMain.findViewById<CheckBox>(R.id.ingredientCheckbox)
            ingredientTextView.text = names.get(position) // Get the name of the ingredient at the corresponding position count

            return rowMain
        }
    }

}
*/
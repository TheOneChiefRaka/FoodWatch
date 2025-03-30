package com.example.foodwatch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.kizitonwose.calendar.view.CalendarView
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwatch.database.entities.Reaction
import com.example.foodwatch.database.viewmodel.MealViewModel
import com.example.foodwatch.database.viewmodel.MealViewModelFactory
import com.example.foodwatch.database.viewmodel.ReactionViewModel
import com.example.foodwatch.database.viewmodel.ReactionViewModelFactory
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.DaySize
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class CalendarListObject(val text: String, val time: String)

class CalendarFragment : Fragment() {

    val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((activity?.application as MealsApplication).meals_repository)
    }

    val reactionViewModel: ReactionViewModel by viewModels {
        ReactionViewModelFactory((activity?.application as MealsApplication).reactions_repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //inflate view
        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)

        //set up linear recycler view for meals
        val mealRecyclerView = view.findViewById<RecyclerView>(R.id.mealListRecycler)
        val mealAdapter = MealListAdapter()
        mealRecyclerView.adapter = mealAdapter
        mealRecyclerView.layoutManager = LinearLayoutManager(this.context)

        //set up recycler view for reactions
        val reactionRecyclerView = view.findViewById<RecyclerView>(R.id.reactionListRecycler)
        val reactionAdapter = ReactionListAdapter()
        reactionRecyclerView.adapter = reactionAdapter
        reactionRecyclerView.layoutManager = LinearLayoutManager(this.context)

        //get navFragment
        val navFragment = activity?.supportFragmentManager?.findFragmentById(R.id.navFragment) as NavHostFragment
        val calendar = view.findViewById<CalendarView>(R.id.calendarView)
        val testList = mutableListOf<Reaction>()
        testList.add(Reaction(0, "1", "Medium"))
        testList.add(Reaction(1, "1", "Mild"))
        testList.add(Reaction(2, "1", "Severe"))
        testList.add(Reaction(3, "1", "Severe"))
        testList.add(Reaction(4, "1", "Mild"))

        calendar.daySize = DaySize.Square

        calendar.dayBinder = object: MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val dotAdapter = ReactionDotAdapter()
                container.dayOfMonth.text = data.date.dayOfMonth.toString()
                container.reactionDotRecycler.adapter = dotAdapter
                container.reactionDotRecycler.layoutManager = GridLayoutManager(calendar.context, 4)
                dotAdapter.submitList(testList)
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek()
        calendar.setup(startMonth, endMonth, daysOfWeek.first())
        calendar.scrollToMonth(currentMonth)

        suspend fun updateList(date: String) {
            val meals = mealViewModel.findMealsByDate(date).await().sortedBy { it.timeEaten }
            val reactions = reactionViewModel.findReactionsByDate(date).await().sortedBy { it.reactionTime }
            mealAdapter.submitList(meals)
            reactionAdapter.submitList(reactions)
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var date: String = LocalDateTime.now().format(formatter)
        lifecycleScope.launch { updateList(date) }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val dayTitleView = view.findViewById<LinearLayout>(R.id.legendLayout)
            val monthTitleView = view.findViewById<TextView>(R.id.calendarMonthText)
        }

        calendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                // Remember that the header is reused so this will be called for each month.
                // However, the first day of the week will not change so no need to bind
                // the same view every time it is reused.
                container.monthTitleView.text = data.yearMonth.month.name.take(3).uppercase()
                if (container.dayTitleView.tag == null) {
                    container.dayTitleView.tag = data.yearMonth
                    container.dayTitleView.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek[index]
                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title.take(1)
                            // In the code above, we use the same `daysOfWeek` list
                            // that was created when we set up the calendar.
                            // However, we can also get the `daysOfWeek` list from the month data:
                            // val daysOfWeek = data.weekDays.first().map { it.date.dayOfWeek }
                            // Alternatively, you can get the value for this specific index:
                            // val dayOfWeek = data.weekDays.first()[index].date.dayOfWeek
                        }
                }
            }
        }

        /*
        calendar.setOnDateChangeListener { calendar, year, month, day ->
            date = "$year"
            if(month < 10) {
                date += "-0${month+1}"
            }
            else {
                date += "-${month+1}"
            }
            if(day < 10) {
                date += "-0${day}"
            }
            else {
                date += "-${day}"
            }
            Log.d("DATE", date)
            lifecycleScope.launch { updateList(date) }
        }*/

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

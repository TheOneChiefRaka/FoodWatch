package com.example.foodwatch

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
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
import com.kizitonwose.calendar.core.CalendarYear
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.DaySize
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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

        val listRecyclerContainer = view.findViewById<ConstraintLayout>(R.id.recyclerContainer)

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

        calendar.daySize = DaySize.Square

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        suspend fun updateDayData(adapter: ReactionDotAdapter, day: LocalDate, mealCountText: TextView) {
            val currentDay = day.format(formatter)
            val mealCount = mealViewModel.findMealsByDate(currentDay).await().count()
            var reactionList = reactionViewModel.findReactionsByDate(currentDay).await()
            //don't display more than 4 reactions
            if(reactionList.count() > 4)
                reactionList = reactionList.take(4)
            //easter egg or lazy UI design? you decide!
            if(mealCount == 0)
                mealCountText.text = ""
            else if(mealCount > 99)
                mealCountText.text = "∞"
            else
                mealCountText.text = mealCount.toString()
            adapter.submitList(reactionList)
        }

        suspend fun updateList(date: String) {
            val meals = mealViewModel.findMealsByDate(date).await().sortedBy { it.timeEaten }
            val reactions = reactionViewModel.findReactionsByDate(date).await().sortedBy { it.reactionTime }
            if(meals.count() > 0 || reactions.count() > 0) {   //if there's data to display
                listRecyclerContainer.background = ContextCompat.getDrawable(view.context, R.drawable.calendar_recycler_border)
            }
            else {
                listRecyclerContainer.background = null
            }
            mealAdapter.submitList(meals)
            reactionAdapter.submitList(reactions)
        }

        var selectedDay = LocalDate.now()

        class DayViewContainer(view: View) : ViewContainer(view) {
            val dayOfMonth = view.findViewById<TextView>(R.id.calendarDayText)
            val mealCountTextView = view.findViewById<TextView>(R.id.dayMealCount)
            val reactionDotRecycler = view.findViewById<RecyclerView>(R.id.reactionDotRecycler)

            lateinit var day: CalendarDay

            init {
                view.setOnClickListener {
                    if(day.position == DayPosition.MonthDate) {
                        val previousSelection = selectedDay
                        lifecycleScope.launch { updateList(day.date.format(formatter)) }
                        selectedDay = day.date
                        calendar.notifyDateChanged(day.date)    //update this day to be selected
                        if(previousSelection != null)
                            calendar.notifyDateChanged(previousSelection)    //update the previous selection to no longer be highlighted
                    }
                }
                reactionDotRecycler.setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP)
                        view.performClick()
                    else
                        false
                }

                reactionDotRecycler.setOnClickListener { view ->
                    if(day.position == DayPosition.MonthDate) {
                        val previousSelection = selectedDay
                        lifecycleScope.launch { updateList(day.date.format(formatter)) }
                        selectedDay = day.date
                        calendar.notifyDateChanged(day.date)    //update this day to be selected
                        if(previousSelection != null)
                            calendar.notifyDateChanged(previousSelection)    //update the previous selection to no longer be highlighted
                    }
                }
            }
        }

        var currentMonth = YearMonth.now()
        selectedDay = LocalDate.now()
        lifecycleScope.launch { updateList(selectedDay.format(formatter)) }
        val startMonth = currentMonth.minusMonths(120)
        val endMonth = currentMonth.plusMonths(120)
        val daysOfWeek = daysOfWeek()

        //worst code i've written in my life, so far!
        val mealsEatenCounts = mutableMapOf<Int, MutableMap<Int, List<Int>>>()             //year -> month -> day -> count of meals eaten
        val reactionsLists = mutableMapOf<Int, MutableMap<Int, List<List<Reaction>>>>()    //year -> month -> day -> list of reactions

        suspend fun updateMonthData(yearMonth: YearMonth) {
            if(mealsEatenCounts[yearMonth.year] == null) {
                mealsEatenCounts[yearMonth.year] = mutableMapOf()
            }
            mealsEatenCounts[yearMonth.year]?.put(yearMonth.month.value, mealViewModel.countMealsEatenByYearMonth(yearMonth).await())
            if(reactionsLists[yearMonth.year] == null) {
                reactionsLists[yearMonth.year] = mutableMapOf()
            }
            reactionsLists[yearMonth.year]?.put(yearMonth.month.value, reactionViewModel.findReactionsByYearMonth(yearMonth).await())
        }

        calendar.dayBinder = object: MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                val dotAdapter = ReactionDotAdapter()
                container.day = data
                container.dayOfMonth.text = data.date.dayOfMonth.toString()
                container.reactionDotRecycler.adapter = dotAdapter
                container.reactionDotRecycler.layoutManager = GridLayoutManager(calendar.context, 4)
                if(data.position == DayPosition.MonthDate) {  //true if day is part of the current month
                    if(data.date == selectedDay)
                        container.view.background = ContextCompat.getDrawable(view.context, R.drawable.calendar_day_border_selected)
                    else
                        container.view.background = ContextCompat.getDrawable(view.context, R.drawable.calendar_day_border)
                    container.dayOfMonth.setTextColor(Color.parseColor("#81B266"))
                    //lifecycleScope.launch { updateDayData(dotAdapter, data.date, container.mealCountTextView) }
                    if(reactionsLists[data.date.yearMonth.year]?.get(data.date.yearMonth.month.value) != null && mealsEatenCounts[data.date.yearMonth.year]?.get(data.date.yearMonth.month.value) != null) {
                        val mealCount = mealsEatenCounts[data.date.yearMonth.year]?.get(data.date.yearMonth.month.value)?.get(data.date.dayOfMonth-1)
                        if(mealCount == 0)
                            container.mealCountTextView.text = ""
                        else
                            container.mealCountTextView.text = mealCount.toString()
                        dotAdapter.submitList(reactionsLists[data.date.yearMonth.year]?.get(data.date.yearMonth.month.value)?.get(data.date.dayOfMonth-1)?.take(4))
                    }
                }
                else {
                    container.mealCountTextView.text = ""
                    container.view.background = ContextCompat.getDrawable(view.context, R.drawable.calendar_day_border_different_month)
                    container.dayOfMonth.setTextColor(Color.parseColor("#B8D3AA"))
                }
            }
        }

        calendar.setup(startMonth, endMonth, daysOfWeek.first())
        calendar.scrollToMonth(currentMonth)

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

        calendar.monthScrollListener = {month ->
            lifecycleScope.launch {
                updateMonthData(month.yearMonth)
                calendar.notifyMonthChanged(month.yearMonth)
                if(reactionsLists[month.yearMonth.previousMonth.year]?.get(month.yearMonth.previousMonth.month.value) == null) {
                    updateMonthData(month.yearMonth.previousMonth)
                    calendar.notifyMonthChanged(month.yearMonth.previousMonth)
                }
                if(reactionsLists[month.yearMonth.nextMonth.year]?.get(month.yearMonth.nextMonth.month.value) == null) {
                    updateMonthData(month.yearMonth.nextMonth)
                    calendar.notifyMonthChanged(month.yearMonth.nextMonth)
                }
            }
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

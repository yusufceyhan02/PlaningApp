package com.ceyhan.planingapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.DailyPlan
import com.ceyhan.planingapp.models.ToDo
import com.ceyhan.planingapp.roomDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddDailyPlanViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(getApplication()).appDao()

    val toDoList = mutableStateListOf<ToDo>()
    val title = mutableStateOf("")
    val hour = mutableIntStateOf(-1)
    val minute = mutableIntStateOf(-1)

    fun insertDailyPlan(complete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(android.icu.util.Calendar.getInstance().time)
            val dailyPlan = DailyPlan(0,title.value.trim(),toDoList,date)
            appDao.insertDailyPlan(dailyPlan)
            launch(Dispatchers.Main) {
                complete()
            }
        }
    }

    fun clear() {
        toDoList.clear()
        title.value = ""
        hour.intValue = -1
        minute.intValue = -1
    }
}
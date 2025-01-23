package com.ceyhan.planingapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.daily.DailyToDo
import com.ceyhan.planingapp.roomDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AddDailyPlanViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(getApplication()).appDao()

    val dailyToDoList = mutableStateListOf<DailyToDo>()
    val title = mutableStateOf("")
    var hour = -1
    var minute = -1

    fun insertDailyPlan(complete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(android.icu.util.Calendar.getInstance().time)
            val dailyPlan = DailyPlanModel(0,title.value.trim(),dailyToDoList,date)
            appDao.insertDailyPlan(dailyPlan)
            launch(Dispatchers.Main) {
                complete()
            }
        }
    }

    fun clear() {
        dailyToDoList.clear()
        title.value = ""
        hour = -1
        minute = -1
    }
}
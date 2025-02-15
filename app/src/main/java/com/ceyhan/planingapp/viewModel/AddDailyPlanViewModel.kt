package com.ceyhan.planingapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.daily.DailyToDo
import com.ceyhan.planingapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AddDailyPlanViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(getApplication()).appDao()
    val progress = mutableStateOf(false)

    var edit = false
    var dailyPlanId = 0

    val dailyToDoList = mutableStateListOf<DailyToDo>()
    val title = mutableStateOf("")
    var hour = -1
    var minute = -1

    fun insertDailyPlan(complete: () -> Unit) {
        progress.value = true
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(android.icu.util.Calendar.getInstance().time)
        val dailyPlan = DailyPlanModel(dailyPlanId,title.value.trim(),dailyToDoList,date)
        viewModelScope.launch(Dispatchers.IO) {
            appDao.insertOrUpdateDailyPlan(dailyPlan)
            launch(Dispatchers.Main) {
                complete()
            }
        }
    }

    fun initDailyPlan(dailyPlan: DailyPlanModel) {
        dailyToDoList.clear()
        dailyToDoList.addAll(dailyPlan.dailyToDos)
        title.value = dailyPlan.title
        edit = true
        dailyPlanId = dailyPlan.uid
        progress.value = false
    }

    fun clear() {
        dailyToDoList.clear()
        title.value = ""
        hour = -1
        minute = -1
        edit = false
        progress.value = false
    }
}
package com.ceyhan.planingapp.viewModel

import android.app.Application
import android.icu.util.Calendar
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.roomDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DailyPlanViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(getApplication()).appDao()
    var process = false

    val dailyPlans = mutableStateListOf<DailyPlanModel>()

    fun init() {
        if (dailyPlans.isEmpty()) {
            getDailyPlans()
        }
    }

    fun getDailyPlans() {
        if (!process) {
            process = true
            dailyPlans.clear()
            viewModelScope.launch(Dispatchers.IO) {
                val dailyPlansData = appDao.getDailyPlans()
                launch(Dispatchers.Main) {
                    checkDateAndChangeData(dailyPlansData)
                }
            }
        }
    }

    private fun checkDateAndChangeData(dailyPlansData: List<DailyPlanModel>) {
        val timeNow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        var changedData = false

        dailyPlansData.forEach {
            if (it.date  != timeNow) {
                it.date = timeNow
                it.dailyToDos.forEach { todo ->
                    todo.selected = false
                }
                updateDailyPlan(it)
                changedData = true
            }
        }

        if (changedData) {
            process = false
            getDailyPlans()
        }
        else {
            dailyPlans.addAll(dailyPlansData)
            process = false
        }
    }

    fun updateDailyPlan(dailyPlan: DailyPlanModel) {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.updateDailyPlan(dailyPlan)
        }
    }
}
package com.ceyhan.planingapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.weekly.WeeklyPlanModel
import com.ceyhan.planingapp.roomDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeeklyPlanViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(application).appDao()
    var process = false

    val weeklyPlanList = mutableStateListOf<WeeklyPlanModel>()

    fun init() {
        if (weeklyPlanList.isEmpty()) {
            getWeeklyPlans()
        }
    }

    fun getWeeklyPlans() {
        if (!process) {
            process = true
            weeklyPlanList.clear()
            viewModelScope.launch(Dispatchers.IO) {
                val weeklyPlansDate = appDao.getWeeklyPlans()
                launch(Dispatchers.Main) {
                    weeklyPlanList.addAll(weeklyPlansDate)
                    process = false
                }
            }
        }
    }

    fun insertOrUpdateWeeklyPlan(weeklyPlan: WeeklyPlanModel) {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.insertOrUpdateWeeklyPlan(weeklyPlan)
            launch(Dispatchers.Main) {
                getWeeklyPlans()
            }
        }
    }
}
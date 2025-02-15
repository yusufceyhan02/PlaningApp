package com.ceyhan.planingapp.viewModel

import android.app.Application
import android.icu.util.Calendar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DailyPlanViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(getApplication()).appDao()
    var init  = false
    var process = mutableStateOf(false)
    var firstStart = mutableStateOf(false)

    val dailyPlans = mutableStateListOf<DailyPlanModel>()

    fun init(firstStartData: Boolean) {
        if (!init) {
            getDailyPlans()
            init = true
        }
        firstStart.value = firstStartData
    }

    fun getDailyPlans() {
        if (!process.value) {
            process.value = true
            dailyPlans.clear()
            viewModelScope.launch(Dispatchers.IO) {
                val dailyPlansData = appDao.getDailyPlans()
                launch(Dispatchers.Main) {
                    if (dailyPlansData.isNotEmpty()) {
                        checkDateAndChangeData(dailyPlansData)
                    }
                    else {
                        process.value = false
                    }
                }
            }
        }
    }

    // Her gün sıfırlanan checkbox
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
            process.value = false
            getDailyPlans()
        }
        else {
            dailyPlans.addAll(dailyPlansData)
            process.value = false
        }
    }

    fun updateDailyPlan(dailyPlan: DailyPlanModel) {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.insertOrUpdateDailyPlan(dailyPlan)
        }
    }

    fun deleteDailyPlan(dailyPlan: DailyPlanModel) {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.deleteDailyPlan(dailyPlan)
            launch(Dispatchers.Main) {
                getDailyPlans()
            }
        }
    }
}
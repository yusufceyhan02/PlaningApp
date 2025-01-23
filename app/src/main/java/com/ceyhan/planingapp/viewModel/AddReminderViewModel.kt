package com.ceyhan.planingapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.ceyhan.planingapp.models.daily.DailyToDo

class AddReminderViewModel(application: Application): AndroidViewModel(application) {
    val toDoList = mutableStateListOf<DailyToDo>()
    var dateMillis: Long = -1
    var hour = -1
    var minute = -1

    fun clear() {
        toDoList.clear()
        dateMillis = -1
        hour = -1
        minute = -1
    }
}
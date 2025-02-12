package com.ceyhan.planingapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.reminder.ReminderModel
import com.ceyhan.planingapp.roomDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(application).appDao()
    var init = false
    var process = mutableStateOf(false)

    val reminderList = mutableStateListOf<ReminderModel>()

    fun init() {
        if (!init) {
            getReminders()
            init = true
        }
    }

    fun getReminders() {
        if (!process.value) {
            process.value = true
            reminderList.clear()
            viewModelScope.launch(Dispatchers.IO) {
                val reminders = appDao.getReminders()
                launch(Dispatchers.Main) {
                    reminderList.addAll(reminders)
                    process.value = false
                }
            }
        }
    }

    fun deleteReminder(reminder: ReminderModel) {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.deleteReminder(reminder)
            launch(Dispatchers.Main) {
                reminderList.clear()
            }
        }
    }
}
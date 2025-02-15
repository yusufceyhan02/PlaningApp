package com.ceyhan.planingapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.reminder.ReminderModel
import com.ceyhan.planingapp.models.reminder.ReminderToDo
import com.ceyhan.planingapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddReminderViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(application).appDao()

    var reminderId = 0

    val toDoList = mutableStateListOf<ReminderToDo>()
    val selectedColor = mutableIntStateOf(0)
    var dateText = mutableStateOf("")
    var dateMillis: Long = -1
    var hour = -1
    var minute = -1


    fun initReminder(reminder: ReminderModel) {
        toDoList.clear()
        toDoList.addAll(reminder.reminderToDos)
        dateText.value = reminder.dateText
        reminderId = reminder.uid
    }

    fun insertReminder(complete: () -> Unit) {
        val reminderModel = ReminderModel(reminderId,selectedColor.intValue,dateMillis,dateText.value,toDoList)
        viewModelScope.launch(Dispatchers.IO) {
            appDao.insertOrUpdateReminder(reminderModel)
            launch(Dispatchers.Main) {
                complete()
            }
        }
    }

    fun clear() {
        toDoList.clear()
        dateMillis = -1
        hour = -1
        minute = -1
    }
}
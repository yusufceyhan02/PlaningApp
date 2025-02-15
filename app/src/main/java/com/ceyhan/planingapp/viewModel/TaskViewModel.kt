package com.ceyhan.planingapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ceyhan.planingapp.models.task.TaskModel
import com.ceyhan.planingapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskViewModel(application: Application): AndroidViewModel(application) {
    val appDao = AppDatabase(application).appDao()
    var init = false
    var process = mutableStateOf(false)
    var firstStart = mutableStateOf(false)

    val taskList = mutableStateListOf<TaskModel>()
    val title = mutableStateOf("")
    val description = mutableStateOf("")

    fun init(firstStartData: Boolean) {
        if (!init) {
            getTasks()
            init = true
        }
        firstStart.value = firstStartData
    }

    fun getTasks() {
        if (!process.value) {
            process.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val tasks = appDao.getTasks()
                launch(Dispatchers.Main) {
                    taskList.addAll(tasks)
                    val timeMillisNow = Calendar.getInstance().timeInMillis
                    taskList.forEach {
                        if (it.selected) { checkSelectedTime(it,timeMillisNow) }
                    }
                    process.value = false
                }
            }
        }

    }

    fun checkSelectedTime(task: TaskModel, timeMillisNow: Long) {
        val timeDiff = (timeMillisNow - task.selectedDateMillis!!)
        if ( timeDiff >= 43200000L ) {
            taskList.remove(task)
            deleteTask(task)
        }
    }

    fun insertOrUpdateTask(id: Int, complete: () -> Unit) {
        val task = TaskModel(id,title.value.trim(),description.value.trim(),false,null)
        viewModelScope.launch(Dispatchers.IO) {
            appDao.insertOrUpdateTask(task)
            launch(Dispatchers.Main) {
                taskList.clear()
                complete()
            }
        }
    }

    fun deleteTask(task: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            appDao.deleteTask(task)
            launch(Dispatchers.Main) {
                taskList.clear()
            }
        }
    }

    fun selectTask(task: TaskModel) {
        task.selected = !task.selected
        if (task.selected) { task.selectedDateMillis = Calendar.getInstance().timeInMillis }
        else { task.selectedDateMillis = null }
        viewModelScope.launch(Dispatchers.IO) {
            appDao.insertOrUpdateTask(task)
        }
    }
}
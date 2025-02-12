package com.ceyhan.planingapp.models.reminder

data class ReminderToDo(
    val title: String,
    val description: String,
    val hour: Int,
    val minute: Int,
    val time: String
)

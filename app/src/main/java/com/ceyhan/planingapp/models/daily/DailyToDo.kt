package com.ceyhan.planingapp.models.daily

data class DailyToDo(
    val title: String,
    val description: String,
    val hour: Int,
    val minute: Int,
    var selected: Boolean
)
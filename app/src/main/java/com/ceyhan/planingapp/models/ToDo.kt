package com.ceyhan.planingapp.models

data class ToDo(
    val title: String,
    val description: String,
    val hour: Int,
    val minute: Int,
    var selected: Boolean
)
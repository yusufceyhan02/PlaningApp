package com.ceyhan.planingapp.models.daily

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyPlanModel(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo("title") var title: String,
    @ColumnInfo("todos") var dailyToDos: List<DailyToDo>,
    @ColumnInfo("date") var date: String
)



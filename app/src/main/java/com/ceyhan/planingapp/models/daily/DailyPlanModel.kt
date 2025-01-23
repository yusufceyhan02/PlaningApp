package com.ceyhan.planingapp.models.daily

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyPlanModel(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("todos") val dailyToDos: List<DailyToDo>,
    @ColumnInfo("date") var date: String
)

package com.ceyhan.planingapp.models.weekly

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeeklyPlanModel(
    @PrimaryKey val uid: Int,
    @ColumnInfo("todos") val weeklyToDos: List<WeeklyToDo>
)
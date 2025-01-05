package com.ceyhan.planingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class DailyPlan(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("todos") val toDos: List<ToDo>,
    @ColumnInfo("date") var date: String
)

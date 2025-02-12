package com.ceyhan.planingapp.models.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskModel(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("selected") var selected: Boolean,
    @ColumnInfo("selectedDate") var selectedDateMillis: Long?
)
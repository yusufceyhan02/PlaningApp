package com.ceyhan.planingapp.models.reminder

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderModel(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo("colorsIndex") val colorsIndex: Int,
    @ColumnInfo("dateMillis") val dateMillis: Long,
    @ColumnInfo("dateText") val dateText: String,
    @ColumnInfo("reminderToDos") val reminderToDos: List<ReminderToDo>
)

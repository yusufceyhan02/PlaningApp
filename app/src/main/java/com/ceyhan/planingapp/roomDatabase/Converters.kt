package com.ceyhan.planingapp.roomDatabase

import androidx.room.TypeConverter
import com.ceyhan.planingapp.models.daily.DailyToDo
import com.ceyhan.planingapp.models.weekly.WeeklyToDo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun toDoListToString(list: List<DailyToDo>): String {
        val type = object: TypeToken<List<DailyToDo>>() {}.type
        return Gson().toJson(list,type)
    }

    @TypeConverter
    fun stringToToDoList(gson: String): List<DailyToDo> {
        val type = object: TypeToken<List<DailyToDo>>() {}.type
        return Gson().fromJson(gson,type)
    }

    @TypeConverter
    fun toDoListToString2(list: List<WeeklyToDo>): String {
        val type = object: TypeToken<List<WeeklyToDo>>() {}.type
        return Gson().toJson(list,type)
    }

    @TypeConverter
    fun stringToToDoList2(gson: String): List<WeeklyToDo> {
        val type = object: TypeToken<List<WeeklyToDo>>() {}.type
        return Gson().fromJson(gson,type)
    }
}
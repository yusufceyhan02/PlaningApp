package com.ceyhan.planingapp.roomDatabase

import androidx.room.TypeConverter
import com.ceyhan.planingapp.models.ToDo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    @TypeConverter
    fun toDoListToString(list: List<ToDo>): String {
        val type = object: TypeToken<List<ToDo>>() {}.type
        return Gson().toJson(list,type)
    }

    @TypeConverter
    fun stringToToDoList(gson: String): List<ToDo> {
        val type = object: TypeToken<List<ToDo>>() {}.type
        return Gson().fromJson(gson,type)
    }
}
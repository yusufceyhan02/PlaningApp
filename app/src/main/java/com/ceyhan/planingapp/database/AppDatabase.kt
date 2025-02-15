package com.ceyhan.planingapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.reminder.ReminderModel
import com.ceyhan.planingapp.models.task.TaskModel
import com.ceyhan.planingapp.models.weekly.WeeklyPlanModel

@Database(entities = [DailyPlanModel::class, WeeklyPlanModel::class, ReminderModel::class, TaskModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance?: synchronized(lock) {
            instance?: makeDatabase(context)
        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase").build()
    }
}
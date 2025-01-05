package com.ceyhan.planingapp.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ceyhan.planingapp.models.DailyPlan

@Database(entities = [DailyPlan::class], version = 2)
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
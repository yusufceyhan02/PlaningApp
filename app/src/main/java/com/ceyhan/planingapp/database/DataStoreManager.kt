package com.ceyhan.planingapp.database

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.ceyhan.planingapp.models.Screen
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("AppData")

data class FirstStartScreen(
    val dailyPlan: Boolean,
    val weeklyPlan: Boolean,
    val reminder: Boolean,
    val tasks: Boolean
)

fun getFirstStartScreen(context: Context) = context.dataStore.data.map {
    val dailyPlan = it[booleanPreferencesKey(Screen.DAILY_PLAN.name)] != false
    val weeklyPlan = it[booleanPreferencesKey(Screen.WEEKLY_PLAN.name)] != false
    val reminder = it[booleanPreferencesKey(Screen.REMINDER.name)] != false
    val tasks = it[booleanPreferencesKey(Screen.TASKS.name)] != false
    FirstStartScreen(dailyPlan,weeklyPlan,reminder,tasks)
}

suspend fun saveFirstStartScreen(screen: Screen, context: Context) {
    context.dataStore.edit {
        it[booleanPreferencesKey(screen.name)] = false
    }
}
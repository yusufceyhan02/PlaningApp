package com.ceyhan.planingapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.reminder.ReminderModel
import com.ceyhan.planingapp.models.task.TaskModel
import com.ceyhan.planingapp.models.weekly.WeeklyPlanModel

@Dao
interface AppDao {

    @Query("SELECT * FROM DailyPlanModel")
    fun getDailyPlans(): List<DailyPlanModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateDailyPlan(dailyPlan: DailyPlanModel)

    @Delete
    fun deleteDailyPlan(dailyPlan: DailyPlanModel)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateWeeklyPlan(weeklyPlanModel: WeeklyPlanModel)

    @Query("SELECT * FROM WeeklyPlanModel")
    fun getWeeklyPlans(): List<WeeklyPlanModel>



    @Query("SELECT * FROM ReminderModel")
    fun getReminders(): List<ReminderModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateReminder(reminderModel: ReminderModel)

    @Delete
    fun deleteReminder(reminder: ReminderModel)



    @Query("SELECT * FROM TaskModel")
    fun getTasks(): List<TaskModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateTask(task: TaskModel)

    @Delete
    fun deleteTask(task: TaskModel)
}
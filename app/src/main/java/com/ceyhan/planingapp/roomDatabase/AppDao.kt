package com.ceyhan.planingapp.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ceyhan.planingapp.models.daily.DailyPlanModel
import com.ceyhan.planingapp.models.weekly.WeeklyPlanModel

@Dao
interface AppDao {
    @Insert
    fun insertDailyPlan(dailyPlan: DailyPlanModel)

    @Query("SELECT * FROM DailyPlanModel")
    fun getDailyPlans(): List<DailyPlanModel>

    @Update
    fun updateDailyPlan(dailyPlan: DailyPlanModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateWeeklyPlan(weeklyPlanModel: WeeklyPlanModel)

    @Query("SELECT * FROM WeeklyPlanModel")
    fun getWeeklyPlans(): List<WeeklyPlanModel>
}
package com.ceyhan.planingapp.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ceyhan.planingapp.models.DailyPlan

@Dao
interface AppDao {
    @Insert
    fun insertDailyPlan(dailyPlan: DailyPlan)

    @Query("SELECT * FROM DailyPlan")
    fun getDailyPlans(): List<DailyPlan>

    @Update
    fun updateDailyPlan(dailyPlan: DailyPlan)
}
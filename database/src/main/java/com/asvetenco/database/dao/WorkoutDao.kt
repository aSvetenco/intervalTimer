package com.asvetenco.database.dao

import androidx.room.*
import com.asvetenco.database.entity.IntervalTimer
import com.asvetenco.database.entity.LapEntity
import com.asvetenco.database.entity.TimerEntity
import kotlinx.coroutines.flow.Flow


@Dao
internal interface WorkoutDao {

    @Transaction
    @Query("SELECT * FROM Timer")
    fun getTimers(): Flow<List<IntervalTimer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimer(timer: TimerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLaps(laps: List<LapEntity>)

}
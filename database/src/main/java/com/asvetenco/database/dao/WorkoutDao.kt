package com.asvetenco.database.dao

import androidx.room.*
import com.asvetenco.database.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow


@Dao
internal interface WorkoutDao {

    @Query("SELECT * FROM Workout")
    fun getTimers(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM Workout WHERE id =:timerId")
    fun getTimer(timerId: Long): Flow<WorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorkouts(timers: List<WorkoutEntity>)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

}
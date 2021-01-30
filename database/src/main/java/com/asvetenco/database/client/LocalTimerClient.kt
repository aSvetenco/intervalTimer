package com.asvetenco.database.client

import com.asvetenco.database.dao.WorkoutDao
import com.asvetenco.database.domain.Workout
import com.asvetenco.database.domain.WorkoutEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LocalTimerClient {
    fun getAllWorkouts(): Flow<List<Workout>>
    suspend fun saveWorkout(workout: Workout)
}

internal class LocalTimerDataSource(
    private val dao: WorkoutDao,
    private val mapper: WorkoutEntityMapper
) : LocalTimerClient {

    override fun getAllWorkouts(): Flow<List<Workout>> =
        dao.getTimers().map { it.map { entity -> mapper.mapToDto(entity) } }

    override suspend fun saveWorkout(workout: Workout) {
        val intervalTimer = mapper.mapFromDto(workout)
        dao.saveTimer(intervalTimer.timer)
        dao.saveLaps(intervalTimer.laps)
    }
}
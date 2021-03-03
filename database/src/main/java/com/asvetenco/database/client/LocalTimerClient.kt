package com.asvetenco.database.client

import com.asvetenco.database.dao.WorkoutDao
import com.asvetenco.database.dto.WorkoutDto
import com.asvetenco.database.dto.WorkoutEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface LocalTimerClient {
    fun getAllWorkouts(): Flow<List<WorkoutDto>>
    fun getWorkoutById(id: Long): Flow<WorkoutDto>
    suspend fun saveWorkout(workouts: List<WorkoutDto>)
    suspend fun deleteWorkout(workout: WorkoutDto)
}

internal class LocalTimerDataSource(
    private val dao: WorkoutDao,
    private val mapper: WorkoutEntityMapper
) : LocalTimerClient {

    override fun getAllWorkouts(): Flow<List<WorkoutDto>> =
        dao.getTimers().map { it.map { entity -> mapper.mapFromEntity(entity) } }

    override fun getWorkoutById(id: Long): Flow<WorkoutDto> =
        dao.getTimer(id)
            .map { mapper.mapFromEntity(it) }
            .catch { throw RoomNotFoundException("Workout with id$id not found") }

    override suspend fun saveWorkout(workouts: List<WorkoutDto>) {
        dao.saveWorkouts(workouts.map { mapper.mapToEntity(it) })
    }

    override suspend fun deleteWorkout(workout: WorkoutDto) {
        dao.deleteWorkout(mapper.mapToEntity(workout))
    }
}


class RoomNotFoundException(message: String) : RuntimeException(message) {
}
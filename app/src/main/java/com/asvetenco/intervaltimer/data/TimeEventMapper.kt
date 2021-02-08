package com.asvetenco.intervaltimer.data

import com.asvetenco.database.dto.LapDto
import com.asvetenco.database.dto.WorkoutDto

class TimeEventMapper {

    fun mapFromDto(dto: WorkoutDto): Workout {
        val events = listOf(
            TimeEvent.warmUp(dto.lap.warmUp),
            TimeEvent.coolDown(dto.lap.coolDown),
            TimeEvent.work(dto.lap.work),
            TimeEvent.rest(dto.lap.rest),
            TimeEvent.exercises(dto.lapsCount)
        )

        return Workout(
            dto.id,
            dto.title,
            dto.lapsCount,
            dto.lap.id,
            events
        )
    }

    fun mapToDto(domain: Workout): WorkoutDto {
        val interim = LapInterim(id = domain.lapId)
        domain.events.forEach {
            when (it) {
                is WarmUp -> interim.warmUp = it.time
                is CoolDown -> interim.coolDown = it.time
                is Work -> interim.work = it.time
                is Rest -> interim.rest = it.time
                is Exercise -> interim.exercise = it.time
            }
        }

        return WorkoutDto(
            domain.id,
            domain.title,
            interim.exercise,
            LapDto(
                interim.id,
                interim.warmUp,
                interim.coolDown,
                interim.work,
                interim.rest
            )
        )
    }

    private data class LapInterim(
        var id: Long = 0L,
        var warmUp: Int = 0,
        var coolDown: Int = 0,
        var work: Int = 0,
        var rest: Int = 0,
        var exercise: Int = 0,
    )
}
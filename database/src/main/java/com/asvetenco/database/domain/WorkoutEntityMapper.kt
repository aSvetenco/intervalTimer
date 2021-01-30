package com.asvetenco.database.domain

import com.asvetenco.database.Mapper
import com.asvetenco.database.entity.IntervalTimer
import com.asvetenco.database.entity.LapEntity
import com.asvetenco.database.entity.TimerEntity

internal class WorkoutEntityMapper : Mapper<IntervalTimer, Workout> {

    override fun mapToDto(domain: IntervalTimer): Workout {
        val laps =
            domain.laps.map {
                Lap(
                    it.id,
                    it.number,
                    it.warmUp,
                    it.coolDawn,
                    it.work,
                    it.rest,
                    it.intervalCount
                )
            }
        return Workout(domain.timer.id, domain.timer.title, laps)
    }

    override fun mapFromDto(dto: Workout): IntervalTimer {
        val laps =
            dto.laps.map {
                LapEntity(
                    it.id,
                    it.number,
                    dto.id,
                    it.warmUp,
                    it.coolDawn,
                    it.work,
                    it.rest,
                    it.intervalCount
                )
            }
        return IntervalTimer(TimerEntity(dto.id, dto.title), laps)
    }


}
package com.asvetenco.database.entity

import androidx.room.Embedded
import androidx.room.Relation

internal data class IntervalTimer(
    @Embedded val timer: TimerEntity,
    @Relation(
            parentColumn = "id",
            entityColumn = "timerId"
        )
        val laps: List<LapEntity>
    )

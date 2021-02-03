package com.asvetenco.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Workout")
internal data class WorkoutEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val lapsCount: Int,
    @Embedded val lap: LapEntity
)

internal data class LapEntity(
    val lapId: Long,
    val warmUp: Int,
    val coolDown: Int,
    val work: Int,
    val rest: Int
)
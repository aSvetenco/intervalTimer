package com.asvetenco.database.dto

data class WorkoutDto(
    val id: Long = 0L,
    val title: String = "",
    val lapsCount: Int = 0,
    val lap: LapDto = LapDto()
)

data class LapDto(
    val id: Long = 0L,
    val warmUp: Int = 0,
    val coolDown: Int = 0,
    val work: Int = 0,
    val rest: Int = 0
)
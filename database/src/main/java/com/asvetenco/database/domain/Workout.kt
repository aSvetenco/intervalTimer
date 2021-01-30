package com.asvetenco.database.domain

data class Workout(
    val id: Long,
    val title: String,
    val laps: List<Lap>
)

data class Lap(
    val id: Long,
    val number: Int,
    val warmUp: Int,
    val coolDawn: Int,
    val work: Int,
    val rest: Int,
    val intervalCount: Int
)
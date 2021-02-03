package com.asvetenco.intervaltimer.countdown

class CountdownService(
    private val countdownTimer: CountdownTimer,
    private val mapper: LapMapper
)

class LapMapper {

}

data class Workout(
    val id: Long = 0L,
    val title: String = "",
    val lapsCount: Int = 0,
    val events: List<TimeEvent> = listOf()
)

data class TimeEvent(
    val title: String,
    val time: Int,
    val phrase: String
)
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
) {
    companion object {
        fun empty() = Workout(events = listOf(
            TimeEvent("WarmUp", 0, "WarmUp"),
            TimeEvent("CoolDown", 0, "CoolDown"),
            TimeEvent("Work", 0, "Work"),
            TimeEvent("Rest", 0, "Rest")
        ))
    }
}

data class TimeEvent(
    val title: String,
    val time: Int,
    val phrase: String
)
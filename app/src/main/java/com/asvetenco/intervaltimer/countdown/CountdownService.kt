package com.asvetenco.intervaltimer.countdown

import com.asvetenco.intervaltimer.data.TimeEvent
import com.asvetenco.intervaltimer.data.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CountdownService(
    private val countdownTimer: CountdownTimer,
    private val scope: CoroutineScope
) : CountdownClient {

    private var currentWorkout = Workout()
    override val event: MutableStateFlow<TimeEvent> = MutableStateFlow(TimeEvent.warmUp(0))
    override val remainingTime = MutableStateFlow(0)

    init {
        countdownTimer.setOnPeriodFinishedListener(this)
    }

    override fun executeWorkout(workout: Workout) {
        currentWorkout = workout
        val events = mapToCountdownModel(workout)
        countdownTimer.start(scope, events)
    }

    override fun pauseWorkout() {
        countdownTimer.stop()
    }

    override suspend fun onFinish(lapIndex: Int) {
        val nextEvent = lapIndex + 1
        event.value =
            if (nextEvent >= currentWorkout.events.size) TimeEvent.congrats()
            else currentWorkout.events[nextEvent]
    }

    override suspend fun onTick(remainingTime: Long) {
        this.remainingTime.value = remainingTime.toSeconds()
    }

    private fun mapToCountdownModel(workout: Workout): List<Long> {
        val events = mutableListOf<Long>()
        for (lap in 0..workout.lapsCount) {
            events += workout.events.map { it.time.toMilliSeconds() }
        }
        return events
    }

    private fun Long.toSeconds() = (this / 1_000).toInt()

    private fun Int.toMilliSeconds() = (this.toLong()) * 1_000

    private fun nextEvent(lapIndex: Int) = lapIndex + 1
}

interface CountdownClient : CountdownTimer.Listener {
    val event: StateFlow<TimeEvent>
    val remainingTime: StateFlow<Int>
    fun executeWorkout(workout: Workout)
    fun pauseWorkout()
}
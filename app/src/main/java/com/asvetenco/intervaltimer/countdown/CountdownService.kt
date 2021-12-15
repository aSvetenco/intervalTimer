package com.asvetenco.intervaltimer.countdown

import com.asvetenco.intervaltimer.data.TimeEvent
import com.asvetenco.intervaltimer.data.TimeEventComparator
import com.asvetenco.intervaltimer.data.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class CountdownService(
    private val countdownTimer: CountdownTimer,
    private val scope: CoroutineScope
) : CountdownClient {

    private var nextEvent = 0
    private var events = listOf<TimeEvent>()
    override val event: MutableStateFlow<TimeEvent> = MutableStateFlow(TimeEvent.warmUp(0))
    override val remainingTime = MutableStateFlow(0)

    init {
        countdownTimer.setOnPeriodFinishedListener(this)
    }

    override fun executeWorkout() {
        countdownTimer.start(scope, events[0].time.toMilliSeconds())
    }

    override fun prepareWorkout(workout: Workout) {
        events = mapCountdownModel(workout)
        event.value = events[0]
        remainingTime.value = events[0].time
    }

    override fun pauseWorkout() {
        countdownTimer.stop()
    }

    override suspend fun onFinish() {
        withContext(Dispatchers.Main) {
            nextEvent += 1
            event.value =
                if (nextEvent >= events.size) {
                    nextEvent = 0
                    TimeEvent.congrats()
                } else {
                    countdownTimer.start(scope, events[nextEvent].time.toMilliSeconds(), startDelay = 500)
                    remainingTime.value = events[nextEvent].time
                    events[nextEvent]
                }
        }
    }

    override suspend fun onTick(remainingTime: Long) {
        this.remainingTime.value = remainingTime.toSeconds()
    }

    private fun mapCountdownModel(workout: Workout): List<TimeEvent> {

        val events = mutableListOf<TimeEvent>()
        val sorted = workout.events.sortedWith(TimeEventComparator())

        require(sorted.size == 5) {
            "Events count cannot be different from 5"
        }

        val exerciseCount = sorted[EXERCISE_INDEX].time
        events += sorted[WARM_UP_INDEX]
        for (i in 1..exerciseCount) {
            events += sorted[WORK_INDEX]
            events += sorted[REST_INDEX]
        }
        events += sorted[COOL_DOWN_INDEX]
        return events.filter { it.time > 0 }
    }

    private fun Long.toSeconds() = (this / 1_000).toInt()

    private fun Int.toMilliSeconds() = (this.toLong()) * 1_000

    companion object {
        private const val WARM_UP_INDEX = 0
        private const val WORK_INDEX = 1
        private const val REST_INDEX = 2
        private const val COOL_DOWN_INDEX = 3
        private const val EXERCISE_INDEX = 4
    }
}

interface CountdownClient : CountdownTimer.Listener {
    val event: StateFlow<TimeEvent>
    val remainingTime: StateFlow<Int>
    fun executeWorkout()
    fun prepareWorkout(workout: Workout)
    fun pauseWorkout()
}
package com.asvetenco.intervaltimer.screen.setup

import com.asvetenco.database.client.LocalTimerClient
import com.asvetenco.database.client.RoomNotFoundException
import com.asvetenco.intervaltimer.base.BaseViewModel
import com.asvetenco.intervaltimer.countdown.TimeEvent
import com.asvetenco.intervaltimer.countdown.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class SetupTimerViewModel(private val client: LocalTimerClient) : BaseViewModel() {

    override val tag: String = SetupTimerViewModel::class.java.simpleName

    val workout = MutableStateFlow(Workout())

    fun retrieveTimerById(id: Long?) {
        if (id != null) {
            launchDataLoad(
                doOnError = {
                    if (it is RoomNotFoundException) {
                        workout.value = Workout.empty()
                    }
                },
                block = {
                    client.getWorkoutById(id).collect { workoutDto ->
                        workout.value = Workout(
                            workoutDto.id,
                            workoutDto.title,
                            workoutDto.lapsCount,
                            listOf(
                                TimeEvent("WarmUp", workoutDto.lap.warmUp, "WarmUp"),
                                TimeEvent("CoolDown", workoutDto.lap.coolDown, "CoolDown"),
                                TimeEvent("Work", workoutDto.lap.work, "Work"),
                                TimeEvent("Rest", workoutDto.lap.rest, "Rest")
                            )
                        )
                    }
                })
        } else {
            workout.value = Workout.empty()
        }
    }

    fun minus(event: TimeEvent, workout: Workout) {
        if (event.time > 0) {
            val laps = workout.events.map {
                if (it.title == event.title) event.copy(time = event.time - 1) else it
            }
            this.workout.value = workout.copy(events = laps)
        }
    }

    fun plus(event: TimeEvent, workout: Workout) {
        if (event.time < 99) {
            val laps = workout.events.map {
                if (it.title == event.title) event.copy(time = event.time + 1) else it
            }
            this.workout.value = workout.copy(events = laps)
        }
    }
}

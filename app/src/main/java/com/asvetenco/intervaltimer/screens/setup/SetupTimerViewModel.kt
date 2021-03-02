package com.asvetenco.intervaltimer.screens.setup

import com.asvetenco.database.client.LocalTimerClient
import com.asvetenco.database.client.RoomNotFoundException
import com.asvetenco.intervaltimer.base.BaseViewModel
import com.asvetenco.intervaltimer.data.TimeEvent
import com.asvetenco.intervaltimer.data.TimeEventMapper
import com.asvetenco.intervaltimer.data.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class SetupTimerViewModel(
    private val client: LocalTimerClient,
    private val mapper: TimeEventMapper
) : BaseViewModel() {

    override val tag: String = SetupTimerViewModel::class.java.simpleName

    val workout = MutableStateFlow(Workout())
    val onWorkoutSaved = MutableStateFlow("")
    val onWorkoutTitleEdited = MutableStateFlow("")

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
                        workout.value = mapper.mapFromDto(workoutDto)
                    }
                })
        } else {
            workout.value = Workout.empty()
        }
    }

    fun minus(event: TimeEvent, workout: Workout) {
        if (event.time > 0) {
            val laps = workout.events.map {
                if (it.title == event.title) event.eventCopy(time = event.time - 1) else it
            }
            this.workout.value = workout.copy(events = laps)
        }
    }

    fun plus(event: TimeEvent, workout: Workout) {
        if (event.time < 99) {
            val laps = workout.events.map {
                if (it.title == event.title) event.eventCopy(time = event.time + 1) else it
            }
            this.workout.value = workout.copy(events = laps)
        }
    }

    fun saveWorkout() {
        launchDataLoad {
            client.saveWorkout(listOf(mapper.mapToDto(workout.value)))
            onWorkoutSaved.value = workout.value.title
        }
    }

    fun onWorkoutTitleEdited(value: String) {
        val updated = workout.value.copy(title = value)
        workout.value = updated
    }

    fun editExercises(event: TimeEvent, workout: Workout, count: String) {
        val min = count.toIntOrNull() ?: 0
        if (min <= 99) {
            editValue(event, workout) { event.eventCopy(time = min) }
        }
    }

    fun editMinutes(event: TimeEvent, workout: Workout, minutes: String) {
        val min = minutes.toIntOrNull() ?: 0
        if (min <= 99) {
            editValue(event, workout) {
                val time = event.seconds + 60 * min
                event.eventCopy(time = time)
            }
        }
    }

    fun editSeconds(event: TimeEvent, workout: Workout, seconds: String) {
        val sec = seconds.toIntOrNull() ?: 0
        if (sec <= 99) {
            editValue(event, workout) {
                val time = event.minutes * 60 + sec
                event.eventCopy(time = time)
            }
        }
    }

    private fun editValue(event: TimeEvent, workout: Workout, editEvent: () -> TimeEvent) {
        val laps = workout.events.map {
            if (it.title == event.title) {
                editEvent()
            } else it
        }
        this.workout.value = workout.copy(events = laps)
    }
}

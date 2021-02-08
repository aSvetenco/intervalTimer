package com.asvetenco.intervaltimer.screen.setup

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
    val onWorkoutSaved = MutableStateFlow(Unit)

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
            onWorkoutSaved.value = Unit
        }
    }
}

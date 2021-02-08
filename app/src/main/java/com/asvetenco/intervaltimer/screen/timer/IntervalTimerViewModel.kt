package com.asvetenco.intervaltimer.screen.timer

import com.asvetenco.database.client.LocalTimerClient
import com.asvetenco.database.client.RoomNotFoundException
import com.asvetenco.intervaltimer.base.BaseViewModel
import com.asvetenco.intervaltimer.countdown.CountdownTimer
import com.asvetenco.intervaltimer.data.TimeEventMapper
import com.asvetenco.intervaltimer.data.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class IntervalTimerViewModel(
    private val client: LocalTimerClient,
    private val mapper: TimeEventMapper,
    private val countdown: CountdownTimer
) : BaseViewModel() {

    override val tag: String = IntervalTimerViewModel::class.java.simpleName

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
                        workout.value = mapper.mapFromDto(workoutDto)
                    }
                })
        } else {
            workout.value = Workout.empty()
        }
    }

}

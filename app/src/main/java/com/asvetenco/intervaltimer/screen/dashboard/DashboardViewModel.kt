package com.asvetenco.intervaltimer.screen.dashboard

import com.asvetenco.database.client.LocalTimerClient
import com.asvetenco.database.dto.LapDto
import com.asvetenco.database.dto.WorkoutDto
import com.asvetenco.intervaltimer.base.BaseViewModel
import com.asvetenco.intervaltimer.data.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlin.random.Random

class DashboardViewModel(private val client: LocalTimerClient) : BaseViewModel() {

    override val tag: String = DashboardViewModel::class.java.canonicalName ?: "DashboardViewModel"

    val workoutFlow = MutableStateFlow(listOf<Workout>())

    fun retrieveWorkouts() {
        launchDataLoad {
            client.getAllWorkouts().collect {
                val workouts = it.map { workoutDto ->
                    Workout(
                        workoutDto.id,
                        workoutDto.title,
                        workoutDto.lapsCount,
                        workoutDto.lap.id,
                        listOf()
                    )
                }
                workoutFlow.value = workouts
            }
        }

    }

    fun fillDb() {
        val w = mutableListOf<WorkoutDto>()
        for (n in 0..20) {
            w.add(
                WorkoutDto(
                    id = System.nanoTime(),
                    title = "Workout $n",
                    lapsCount = Random.nextInt(1, 4),
                    LapDto(
                        id = System.nanoTime(),
                        warmUp = Random.nextInt(1, 100),
                        coolDown = Random.nextInt(1, 100),
                        work = Random.nextInt(1, 100),
                        rest = Random.nextInt(1, 100)
                    )

                )
            )
        }
        launchDataLoad {
            client.saveWorkout(w)
        }
    }

}
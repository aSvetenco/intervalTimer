package com.asvetenco.intervaltimer.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asvetenco.database.client.LocalTimerClient
import com.asvetenco.database.domain.Lap
import com.asvetenco.database.domain.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

class DashboardViewModel(private val client: LocalTimerClient) : ViewModel() {

    val workoutFlow = MutableStateFlow(listOf<Workout>())

    fun retrieveWorkouts() {
        viewModelScope.launch {
            client.getAllWorkouts().collect {
                workoutFlow.value = it
            }
        }

    }

    fun fillDb() {
        val w = mutableListOf<Workout>()
        for (n in 0..20) {
            w.add(
                Workout(
                    id = System.nanoTime(),
                    title = "Workout $n",
                    listOf(
                        Lap(
                            id = System.nanoTime(),
                            number = n,
                            warmUp = Random.nextInt(1, 100),
                            coolDawn = Random.nextInt(1, 100),
                            work = Random.nextInt(1, 100),
                            rest = Random.nextInt(1, 100),
                            intervalCount = 2
                        )
                    )
                ))
        }
        viewModelScope.launch {
            w.forEach {
                client.saveWorkout(it)
            }
        }
    }

}
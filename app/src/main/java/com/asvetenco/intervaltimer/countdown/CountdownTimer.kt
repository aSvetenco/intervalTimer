package com.asvetenco.intervaltimer.countdown

import androidx.annotation.WorkerThread
import kotlinx.coroutines.*

class CountdownTimer {

    private var listener: Listener? = null
    private var timerJob: Job? = null

    fun setOnPeriodFinishedListener(listener: Listener) {
        this.listener = listener
    }

    fun start(coroutineScope: CoroutineScope, time: Long, tickPeriod: Long = 1_000) {
        if (timerJob?.isActive == true) stop()
        timerJob = coroutineScope.launch {
            withContext(Dispatchers.Default) {
                createCountdownTimer(time, 0, tickPeriod)
            }
        }
    }


    fun start(coroutineScope: CoroutineScope, times: List<Long>, tickPeriod: Long = 1_000) {
        if (timerJob?.isActive == true) stop()
        timerJob = coroutineScope.launch {
            withContext(Dispatchers.Default) {
                times.forEachIndexed { index, time ->
                    createCountdownTimer(time, index, tickPeriod)
                }
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
    }

    private suspend fun createCountdownTimer(time: Long, lapIndex: Int, tickPeriod: Long) {
        var delta = time
        while (delta > tickPeriod) {
            delta -= tickPeriod
            delay(tickPeriod)
            listener?.onTick(delta)
        }
        listener?.onFinish(lapIndex)
    }

    interface Listener {
        @WorkerThread
        suspend fun onFinish(lapIndex: Int)

        @WorkerThread
        suspend fun onTick(remainingTime: Long)
    }
}
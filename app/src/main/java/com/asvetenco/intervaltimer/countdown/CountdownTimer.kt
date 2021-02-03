package com.asvetenco.intervaltimer.countdown

import androidx.annotation.WorkerThread
import kotlinx.coroutines.*

class CountdownTimer {

    private var listener: Listener? = null
    private var timerJob: Job? = null

    fun setOnPeriodFinishedListener(listener: Listener) {
        this.listener = listener
    }

    fun start(coroutineScope: CoroutineScope, time: Long, tickPeriod: Long = 1_00) {
        if (timerJob?.isActive == true) stop()
        timerJob = coroutineScope.launch {
            withContext(Dispatchers.Default) {
                createCountdownTimer(time, tickPeriod)
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
    }

    private suspend fun createCountdownTimer(time: Long, tickPeriod: Long) {
        var delta = time
        while (delta > tickPeriod) {
            delta -= tickPeriod
            delay(tickPeriod)
            listener?.onTick(delta)
        }
        listener?.onFinish()
    }

    interface Listener {
        @WorkerThread
        suspend fun onFinish()

        @WorkerThread
        suspend fun onTick(remainingTime: Long)
    }
}
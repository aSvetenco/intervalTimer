package com.asvetenco.database

import android.content.Context
import com.asvetenco.database.client.LocalTimerClient
import com.asvetenco.database.client.LocalTimerDataSource
import com.asvetenco.database.dto.WorkoutEntityMapper

class WorkoutProvider(private val appContext: Context, ) {

    fun localTimerClient(): LocalTimerClient {
        val dao = IntervalTimerDatabase.getInstance(context = appContext).workoutDao()
        return LocalTimerDataSource(dao, WorkoutEntityMapper())
    }
}
package com.asvetenco.intervaltimer.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.asvetenco.database.WorkoutProvider
import com.asvetenco.intervaltimer.data.TimeEventMapper

class DependenciesContainer(app: Application) {

    private val dataSource = WorkoutProvider(app.applicationContext).localTimerClient()

    fun setupTimerVMFactory(): ViewModelProvider.Factory = SetupViewModelFactory(dataSource, TimeEventMapper())

    fun dashboardVMFactory(): ViewModelProvider.Factory = DashboardViewModelFactory(dataSource)
}


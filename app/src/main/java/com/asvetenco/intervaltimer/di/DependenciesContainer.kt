package com.asvetenco.intervaltimer.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.asvetenco.database.WorkoutProvider

class DependenciesContainer(app: Application) {

    private val dataSource = WorkoutProvider(app.applicationContext).localTimerClient()

    fun setupTimerVMFactory(): ViewModelProvider.Factory = SetupViewModelFactory(dataSource)

    fun dashboardVMFactory(): ViewModelProvider.Factory = DashboardViewModelFactory(dataSource)
}


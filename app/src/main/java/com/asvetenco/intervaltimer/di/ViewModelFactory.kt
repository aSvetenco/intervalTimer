package com.asvetenco.intervaltimer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asvetenco.database.client.LocalTimerClient
import com.asvetenco.intervaltimer.base.BaseViewModel
import com.asvetenco.intervaltimer.data.TimeEventMapper
import com.asvetenco.intervaltimer.screens.dashboard.DashboardViewModel
import com.asvetenco.intervaltimer.screens.setup.SetupTimerViewModel

class SetupViewModelFactory(
    private val dataSource: LocalTimerClient,
    private val mapper: TimeEventMapper,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != BaseViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SetupTimerViewModel(dataSource, mapper) as T
    }
}

class DashboardViewModelFactory(
    private val dataSource: LocalTimerClient,
    private val mapper: TimeEventMapper
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != BaseViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return DashboardViewModel(dataSource, mapper) as T
    }
}

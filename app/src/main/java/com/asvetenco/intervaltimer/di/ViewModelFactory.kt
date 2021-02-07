package com.asvetenco.intervaltimer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asvetenco.database.client.LocalTimerClient
import com.asvetenco.intervaltimer.base.BaseViewModel
import com.asvetenco.intervaltimer.screen.dashboard.DashboardViewModel
import com.asvetenco.intervaltimer.screen.setup.SetupTimerViewModel
import java.util.*

class SetupViewModelFactory(
    private val dataSource: LocalTimerClient
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != BaseViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return SetupTimerViewModel(dataSource) as T
    }
}

class DashboardViewModelFactory(
    private val dataSource: LocalTimerClient
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != BaseViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return DashboardViewModel(dataSource) as T
    }
}
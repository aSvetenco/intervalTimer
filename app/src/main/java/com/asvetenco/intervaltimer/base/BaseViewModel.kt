package com.asvetenco.intervaltimer.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    abstract val tag: String

    protected fun launchDataLoad(
        doOnError: (Throwable) -> Unit = { },
        doOnComplete: () -> Unit = { },
        block: suspend () -> Unit
    ): Job = viewModelScope.launch {
        try {
            block()
        } catch (t: Throwable) {
            doOnError(t)
            onError(t)
        } finally {
            doOnComplete()
        }
    }

    private fun onError(throwable: Throwable) {
        when (throwable) {
            is CancellationException -> Unit
            else -> Log.e(tag, throwable.message, throwable)
        }
    }
}

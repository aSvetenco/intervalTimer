package com.asvetenco.intervaltimer


fun <T> lazily(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Int.toSeconds() = this % 60

fun Int.toMinutes() = (this / 60)
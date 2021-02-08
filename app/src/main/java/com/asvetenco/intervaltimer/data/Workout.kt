package com.asvetenco.intervaltimer.data

import com.asvetenco.intervaltimer.R


data class Workout(
    val id: Long = 0L,
    val title: String = "",
    val lapsCount: Int = 0,
    val lapId: Long = 0L,
    val events: List<TimeEvent> = listOf()
) {
    companion object {
        fun empty() = Workout(
            id = System.nanoTime(),
            lapId = System.nanoTime(),
            events = listOf(
                TimeEvent.warmUp(),
                TimeEvent.coolDown(),
                TimeEvent.work(),
                TimeEvent.rest(),
                TimeEvent.exercises()
            )
        )
    }
}

sealed class TimeEvent(
    open val title: Int,
    open val time: Int,
    open val phrase: Int
) {

    abstract fun eventCopy(
        title: Int = this.title,
        time: Int = this.time,
        phrase: Int = this.phrase
    ): TimeEvent

    companion object {
        fun warmUp(time: Int = 0) =
            WarmUp(R.string.set_up_timer_warm_up, time, R.string.set_up_timer_warm_up)

        fun coolDown(time: Int = 0) =
            CoolDown(R.string.set_up_timer_cool_down, time, R.string.set_up_timer_warm_up)

        fun work(time: Int = 0) =
            Work(R.string.set_up_timer_work, time, R.string.set_up_timer_work)

        fun rest(time: Int = 0) =
            Rest(R.string.set_up_timer_rest, time, R.string.set_up_timer_rest)

        fun exercises(time: Int = 0) =
            Exercise(R.string.set_up_timer_exercises, time, R.string.set_up_timer_exercises)
    }
}

data class WarmUp(
    override val title: Int,
    override val time: Int,
    override val phrase: Int
) : TimeEvent(title, time, phrase) {

    override fun eventCopy(title: Int, time: Int, phrase: Int): WarmUp = copy(
        title = title,
        time = time,
        phrase = phrase
    )
}

data class CoolDown(
    override val title: Int,
    override val time: Int,
    override val phrase: Int
) : TimeEvent(title, time, phrase) {

    override fun eventCopy(title: Int, time: Int, phrase: Int): CoolDown = copy(
        title = title,
        time = time,
        phrase = phrase
    )
}

data class Work(
    override val title: Int,
    override val time: Int,
    override val phrase: Int
) : TimeEvent(title, time, phrase) {

    override fun eventCopy(title: Int, time: Int, phrase: Int): Work = copy(
        title = title,
        time = time,
        phrase = phrase
    )
}

data class Rest(
    override val title: Int,
    override val time: Int,
    override val phrase: Int
) : TimeEvent(title, time, phrase) {

    override fun eventCopy(title: Int, time: Int, phrase: Int): Rest = copy(
        title = title,
        time = time,
        phrase = phrase
    )
}

data class Exercise(
    override val title: Int,
    override val time: Int,
    override val phrase: Int
) : TimeEvent(title, time, phrase) {

    override fun eventCopy(title: Int, time: Int, phrase: Int): Exercise = copy(
        title = title,
        time = time,
        phrase = phrase
    )
}
package com.asvetenco.intervaltimer.data

import com.asvetenco.intervaltimer.R
import com.asvetenco.intervaltimer.toMinutes
import com.asvetenco.intervaltimer.toSeconds


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
    open val phrase: Int,
    open val minutes: Int,
    open val seconds: Int,
) {

    abstract fun eventCopy(
        title: Int = this.title,
        time: Int = this.time,
        phrase: Int = this.phrase,
        minutes: Int = this.minutes,
        seconds: Int = this.seconds,
    ): TimeEvent

    companion object {

        fun warmUp(time: Int = 45) =
            WarmUp(
                R.string.set_up_timer_warm_up, time, R.string.set_up_timer_warm_up,
                time.toMinutes(), time.toSeconds()
            )

        fun coolDown(time: Int = 45) =
            CoolDown(
                R.string.set_up_timer_cool_down, time, R.string.set_up_timer_warm_up,
                time.toMinutes(), time.toSeconds()
            )

        fun work(time: Int = 25) =
            Work(
                R.string.set_up_timer_work, time, R.string.set_up_timer_work,
                time.toMinutes(), time.toSeconds()
            )

        fun rest(time: Int = 10) =
            Rest(
                R.string.set_up_timer_rest, time, R.string.set_up_timer_rest,
                time.toMinutes(), time.toSeconds()
            )

        fun exercises(time: Int = 1) =
            Exercise(
                R.string.set_up_timer_exercises, time, R.string.set_up_timer_exercises,
                0, 0
            )

        fun congrats(time: Int = 1) =
            Exercise(
                R.string.set_up_timer_congrats, time, R.string.set_up_timer_congrats,
                0, 0
            )
    }
}

data class WarmUp(
    override val title: Int,
    override val time: Int,
    override val phrase: Int,
    override val minutes: Int,
    override val seconds: Int,
) : TimeEvent(title, time, phrase, minutes, seconds) {

    override fun eventCopy(
        title: Int,
        time: Int,
        phrase: Int,
        minutes: Int,
        seconds: Int,
    ) = WarmUp(
        title = title,
        time = time,
        phrase = phrase,
        minutes = time.toMinutes(),
        seconds = time.toSeconds(),
    )

}

data class CoolDown(
    override val title: Int,
    override val time: Int,
    override val phrase: Int,
    override val minutes: Int = 0,
    override val seconds: Int = 0,
) : TimeEvent(title, time, phrase, minutes, seconds) {

    override fun eventCopy(
        title: Int,
        time: Int,
        phrase: Int,
        minutes: Int,
        seconds: Int,
    ) = CoolDown(
        title = title,
        time = time,
        phrase = phrase,
        minutes = time.toMinutes(),
        seconds = time.toSeconds(),
    )
}

data class Work(
    override val title: Int,
    override val time: Int,
    override val phrase: Int,
    override val minutes: Int,
    override val seconds: Int,
) : TimeEvent(title, time, phrase, minutes, seconds) {

    override fun eventCopy(
        title: Int,
        time: Int,
        phrase: Int,
        minutes: Int,
        seconds: Int,
    ) = Work(
        title = title,
        time = time,
        phrase = phrase,
        minutes = time.toMinutes(),
        seconds = time.toSeconds(),
    )
}

data class Rest(
    override val title: Int,
    override val time: Int,
    override val phrase: Int,
    override val minutes: Int,
    override val seconds: Int,
) : TimeEvent(title, time, phrase, minutes, seconds) {

    override fun eventCopy(
        title: Int,
        time: Int,
        phrase: Int,
        minutes: Int,
        seconds: Int,
    ) = Rest(
        title = title,
        time = time,
        phrase = phrase,
        minutes = time.toMinutes(),
        seconds = time.toSeconds(),
    )
}

data class Exercise(
    override val title: Int,
    override val time: Int,
    override val phrase: Int,
    override val minutes: Int,
    override val seconds: Int,
) : TimeEvent(title, time, phrase, minutes, seconds) {

    override fun eventCopy(
        title: Int,
        time: Int,
        phrase: Int,
        minutes: Int,
        seconds: Int,
    ) = Exercise(
        title = title,
        time = time,
        phrase = phrase,
        minutes = time.toMinutes(),
        seconds = time.toSeconds(),
    )
}

data class Congrats(
    override val title: Int,
    override val time: Int,
    override val phrase: Int,
    override val minutes: Int,
    override val seconds: Int,
) : TimeEvent(title, time, phrase, minutes, seconds) {

    override fun eventCopy(
        title: Int,
        time: Int,
        phrase: Int,
        minutes: Int,
        seconds: Int,
    ) = Congrats(
        title = title,
        time = time,
        phrase = phrase,
        minutes = time.toMinutes(),
        seconds = time.toSeconds(),
    )
}

class TimeEventComparator : Comparator<TimeEvent> {
    override fun compare(o1: TimeEvent, o2: TimeEvent): Int {
        return when {
            o1 is WarmUp -> -1
            o2 is WarmUp -> 1
            o1 is Work -> -1
            o2 is Work -> 1
            o1 is Rest -> -1
            o2 is Rest -> 1
            o1 is CoolDown -> -1
            o2 is CoolDown -> 1
            else -> 1
        }
    }


}

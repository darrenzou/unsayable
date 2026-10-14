package com.darrenzou.unsayable.game

/** Round timer length options. Zero seconds means no timer. */
object TimerLength {
    const val OFF = 0
    const val STEP = 15
    const val MAX = 5 * 60
    const val DEFAULT = 60

    /** Steps the length up or down by [STEP], going from the shortest length down to off. */
    fun adjust(seconds: Int, steps: Int): Int = (seconds + steps * STEP).coerceIn(OFF, MAX)

    fun label(seconds: Int): String = if (seconds == OFF) "No timer" else format(seconds)

    fun format(seconds: Int): String = "%d:%02d".format(seconds / 60, seconds % 60)
}

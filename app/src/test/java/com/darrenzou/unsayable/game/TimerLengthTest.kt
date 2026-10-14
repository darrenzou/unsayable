package com.darrenzou.unsayable.game

import org.junit.Assert.assertEquals
import org.junit.Test

class TimerLengthTest {
    @Test
    fun stepsDownToNoTimerAndStopsThere() {
        assertEquals(TimerLength.OFF, TimerLength.adjust(15, -1))
        assertEquals(TimerLength.OFF, TimerLength.adjust(TimerLength.OFF, -1))
        assertEquals(15, TimerLength.adjust(TimerLength.OFF, 1))
    }

    @Test
    fun capsAtFiveMinutes() {
        assertEquals(TimerLength.MAX, TimerLength.adjust(TimerLength.MAX, 1))
        assertEquals(300, TimerLength.MAX)
    }

    @Test
    fun labels() {
        assertEquals("No timer", TimerLength.label(0))
        assertEquals("1:00", TimerLength.label(60))
        assertEquals("1:45", TimerLength.label(105))
        assertEquals("0:09", TimerLength.format(9))
    }
}

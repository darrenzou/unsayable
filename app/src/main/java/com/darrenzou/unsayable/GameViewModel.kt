package com.darrenzou.unsayable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darrenzou.unsayable.game.Card
import com.darrenzou.unsayable.game.CardDeck
import com.darrenzou.unsayable.game.Difficulty
import com.darrenzou.unsayable.game.StarterDeck
import com.darrenzou.unsayable.game.TimerLength
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

/** Countdown for one round. [secondsLeft] reaching zero means time's up. */
data class Countdown(val total: Int, val secondsLeft: Int, val paused: Boolean = false) {
    val timeUp: Boolean get() = secondsLeft <= 0
}

class GameViewModel : ViewModel() {
    private var deck: CardDeck? = null
    private var ticker: Job? = null

    /** The card on screen, or null on the home screen. */
    private val _card = MutableStateFlow<Card?>(null)
    val card: StateFlow<Card?> = _card.asStateFlow()

    private val _difficulty = MutableStateFlow(Difficulty.EASY)
    val difficulty: StateFlow<Difficulty> = _difficulty.asStateFlow()

    /** Chosen round length in seconds, [TimerLength.OFF] for no timer. */
    private val _timerSeconds = MutableStateFlow(TimerLength.DEFAULT)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    /** The running countdown, or null when the timer is off or not in a round. */
    private val _countdown = MutableStateFlow<Countdown?>(null)
    val countdown: StateFlow<Countdown?> = _countdown.asStateFlow()

    fun selectDifficulty(difficulty: Difficulty) {
        _difficulty.value = difficulty
    }

    fun adjustTimer(steps: Int) {
        _timerSeconds.update { TimerLength.adjust(it, steps) }
    }

    fun start() {
        deck = CardDeck(StarterDeck.cards, _difficulty.value)
        nextCard()
        startCountdown()
    }

    fun nextCard() {
        if (_countdown.value?.timeUp == true) return
        _card.value = deck?.next(_card.value)
    }

    /** After time's up: a fresh card and a full timer. */
    fun playAgain() {
        _countdown.value = null
        nextCard()
        startCountdown()
    }

    fun togglePause() {
        _countdown.update { c -> if (c == null || c.timeUp) c else c.copy(paused = !c.paused) }
    }

    fun home() {
        ticker?.cancel()
        _countdown.value = null
        _card.value = null
    }

    private fun startCountdown() {
        ticker?.cancel()
        val total = _timerSeconds.value
        if (total == TimerLength.OFF) {
            _countdown.value = null
            return
        }
        _countdown.value = Countdown(total, total)
        ticker = viewModelScope.launch {
            while (true) {
                delay(1_000)
                val next = _countdown.updateAndGet { c ->
                    if (c == null || c.paused || c.timeUp) c else c.copy(secondsLeft = c.secondsLeft - 1)
                }
                if (next == null || next.timeUp) break
            }
        }
    }
}

package com.darrenzou.unsayable

import androidx.lifecycle.ViewModel
import com.darrenzou.unsayable.game.Card
import com.darrenzou.unsayable.game.CardDeck
import com.darrenzou.unsayable.game.Difficulty
import com.darrenzou.unsayable.game.StarterDeck
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    private var deck: CardDeck? = null

    /** The card on screen, or null on the home screen. */
    private val _card = MutableStateFlow<Card?>(null)
    val card: StateFlow<Card?> = _card.asStateFlow()

    private val _difficulty = MutableStateFlow(Difficulty.EASY)
    val difficulty: StateFlow<Difficulty> = _difficulty.asStateFlow()

    fun selectDifficulty(difficulty: Difficulty) {
        _difficulty.value = difficulty
    }

    fun start() {
        deck = CardDeck(StarterDeck.cards, _difficulty.value)
        nextCard()
    }

    fun nextCard() {
        _card.value = deck?.next(_card.value)
    }

    fun home() {
        _card.value = null
    }
}

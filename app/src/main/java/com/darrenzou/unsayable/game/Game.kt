package com.darrenzou.unsayable.game

import kotlin.random.Random

enum class Difficulty(val label: String, val description: String) {
    EASY("Easy", "Everyday things you can picture"),
    HARD("Hard", "Trickier words and phrases"),
    EVEN_HARDER("Even Harder", "Abstract ideas that are hard to pin down"),
}

/** A word to describe and the five words you can't say while describing it. */
data class Card(val target: String, val forbidden: List<String>, val difficulty: Difficulty)

/**
 * Deals the cards of one difficulty in a random order. No card repeats until
 * every card at that difficulty has been seen, then the deck reshuffles.
 */
class CardDeck(
    allCards: List<Card>,
    val difficulty: Difficulty,
    private val random: Random = Random,
) {
    private val cards = allCards.filter { it.difficulty == difficulty }
    private val pile = ArrayDeque<Card>()

    init {
        require(cards.isNotEmpty()) { "No ${difficulty.label} cards" }
    }

    fun next(previous: Card? = null): Card {
        if (pile.isEmpty()) {
            pile.addAll(cards.shuffled(random))
            // Don't show the same card twice in a row across a reshuffle.
            if (pile.size > 1 && pile.first() == previous) pile.addLast(pile.removeFirst())
        }
        return pile.removeFirst()
    }
}

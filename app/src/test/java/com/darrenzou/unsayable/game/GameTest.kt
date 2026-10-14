package com.darrenzou.unsayable.game

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameTest {
    private val words = listOf("1", "2", "3", "4", "5")
    private val cards = listOf(
        Card("A", words, Difficulty.EASY),
        Card("B", words, Difficulty.EASY),
        Card("C", words, Difficulty.EASY),
        Card("D", words, Difficulty.HARD),
    )

    @Test
    fun dealsOnlyTheChosenDifficulty() {
        val deck = CardDeck(cards, Difficulty.EASY, Random(1))
        repeat(20) { assertEquals(Difficulty.EASY, deck.next().difficulty) }
    }

    @Test
    fun noRepeatsUntilEveryCardIsSeen() {
        val deck = CardDeck(cards, Difficulty.EASY, Random(2))
        val firstPass = List(3) { deck.next().target }.toSet()
        assertEquals(setOf("A", "B", "C"), firstPass)
    }

    @Test
    fun neverTheSameCardTwiceInARow() {
        val deck = CardDeck(cards, Difficulty.EASY, Random(3))
        var previous: Card? = null
        repeat(300) {
            val card = deck.next(previous)
            assertNotEquals(previous, card)
            previous = card
        }
    }

    @Test
    fun starterDeckIsWellFormed() {
        val deck = StarterDeck.cards
        assertEquals(deck.size, deck.map { it.target.lowercase() }.toSet().size)
        deck.forEach { assertEquals(it.target, 5, it.forbidden.size) }
        Difficulty.entries.forEach { d ->
            assertTrue("$d has too few cards", deck.count { it.difficulty == d } >= 30)
        }
    }
}

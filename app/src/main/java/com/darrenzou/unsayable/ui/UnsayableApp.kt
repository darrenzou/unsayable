package com.darrenzou.unsayable.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darrenzou.unsayable.Countdown
import com.darrenzou.unsayable.GameViewModel
import com.darrenzou.unsayable.game.Card as WordCard
import com.darrenzou.unsayable.game.Difficulty
import com.darrenzou.unsayable.game.TimerLength

@Composable
fun UnsayableApp(vm: GameViewModel = viewModel()) {
    val card by vm.card.collectAsStateWithLifecycle()
    val difficulty by vm.difficulty.collectAsStateWithLifecycle()
    val timerSeconds by vm.timerSeconds.collectAsStateWithLifecycle()
    val countdown by vm.countdown.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .safeDrawingPadding()
                .padding(20.dp)
        ) {
            val current = card
            if (current == null) {
                HomeScreen(
                    selected = difficulty,
                    timerSeconds = timerSeconds,
                    onSelect = vm::selectDifficulty,
                    onAdjustTimer = vm::adjustTimer,
                    onStart = vm::start,
                )
            } else {
                BackHandler(onBack = vm::home)
                CardScreen(
                    card = current,
                    countdown = countdown,
                    onNext = vm::nextCard,
                    onTogglePause = vm::togglePause,
                    onPlayAgain = vm::playAgain,
                    onHome = vm::home,
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    selected: Difficulty,
    timerSeconds: Int,
    onSelect: (Difficulty) -> Unit,
    onAdjustTimer: (steps: Int) -> Unit,
    onStart: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Unsayable", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black)
        Text(
            "Describe the word at the top of the card without saying any of the words underneath.",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(8.dp))
        Text("Difficulty", style = MaterialTheme.typography.titleMedium)
        Difficulty.entries.forEach { difficulty ->
            DifficultyOption(difficulty, isSelected = difficulty == selected) { onSelect(difficulty) }
        }
        Spacer(Modifier.height(8.dp))
        Text("Timer", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { onAdjustTimer(-1) }, enabled = timerSeconds > TimerLength.OFF) { Text("−") }
            Text(
                TimerLength.label(timerSeconds),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            OutlinedButton(onClick = { onAdjustTimer(1) }, enabled = timerSeconds < TimerLength.MAX) { Text("+") }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text("Start", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun DifficultyOption(difficulty: Difficulty, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(difficulty.label, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(difficulty.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun CardScreen(
    card: WordCard,
    countdown: Countdown?,
    onNext: () -> Unit,
    onTogglePause: () -> Unit,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
) {
    val timeUp = countdown?.timeUp == true
    val paused = countdown?.paused == true
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(card.difficulty.label, style = MaterialTheme.typography.labelLarge)
        if (countdown != null) {
            val lowTime = countdown.secondsLeft <= 10
            Text(
                TimerLength.format(countdown.secondsLeft.coerceAtLeast(0)),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = if (lowTime) ForbiddenRed else MaterialTheme.colorScheme.onSurface,
            )
            LinearProgressIndicator(
                progress = { countdown.secondsLeft.coerceAtLeast(0) / countdown.total.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = if (lowTime) ForbiddenRed else MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                when {
                    timeUp -> Text(
                        "Time's up!",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = ForbiddenRed,
                    )
                    // Hide the card while paused so nobody gets a head start.
                    paused -> Text("Paused", style = MaterialTheme.typography.headlineLarge)
                    else -> {
                        Text(
                            card.target.uppercase(),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        HorizontalDivider(Modifier.padding(vertical = 16.dp))
                        card.forbidden.forEach { word ->
                            Text(
                                word,
                                style = MaterialTheme.typography.titleLarge,
                                color = ForbiddenRed,
                                modifier = Modifier.padding(vertical = 4.dp),
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = if (timeUp) onPlayAgain else onNext,
            enabled = !paused,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        ) {
            Text(if (timeUp) "Play again" else "Next card", style = MaterialTheme.typography.titleLarge)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = onHome) { Text("Change settings") }
            if (countdown != null && !timeUp) {
                TextButton(onClick = onTogglePause) { Text(if (paused) "Resume" else "Pause") }
            }
        }
    }
}

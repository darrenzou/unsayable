package com.darrenzou.unsayable.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.darrenzou.unsayable.GameViewModel
import com.darrenzou.unsayable.game.Card as WordCard
import com.darrenzou.unsayable.game.Difficulty

@Composable
fun UnsayableApp(vm: GameViewModel = viewModel()) {
    val card by vm.card.collectAsStateWithLifecycle()
    val difficulty by vm.difficulty.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .safeDrawingPadding()
                .padding(20.dp)
        ) {
            val current = card
            if (current == null) {
                HomeScreen(difficulty, onSelect = vm::selectDifficulty, onStart = vm::start)
            } else {
                BackHandler(onBack = vm::home)
                CardScreen(current, onNext = vm::nextCard, onHome = vm::home)
            }
        }
    }
}

@Composable
private fun HomeScreen(selected: Difficulty, onSelect: (Difficulty) -> Unit, onStart: () -> Unit) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
        Spacer(Modifier.weight(1f))
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
private fun CardScreen(card: WordCard, onNext: () -> Unit, onHome: () -> Unit) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(card.difficulty.label, style = MaterialTheme.typography.labelLarge)
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
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        ) {
            Text("Next card", style = MaterialTheme.typography.titleLarge)
        }
        TextButton(onClick = onHome) { Text("Change difficulty") }
    }
}

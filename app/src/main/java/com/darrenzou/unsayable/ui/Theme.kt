package com.darrenzou.unsayable.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Plum = Color(0xFF5B2A86)
private val PlumLight = Color(0xFFD7B8F3)
private val Coral = Color(0xFFE5484D)
private val Mint = Color(0xFF2E9E6A)

val ForbiddenRed = Coral

private val LightColors = lightColorScheme(
    primary = Plum,
    onPrimary = Color.White,
    secondary = Mint,
    error = Coral,
)

private val DarkColors = darkColorScheme(
    primary = PlumLight,
    onPrimary = Color(0xFF2A0D45),
    secondary = Mint,
    error = Coral,
)

@Composable
fun UnsayableTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}

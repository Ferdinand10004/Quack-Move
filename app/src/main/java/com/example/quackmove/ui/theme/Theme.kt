

package com.example.quackmove.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Paleta oscura de QuackMove
private val QuackMoveDarkColorScheme = darkColorScheme(
    primary = QuackPurple,
    onPrimary = QuackTextPrimary,

    secondary = QuackMint,
    onSecondary = QuackBackground,

    tertiary = QuackBlue,

    background = QuackBackground,
    onBackground = QuackTextPrimary,

    surface = QuackSurface,
    onSurface = QuackTextPrimary,

    surfaceVariant = QuackSurfaceVariant,
    onSurfaceVariant = QuackTextSecondary,

    outline = QuackBorder
)

@Composable
fun QuackMoveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = QuackMoveDarkColorScheme,
        typography = Typography,
        content = content
    )
}
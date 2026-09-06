package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = KhakiLight,
    onPrimary = PoliceNavyDark,
    primaryContainer = PoliceNavyMedium,
    onPrimaryContainer = BrassGoldLight,
    secondary = BrassGold,
    onSecondary = PoliceNavyDark,
    tertiary = SealRedLight,
    background = LedgerPaperDark,
    onBackground = Color(0xFFE2E8F0),
    surface = LedgerSurfaceDark,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF2A3241),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
    primary = PoliceNavyDark,
    onPrimary = Color.White,
    primaryContainer = KhakiContainer,
    onPrimaryContainer = KhakiOnContainer,
    secondary = KhakiPrimary,
    onSecondary = Color.White,
    tertiary = SealRed,
    background = LedgerPaper,
    onBackground = Color(0xFF1E293B),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1EFE9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent authentic institutional styling
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


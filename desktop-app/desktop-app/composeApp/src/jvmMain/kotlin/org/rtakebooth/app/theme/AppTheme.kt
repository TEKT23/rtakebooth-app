package org.rtakebooth.app.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Luminous Pro Design System Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE91E63),           // Primary (Pink/Magenta)
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBC004B),
    onPrimaryContainer = Color(0xFFFFD9DE),
    
    secondary = Color(0xFF2D2D2D),         // Secondary
    onSecondary = Color(0xFFE5E2E1),
    secondaryContainer = Color(0xFF474747),
    onSecondaryContainer = Color(0xFFE5E2E1),
    
    tertiary = Color(0xFF008C47),          // Tertiary (Green)
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF005227),
    onTertiaryContainer = Color(0xFF89FAA8),
    
    background = Color(0xFF121212),        // Neutral Background
    onBackground = Color(0xFFE5E2E1),
    
    surface = Color(0xFF1E1E1E),           // Card/Panel Surface
    onSurface = Color(0xFFE5E2E1),
    surfaceVariant = Color(0xFF2D2D2D),    // Inputs, toggle background
    onSurfaceVariant = Color(0xFFB6B5B4),
    
    outline = Color(0xFF474747),
    outlineVariant = Color(0xFF2D2D2D),
    
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

@Composable
fun RtakeBoothTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        // We can add Typography here later if needed
        content = content
    )
}

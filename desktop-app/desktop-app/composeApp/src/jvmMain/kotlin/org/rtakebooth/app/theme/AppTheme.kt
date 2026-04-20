package org.rtakebooth.app.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Brand colors — dark professional theme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6C9FFF),           // Soft blue
    onPrimary = Color(0xFF003063),
    primaryContainer = Color(0xFF1B1B2F),  // Dark navy sidebar
    onPrimaryContainer = Color(0xFFD6E3FF),
    secondary = Color(0xFF9ECAFF),
    secondaryContainer = Color(0xFF004A77),
    onSecondaryContainer = Color(0xFFD6E3FF),
    background = Color(0xFF111118),        // Main dark background
    onBackground = Color(0xFFE2E2E9),
    surface = Color(0xFF1B1B23),           // Card/panel surface
    onSurface = Color(0xFFE2E2E9),
    surfaceVariant = Color(0xFF44464F),
    onSurfaceVariant = Color(0xFFC5C6D0),
    outline = Color(0xFF8F9099),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

@Composable
fun RtakeBoothTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}

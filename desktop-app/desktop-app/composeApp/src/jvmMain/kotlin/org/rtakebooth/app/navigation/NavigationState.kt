package org.rtakebooth.app.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class Screen(val label: String, val icon: String) {
    EDITOR("Screen Editor", "🎨"),
    GENERAL("General", "⚙️"),
    CAPTURE("Capture", "📷"),
    CAMERA("Camera", "🎥"),
    ATTENDANT("Attendant", "🎙️"),
    LAYOUT("Photo Layout", "🖼️"),
    SHARING("Sharing", "📤"),
    PRINT("Print Setup", "🖨️"),
    PRESETS_TEMPLATE("Presets/Template", "📋"),
    // -- separator -- (SHARING_STATUS and EXPORT are in the Event group)
    SHARING_STATUS("Sharing Status", "📊"),
    EXPORT("Export & Config", "💾"),
    EVENTS("Events", "📅"),
}

// Screens that belong to the "Settings" group (first block in sidebar)
val SETTINGS_SCREENS = listOf(
    Screen.EDITOR,
    Screen.GENERAL,
    Screen.CAPTURE,
    Screen.CAMERA,
    Screen.ATTENDANT,
    Screen.LAYOUT,
    Screen.SHARING,
    Screen.PRINT,
    Screen.PRESETS_TEMPLATE,
)

// Screens that belong to the "Event" group (second block in sidebar)
val EVENT_SCREENS = listOf(
    Screen.SHARING_STATUS,
    Screen.EXPORT,
    Screen.EVENTS,
)

class NavigationState {
    var currentScreen by mutableStateOf(Screen.GENERAL)
        private set

    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }
}

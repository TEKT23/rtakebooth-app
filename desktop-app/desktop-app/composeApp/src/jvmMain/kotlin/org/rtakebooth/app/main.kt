package org.rtakebooth.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "desktop-app",
    ) {
        App()
    }
}
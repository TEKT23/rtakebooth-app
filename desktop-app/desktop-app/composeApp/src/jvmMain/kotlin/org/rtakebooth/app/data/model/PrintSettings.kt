package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PrintSettings(
    val mainPrinter: String = "",
    val secondaryPrinter: String = "",
    val scale: Float = 100.0f,
    val horizontalPosition: Float = 0.0f,
    val verticalPosition: Float = 0.0f,
    val printAutomatically: Boolean = false,
    val showPrintButton: Boolean = true,
    val printToBothPrinters: Boolean = false,
    val automateToFit: Boolean = true,
    val limitPerEvent: Int = 0,
    val limitPerSession: Int = 0
)

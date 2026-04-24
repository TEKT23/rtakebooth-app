package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PrintTemplate(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "Untitled Template",
    val elements: List<CanvasElement> = emptyList(),
    val paperSize: String = "4x6",
    val resolution: Int = 300,
    val orientation: Orientation = Orientation.LANDSCAPE,
    val widthPx: Int = 1800,  // 4 inch * 300 dpi = 1200, 6 inch * 300 dpi = 1800
    val heightPx: Int = 1200,
    val printToSecondary: Boolean = false,
    val backgroundColor: String = "#FFFFFF"
)

@Serializable
data class TemplateEditorState(
    val currentTemplate: PrintTemplate = PrintTemplate(),
    val selectedElementId: String? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

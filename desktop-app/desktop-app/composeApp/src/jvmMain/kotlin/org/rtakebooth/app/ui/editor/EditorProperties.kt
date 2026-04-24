package org.rtakebooth.app.ui.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.data.model.*
import org.rtakebooth.app.ui.components.*

@Composable
fun EditorProperties(
    state: EditorState,
    selectedElement: CanvasElement?,
    onUpdateAspectRatio: (AspectRatio) -> Unit,
    onUpdateOrientation: (Orientation) -> Unit,
    onUpdateCanvasBackground: (String) -> Unit,
    onUpdateShowLiveView: (Boolean) -> Unit,
    onUpdateCropLiveView: (Boolean) -> Unit,
    onUpdateMirrorLiveView: (Boolean) -> Unit,
    onUpdateShowGalleryButton: (Boolean) -> Unit,
    onUpdateShowCountdown: (Boolean) -> Unit,
    onUpdateShowCancelButton: (Boolean) -> Unit,
    onUpdateSessionTrigger: (SessionTrigger) -> Unit,
    onUpdateElement: (String, (CanvasElement) -> CanvasElement) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxHeight().width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
        ) {
            // === Canvas Properties ===
            PropertySection(title = "Canvas Layout") {
                DropdownRow(
                    label = "Aspect Ratio",
                    selectedValue = state.aspectRatio.label,
                    options = AspectRatio.entries.map { it.label },
                    onValueChange = { label ->
                        AspectRatio.entries.find { it.label == label }?.let { onUpdateAspectRatio(it) }
                    }
                )

                RadioGroupRow(
                    label = "Orientation",
                    selectedOption = state.orientation.name.lowercase().replaceFirstChar { it.uppercase() },
                    options = listOf("Landscape", "Portrait"),
                    onOptionSelected = { 
                        onUpdateOrientation(Orientation.valueOf(it.uppercase()))
                    }
                )

                TextFieldRow(
                    label = "Background Color",
                    value = state.canvasBackgroundColor,
                    placeholder = "#123456",
                    onValueChange = onUpdateCanvasBackground
                )
            }

            // === View Options ===
            PropertySection(title = "UI Visibility") {
                ToggleRow(label = "Show Live View", checked = state.showLiveView, onCheckedChange = onUpdateShowLiveView)
                ToggleRow(label = "Crop Live View", checked = state.cropLiveView, onCheckedChange = onUpdateShowLiveView) // Fix call if needed
                ToggleRow(label = "Mirror Live View", checked = state.mirrorLiveView, onCheckedChange = onUpdateMirrorLiveView)
                ToggleRow(label = "Show Gallery Button", checked = state.showGalleryButton, onCheckedChange = onUpdateShowGalleryButton)
                ToggleRow(label = "Show Countdown", checked = state.showCountdown, onCheckedChange = onUpdateShowCountdown)
                ToggleRow(label = "Show Cancel Button", checked = state.showCancelButton, onCheckedChange = onUpdateShowCancelButton)
            }

            // === Session Trigger ===
            PropertySection(title = "Capture Trigger") {
                RadioGroupRow(
                    label = "Activation Method",
                    selectedOption = state.sessionTrigger.label,
                    options = SessionTrigger.entries.map { it.label },
                    onOptionSelected = { label ->
                        SessionTrigger.entries.find { it.label == label }?.let { onUpdateSessionTrigger(it) }
                    }
                )
            }

            // === Element Properties ===
            if (selectedElement != null) {
                PropertySection(title = "Element: ${selectedElement.type.name}") {
                    if (selectedElement.type == ElementType.TEXT || selectedElement.type == ElementType.QR_CODE) {
                        TextFieldRow(
                            label = "Content / URL",
                            value = selectedElement.content,
                            onValueChange = { newVal ->
                                onUpdateElement(selectedElement.id) { it.copy(content = newVal) }
                            }
                        )
                    }

                    // Positioning Group
                    Text("Transform", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    
                    SliderRow(
                        label = "X Pos",
                        value = selectedElement.x,
                        onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(x = newVal) } },
                        valueRange = 0f..1000f,
                        unit = "px"
                    )

                    SliderRow(
                        label = "Y Pos",
                        value = selectedElement.y,
                        onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(y = newVal) } },
                        valueRange = 0f..1000f,
                        unit = "px"
                    )

                    SliderRow(
                        label = "Width",
                        value = selectedElement.width,
                        onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(width = newVal) } },
                        valueRange = 10f..1000f,
                        unit = "px"
                    )

                    SliderRow(
                        label = "Height",
                        value = selectedElement.height,
                        onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(height = newVal) } },
                        valueRange = 10f..1000f,
                        unit = "px"
                    )

                    // Styling Group
                    Text("Appearance", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                    if (selectedElement.type == ElementType.TEXT) {
                        SliderRow(
                            label = "Font Size",
                            value = selectedElement.fontSize,
                            onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(fontSize = newVal) } },
                            valueRange = 8f..120f,
                            unit = "sp"
                        )

                        TextFieldRow(
                            label = "Font Color",
                            value = selectedElement.fontColor,
                            onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(fontColor = newVal) } }
                        )
                    }

                    SliderRow(
                        label = "Opacity",
                        value = selectedElement.opacity,
                        onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(opacity = newVal) } },
                        valueRange = 0f..1f
                    )

                    TextFieldRow(
                        label = "Background Hex",
                        value = selectedElement.backgroundColor,
                        onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(backgroundColor = newVal) } }
                    )

                    if (selectedElement.type == ElementType.SHAPE) {
                        SliderRow(
                            label = "Corner Radius",
                            value = selectedElement.cornerRadius,
                            onValueChange = { newVal -> onUpdateElement(selectedElement.id) { it.copy(cornerRadius = newVal) } },
                            valueRange = 0f..100f
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PropertySection(title: String, content: @Composable ColumnScope.() -> Unit) {
    SectionHeader(title)
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        content()
    }
}

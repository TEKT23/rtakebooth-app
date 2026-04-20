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
        modifier = modifier.fillMaxHeight().width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp)
        ) {
            // === Canvas Properties ===
            SectionHeader("Canvas Properties")
            
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
                label = "Background Color (Hex)",
                value = state.canvasBackgroundColor,
                onValueChange = onUpdateCanvasBackground
            )

            // === View Options ===
            SectionHeader("View Options")
            
            ToggleRow(label = "Show Live View", checked = state.showLiveView, onCheckedChange = onUpdateShowLiveView)
            ToggleRow(label = "Crop Live View", checked = state.cropLiveView, onCheckedChange = onUpdateCropLiveView)
            ToggleRow(label = "Mirror Live View", checked = state.mirrorLiveView, onCheckedChange = onUpdateMirrorLiveView)
            ToggleRow(label = "Show Gallery Button", checked = state.showGalleryButton, onCheckedChange = onUpdateShowGalleryButton)
            ToggleRow(label = "Show Countdown", checked = state.showCountdown, onCheckedChange = onUpdateShowCountdown)
            ToggleRow(label = "Show Cancel Button", checked = state.showCancelButton, onCheckedChange = onUpdateShowCancelButton)

            // === Session Trigger ===
            SectionHeader("Session Trigger")
            
            RadioGroupRow(
                label = "Trigger",
                selectedOption = state.sessionTrigger.label,
                options = SessionTrigger.entries.map { it.label },
                onOptionSelected = { label ->
                    SessionTrigger.entries.find { it.label == label }?.let { onUpdateSessionTrigger(it) }
                }
            )

            // === Element Properties ===
            if (selectedElement != null) {
                SectionHeader("Element Properties (${selectedElement.type})")
                
                if (selectedElement.type == ElementType.TEXT || selectedElement.type == ElementType.QR_CODE) {
                    TextFieldRow(
                        label = "Content",
                        value = selectedElement.content,
                        onValueChange = { newVal ->
                            onUpdateElement(selectedElement.id) { it.copy(content = newVal) }
                        }
                    )
                }

                SliderRow(
                    label = "X Position",
                    value = selectedElement.x,
                    onValueChange = { newVal ->
                        onUpdateElement(selectedElement.id) { it.copy(x = newVal) }
                    },
                    valueRange = 0f..1000f
                )

                SliderRow(
                    label = "Y Position",
                    value = selectedElement.y,
                    onValueChange = { newVal ->
                        onUpdateElement(selectedElement.id) { it.copy(y = newVal) }
                    },
                    valueRange = 0f..1000f
                )

                SliderRow(
                    label = "Width",
                    value = selectedElement.width,
                    onValueChange = { newVal ->
                        onUpdateElement(selectedElement.id) { it.copy(width = newVal) }
                    },
                    valueRange = 10f..1000f
                )

                SliderRow(
                    label = "Height",
                    value = selectedElement.height,
                    onValueChange = { newVal ->
                        onUpdateElement(selectedElement.id) { it.copy(height = newVal) }
                    },
                    valueRange = 10f..1000f
                )

                if (selectedElement.type == ElementType.TEXT) {
                    SliderRow(
                        label = "Font Size",
                        value = selectedElement.fontSize,
                        onValueChange = { newVal ->
                            onUpdateElement(selectedElement.id) { it.copy(fontSize = newVal) }
                        },
                        valueRange = 8f..72f
                    )

                    TextFieldRow(
                        label = "Font Color (Hex)",
                        value = selectedElement.fontColor,
                        onValueChange = { newVal ->
                            onUpdateElement(selectedElement.id) { it.copy(fontColor = newVal) }
                        }
                    )
                }

                SliderRow(
                    label = "Opacity",
                    value = selectedElement.opacity,
                    onValueChange = { newVal ->
                        onUpdateElement(selectedElement.id) { it.copy(opacity = newVal) }
                    },
                    valueRange = 0f..1f
                )

                TextFieldRow(
                    label = "Background Color (Hex)",
                    value = selectedElement.backgroundColor,
                    onValueChange = { newVal ->
                        onUpdateElement(selectedElement.id) { it.copy(backgroundColor = newVal) }
                    }
                )

                if (selectedElement.type == ElementType.SHAPE) {
                    SliderRow(
                        label = "Corner Radius",
                        value = selectedElement.cornerRadius,
                        onValueChange = { newVal ->
                            onUpdateElement(selectedElement.id) { it.copy(cornerRadius = newVal) }
                        },
                        valueRange = 0f..100f
                    )
                }
            }
        }
    }
}

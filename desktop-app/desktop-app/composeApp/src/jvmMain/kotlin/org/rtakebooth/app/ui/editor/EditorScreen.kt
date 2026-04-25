package org.rtakebooth.app.ui.editor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.data.model.ScreenTab
import org.rtakebooth.app.ui.components.SubNavBar
import org.rtakebooth.app.viewmodel.EditorViewModel

@Composable
fun EditorScreen(viewModel: EditorViewModel = remember { EditorViewModel() }) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when {
                        keyEvent.isCtrlPressed && keyEvent.key == Key.Z -> {
                            viewModel.undo()
                            true
                        }
                        keyEvent.isCtrlPressed && keyEvent.key == Key.Y -> {
                            viewModel.redo()
                            true
                        }
                        keyEvent.key == Key.Delete || keyEvent.key == Key.Backspace -> {
                            viewModel.removeSelectedElement()
                            true
                        }
                        else -> false
                    }
                } else false
            }
            .focusRequester(remember { FocusRequester() })
            .focusable()
    ) {
        // Sub-Navigation (Welcome / Capture / Sharing)
        SubNavBar(
            options = ScreenTab.entries.map { it.name.replace("_", " ") },
            selectedOption = state.currentTab.name.replace("_", " "),
            onOptionSelected = { name -> 
                ScreenTab.entries.find { it.name.replace("_", " ") == name }?.let {
                    viewModel.switchTab(it)
                }
            }
        )

        // Main content area: Canvas + Properties
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            // Canvas Area (Left)
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                EditorCanvas(
                    state = state,
                    elements = viewModel.currentElements(),
                    onSelectElement = { viewModel.selectElement(it) },
                    onMoveElement = { id, x, y, undo -> viewModel.moveElement(id, x, y, undo) },
                    onResizeElement = { id, w, h, x, y, undo -> viewModel.resizeElement(id, w, h, x, y, undo) },
                    onDragEnd = { viewModel.onDragEnd() },
                    modifier = Modifier.fillMaxSize()
                )

                // Floating Toolbar (WYSIWYG tools)
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { viewModel.addElement(ElementType.TEXT) }) {
                            Text("T", fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { viewModel.addElement(ElementType.IMAGE) }) {
                            Text("🖼")
                        }
                        IconButton(onClick = { viewModel.addElement(ElementType.QR_CODE) }) {
                            Text("QR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { viewModel.addElement(ElementType.SHAPE) }) {
                            Text("▢")
                        }
                        
                        VerticalDivider(modifier = Modifier.height(24.dp))
                        
                        IconButton(onClick = { viewModel.undo() }, enabled = viewModel.canUndo) {
                            Text("↩")
                        }
                        IconButton(onClick = { viewModel.redo() }, enabled = viewModel.canRedo) {
                            Text("↪")
                        }
                        
                        VerticalDivider(modifier = Modifier.height(24.dp))
                        
                        IconButton(
                            onClick = { viewModel.removeSelectedElement() },
                            enabled = state.selectedElementId != null,
                            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("🗑")
                        }
                    }
                }
            }

            // Properties (Right)
            EditorProperties(
                state = state,
                selectedElement = viewModel.selectedElement(),
                onUpdateAspectRatio = { viewModel.updateAspectRatio(it) },
                onUpdateOrientation = { viewModel.updateOrientation(it) },
                onUpdateCanvasBackground = { viewModel.updateCanvasBackground(it) },
                onUpdateShowLiveView = { viewModel.updateShowLiveView(it) },
                onUpdateCropLiveView = { viewModel.updateCropLiveView(it) },
                onUpdateMirrorLiveView = { viewModel.updateMirrorLiveView(it) },
                onUpdateShowGalleryButton = { viewModel.updateShowGalleryButton(it) },
                onUpdateShowCountdown = { viewModel.updateShowCountdown(it) },
                onUpdateShowCancelButton = { viewModel.updateShowCancelButton(it) },
                onUpdateSessionTrigger = { viewModel.updateSessionTrigger(it) },
                onUpdateElement = { id, transform -> viewModel.updateElement(id, false, transform) },
                modifier = Modifier.width(320.dp).fillMaxHeight()
            )
        }
    }
}

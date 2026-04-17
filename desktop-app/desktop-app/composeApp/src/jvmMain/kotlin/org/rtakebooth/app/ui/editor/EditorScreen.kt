package org.rtakebooth.app.ui.editor

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
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
        // Toolbar
        EditorToolbar(
            state = state,
            canUndo = viewModel.canUndo,
            canRedo = viewModel.canRedo,
            onTabSwitch = { viewModel.switchTab(it) },
            onAddElement = { viewModel.addElement(it) },
            onUndo = { viewModel.undo() },
            onRedo = { viewModel.redo() },
            onDelete = { viewModel.removeSelectedElement() }
        )

        // Main content area: Canvas + Properties
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            // Canvas (Middle)
            EditorCanvas(
                state = state,
                elements = viewModel.currentElements(),
                onSelectElement = { viewModel.selectElement(it) },
                modifier = Modifier.weight(1f)
            )

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
                onUpdateElement = { id, transform -> viewModel.updateElement(id, transform) }
            )
        }
    }
}

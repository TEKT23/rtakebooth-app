package org.rtakebooth.app.ui.template

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.viewmodel.TemplateViewModel
import org.rtakebooth.app.ui.editor.EditorCanvas

@Composable
fun TemplateEditorScreen(viewModel: TemplateViewModel = remember { TemplateViewModel() }) {
    val state = viewModel.state
    val template = state.currentTemplate

    Column(modifier = Modifier.fillMaxSize()) {
        // Toolbar
        TemplateToolbar(
            canUndo = viewModel.canUndo,
            canRedo = viewModel.canRedo,
            onUndo = { viewModel.undo() },
            onRedo = { viewModel.redo() },
            onSave = { viewModel.saveTemplate() }
        )

        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            // Left Sidebar: Add & Layout
            TemplateSidebar(
                template = template,
                onAddElement = { viewModel.addElement(it) },
                onUpdatePaperSize = { viewModel.updatePaperSize(it) },
                onUpdateOrientation = { viewModel.updateOrientation(it) }
            )

            // Center Canvas
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                // We adapt the EditorCanvas for Template usage
                // Note: EditorCanvas might need slight adjustments to handle the new TemplateState
                // For now, let's assume we can reuse it if we pass the elements correctly.
                // However, Template editor needs its own specific canvas features like ruler/units.
                TemplateCanvas(
                    elements = template.elements,
                    selectedElementId = state.selectedElementId,
                    onSelectElement = { viewModel.selectElement(it) },
                    onMoveElement = { id, x, y, _ -> 
                        viewModel.updateElement(id) { it.copy(x = x, y = y) } 
                    },
                    onResizeElement = { id, w, h, x, y, _ -> 
                        viewModel.updateElement(id) { it.copy(width = w, height = h, x = x, y = y) } 
                    },
                    onDragEnd = { /* viewModel handles undo in individual methods for now */ },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Right Sidebar: Selected & Layers
            TemplateProperties(
                selectedElement = viewModel.selectedElement(),
                elements = template.elements,
                onUpdateElement = { id, transform -> viewModel.updateElement(id, false, transform) }
            )
        }
    }
}

@Composable
fun TemplateToolbar(
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onSave: () -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text("Template Editor", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onUndo, enabled = canUndo) { Text("↩️") }
            IconButton(onClick = onRedo, enabled = canRedo) { Text("↪️") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onSave) { Text("Save Template") }
        }
    }
}

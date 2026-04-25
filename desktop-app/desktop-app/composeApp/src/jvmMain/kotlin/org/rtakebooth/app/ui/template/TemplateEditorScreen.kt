package org.rtakebooth.app.ui.template

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.viewmodel.TemplateViewModel

@Composable
fun TemplateEditorScreen(viewModel: TemplateViewModel = remember { TemplateViewModel() }) {
    val state = viewModel.state
    val template = state.currentTemplate

    Row(modifier = Modifier.fillMaxSize()) {
        // Center Canvas Area
        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
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
                onDragEnd = { /* viewModel handles undo in individual methods */ },
                modifier = Modifier.fillMaxSize()
            )

            // Floating Toolbar (Tools & History)
            Surface(
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Add Tools
                    IconButton(onClick = { viewModel.addElement(org.rtakebooth.app.data.model.ElementType.PHOTO_FROM_BOOTH) }) {
                        Text("📷")
                    }
                    IconButton(onClick = { viewModel.addElement(org.rtakebooth.app.data.model.ElementType.IMAGE) }) {
                        Text("🖼")
                    }
                    IconButton(onClick = { viewModel.addElement(org.rtakebooth.app.data.model.ElementType.TEXT) }) {
                        Text("✍️")
                    }
                    IconButton(onClick = { viewModel.addElement(org.rtakebooth.app.data.model.ElementType.SESSION_DATA) }) {
                        Text("📊")
                    }
                    
                    androidx.compose.material3.VerticalDivider(modifier = Modifier.height(24.dp))
                    
                    // History
                    IconButton(onClick = { viewModel.undo() }, enabled = viewModel.canUndo) {
                        Text("↩")
                    }
                    IconButton(onClick = { viewModel.redo() }, enabled = viewModel.canRedo) {
                        Text("↪")
                    }

                    androidx.compose.material3.VerticalDivider(modifier = Modifier.height(24.dp))

                    // Save
                    Button(
                        onClick = { viewModel.saveTemplate() },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }

        // Right Sidebar: Selected & Layers & Layout
        TemplateProperties(
            template = template,
            selectedElement = viewModel.selectedElement(),
            elements = template.elements,
            onUpdatePaperSize = { viewModel.updatePaperSize(it) },
            onUpdateOrientation = { viewModel.updateOrientation(it) },
            onUpdateElement = { id, transform -> viewModel.updateElement(id, false, transform) }
        )
    }
}



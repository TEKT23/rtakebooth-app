package org.rtakebooth.app.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.data.model.EditorState
import org.rtakebooth.app.data.model.ScreenTab

@Composable
fun EditorToolbar(
    state: EditorState,
    canUndo: Boolean,
    canRedo: Boolean,
    onTabSwitch: (ScreenTab) -> Unit,
    onAddElement: (ElementType) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab Buttons
            Row(modifier = Modifier.weight(1f)) {
                ScreenTab.entries.forEach { tab ->
                    FilterChip(
                        selected = state.currentTab == tab,
                        onClick = { onTabSwitch(tab) },
                        label = { Text(tab.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            // Toolbar Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Add Element Buttons
                IconButton(onClick = { onAddElement(ElementType.TEXT) }) {
                    Text("T", style = MaterialTheme.typography.titleLarge)
                }
                IconButton(onClick = { onAddElement(ElementType.IMAGE) }) {
                    Text("🖼", style = MaterialTheme.typography.titleLarge)
                }
                IconButton(onClick = { onAddElement(ElementType.QR_CODE) }) {
                    Text("QR", style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = { onAddElement(ElementType.SHAPE) }) {
                    Text("▢", style = MaterialTheme.typography.titleLarge)
                }

                VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 8.dp))

                // Undo/Redo Buttons
                IconButton(onClick = onUndo, enabled = canUndo) {
                    Text("↩", style = MaterialTheme.typography.titleLarge)
                }
                IconButton(onClick = onRedo, enabled = canRedo) {
                    Text("↪", style = MaterialTheme.typography.titleLarge)
                }

                VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 8.dp))

                // Delete Button
                IconButton(onClick = onDelete, enabled = state.selectedElementId != null) {
                    Text("🗑", style = MaterialTheme.typography.titleLarge, color = if (state.selectedElementId != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

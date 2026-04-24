package org.rtakebooth.app.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.data.model.EditorState
import org.rtakebooth.app.data.model.ScreenTab

@OptIn(ExperimentalMaterial3Api::class)
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
        tonalElevation = 3.dp,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab Selection (Premium Pill style)
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.weight(1f)
            ) {
                ScreenTab.entries.forEachIndexed { index, tab ->
                    SegmentedButton(
                        selected = state.currentTab == tab,
                        onClick = { onTabSwitch(tab) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = ScreenTab.entries.size),
                        label = { Text(tab.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Toolbar Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Add Elements Group
                ToolButtonGroup(label = "Add") {
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
                }

                VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 4.dp))

                // History Group
                Row {
                    IconButton(onClick = onUndo, enabled = canUndo) {
                        Text("↩", style = MaterialTheme.typography.titleLarge)
                    }
                    IconButton(onClick = onRedo, enabled = canRedo) {
                        Text("↪", style = MaterialTheme.typography.titleLarge)
                    }
                }

                VerticalDivider(modifier = Modifier.height(32.dp).padding(horizontal = 4.dp))

                // Action Group
                IconButton(
                    onClick = onDelete, 
                    enabled = state.selectedElementId != null,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                        disabledContentColor = MaterialTheme.colorScheme.outline
                    )
                ) {
                    Text("🗑", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@Composable
fun ToolButtonGroup(label: String, content: @Composable RowScope.() -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), MaterialTheme.shapes.medium)
            .padding(horizontal = 4.dp)
    ) {
        content()
    }
}

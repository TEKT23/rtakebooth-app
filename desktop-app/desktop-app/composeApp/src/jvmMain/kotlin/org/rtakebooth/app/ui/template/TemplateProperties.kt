package org.rtakebooth.app.ui.template

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.data.model.CanvasElement
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.ui.components.SectionHeader
import org.rtakebooth.app.ui.components.TextFieldRow

@Composable
fun TemplateProperties(
    selectedElement: CanvasElement?,
    elements: List<CanvasElement>,
    onUpdateElement: (String, (CanvasElement) -> CanvasElement) -> Unit
) {
    Surface(
        modifier = Modifier.width(300.dp).fillMaxHeight(),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = 0) { // Simple for now
                Tab(selected = true, onClick = {}) { Text("Selected", modifier = Modifier.padding(12.dp)) }
                Tab(selected = false, onClick = {}) { Text("Layers", modifier = Modifier.padding(12.dp)) }
            }

            Box(modifier = Modifier.weight(1f)) {
                if (selectedElement != null) {
                    SelectedProperties(selectedElement, onUpdateElement)
                } else {
                    LayersList(elements, onUpdateElement)
                }
            }
        }
    }
}

@Composable
fun SelectedProperties(
    element: CanvasElement,
    onUpdateElement: (String, (CanvasElement) -> CanvasElement) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SectionHeader("Transform")
        
        TextFieldRow("Pos X", element.x.toString(), { val v = it.toFloatOrNull() ?: 0f; onUpdateElement(element.id) { e -> e.copy(x = v) } })
        TextFieldRow("Pos Y", element.y.toString(), { val v = it.toFloatOrNull() ?: 0f; onUpdateElement(element.id) { e -> e.copy(y = v) } })
        TextFieldRow("Width", element.width.toString(), { val v = it.toFloatOrNull() ?: 0f; onUpdateElement(element.id) { e -> e.copy(width = v) } })
        TextFieldRow("Height", element.height.toString(), { val v = it.toFloatOrNull() ?: 0f; onUpdateElement(element.id) { e -> e.copy(height = v) } })

        if (element.type == ElementType.PHOTO_FROM_BOOTH) {
            SectionHeader("Photo Settings")
            TextFieldRow("Sequence #", element.sequenceNumber.toString(), { val v = it.toIntOrNull() ?: 1; onUpdateElement(element.id) { e -> e.copy(sequenceNumber = v) } })
        }

        Spacer(modifier = Modifier.height(16.dp))
        SectionHeader("State")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = element.isLocked, onCheckedChange = { v -> onUpdateElement(element.id) { e -> e.copy(isLocked = v) } })
            Text("Lock Layer")
        }
    }
}

@Composable
fun LayersList(
    elements: List<CanvasElement>,
    onUpdateElement: (String, (CanvasElement) -> CanvasElement) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(elements.sortedByDescending { it.zIndex }) { element ->
            ListItem(
                headlineContent = { Text("${element.type.name} - ${element.id.take(4)}") },
                supportingContent = { Text("Z-Index: ${element.zIndex}") },
                leadingContent = { Text(if (element.isLocked) "🔒" else "📄") },
                modifier = Modifier.clickable { /* Select element */ }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        }
    }
}

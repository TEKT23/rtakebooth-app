package org.rtakebooth.app.ui.template

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.data.model.CanvasElement
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.ui.components.SectionHeader
import org.rtakebooth.app.ui.components.TextFieldRow

@Composable
fun TemplateProperties(
    template: org.rtakebooth.app.data.model.PrintTemplate,
    selectedElement: CanvasElement?,
    elements: List<CanvasElement>,
    onUpdatePaperSize: (String) -> Unit,
    onUpdateOrientation: (org.rtakebooth.app.data.model.Orientation) -> Unit,
    onUpdateElement: (String, (CanvasElement) -> CanvasElement) -> Unit
) {
    var selectedTab by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.width(320.dp).fillMaxHeight()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) { 
                    Text("Layout", modifier = Modifier.padding(12.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold) 
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) { 
                    Text("Element", modifier = Modifier.padding(12.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold) 
                }
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) { 
                    Text("Layers", modifier = Modifier.padding(12.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold) 
                }
            }

            Box(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                when (selectedTab) {
                    0 -> LayoutProperties(template, onUpdatePaperSize, onUpdateOrientation)
                    1 -> {
                        if (selectedElement != null) {
                            SelectedProperties(selectedElement, onUpdateElement)
                        } else {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No element selected", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    2 -> LayersList(elements, onUpdateElement)
                }
            }
        }
    }
}

@Composable
fun LayoutProperties(
    template: org.rtakebooth.app.data.model.PrintTemplate,
    onUpdatePaperSize: (String) -> Unit,
    onUpdateOrientation: (org.rtakebooth.app.data.model.Orientation) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SectionHeader("Paper Settings")
        
        org.rtakebooth.app.ui.components.DropdownRow(
            label = "Paper Size",
            selectedValue = template.paperSize,
            options = listOf("4x6", "4x8", "5x7", "8x10", "Custom"),
            onValueChange = onUpdatePaperSize
        )

        org.rtakebooth.app.ui.components.RadioGroupRow(
            label = "Orientation",
            selectedOption = template.orientation.name,
            options = listOf("LANDSCAPE", "PORTRAIT"),
            onOptionSelected = { onUpdateOrientation(org.rtakebooth.app.data.model.Orientation.valueOf(it)) }
        )
        
        TextFieldRow(
            label = "Resolution (DPI)",
            value = template.resolution.toString(),
            onValueChange = { /* handle int conversion */ }
        )
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

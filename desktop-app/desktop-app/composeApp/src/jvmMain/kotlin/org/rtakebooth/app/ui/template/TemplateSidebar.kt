package org.rtakebooth.app.ui.template

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.data.model.Orientation
import org.rtakebooth.app.data.model.PrintTemplate
import org.rtakebooth.app.ui.components.SectionHeader
import org.rtakebooth.app.ui.components.DropdownRow

@Composable
fun TemplateSidebar(
    template: PrintTemplate,
    onAddElement: (ElementType) -> Unit,
    onUpdatePaperSize: (String) -> Unit,
    onUpdateOrientation: (Orientation) -> Unit
) {
    Surface(
        modifier = Modifier.width(280.dp).fillMaxHeight(),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
        ) {
            // === SECTION: Add Elements ===
            SectionHeader("Add Elements")
            
            Button(
                onClick = { onAddElement(ElementType.PHOTO_FROM_BOOTH) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("📷 Photo from Booth")
            }
            
            Button(
                onClick = { onAddElement(ElementType.IMAGE) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("🖼️ Image (Overlay)")
            }
            
            Button(
                onClick = { onAddElement(ElementType.TEXT) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("✍️ Text")
            }
            
            Button(
                onClick = { onAddElement(ElementType.SESSION_DATA) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("📊 Session Data")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // === SECTION: Layout Settings ===
            SectionHeader("Layout Settings")
            
            DropdownRow(
                label = "Paper Size",
                selectedValue = template.paperSize,
                options = listOf("4x6", "4x8", "5x7", "8x10", "Custom"),
                onValueChange = onUpdatePaperSize
            )

            DropdownRow(
                label = "Orientation",
                selectedValue = template.orientation.name,
                options = listOf("LANDSCAPE", "PORTRAIT"),
                onValueChange = { onUpdateOrientation(Orientation.valueOf(it)) }
            )
            
            OutlinedTextField(
                value = template.resolution.toString(),
                onValueChange = { /* handle int conversion */ },
                label = { Text("Resolution (DPI)") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }
    }
}

package org.rtakebooth.app.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.PrintViewModel

@Composable
fun PrintScreen(viewModel: PrintViewModel = remember { PrintViewModel() }) {
    val settings = viewModel.settings
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .widthIn(max = 800.dp)
                .fillMaxHeight()
                .padding(horizontal = 32.dp, vertical = 24.dp)
        ) {
            // Screen Title
            Text(
                text = "Print Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                if (viewModel.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
                }

                // Printer Selection Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Printer Selection")
                        DropdownRow(
                            label = "Main Printer",
                            selectedValue = settings.mainPrinter.ifEmpty { viewModel.availablePrinters.firstOrNull() ?: "" },
                            options = viewModel.availablePrinters,
                            onValueChange = { viewModel.updateSettings(settings.copy(mainPrinter = it)) }
                        )
                        DropdownRow(
                            label = "Secondary Printer",
                            selectedValue = settings.secondaryPrinter.ifEmpty { viewModel.availablePrinters.getOrNull(1) ?: "" },
                            options = viewModel.availablePrinters,
                            onValueChange = { viewModel.updateSettings(settings.copy(secondaryPrinter = it)) }
                        )
                        ActionButtonRow(label = "Configure Printer", buttonText = "Configure", onClick = { println("Clicked Configure Printer") })
                    }
                }

                // Printer Alignment Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Printer Alignment")
                        SliderRow(label = "Scale", value = settings.scale, onValueChange = { viewModel.updateSettings(settings.copy(scale = it)) }, valueRange = 50f..150f, unit = "%")
                        SliderRow(label = "Horizontal Position", value = settings.horizontalPosition, onValueChange = { viewModel.updateSettings(settings.copy(horizontalPosition = it)) }, valueRange = -50f..50f, unit = "px")
                        SliderRow(label = "Vertical Position", value = settings.verticalPosition, onValueChange = { viewModel.updateSettings(settings.copy(verticalPosition = it)) }, valueRange = -50f..50f, unit = "px")
                    }
                }

                // Print Logic Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Print Logic")
                        ToggleRow(label = "Print Automatically", checked = settings.printAutomatically, onCheckedChange = { viewModel.updateSettings(settings.copy(printAutomatically = it)) }, description = "Automatically print after capture")
                        ToggleRow(label = "Show Print Button", checked = settings.showPrintButton, onCheckedChange = { viewModel.updateSettings(settings.copy(showPrintButton = it)) }, description = "Show manual print button to user")
                        ToggleRow(label = "Print to Both Printers", checked = settings.printToBothPrinters, onCheckedChange = { viewModel.updateSettings(settings.copy(printToBothPrinters = it)) })
                        ToggleRow(label = "Automate to Fit", checked = settings.automateToFit, onCheckedChange = { viewModel.updateSettings(settings.copy(automateToFit = it)) }, description = "Automatically fit layout to paper size")
                    }
                }

                // Print Limits Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Print Limits")
                        TextFieldRow(label = "Limit Per Event", value = settings.limitPerEvent.toString(), onValueChange = { it.toIntOrNull()?.let { limit -> viewModel.updateSettings(settings.copy(limitPerEvent = limit)) } }, placeholder = "0 = unlimited")
                        TextFieldRow(label = "Limit Per Session", value = settings.limitPerSession.toString(), onValueChange = { it.toIntOrNull()?.let { limit -> viewModel.updateSettings(settings.copy(limitPerSession = limit)) } }, placeholder = "0 = unlimited")
                        ActionButtonRow(label = "Print Test Page", buttonText = "Print Test", onClick = { println("Clicked Print Test Page") })
                    }
                }
            }

            // Bottom bar: Messages + Save button
            Surface(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        viewModel.saveMessage?.let { Text(text = it, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary) }
                        viewModel.errorMessage?.let { Text(text = it, fontSize = 12.sp, color = MaterialTheme.colorScheme.error) }
                    }

                    Button(
                        onClick = { viewModel.saveSettings() },
                        enabled = !viewModel.isSaving,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (viewModel.isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(if (viewModel.isSaving) "Saving..." else "Save Settings")
                    }
                }
            }
        }
    }
}

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
import org.rtakebooth.app.viewmodel.GeneralViewModel

@Composable
fun GeneralScreen(viewModel: GeneralViewModel = remember { GeneralViewModel() }) {
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
                text = "General Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Scrollable form content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                if (viewModel.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
                }

                // Security Section Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Security")
                        TextFieldRow(
                            label = "Security PIN",
                            value = settings.securityPin,
                            onValueChange = { viewModel.updateSettings(settings.copy(securityPin = it)) },
                            placeholder = "Enter numeric PIN",
                            isPassword = true
                        )
                    }
                }

                // Appearance Section Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Appearance")
                        ToggleRow(
                            label = "Hardware Acceleration",
                            checked = settings.hardwareAcceleration,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(hardwareAcceleration = it)) },
                            description = "Enable GPU acceleration for better performance"
                        )
                        ToggleRow(
                            label = "Show Saved Events",
                            checked = settings.showSavedEvents,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(showSavedEvents = it)) },
                            description = "Display previously saved events in the event list"
                        )
                        ToggleRow(
                            label = "Start App in Full Screen",
                            checked = settings.startFullScreen,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(startFullScreen = it)) },
                            description = "Launch the application in full screen mode"
                        )
                    }
                }

                // Storage & Directories Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Storage & Directories")
                        FilePickerRow(label = "Data Directory", path = settings.dataDirectory, onPathSelected = { viewModel.updateSettings(settings.copy(dataDirectory = it)) }, pickDirectory = true)
                        FilePickerRow(label = "Export — Prints", path = settings.exportPrints, onPathSelected = { viewModel.updateSettings(settings.copy(exportPrints = it)) }, pickDirectory = true)
                        FilePickerRow(label = "Export — Originals", path = settings.exportOriginals, onPathSelected = { viewModel.updateSettings(settings.copy(exportOriginals = it)) }, pickDirectory = true)
                        FilePickerRow(label = "Export — GIFs", path = settings.exportGifs, onPathSelected = { viewModel.updateSettings(settings.copy(exportGifs = it)) }, pickDirectory = true)
                        FilePickerRow(label = "Export — Videos", path = settings.exportVideos, onPathSelected = { viewModel.updateSettings(settings.copy(exportVideos = it)) }, pickDirectory = true)
                    }
                }

                // Remote API Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Remote API")
                        TextFieldRow(label = "API URL", value = settings.apiUrl, onValueChange = { viewModel.updateSettings(settings.copy(apiUrl = it)) }, placeholder = "http://localhost:8080")
                        TextFieldRow(label = "API Password", value = settings.apiPassword, onValueChange = { viewModel.updateSettings(settings.copy(apiPassword = it)) }, placeholder = "Enter API password", isPassword = true)
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

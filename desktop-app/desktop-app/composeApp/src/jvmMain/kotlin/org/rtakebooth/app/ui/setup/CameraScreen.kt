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
import org.rtakebooth.app.viewmodel.CameraViewModel

@Composable
fun CameraScreen(viewModel: CameraViewModel = remember { CameraViewModel() }) {
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
                text = "Camera Settings",
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

                // Camera Section Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Camera")
                        ToggleRow(
                            label = "Webcam Enabled",
                            checked = settings.webcamEnabled,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(webcamEnabled = it)) },
                            description = "Enable camera input for capture"
                        )
                        DropdownRow(
                            label = "Camera Source",
                            selectedValue = settings.selectedCamera.ifEmpty { viewModel.availableCameras.firstOrNull() ?: "" },
                            options = viewModel.availableCameras,
                            onValueChange = { viewModel.updateSettings(settings.copy(selectedCamera = it)) }
                        )
                    }
                }

                // Resolution & Orientation Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Resolution & Orientation")
                        RadioGroupRow(
                            label = "Resolution Mode",
                            selectedOption = settings.resolutionMode,
                            options = listOf("Faster Framerate", "Higher Quality"),
                            onOptionSelected = { viewModel.updateSettings(settings.copy(resolutionMode = it)) }
                        )
                        DropdownRow(
                            label = "Rotation",
                            selectedValue = "${settings.rotation}°",
                            options = listOf("0°", "90°", "180°", "270°"),
                            onValueChange = {
                                val rotationValue = it.replace("°", "").toIntOrNull() ?: 0
                                viewModel.updateSettings(settings.copy(rotation = rotationValue))
                            }
                        )
                    }
                }

                // Audio Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Audio")
                        DropdownRow(
                            label = "Microphone Source",
                            selectedValue = settings.selectedMicrophone.ifEmpty { viewModel.availableMicrophones.firstOrNull() ?: "" },
                            options = viewModel.availableMicrophones,
                            onValueChange = { viewModel.updateSettings(settings.copy(selectedMicrophone = it)) }
                        )
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

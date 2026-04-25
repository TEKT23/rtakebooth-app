package org.rtakebooth.app.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.AttendantViewModel

@Composable
fun AttendantScreen(viewModel: AttendantViewModel = remember { AttendantViewModel() }) {
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
                text = "Attendant Settings",
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

                // Voice Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Voice")
                        DropdownRow(
                            label = "Voice Style",
                            selectedValue = settings.voiceStyle,
                            options = listOf("American Female", "American Male", "British Female", "British Male"),
                            onValueChange = { viewModel.updateSettings(settings.copy(voiceStyle = it)) }
                        )
                    }
                }

                // Start & Countdown Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Start & Countdown")
                        FilePickerRow(label = "Start Screen Video", path = settings.startScreenVideo, onPathSelected = { viewModel.updateSettings(settings.copy(startScreenVideo = it)) })
                        FilePickerRow(label = "Countdown Media", path = settings.countdownMedia, onPathSelected = { viewModel.updateSettings(settings.copy(countdownMedia = it)) })
                    }
                }

                // Before & After Capture Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Before & After Capture")
                        MultiFileListField(
                            label = "Before Countdown Audios",
                            files = settings.beforeCountdownAudios,
                            onAdd = { viewModel.addBeforeCountdownAudio(it) },
                            onRemove = { viewModel.removeBeforeCountdownAudio(it) }
                        )
                        ToggleRow(label = "Randomize Before Countdown", checked = settings.beforeCountdownRandomize, onCheckedChange = { viewModel.updateSettings(settings.copy(beforeCountdownRandomize = it)) })
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        MultiFileListField(
                            label = "After Capture Audios",
                            files = settings.afterCaptureAudios,
                            onAdd = { viewModel.addAfterCaptureAudio(it) },
                            onRemove = { viewModel.removeAfterCaptureAudio(it) }
                        )
                        ToggleRow(label = "Randomize After Capture", checked = settings.afterCaptureRandomize, onCheckedChange = { viewModel.updateSettings(settings.copy(afterCaptureRandomize = it)) })
                    }
                }

                // Processing & End Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Processing & End")
                        FilePickerRow(label = "Processing Audio", path = settings.processingAudio, onPathSelected = { viewModel.updateSettings(settings.copy(processingAudio = it)) })
                        FilePickerRow(label = "End Session Audio", path = settings.endSessionAudio, onPathSelected = { viewModel.updateSettings(settings.copy(endSessionAudio = it)) })
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

@Composable
fun MultiFileListField(
    label: String,
    files: List<String>,
    onAdd: (String) -> Unit,
    onRemove: (Int) -> Unit
) {
    var newFilePath by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        
        // List of files
        files.forEachIndexed { index, path ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = path.split(java.io.File.separator).lastOrNull() ?: path,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                IconButton(onClick = { onRemove(index) }, modifier = Modifier.size(24.dp)) {
                    Text("✕", fontSize = 10.sp)
                }
            }
        }

        // Add new file row
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilePickerRow(
                label = "", 
                path = newFilePath,
                onPathSelected = { newFilePath = it },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { 
                    onAdd(newFilePath)
                    newFilePath = ""
                },
                enabled = newFilePath.isNotEmpty()
            ) {
                Text("Add")
            }
        }
    }
}

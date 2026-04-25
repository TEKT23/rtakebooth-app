package org.rtakebooth.app.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.data.model.GifResolution
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.CaptureViewModel

@Composable
fun CaptureScreen(viewModel: CaptureViewModel) {
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
                text = "Capture Settings",
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

                // Capture Modes Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Capture Modes")
                        ToggleRow(label = "Photo Mode", checked = settings.photoEnabled, onCheckedChange = { viewModel.updatePhotoEnabled(it) })
                        ToggleRow(label = "GIF Mode", checked = settings.gifEnabled, onCheckedChange = { viewModel.updateGifEnabled(it) })
                        ToggleRow(label = "360 / Slow-mo Mode", checked = settings.slowMoEnabled, onCheckedChange = { viewModel.updateSlowMoEnabled(it) })
                        ToggleRow(label = "Video Mode", checked = settings.videoEnabled, onCheckedChange = { viewModel.updateVideoEnabled(it) })
                        ToggleRow(label = "Live Photo", checked = settings.livePhotoEnabled, onCheckedChange = { viewModel.updateLivePhotoEnabled(it) })
                    }
                }

                // Advanced Options Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Advanced Options")
                        ToggleRow(label = "Advanced Retake (Backtrack)", checked = settings.advancedRetakeEnabled, onCheckedChange = { viewModel.updateAdvancedRetakeEnabled(it) })
                        Text(
                            text = "If enabled, retaking a photo will jump back to that sequence number, discarding subsequent photos but keeping the original snapshots for backend processing.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                // Timings Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("Timings")
                        SliderRow(label = "Before 1st Photo", value = settings.delayBeforeFirstPhoto, onValueChange = { viewModel.updateDelayBeforeFirstPhoto(it) }, valueRange = 1f..10f, unit = "s")
                        SliderRow(label = "Before Other Photos", value = settings.delayBeforeOtherPhotos, onValueChange = { viewModel.updateDelayBeforeOtherPhotos(it) }, valueRange = 0.5f..10f, unit = "s")
                        SliderRow(label = "Photo Review Time", value = settings.photoReviewTime, onValueChange = { viewModel.updatePhotoReviewTime(it) }, valueRange = 0.5f..10f, unit = "s")
                    }
                }

                // GIF & Live Photo Settings Card
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                ) {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        SectionHeader("GIF & Live Photo Settings")
                        DropdownRow(
                            label = "Resolution",
                            selectedValue = settings.gifResolution.label,
                            options = GifResolution.entries.map { it.label },
                            onValueChange = { label ->
                                GifResolution.entries.find { it.label == label }?.let { viewModel.updateGifResolution(it) }
                            }
                        )
                        SliderRow(label = "Frame Delay", value = settings.gifFrameDelay.toFloat(), onValueChange = { viewModel.updateGifFrameDelay(it.toInt()) }, valueRange = 50f..1000f, unit = "ms")
                        SliderRow(label = "Photo Count", value = settings.gifPhotoCount.toFloat(), onValueChange = { viewModel.updateGifPhotoCount(it.toInt()) }, valueRange = 2f..24f, unit = "frames")
                        ToggleRow(label = "Reverse / Boomerang Effect", checked = settings.gifReverse, onCheckedChange = { viewModel.updateGifReverse(it) })
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

package org.rtakebooth.app.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.data.model.GifResolution
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.CaptureViewModel

@Composable
fun CaptureScreen(viewModel: CaptureViewModel) {
    val settings = viewModel.settings

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar actions (Save button)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp))
            }
            if (viewModel.saveMessage != null) {
                Text(
                    text = viewModel.saveMessage!!,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 16.dp, top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(end = 16.dp, top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                onClick = { viewModel.saveSettings() },
                enabled = !viewModel.isSaving
            ) {
                Text(if (viewModel.isSaving) "Saving..." else "Save Settings")
            }
        }

        HorizontalDivider()

        // Content
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // === Capture Modes ===
            item {
                SectionHeader("Capture Modes")
                ToggleRow(
                    label = "Photo Mode",
                    checked = settings.photoEnabled,
                    onCheckedChange = { viewModel.updatePhotoEnabled(it) }
                )
                ToggleRow(
                    label = "GIF Mode",
                    checked = settings.gifEnabled,
                    onCheckedChange = { viewModel.updateGifEnabled(it) }
                )
                ToggleRow(
                    label = "360 / Slow-mo Mode",
                    checked = settings.slowMoEnabled,
                    onCheckedChange = { viewModel.updateSlowMoEnabled(it) }
                )
                ToggleRow(
                    label = "Video Mode",
                    checked = settings.videoEnabled,
                    onCheckedChange = { viewModel.updateVideoEnabled(it) }
                )
                ToggleRow(
                    label = "Live Photo",
                    checked = settings.livePhotoEnabled,
                    onCheckedChange = { viewModel.updateLivePhotoEnabled(it) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // === Advanced Options ===
            item {
                SectionHeader("Advanced Options")
                ToggleRow(
                    label = "Advanced Retake (Backtrack)",
                    checked = settings.advancedRetakeEnabled,
                    onCheckedChange = { viewModel.updateAdvancedRetakeEnabled(it) }
                )
                Text(
                    text = "If enabled, retaking a photo will jump back to that sequence number, discarding subsequent photos but keeping the original snapshots for backend processing.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // === Timings ===
            item {
                SectionHeader("Timings")
                SliderRow(
                    label = "Before 1st Photo",
                    value = settings.delayBeforeFirstPhoto,
                    onValueChange = { viewModel.updateDelayBeforeFirstPhoto(it) },
                    valueRange = 1f..10f,
                    unit = "s"
                )
                SliderRow(
                    label = "Before Other Photos",
                    value = settings.delayBeforeOtherPhotos,
                    onValueChange = { viewModel.updateDelayBeforeOtherPhotos(it) },
                    valueRange = 0.5f..10f,
                    unit = "s"
                )
                SliderRow(
                    label = "Photo Review Time",
                    value = settings.photoReviewTime,
                    onValueChange = { viewModel.updatePhotoReviewTime(it) },
                    valueRange = 0.5f..10f,
                    unit = "s"
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // === GIF & Live Photo Details ===
            item {
                SectionHeader("GIF & Live Photo Settings")
                DropdownRow(
                    label = "Resolution",
                    selectedValue = settings.gifResolution.label,
                    options = GifResolution.entries.map { it.label },
                    onValueChange = { label ->
                        GifResolution.entries.find { it.label == label }?.let {
                            viewModel.updateGifResolution(it)
                        }
                    }
                )
                SliderRow(
                    label = "Frame Delay",
                    value = settings.gifFrameDelay.toFloat(),
                    onValueChange = { viewModel.updateGifFrameDelay(it.toInt()) },
                    valueRange = 50f..1000f,
                    unit = "ms"
                )
                SliderRow(
                    label = "Photo Count",
                    value = settings.gifPhotoCount.toFloat(),
                    onValueChange = { viewModel.updateGifPhotoCount(it.toInt()) },
                    valueRange = 2f..24f,
                    unit = "frames"
                )
                ToggleRow(
                    label = "Reverse / Boomerang Effect",
                    checked = settings.gifReverse,
                    onCheckedChange = { viewModel.updateGifReverse(it) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

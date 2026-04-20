package org.rtakebooth.app.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.CaptureViewModel

@Composable
fun CaptureScreen(viewModel: CaptureViewModel = remember { CaptureViewModel() }) {
    val settings = viewModel.settings
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Scrollable form content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(bottom = 16.dp)
            ) {
                // Loading indicator
                if (viewModel.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // === SECTION: Capture Modes ===
                SectionHeader("Capture Modes")

                ToggleRow(
                    label = "Photo Enabled",
                    checked = settings.photoEnabled,
                    onCheckedChange = {
                        viewModel.updateSettings(settings.copy(photoEnabled = it))
                    },
                    description = "Standard photo capture"
                )

                ToggleRow(
                    label = "GIF Enabled",
                    checked = settings.gifEnabled,
                    onCheckedChange = {
                        viewModel.updateSettings(settings.copy(gifEnabled = it))
                    },
                    description = "Animated GIF capture"
                )

                ToggleRow(
                    label = "360/Slow-mo Enabled",
                    checked = settings.slowMoEnabled,
                    onCheckedChange = {
                        viewModel.updateSettings(settings.copy(slowMoEnabled = it))
                    },
                    description = "Slow motion video capture"
                )

                ToggleRow(
                    label = "Video Enabled",
                    checked = settings.videoEnabled,
                    onCheckedChange = {
                        viewModel.updateSettings(settings.copy(videoEnabled = it))
                    },
                    description = "Standard video recording"
                )

                // === SECTION: Timing ===
                SectionHeader("Timing")

                SliderRow(
                    label = "Delay Before First Photo",
                    value = settings.delayBeforeFirstPhoto,
                    onValueChange = {
                        viewModel.updateSettings(settings.copy(delayBeforeFirstPhoto = it))
                    },
                    valueRange = 1f..10f,
                    unit = "s"
                )

                SliderRow(
                    label = "Delay Before Other Photos",
                    value = settings.delayBeforeOtherPhotos,
                    onValueChange = {
                        viewModel.updateSettings(settings.copy(delayBeforeOtherPhotos = it))
                    },
                    valueRange = 0.5f..5f,
                    unit = "s"
                )

                SliderRow(
                    label = "Photo Review Time",
                    value = settings.photoReviewTime,
                    onValueChange = {
                        viewModel.updateSettings(settings.copy(photoReviewTime = it))
                    },
                    valueRange = 1f..15f,
                    unit = "s"
                )

                // === SECTION: GIF Settings ===
                SectionHeader("GIF Settings")

                DropdownRow(
                    label = "GIF Resolution",
                    selectedValue = settings.gifResolution,
                    options = listOf("Low", "Medium", "High"),
                    onValueChange = {
                        viewModel.updateSettings(settings.copy(gifResolution = it))
                    }
                )

                SliderRow(
                    label = "Frame Delay",
                    value = settings.gifFrameDelay.toFloat(),
                    onValueChange = {
                        viewModel.updateSettings(settings.copy(gifFrameDelay = it.toInt()))
                    },
                    valueRange = 50f..500f,
                    unit = "ms",
                    steps = 45 // 10ms increments
                )

                ToggleRow(
                    label = "Reverse/Boomerang GIF",
                    checked = settings.gifReverse,
                    onCheckedChange = {
                        viewModel.updateSettings(settings.copy(gifReverse = it))
                    }
                )

                SliderRow(
                    label = "Number of Photos",
                    value = settings.gifPhotoCount.toFloat(),
                    onValueChange = {
                        viewModel.updateSettings(settings.copy(gifPhotoCount = it.toInt()))
                    },
                    valueRange = 2f..20f,
                    steps = 18
                )
            }

            // Bottom bar: Messages + Save button
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status messages
                Column(modifier = Modifier.weight(1f)) {
                    viewModel.saveMessage?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    viewModel.errorMessage?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                // Save button
                Button(
                    onClick = { viewModel.saveSettings() },
                    enabled = !viewModel.isSaving
                ) {
                    if (viewModel.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (viewModel.isSaving) "Saving..." else "Save Settings")
                }
            }
        }
    }
}

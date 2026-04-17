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
import org.rtakebooth.app.viewmodel.LayoutViewModel

@Composable
fun LayoutScreen(viewModel: LayoutViewModel = remember { LayoutViewModel() }) {
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

                // === SECTION: Effects & Stickers ===
                SectionHeader("Effects & Stickers")

                ToggleRow(
                    label = "Beauty Filter",
                    checked = settings.beautyFilterEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(beautyFilterEnabled = it)) }
                )
                ToggleRow(
                    label = "Color Filters",
                    checked = settings.colorFiltersEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(colorFiltersEnabled = it)) }
                )
                ToggleRow(
                    label = "Post-Processing / Color Grading",
                    checked = settings.postProcessingEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(postProcessingEnabled = it)) }
                )
                ToggleRow(
                    label = "Stickers",
                    checked = settings.stickersEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(stickersEnabled = it)) }
                )
                ToggleRow(
                    label = "Watermark",
                    checked = settings.watermarkEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(watermarkEnabled = it)) }
                )
                if (settings.watermarkEnabled) {
                    FilePickerRow(
                        label = "Watermark Image",
                        path = settings.watermarkImagePath,
                        onPathSelected = { viewModel.updateSettings(settings.copy(watermarkImagePath = it)) }
                    )
                }

                // === SECTION: AI Portraits ===
                SectionHeader("AI Portraits")

                ToggleRow(
                    label = "Enable AI Portraits",
                    checked = settings.aiPortraitEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(aiPortraitEnabled = it)) }
                )
                if (settings.aiPortraitEnabled) {
                    TextFieldRow(
                        label = "Choose your style label",
                        value = settings.aiPortraitChooseLabel,
                        onValueChange = { viewModel.updateSettings(settings.copy(aiPortraitChooseLabel = it)) }
                    )
                    TextFieldRow(
                        label = "Creating AI Portrait label",
                        value = settings.aiPortraitCreatingLabel,
                        onValueChange = { viewModel.updateSettings(settings.copy(aiPortraitCreatingLabel = it)) }
                    )
                    TextFieldRow(
                        label = "Retrying label",
                        value = settings.aiPortraitRetryingLabel,
                        onValueChange = { viewModel.updateSettings(settings.copy(aiPortraitRetryingLabel = it)) }
                    )
                    TextFieldRow(
                        label = "Unable to create label",
                        value = settings.aiPortraitErrorLabel,
                        onValueChange = { viewModel.updateSettings(settings.copy(aiPortraitErrorLabel = it)) }
                    )
                }

                // === SECTION: Background Removal ===
                SectionHeader("Background Removal")

                ToggleRow(
                    label = "Enable Background Removal",
                    checked = settings.bgRemovalEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(bgRemovalEnabled = it)) }
                )
                if (settings.bgRemovalEnabled) {
                    RadioGroupRow(
                        label = "Mode",
                        selectedOption = settings.bgRemovalMode,
                        options = listOf("Green Screen", "AI Removal"),
                        onOptionSelected = { viewModel.updateSettings(settings.copy(bgRemovalMode = it)) }
                    )
                    FilePickerRow(
                        label = "Background Replacement Image",
                        path = settings.bgReplacementImagePath,
                        onPathSelected = { viewModel.updateSettings(settings.copy(bgReplacementImagePath = it)) }
                    )
                }

                // === SECTION: Survey & Disclaimer ===
                SectionHeader("Survey & Disclaimer")

                ToggleRow(
                    label = "Enable Survey",
                    checked = settings.surveyEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(surveyEnabled = it)) }
                )
                if (settings.surveyEnabled) {
                    SliderRow(
                        label = "Survey Font Size",
                        value = settings.surveyFontSize,
                        onValueChange = { viewModel.updateSettings(settings.copy(surveyFontSize = it)) },
                        valueRange = 10f..24f,
                        unit = "sp"
                    )
                    ActionButtonRow(
                        label = "View Responses",
                        buttonText = "View",
                        onClick = { println("Clicked View Responses") }
                    )
                }

                ToggleRow(
                    label = "Enable Disclaimer",
                    checked = settings.disclaimerEnabled,
                    onCheckedChange = { viewModel.updateSettings(settings.copy(disclaimerEnabled = it)) }
                )
                if (settings.disclaimerEnabled) {
                    TextFieldRow(
                        label = "Disclaimer Header",
                        value = settings.disclaimerHeader,
                        onValueChange = { viewModel.updateSettings(settings.copy(disclaimerHeader = it)) }
                    )
                    LargeTextAreaRow(
                        label = "Disclaimer Content / Terms & Conditions",
                        value = settings.disclaimerContent,
                        onValueChange = { viewModel.updateSettings(settings.copy(disclaimerContent = it)) }
                    )
                }
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

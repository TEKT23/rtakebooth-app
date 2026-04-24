package org.rtakebooth.app.ui.setup.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.LayoutViewModel

@Composable
fun AiPortraitSection(viewModel: LayoutViewModel) {
    val settings = viewModel.settings

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        SectionHeader("AI Portrait Engine")
        ToggleRow(
            label = "Enable AI Portrait Generation",
            checked = settings.aiPortraitEnabled,
            onCheckedChange = { viewModel.updateAiPortraitEnabled(it) }
        )

        if (settings.aiPortraitEnabled) {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader("Custom Text Labels")
            
            TextFieldRow(
                label = "Selection Message",
                value = settings.aiPortraitChooseLabel,
                onValueChange = { viewModel.updateAiLabels(it, settings.aiPortraitCreatingLabel, settings.aiPortraitRetryingLabel, settings.aiPortraitErrorLabel) }
            )
            TextFieldRow(
                label = "Processing Message",
                value = settings.aiPortraitCreatingLabel,
                onValueChange = { viewModel.updateAiLabels(settings.aiPortraitChooseLabel, it, settings.aiPortraitRetryingLabel, settings.aiPortraitErrorLabel) }
            )
            TextFieldRow(
                label = "Retry Message",
                value = settings.aiPortraitRetryingLabel,
                onValueChange = { viewModel.updateAiLabels(settings.aiPortraitChooseLabel, settings.aiPortraitCreatingLabel, it, settings.aiPortraitErrorLabel) }
            )
            TextFieldRow(
                label = "Error Message",
                value = settings.aiPortraitErrorLabel,
                onValueChange = { viewModel.updateAiLabels(settings.aiPortraitChooseLabel, settings.aiPortraitCreatingLabel, settings.aiPortraitRetryingLabel, it) }
            )
        }
    }
}

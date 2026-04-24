package org.rtakebooth.app.ui.setup.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.data.model.BgRemovalMode
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.LayoutViewModel

@Composable
fun BgRemovalSection(viewModel: LayoutViewModel) {
    val settings = viewModel.settings

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        SectionHeader("Background Removal")
        ToggleRow(
            label = "Enable BG Removal",
            checked = settings.bgRemovalEnabled,
            onCheckedChange = { viewModel.updateBgRemovalEnabled(it) }
        )

        if (settings.bgRemovalEnabled) {
            Spacer(modifier = Modifier.height(16.dp))
            RadioGroupRow(
                label = "Removal Method",
                selectedOption = if (settings.bgRemovalMode == BgRemovalMode.GREEN_SCREEN) "Green Screen" else "AI Removal",
                options = listOf("Green Screen", "AI Removal"),
                onOptionSelected = { 
                    val mode = if (it == "Green Screen") BgRemovalMode.GREEN_SCREEN else BgRemovalMode.AI_REMOVAL
                    viewModel.updateBgRemovalMode(mode)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            TextFieldRow(
                label = "Replacement Image Path",
                value = settings.bgReplacementImagePath,
                onValueChange = { viewModel.updateBgReplacementImagePath(it) },
                placeholder = "C:\\path\\to\\background.jpg"
            )
        }
    }
}

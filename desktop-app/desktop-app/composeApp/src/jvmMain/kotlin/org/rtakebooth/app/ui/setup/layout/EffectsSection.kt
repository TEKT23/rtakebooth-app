package org.rtakebooth.app.ui.setup.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.LayoutViewModel

@Composable
fun EffectsSection(viewModel: LayoutViewModel) {
    val settings = viewModel.settings

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        SectionHeader("Filters & Enhancements")
        ToggleRow(
            label = "Beauty Filter",
            checked = settings.beautyFilterEnabled,
            onCheckedChange = { viewModel.updateBeautyFilterEnabled(it) }
        )
        ToggleRow(
            label = "Color Filters",
            checked = settings.colorFiltersEnabled,
            onCheckedChange = { viewModel.updateColorFiltersEnabled(it) }
        )
        ToggleRow(
            label = "Post-Processing / Color Grading",
            checked = settings.postProcessingEnabled,
            onCheckedChange = { viewModel.updatePostProcessingEnabled(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader("Assets & Decorations")
        ToggleRow(
            label = "Stickers",
            checked = settings.stickersEnabled,
            onCheckedChange = { viewModel.updateStickersEnabled(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader("Watermark")
        ToggleRow(
            label = "Enable Watermark Overlay",
            checked = settings.watermarkEnabled,
            onCheckedChange = { viewModel.updateWatermarkEnabled(it) }
        )
        
        if (settings.watermarkEnabled) {
            TextFieldRow(
                label = "Watermark Image Path",
                value = settings.watermarkImagePath,
                onValueChange = { viewModel.updateWatermarkImagePath(it) },
                placeholder = "C:\\path\\to\\watermark.png"
            )
        }
    }
}

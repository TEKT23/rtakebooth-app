package org.rtakebooth.app.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.ui.setup.layout.*
import org.rtakebooth.app.viewmodel.LayoutViewModel

@Composable
fun LayoutScreen(viewModel: LayoutViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Effects", "AI Portrait", "BG Removal", "Survey")
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Internal Navigation Tabs
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                if (viewModel.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                when (selectedTabIndex) {
                    0 -> EffectsSection(viewModel)
                    1 -> AiPortraitSection(viewModel)
                    2 -> BgRemovalSection(viewModel)
                    3 -> SurveyDisclaimerSection(viewModel)
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

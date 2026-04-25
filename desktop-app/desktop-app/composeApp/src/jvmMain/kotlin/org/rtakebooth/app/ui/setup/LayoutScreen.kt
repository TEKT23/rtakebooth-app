package org.rtakebooth.app.ui.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.ui.components.SubNavBar
import org.rtakebooth.app.ui.setup.layout.*
import org.rtakebooth.app.viewmodel.LayoutViewModel

@Composable
fun LayoutScreen(viewModel: LayoutViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Effects", "AI Portrait", "BG Removal", "Survey")

    Column(modifier = Modifier.fillMaxSize()) {
        // Sub-Navigation (Secondary menu)
        SubNavBar(
            options = tabs,
            selectedOption = tabs[selectedTabIndex],
            onOptionSelected = { selectedTabIndex = tabs.indexOf(it) }
        )

        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            // Left Canvas Placeholder (Visual Preview area)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PREVIEW AREA", 
                        color = Color.Gray.copy(alpha = 0.5f), 
                        fontSize = 24.sp, 
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Viewing: ${tabs[selectedTabIndex]}", 
                        color = Color.Gray.copy(alpha = 0.5f), 
                        fontSize = 14.sp
                    )
                }
            }

            // Right Sidebar (Properties/Settings)
            Surface(
                modifier = Modifier.width(320.dp).fillMaxHeight(),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Scrollable section content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
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

                    // Save Action Bar (at bottom of sidebar)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        tonalElevation = 2.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Status Messages
                            viewModel.saveMessage?.let {
                                Text(text = it, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 4.dp))
                            }
                            viewModel.errorMessage?.let {
                                Text(text = it, fontSize = 12.sp, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 4.dp))
                            }

                            Button(
                                onClick = { viewModel.saveSettings() },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !viewModel.isSaving,
                                shape = RoundedCornerShape(8.dp)
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
        }
    }
}

package org.rtakebooth.app.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.ui.components.*
import org.rtakebooth.app.viewmodel.ExportViewModel

@Composable
fun ExportScreen(viewModel: ExportViewModel = remember { ExportViewModel() }) {
    val config = viewModel.exportConfig
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Main content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Export & Configuration",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // === SECTION: Media Export ===
                SectionHeader("Media Export")
                
                CheckboxRow(
                    label = "Export Prints",
                    checked = config.exportPrints,
                    onCheckedChange = { viewModel.updateExportConfig(config.copy(exportPrints = it)) }
                )
                CheckboxRow(
                    label = "Export GIFs",
                    checked = config.exportGifs,
                    onCheckedChange = { viewModel.updateExportConfig(config.copy(exportGifs = it)) }
                )
                CheckboxRow(
                    label = "Export Videos",
                    checked = config.exportVideos,
                    onCheckedChange = { viewModel.updateExportConfig(config.copy(exportVideos = it)) }
                )
                CheckboxRow(
                    label = "Export Original Photos",
                    checked = config.exportOriginals,
                    onCheckedChange = { viewModel.updateExportConfig(config.copy(exportOriginals = it)) }
                )

                DropdownRow(
                    label = "Select Event",
                    selectedValue = config.selectedEventId ?: "All Events",
                    options = listOf("All Events") + viewModel.availableEvents,
                    onValueChange = { 
                        viewModel.updateExportConfig(config.copy(selectedEventId = if (it == "All Events") null else it)) 
                    }
                )

                DropdownRow(
                    label = "Time Filter",
                    selectedValue = config.timeFilter,
                    options = listOf("1 Day", "1 Week", "All Time"),
                    onValueChange = { viewModel.updateExportConfig(config.copy(timeFilter = it)) }
                )

                FilePickerRow(
                    label = "Destination Path",
                    path = config.destinationPath,
                    onPathSelected = { viewModel.updateExportConfig(config.copy(destinationPath = it)) },
                    pickDirectory = true
                )

                Spacer(modifier = Modifier.height(8.dp))
                
                ActionButtonRow(
                    label = "Run Media Export",
                    buttonText = "Export Media",
                    onClick = { viewModel.exportMedia() }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // === SECTION: Configuration Management ===
                SectionHeader("Configuration Management")
                
                Text(
                    text = "Backup or restore all application settings via JSON file.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                ActionButtonRow(
                    label = "Backup all settings to a JSON file",
                    buttonText = "Export JSON",
                    onClick = { viewModel.exportConfigToJson() }
                )

                ActionButtonRow(
                    label = "Restore settings from a JSON file",
                    buttonText = "Import JSON",
                    onClick = { viewModel.importConfigFromJson() }
                )
            }

            // Footer / Status messages
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.isExporting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    viewModel.exportMessage?.let {
                        Text(it, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    viewModel.errorMessage?.let {
                        Text(it, fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
                    }
                }

                if (viewModel.exportMessage != null || viewModel.errorMessage != null) {
                    TextButton(onClick = { viewModel.clearMessages() }) {
                        Text("Clear")
                    }
                }
            }
        }
    }
}

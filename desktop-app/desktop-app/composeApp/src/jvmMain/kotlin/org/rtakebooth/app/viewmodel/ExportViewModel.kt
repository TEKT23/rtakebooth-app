package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.rtakebooth.app.data.model.AppSettings
import org.rtakebooth.app.data.model.ExportConfig
import org.rtakebooth.app.data.repository.SettingsRepository
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class ExportViewModel {
    private val repository = SettingsRepository()
    private val scope = CoroutineScope(Dispatchers.Default)
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    var exportConfig by mutableStateOf(ExportConfig())
        private set

    var isExporting by mutableStateOf(false)
        private set

    var exportMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var availableEvents by mutableStateOf(listOf("Event 001 — Wedding", "Event 002 — Birthday", "Event 003 — Corporate"))
        private set

    fun updateExportConfig(newConfig: ExportConfig) {
        exportConfig = newConfig
    }

    fun exportMedia() {
        scope.launch {
            isExporting = true
            exportMessage = null
            errorMessage = null
            try {
                // Placeholder logic
                println("Exporting media with config: $exportConfig")
                kotlinx.coroutines.delay(1500) // Simulate work
                exportMessage = "Media export completed successfully (Placeholder)"
            } catch (e: Exception) {
                errorMessage = "Export failed: ${e.message}"
            } finally {
                isExporting = false
            }
        }
    }

    fun exportConfigToJson() {
        scope.launch {
            try {
                // 1. Ambil semua settings dari repository
                val appSettings = AppSettings(
                    general = repository.getGeneralSettings(),
                    capture = repository.getCaptureSettings(),
                    camera = repository.getCameraSettings(),
                    attendant = repository.getAttendantSettings(),
                    layout = repository.getLayoutSettings(),
                    sharing = repository.getSharingSettings(),
                    print = repository.getPrintSettings()
                )

                // 2. Serialize ke JSON
                val jsonString = json.encodeToString(appSettings)

                // 3. Buka File Chooser (Swing)
                val chooser = JFileChooser()
                chooser.dialogTitle = "Save Configuration"
                chooser.fileFilter = FileNameExtensionFilter("JSON Files", "json")
                
                val result = chooser.showSaveDialog(null)
                if (result == JFileChooser.APPROVE_OPTION) {
                    var file = chooser.selectedFile
                    if (!file.name.endsWith(".json")) {
                        file = java.io.File(file.absolutePath + ".json")
                    }
                    file.writeText(jsonString)
                    exportMessage = "Configuration exported to: ${file.absolutePath}"
                }
            } catch (e: Exception) {
                errorMessage = "Failed to export config: ${e.message}"
            }
        }
    }

    fun importConfigFromJson() {
        scope.launch {
            try {
                // 1. Pilih file
                val chooser = JFileChooser()
                chooser.dialogTitle = "Import Configuration"
                chooser.fileFilter = FileNameExtensionFilter("JSON Files", "json")

                val result = chooser.showOpenDialog(null)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val content = chooser.selectedFile.readText()
                    
                    // 2. Deserialize
                    val appSettings = json.decodeFromString<AppSettings>(content)

                    // 3. Simpan per category ke repository
                    val results = listOf(
                        repository.saveGeneralSettings(appSettings.general),
                        repository.saveCaptureSettings(appSettings.capture),
                        repository.saveCameraSettings(appSettings.camera),
                        repository.saveAttendantSettings(appSettings.attendant),
                        repository.saveLayoutSettings(appSettings.layout),
                        repository.saveSharingSettings(appSettings.sharing),
                        repository.savePrintSettings(appSettings.print)
                    )

                    if (results.all { it }) {
                        exportMessage = "Configuration imported successfully from ${chooser.selectedFile.name}"
                    } else {
                        errorMessage = "Some settings failed to import"
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Failed to import config: ${e.message}"
            }
        }
    }

    fun clearMessages() {
        exportMessage = null
        errorMessage = null
    }
}

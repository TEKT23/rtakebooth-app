package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rtakebooth.app.data.model.GeneralSettings
import org.rtakebooth.app.data.repository.SettingsRepository

class GeneralViewModel {
    private val repository = SettingsRepository()
    private val scope = CoroutineScope(Dispatchers.Default)

    var settings by mutableStateOf(GeneralSettings())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isSaving by mutableStateOf(false)
        private set

    var saveMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadSettings()
    }

    fun loadSettings() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                settings = repository.getGeneralSettings()
            } catch (e: Exception) {
                errorMessage = "Failed to load settings: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun saveSettings() {
        scope.launch {
            isSaving = true
            saveMessage = null
            errorMessage = null
            try {
                val success = repository.saveGeneralSettings(settings)
                if (success) {
                    saveMessage = "Settings saved successfully!"
                } else {
                    errorMessage = "Failed to save settings"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isSaving = false
            }
        }
    }

    fun updateSettings(newSettings: GeneralSettings) {
        settings = newSettings
        saveMessage = null // Clear pesan sukses saat user edit lagi
    }

    fun clearMessages() {
        saveMessage = null
        errorMessage = null
    }
}

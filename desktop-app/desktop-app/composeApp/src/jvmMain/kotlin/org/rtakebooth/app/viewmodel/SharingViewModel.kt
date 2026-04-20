package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rtakebooth.app.data.model.SharingSettings
import org.rtakebooth.app.data.repository.SettingsRepository

class SharingViewModel {
    private val repository = SettingsRepository()
    private val scope = CoroutineScope(Dispatchers.Default)

    var settings by mutableStateOf(SharingSettings())
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
                settings = repository.getSharingSettings()
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
                val success = repository.saveSharingSettings(settings)
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

    fun updateSettings(newSettings: SharingSettings) {
        settings = newSettings
        saveMessage = null
    }

    fun clearMessages() {
        saveMessage = null
        errorMessage = null
    }
}

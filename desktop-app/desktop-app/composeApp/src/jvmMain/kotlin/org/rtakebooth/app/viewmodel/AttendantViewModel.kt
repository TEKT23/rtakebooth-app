package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rtakebooth.app.data.model.AttendantSettings
import org.rtakebooth.app.data.repository.SettingsRepository

class AttendantViewModel {
    private val repository = SettingsRepository()
    private val scope = CoroutineScope(Dispatchers.Default)

    var settings by mutableStateOf(AttendantSettings())
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
                settings = repository.getAttendantSettings()
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
                val success = repository.saveAttendantSettings(settings)
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

    fun updateSettings(newSettings: AttendantSettings) {
        settings = newSettings
        saveMessage = null
    }

    fun addBeforeCountdownAudio(path: String) {
        if (path.isNotEmpty() && !settings.beforeCountdownAudios.contains(path)) {
            settings = settings.copy(
                beforeCountdownAudios = settings.beforeCountdownAudios + path
            )
        }
    }

    fun removeBeforeCountdownAudio(index: Int) {
        if (index in settings.beforeCountdownAudios.indices) {
            settings = settings.copy(
                beforeCountdownAudios = settings.beforeCountdownAudios.toMutableList().apply { 
                    removeAt(index) 
                }
            )
        }
    }

    fun addAfterCaptureAudio(path: String) {
        if (path.isNotEmpty() && !settings.afterCaptureAudios.contains(path)) {
            settings = settings.copy(
                afterCaptureAudios = settings.afterCaptureAudios + path
            )
        }
    }

    fun removeAfterCaptureAudio(index: Int) {
        if (index in settings.afterCaptureAudios.indices) {
            settings = settings.copy(
                afterCaptureAudios = settings.afterCaptureAudios.toMutableList().apply { 
                    removeAt(index) 
                }
            )
        }
    }

    fun clearMessages() {
        saveMessage = null
        errorMessage = null
    }
}

package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rtakebooth.app.data.model.CaptureSettings
import org.rtakebooth.app.data.repository.SettingsRepository

class CaptureViewModel {
    private val repository = SettingsRepository()
    private val scope = CoroutineScope(Dispatchers.Default)

    var settings by mutableStateOf(CaptureSettings())
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
                settings = repository.getCaptureSettings()
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
                val success = repository.saveCaptureSettings(settings)
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

    fun updateSettings(newSettings: CaptureSettings) {
        settings = newSettings
        saveMessage = null
    }

    // Individual updaters for UI binding
    fun updatePhotoEnabled(enabled: Boolean) { updateSettings(settings.copy(photoEnabled = enabled)) }
    fun updateGifEnabled(enabled: Boolean) { updateSettings(settings.copy(gifEnabled = enabled)) }
    fun updateSlowMoEnabled(enabled: Boolean) { updateSettings(settings.copy(slowMoEnabled = enabled)) }
    fun updateVideoEnabled(enabled: Boolean) { updateSettings(settings.copy(videoEnabled = enabled)) }
    fun updateLivePhotoEnabled(enabled: Boolean) { updateSettings(settings.copy(livePhotoEnabled = enabled)) }
    fun updateAdvancedRetakeEnabled(enabled: Boolean) { updateSettings(settings.copy(advancedRetakeEnabled = enabled)) }
    
    fun updateDelayBeforeFirstPhoto(delay: Float) { updateSettings(settings.copy(delayBeforeFirstPhoto = delay)) }
    fun updateDelayBeforeOtherPhotos(delay: Float) { updateSettings(settings.copy(delayBeforeOtherPhotos = delay)) }
    fun updatePhotoReviewTime(time: Float) { updateSettings(settings.copy(photoReviewTime = time)) }
    
    fun updateGifResolution(resolution: org.rtakebooth.app.data.model.GifResolution) { updateSettings(settings.copy(gifResolution = resolution)) }
    fun updateGifFrameDelay(delay: Int) { updateSettings(settings.copy(gifFrameDelay = delay)) }
    fun updateGifReverse(reverse: Boolean) { updateSettings(settings.copy(gifReverse = reverse)) }
    fun updateGifPhotoCount(count: Int) { updateSettings(settings.copy(gifPhotoCount = count)) }

    fun clearMessages() {
        saveMessage = null
        errorMessage = null
    }
}

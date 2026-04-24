package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rtakebooth.app.data.model.LayoutSettings
import org.rtakebooth.app.data.model.BgRemovalMode
import org.rtakebooth.app.data.model.SurveyQuestion
import org.rtakebooth.app.data.repository.SettingsRepository
import java.util.UUID

class LayoutViewModel(private val repository: SettingsRepository = SettingsRepository()) {
    private val scope = CoroutineScope(Dispatchers.Default)

    var settings by mutableStateOf(LayoutSettings())
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
                settings = repository.getLayoutSettings()
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
                val success = repository.saveLayoutSettings(settings)
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

    fun updateSettings(newSettings: LayoutSettings) {
        settings = newSettings
        saveMessage = null
    }

    // Helper Updaters
    fun updateBeautyFilterEnabled(enabled: Boolean) = updateSettings(settings.copy(beautyFilterEnabled = enabled))
    fun updateColorFiltersEnabled(enabled: Boolean) = updateSettings(settings.copy(colorFiltersEnabled = enabled))
    fun updatePostProcessingEnabled(enabled: Boolean) = updateSettings(settings.copy(postProcessingEnabled = enabled))
    fun updateStickersEnabled(enabled: Boolean) = updateSettings(settings.copy(stickersEnabled = enabled))
    fun updateWatermarkEnabled(enabled: Boolean) = updateSettings(settings.copy(watermarkEnabled = enabled))
    fun updateWatermarkImagePath(path: String) = updateSettings(settings.copy(watermarkImagePath = path))

    fun updateAiPortraitEnabled(enabled: Boolean) = updateSettings(settings.copy(aiPortraitEnabled = enabled))
    fun updateAiLabels(choose: String, creating: String, retrying: String, error: String) = 
        updateSettings(settings.copy(
            aiPortraitChooseLabel = choose,
            aiPortraitCreatingLabel = creating,
            aiPortraitRetryingLabel = retrying,
            aiPortraitErrorLabel = error
        ))

    fun updateBgRemovalEnabled(enabled: Boolean) = updateSettings(settings.copy(bgRemovalEnabled = enabled))
    fun updateBgRemovalMode(mode: BgRemovalMode) = updateSettings(settings.copy(bgRemovalMode = mode))
    fun updateBgReplacementImagePath(path: String) = updateSettings(settings.copy(bgReplacementImagePath = path))

    fun updateSurveyEnabled(enabled: Boolean) = updateSettings(settings.copy(surveyEnabled = enabled))
    fun addSurveyQuestion(question: String) {
        val newQuestion = SurveyQuestion(id = UUID.randomUUID().toString(), question = question)
        updateSettings(settings.copy(surveyQuestions = settings.surveyQuestions + newQuestion))
    }
    fun removeSurveyQuestion(id: String) {
        updateSettings(settings.copy(surveyQuestions = settings.surveyQuestions.filter { it.id != id }))
    }
    fun updateSurveyFontSize(size: Float) = updateSettings(settings.copy(surveyFontSize = size))

    fun updateDisclaimerEnabled(enabled: Boolean) = updateSettings(settings.copy(disclaimerEnabled = enabled))
    fun updateDisclaimerHeader(header: String) = updateSettings(settings.copy(disclaimerHeader = header))
    fun updateDisclaimerContent(content: String) = updateSettings(settings.copy(disclaimerContent = content))

    fun clearMessages() {
        saveMessage = null
        errorMessage = null
    }
}

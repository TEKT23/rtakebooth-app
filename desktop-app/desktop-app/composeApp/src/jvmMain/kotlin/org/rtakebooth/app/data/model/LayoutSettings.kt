package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class BgRemovalMode {
    GREEN_SCREEN,
    AI_REMOVAL
}

@Serializable
data class SurveyQuestion(
    val id: String,
    val question: String,
    val type: String = "Text"
)

@Serializable
data class LayoutSettings(
    val beautyFilterEnabled: Boolean = false,
    val colorFiltersEnabled: Boolean = false,
    val postProcessingEnabled: Boolean = false,
    val stickersEnabled: Boolean = false,
    val watermarkEnabled: Boolean = false,
    val watermarkImagePath: String = "",
    val aiPortraitEnabled: Boolean = false,
    val aiPortraitChooseLabel: String = "Choose your style",
    val aiPortraitCreatingLabel: String = "Creating AI Portrait",
    val aiPortraitRetryingLabel: String = "Retrying",
    val aiPortraitErrorLabel: String = "Unable to create",
    val bgRemovalEnabled: Boolean = false,
    val bgRemovalMode: BgRemovalMode = BgRemovalMode.AI_REMOVAL,
    val bgReplacementImagePath: String = "",
    val surveyEnabled: Boolean = false,
    val surveyQuestions: List<SurveyQuestion> = emptyList(),
    val surveyFontSize: Float = 14f,
    val disclaimerEnabled: Boolean = false,
    val disclaimerHeader: String = "Terms & Conditions",
    val disclaimerContent: String = ""
)

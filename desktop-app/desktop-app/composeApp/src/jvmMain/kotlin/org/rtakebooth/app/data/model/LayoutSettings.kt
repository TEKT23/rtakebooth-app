package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

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
    val aiPortraitCreatingLabel: String = "Creating AI Portrait...",
    val aiPortraitRetryingLabel: String = "Retrying...",
    val aiPortraitErrorLabel: String = "Unable to create portrait",
    val bgRemovalEnabled: Boolean = false,
    val bgRemovalMode: String = "AI Removal",
    val bgReplacementImagePath: String = "",
    val surveyEnabled: Boolean = false,
    val surveyFontSize: Float = 14.0f,
    val disclaimerEnabled: Boolean = false,
    val disclaimerHeader: String = "",
    val disclaimerContent: String = ""
)

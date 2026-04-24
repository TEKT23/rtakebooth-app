package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class GifResolution(val label: String) {
    R_480P("480p"),
    R_720P("720p"),
    R_1080P("1080p")
}

@Serializable
data class CaptureSettings(
    val photoEnabled: Boolean = true,
    val gifEnabled: Boolean = false,
    val slowMoEnabled: Boolean = false,
    val videoEnabled: Boolean = false,
    val livePhotoEnabled: Boolean = false,
    val advancedRetakeEnabled: Boolean = false,
    val delayBeforeFirstPhoto: Float = 3.0f,
    val delayBeforeOtherPhotos: Float = 1.5f,
    val photoReviewTime: Float = 5.0f,
    val gifResolution: GifResolution = GifResolution.R_720P,
    val gifFrameDelay: Int = 200,
    val gifReverse: Boolean = false,
    val gifPhotoCount: Int = 8
)

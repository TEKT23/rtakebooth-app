package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptureSettings(
    val photoEnabled: Boolean = true,
    val gifEnabled: Boolean = false,
    val slowMoEnabled: Boolean = false,
    val videoEnabled: Boolean = false,
    val delayBeforeFirstPhoto: Float = 3.0f,
    val delayBeforeOtherPhotos: Float = 1.5f,
    val photoReviewTime: Float = 5.0f,
    val gifResolution: String = "Medium",
    val gifFrameDelay: Int = 200,
    val gifReverse: Boolean = false,
    val gifPhotoCount: Int = 8
)

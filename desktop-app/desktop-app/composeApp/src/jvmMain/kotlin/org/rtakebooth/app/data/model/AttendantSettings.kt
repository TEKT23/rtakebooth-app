package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AttendantSettings(
    val voiceStyle: String = "American Female",
    val startScreenVideo: String = "",
    val countdownMedia: String = "",
    val beforeCountdownAudios: List<String> = emptyList(),
    val beforeCountdownRandomize: Boolean = false,
    val afterCaptureAudios: List<String> = emptyList(),
    val afterCaptureRandomize: Boolean = false,
    val processingAudio: String = "",
    val endSessionAudio: String = ""
)

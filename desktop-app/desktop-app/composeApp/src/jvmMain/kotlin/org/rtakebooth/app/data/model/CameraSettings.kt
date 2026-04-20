package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CameraSettings(
    val webcamEnabled: Boolean = true,
    val selectedCamera: String = "",
    val resolutionMode: String = "Faster Framerate",
    val rotation: Int = 0,
    val selectedMicrophone: String = ""
)

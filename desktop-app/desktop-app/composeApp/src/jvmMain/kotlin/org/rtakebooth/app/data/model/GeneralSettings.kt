package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GeneralSettings(
    val securityPin: String = "",
    val hardwareAcceleration: Boolean = true,
    val showSavedEvents: Boolean = true,
    val startFullScreen: Boolean = false,
    val dataDirectory: String = "",
    val exportPrints: String = "",
    val exportOriginals: String = "",
    val exportGifs: String = "",
    val exportVideos: String = "",
    val apiUrl: String = "http://localhost:8080",
    val apiPassword: String = ""
)

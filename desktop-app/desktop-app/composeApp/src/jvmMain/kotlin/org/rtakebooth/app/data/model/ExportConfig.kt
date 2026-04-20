package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ExportConfig(
    val exportPrints: Boolean = true,
    val exportGifs: Boolean = true,
    val exportVideos: Boolean = false,
    val exportOriginals: Boolean = false,
    val selectedEventId: String? = null,
    val timeFilter: String = "All Time",
    val destinationPath: String = ""
)

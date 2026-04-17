package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ShareQueue(
    val channel: String = "",
    val sent: Int = 0,
    val pending: Int = 0
)

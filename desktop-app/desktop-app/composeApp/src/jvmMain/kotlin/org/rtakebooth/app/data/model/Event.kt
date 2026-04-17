package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String = "",
    val name: String = "",
    val date: String = "",          // ISO 8601 format: "2026-04-16"
    val location: String = "",
    val description: String = "",
    val status: String = "draft",   // "draft", "active", "completed"
    val photoCount: Int = 0,
    val printCount: Int = 0,
)

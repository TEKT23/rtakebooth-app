package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SharingSettings(
    val enhancedSharingEnabled: Boolean = false,
    val uploadOriginalPhotos: Boolean = false,
    val emailEnabled: Boolean = false,
    val emailMasked: Boolean = false,
    val emailSubject: String = "",
    val emailHtmlBody: String = "",
    val whatsappEnabled: Boolean = false,
    val whatsappMessage: String = "",
    val whatsappCountryCode: String = "+62",
    val smsEnabled: Boolean = false,
    val smsMessage: String = "",
    val smsCountryCode: String = "+62",
    val messagingProvider: String = "",
    val skipSharingScreen: Boolean = false,
    val showDoneButton: Boolean = true,
    val showRetakeButton: Boolean = true,
    val finalScreenTimeout: Int = 30
)

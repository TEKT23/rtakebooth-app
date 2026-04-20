package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable

/**
 * Unified settings container (root state holder).
 * Dipakai untuk JSON export/import di Module 10.
 */
@Serializable
data class AppSettings(
    val general: GeneralSettings = GeneralSettings(),
    val capture: CaptureSettings = CaptureSettings(),
    val camera: CameraSettings = CameraSettings(),
    val attendant: AttendantSettings = AttendantSettings(),
    val layout: LayoutSettings = LayoutSettings(),
    val sharing: SharingSettings = SharingSettings(),
    val print: PrintSettings = PrintSettings()
)

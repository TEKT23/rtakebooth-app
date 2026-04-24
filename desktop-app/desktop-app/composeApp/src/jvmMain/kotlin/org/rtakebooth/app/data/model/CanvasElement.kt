package org.rtakebooth.app.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CanvasElement(
    val id: String = UUID.randomUUID().toString(),
    val type: ElementType = ElementType.TEXT,
    val x: Float = 100f,
    val y: Float = 100f,
    val width: Float = 200f,
    val height: Float = 50f,
    val rotation: Float = 0f,
    val content: String = "New Text",       // untuk TEXT: isi teks, untuk IMAGE: path file
    val fontSize: Float = 16f,              // hanya untuk TEXT
    val fontColor: String = "#FFFFFF",      // hanya untuk TEXT
    val backgroundColor: String = "",       // kosong = transparent
    val cornerRadius: Float = 0f,           // untuk SHAPE
    val opacity: Float = 1.0f,
    val zIndex: Int = 0,
    val sequenceNumber: Int = 1,            // hanya untuk PHOTO_FROM_BOOTH
    val isLocked: Boolean = false,
)

enum class ElementType {
    TEXT, IMAGE, QR_CODE, SHAPE, PHOTO_FROM_BOOTH, SESSION_DATA
}

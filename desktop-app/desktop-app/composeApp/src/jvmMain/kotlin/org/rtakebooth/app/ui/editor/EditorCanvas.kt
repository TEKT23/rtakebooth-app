package org.rtakebooth.app.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.data.model.CanvasElement
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.data.model.EditorState
import org.rtakebooth.app.data.model.Orientation

@Composable
fun EditorCanvas(
    state: EditorState,
    elements: List<CanvasElement>,
    onSelectElement: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val canvasColor = try {
        Color(parseHexColor(state.canvasBackgroundColor))
    } catch (e: Exception) {
        Color(0xFF1a1a2e)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        // Aspect Ratio Box
        Box(
            modifier = Modifier
                .aspectRatio(
                    if (state.orientation == Orientation.LANDSCAPE) {
                        state.aspectRatio.widthRatio.toFloat() / state.aspectRatio.heightRatio
                    } else {
                        state.aspectRatio.heightRatio.toFloat() / state.aspectRatio.widthRatio
                    }
                )
                .background(canvasColor)
                .clickable { onSelectElement(null) }
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        ) {
            // Render Elements
            elements.sortedBy { it.zIndex }.forEach { element ->
                val elementColor = try {
                    if (element.fontColor.isNotEmpty()) Color(parseHexColor(element.fontColor)) else Color.White
                } catch (e: Exception) {
                    Color.White
                }

                val elementBgColor = try {
                    if (element.backgroundColor.isNotEmpty()) Color(parseHexColor(element.backgroundColor)) else Color.Transparent
                } catch (e: Exception) {
                    Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .offset(x = element.x.dp, y = element.y.dp)
                        .size(width = element.width.dp, height = element.height.dp)
                        .clip(RoundedCornerShape(element.cornerRadius.dp))
                        .background(elementBgColor.copy(alpha = element.opacity))
                        .border(
                            width = if (state.selectedElementId == element.id) 2.dp else 0.dp,
                            color = if (state.selectedElementId == element.id) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(element.cornerRadius.dp)
                        )
                        .clickable { onSelectElement(element.id) }
                ) {
                    when (element.type) {
                        ElementType.TEXT -> {
                            Text(
                                text = element.content,
                                color = elementColor,
                                fontSize = element.fontSize.sp,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                        ElementType.IMAGE -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🖼️", fontSize = 24.sp)
                                    Text(
                                        text = element.content.split(java.io.File.separator).lastOrNull() ?: "Image",
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                        ElementType.QR_CODE -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .border(1.dp, Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("QR", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color.White)
                            }
                        }
                        ElementType.SHAPE -> {
                            // Already handled by background and clip
                        }
                    }
                }
            }
        }
    }
}

// Simple hex parser for #RRGGBB or #AARRGGBB
fun parseHexColor(hex: String): Long {
    val cleanHex = hex.removePrefix("#")
    return when (cleanHex.length) {
        6 -> ("FF$cleanHex").toLong(16)
        8 -> cleanHex.toLong(16)
        else -> 0xFFFFFFFFL
    }
}

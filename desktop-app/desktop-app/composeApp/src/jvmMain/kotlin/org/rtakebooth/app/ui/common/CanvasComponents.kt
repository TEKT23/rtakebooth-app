package org.rtakebooth.app.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.rtakebooth.app.data.model.CanvasElement
import org.rtakebooth.app.data.model.ElementType

@Composable
fun BoxScope.ResizeHandle(
    alignment: Alignment,
    onDrag: (Float, Float) -> Unit,
    onDragEnd: () -> Unit
) {
    Box(
        modifier = Modifier
            .align(alignment)
            .size(24.dp)
            .zIndex(2f)
            .pointerInput(alignment) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount.x.toDp().value, dragAmount.y.toDp().value)
                    },
                    onDragEnd = onDragEnd
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(Color.White, RoundedCornerShape(2.dp))
                .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
fun ElementContent(element: CanvasElement, color: Color) {
    when (element.type) {
        ElementType.TEXT -> {
            Text(
                text = element.content,
                color = color,
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
                        color = color.copy(alpha = 0.7f)
                    )
                }
            }
        }
        ElementType.QR_CODE -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .border(2.dp, color.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                    val sizePx = size.minDimension
                    val steps = 7
                    val stepSize = sizePx / steps
                    for (i in 0 until steps) {
                        for (j in 0 until steps) {
                            if ((i + j) % 2 == 0) {
                                drawRect(color.copy(alpha = 0.4f), Offset(i * stepSize, j * stepSize), androidx.compose.ui.geometry.Size(stepSize, stepSize))
                            }
                        }
                    }
                }
                Text("QR", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = color)
            }
        }
        ElementType.SHAPE -> { }
        else -> { /* Other types handled by specialized canvas */ }
    }
}

fun parseHexColor(hex: String): Long {
    val cleanHex = hex.removePrefix("#")
    return when (cleanHex.length) {
        6 -> ("FF$cleanHex").toLong(16)
        8 -> cleanHex.toLong(16)
        else -> 0xFFFFFFFFL
    }
}

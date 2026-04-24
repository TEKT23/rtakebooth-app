package org.rtakebooth.app.ui.template

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.rtakebooth.app.data.model.CanvasElement
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.ui.common.*
import kotlin.math.roundToInt

@Composable
fun TemplateCanvas(
    elements: List<CanvasElement>,
    selectedElementId: String?,
    onSelectElement: (String?) -> Unit,
    onMoveElement: (String, Float, Float, Boolean) -> Unit,
    onResizeElement: (String, Float, Float, Float, Float, Boolean) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onSelectElement(null) }
            .padding(64.dp),
        contentAlignment = Alignment.Center
    ) {
        // Template Sheet (Visual representation of paper)
        Box(
            modifier = Modifier
                .aspectRatio(1.5f) // 6:4 ratio
                .fillMaxSize()
                .shadow(8.dp)
                .background(Color.White)
        ) {
            elements.sortedBy { it.zIndex }.forEach { element ->
                val isSelected = selectedElementId == element.id
                val currentElement by rememberUpdatedState(element)
                val currentOnMove by rememberUpdatedState(onMoveElement)
                val currentOnResize by rememberUpdatedState(onResizeElement)
                val currentOnSelect by rememberUpdatedState(onSelectElement)

                Box(
                    modifier = Modifier
                        .offset { IntOffset(element.x.dp.roundToPx(), element.y.dp.roundToPx()) }
                        .size(width = element.width.dp, height = element.height.dp)
                ) {
                    // Element Content Wrapper
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(element.cornerRadius.dp))
                            .background(if (element.type == ElementType.PHOTO_FROM_BOOTH) Color.LightGray.copy(alpha = 0.3f) else Color.Transparent)
                            .border(
                                width = if (isSelected) 2.dp else 0.5.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)
                            )
                            .pointerInput(element.id) {
                                if (!element.isLocked) {
                                    detectDragGestures(
                                        onDragStart = { currentOnSelect(currentElement.id) },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            currentOnMove(
                                                currentElement.id,
                                                currentElement.x + dragAmount.x.toDp().value,
                                                currentElement.y + dragAmount.y.toDp().value,
                                                false
                                            )
                                        },
                                        onDragEnd = onDragEnd
                                    )
                                }
                            }
                            .clickable { onSelectElement(element.id) }
                    ) {
                        TemplateElementContent(element)
                    }

                    // Resize Handles (Reuse from EditorCanvas)
                    if (isSelected && !element.isLocked) {
                        ResizeHandle(Alignment.BottomEnd, onDrag = { dx, dy ->
                            val newW = (currentElement.width + dx).coerceAtLeast(20f)
                            val newH = (currentElement.height + dy).coerceAtLeast(20f)
                            currentOnResize(currentElement.id, newW, newH, currentElement.x, currentElement.y, false)
                        }, onDragEnd = onDragEnd)
                        // ... other handles could be added here
                    }
                    
                    if (element.isLocked && isSelected) {
                        // Show lock icon
                        Text("🔒", modifier = Modifier.align(Alignment.TopStart).padding(4.dp), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TemplateElementContent(element: CanvasElement) {
    when (element.type) {
        ElementType.PHOTO_FROM_BOOTH -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📷", fontSize = 32.sp)
                    Text("Photo #${element.sequenceNumber}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Text("Placeholder", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
        ElementType.SESSION_DATA -> {
            Box(modifier = Modifier.fillMaxSize().padding(4.dp), contentAlignment = Alignment.Center) {
                Text(element.content, color = Color.Blue, fontSize = 14.sp)
            }
        }
        else -> ElementContent(element, Color.Black)
    }
}

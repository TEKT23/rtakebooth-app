package org.rtakebooth.app.ui.editor

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.rtakebooth.app.data.model.CanvasElement
import org.rtakebooth.app.data.model.ElementType
import org.rtakebooth.app.data.model.EditorState
import org.rtakebooth.app.data.model.Orientation
import org.rtakebooth.app.ui.common.*
import kotlin.math.roundToInt

@Composable
fun EditorCanvas(
    state: EditorState,
    elements: List<CanvasElement>,
    onSelectElement: (String?) -> Unit,
    onMoveElement: (String, Float, Float, Boolean) -> Unit,
    onResizeElement: (String, Float, Float, Float, Float, Boolean) -> Unit,
    onDragEnd: () -> Unit,
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
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .pointerInput(Unit) {
                detectDragGestures(onDrag = { _, _ -> }, onDragEnd = { }) // Consume background drag
            }
            .clickable { onSelectElement(null) }
            .padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        // Main Frame with Shadow and Border
        Box(
            modifier = Modifier
                .aspectRatio(
                    if (state.orientation == Orientation.LANDSCAPE) {
                        state.aspectRatio.widthRatio.toFloat() / state.aspectRatio.heightRatio
                    } else {
                        state.aspectRatio.heightRatio.toFloat() / state.aspectRatio.widthRatio
                    }
                )
                .shadow(12.dp, RoundedCornerShape(4.dp))
                .background(canvasColor)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            // Grid Pattern
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = 50.dp.toPx()
                for (x in 0..(size.width / step).toInt()) {
                    drawLine(Color.White.copy(alpha = 0.05f), Offset(x * step, 0f), Offset(x * step, size.height))
                }
                for (y in 0..(size.height / step).toInt()) {
                    drawLine(Color.White.copy(alpha = 0.05f), Offset(0f, y * step), Offset(size.width, y * step))
                }
            }

            // Render Elements
            elements.sortedBy { it.zIndex }.forEach { element ->
                val isSelected = state.selectedElementId == element.id
                
                // Use rememberUpdatedState to ensure gestures use the latest values without restarting pointerInput
                val currentElement by rememberUpdatedState(element)
                val currentOnMove by rememberUpdatedState(onMoveElement)
                val currentOnResize by rememberUpdatedState(onResizeElement)
                val currentOnSelect by rememberUpdatedState(onSelectElement)

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
                        .offset { IntOffset(element.x.dp.roundToPx(), element.y.dp.roundToPx()) }
                        .size(width = element.width.dp, height = element.height.dp)
                ) {
                    // Element Content
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(element.cornerRadius.dp))
                            .background(elementBgColor.copy(alpha = element.opacity))
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = RoundedCornerShape(element.cornerRadius.dp)
                            )
                            .pointerInput(element.id) {
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
                                    onDragEnd = { onDragEnd() }
                                )
                            }
                            .clickable { onSelectElement(element.id) }
                    ) {
                        ElementContent(element, elementColor)
                    }

                    // Resize Handles
                    if (isSelected) {
                        // Bottom-Right Handle
                        ResizeHandle(
                            alignment = Alignment.BottomEnd,
                            onDrag = { dx, dy ->
                                val newWidth = (currentElement.width + dx).coerceAtLeast(20f)
                                val newHeight = (currentElement.height + dy).coerceAtLeast(20f)
                                currentOnResize(currentElement.id, newWidth, newHeight, currentElement.x, currentElement.y, false)
                            },
                            onDragEnd = onDragEnd
                        )

                        // Bottom-Left Handle
                        ResizeHandle(
                            alignment = Alignment.BottomStart,
                            onDrag = { dx, dy ->
                                val newWidth = (currentElement.width - dx).coerceAtLeast(20f)
                                val newHeight = (currentElement.height + dy).coerceAtLeast(20f)
                                val newX = currentElement.x + (currentElement.width - newWidth)
                                currentOnResize(currentElement.id, newWidth, newHeight, newX, currentElement.y, false)
                            },
                            onDragEnd = onDragEnd
                        )

                        // Top-Right Handle
                        ResizeHandle(
                            alignment = Alignment.TopEnd,
                            onDrag = { dx, dy ->
                                val newWidth = (currentElement.width + dx).coerceAtLeast(20f)
                                val newHeight = (currentElement.height - dy).coerceAtLeast(20f)
                                val newY = currentElement.y + (currentElement.height - newHeight)
                                currentOnResize(currentElement.id, newWidth, newHeight, currentElement.x, newY, false)
                            },
                            onDragEnd = onDragEnd
                        )

                        // Top-Left Handle
                        ResizeHandle(
                            alignment = Alignment.TopStart,
                            onDrag = { dx, dy ->
                                val newWidth = (currentElement.width - dx).coerceAtLeast(20f)
                                val newHeight = (currentElement.height - dy).coerceAtLeast(20f)
                                val newX = currentElement.x + (currentElement.width - newWidth)
                                val newY = currentElement.y + (currentElement.height - newHeight)
                                currentOnResize(currentElement.id, newWidth, newHeight, newX, newY, false)
                            },
                            onDragEnd = onDragEnd
                        )
                    }
                }
            }
        }
    }
}

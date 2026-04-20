package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.rtakebooth.app.data.model.*

class EditorViewModel {
    var state by mutableStateOf(EditorState())
        private set

    private val undoRedoManager = UndoRedoManager<EditorState>()

    var canUndo by mutableStateOf(false)
        private set
    var canRedo by mutableStateOf(false)
        private set

    // ---- Tab Management ----
    fun switchTab(tab: ScreenTab) {
        saveStateForUndo()
        state = state.copy(currentTab = tab, selectedElementId = null)
    }

    // ---- Element CRUD ----
    fun addElement(type: ElementType) {
        saveStateForUndo()
        val currentTab = state.currentTab
        val elements = currentElements().toMutableList()
        val newElement = CanvasElement(
            type = type,
            x = 50f + (elements.size * 20f) % 200f,  // offset agar tidak stack
            y = 50f + (elements.size * 20f) % 200f,
            content = when (type) {
                ElementType.TEXT -> "New Text"
                ElementType.IMAGE -> ""
                ElementType.QR_CODE -> "https://rtakebooth.com"
                ElementType.SHAPE -> ""
            },
            width = when (type) {
                ElementType.TEXT -> 200f
                ElementType.IMAGE -> 200f
                ElementType.QR_CODE -> 120f
                ElementType.SHAPE -> 150f
            },
            height = when (type) {
                ElementType.TEXT -> 50f
                ElementType.IMAGE -> 150f
                ElementType.QR_CODE -> 120f
                ElementType.SHAPE -> 150f
            },
            zIndex = elements.size,
        )
        val updatedMap = state.canvasElements.toMutableMap()
        updatedMap[currentTab] = elements + newElement
        state = state.copy(
            canvasElements = updatedMap,
            selectedElementId = newElement.id
        )
    }

    fun removeSelectedElement() {
        val selectedId = state.selectedElementId ?: return
        saveStateForUndo()
        val currentTab = state.currentTab
        val updatedMap = state.canvasElements.toMutableMap()
        updatedMap[currentTab] = currentElements().filter { it.id != selectedId }
        state = state.copy(canvasElements = updatedMap, selectedElementId = null)
    }

    fun updateElement(elementId: String, transform: (CanvasElement) -> CanvasElement) {
        val currentTab = state.currentTab
        val updatedMap = state.canvasElements.toMutableMap()
        updatedMap[currentTab] = currentElements().map {
            if (it.id == elementId) transform(it) else it
        }
        state = state.copy(canvasElements = updatedMap)
    }

    fun selectElement(elementId: String?) {
        state = state.copy(selectedElementId = elementId)
    }

    fun moveElement(elementId: String, newX: Float, newY: Float) {
        updateElement(elementId) { it.copy(x = newX, y = newY) }
    }

    fun resizeElement(elementId: String, newWidth: Float, newHeight: Float) {
        updateElement(elementId) { it.copy(width = newWidth, height = newHeight) }
    }

    // ---- Properties Panel Updates ----
    fun updateAspectRatio(ratio: AspectRatio) {
        saveStateForUndo()
        state = state.copy(aspectRatio = ratio)
    }

    fun updateOrientation(orientation: Orientation) {
        saveStateForUndo()
        state = state.copy(orientation = orientation)
    }

    fun updateShowLiveView(show: Boolean) { state = state.copy(showLiveView = show) }
    fun updateCropLiveView(crop: Boolean) { state = state.copy(cropLiveView = crop) }
    fun updateMirrorLiveView(mirror: Boolean) { state = state.copy(mirrorLiveView = mirror) }
    fun updateShowGalleryButton(show: Boolean) { state = state.copy(showGalleryButton = show) }
    fun updateShowCountdown(show: Boolean) { state = state.copy(showCountdown = show) }
    fun updateShowCancelButton(show: Boolean) { state = state.copy(showCancelButton = show) }
    fun updateSessionTrigger(trigger: SessionTrigger) { state = state.copy(sessionTrigger = trigger) }
    fun updateCanvasBackground(color: String) {
        saveStateForUndo()
        state = state.copy(canvasBackgroundColor = color)
    }

    // ---- Undo/Redo ----
    fun undo() {
        val previousState = undoRedoManager.undo(state) ?: return
        state = previousState
        refreshUndoRedoState()
    }

    fun redo() {
        val nextState = undoRedoManager.redo(state) ?: return
        state = nextState
        refreshUndoRedoState()
    }

    // ---- Helpers ----
    fun currentElements(): List<CanvasElement> {
        return state.canvasElements[state.currentTab] ?: emptyList()
    }

    fun selectedElement(): CanvasElement? {
        return currentElements().find { it.id == state.selectedElementId }
    }

    private fun saveStateForUndo() {
        undoRedoManager.pushState(state)
        refreshUndoRedoState()
    }

    private fun refreshUndoRedoState() {
        canUndo = undoRedoManager.canUndo
        canRedo = undoRedoManager.canRedo
    }
}

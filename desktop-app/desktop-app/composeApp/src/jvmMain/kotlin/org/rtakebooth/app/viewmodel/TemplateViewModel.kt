package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rtakebooth.app.data.model.*
import org.rtakebooth.app.data.repository.SettingsRepository

class TemplateViewModel {
    private val repository = SettingsRepository()
    private val scope = CoroutineScope(Dispatchers.Default)
    private val undoRedoManager = UndoRedoManager<TemplateEditorState>()

    var state by mutableStateOf(TemplateEditorState())
        private set

    var canUndo by mutableStateOf(false)
        private set
    var canRedo by mutableStateOf(false)
        private set

    init {
        // Load existing templates (for now just load a blank one or the first one)
        loadTemplates()
    }

    private fun loadTemplates() {
        scope.launch {
            val templates = repository.getPrintTemplates()
            if (templates.isNotEmpty()) {
                state = state.copy(currentTemplate = templates.first())
            }
        }
    }

    fun saveTemplate() {
        scope.launch {
            state = state.copy(isSaving = true)
            val success = repository.savePrintTemplate(state.currentTemplate)
            state = state.copy(isSaving = false)
        }
    }

    // ---- Element Management ----
    fun addElement(type: ElementType) {
        saveStateForUndo()
        val elements = state.currentTemplate.elements.toMutableList()
        val newElement = CanvasElement(
            type = type,
            x = 100f + (elements.size * 20f),
            y = 100f + (elements.size * 20f),
            content = when (type) {
                ElementType.PHOTO_FROM_BOOTH -> "Photo Placeholder"
                ElementType.SESSION_DATA -> "{event_name}"
                ElementType.IMAGE -> ""
                ElementType.TEXT -> "New Text"
                else -> ""
            },
            width = if (type == ElementType.PHOTO_FROM_BOOTH) 400f else 200f,
            height = if (type == ElementType.PHOTO_FROM_BOOTH) 300f else 50f,
            zIndex = elements.size
        )
        updateTemplate { it.copy(elements = elements + newElement) }
        state = state.copy(selectedElementId = newElement.id)
    }

    fun removeSelectedElement() {
        val selectedId = state.selectedElementId ?: return
        saveStateForUndo()
        updateTemplate { it.copy(elements = it.elements.filter { el -> el.id != selectedId }) }
        state = state.copy(selectedElementId = null)
    }

    fun updateElement(elementId: String, pushToUndo: Boolean = false, transform: (CanvasElement) -> CanvasElement) {
        if (pushToUndo) saveStateForUndo()
        updateTemplate { template ->
            template.copy(elements = template.elements.map { 
                if (it.id == elementId) transform(it) else it 
            })
        }
    }

    fun selectElement(id: String?) {
        state = state.copy(selectedElementId = id)
    }

    // ---- Layout Updates ----
    fun updatePaperSize(size: String) {
        saveStateForUndo()
        updateTemplate { it.copy(paperSize = size) }
    }

    fun updateResolution(res: Int) {
        saveStateForUndo()
        updateTemplate { it.copy(resolution = res) }
    }

    fun updateOrientation(orientation: Orientation) {
        saveStateForUndo()
        updateTemplate { it.copy(orientation = orientation) }
    }

    // ---- Helpers ----
    private fun updateTemplate(transform: (PrintTemplate) -> PrintTemplate) {
        state = state.copy(currentTemplate = transform(state.currentTemplate))
    }

    fun selectedElement(): CanvasElement? {
        return state.currentTemplate.elements.find { it.id == state.selectedElementId }
    }

    // ---- Undo/Redo ----
    fun undo() {
        val previous = undoRedoManager.undo(state) ?: return
        state = previous
        refreshUndoRedoState()
    }

    fun redo() {
        val next = undoRedoManager.redo(state) ?: return
        state = next
        refreshUndoRedoState()
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

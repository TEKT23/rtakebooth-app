package org.rtakebooth.app.data.model

class UndoRedoManager<T>(
    private val maxHistorySize: Int = 50
) {
    private val undoStack = mutableListOf<T>()
    private val redoStack = mutableListOf<T>()

    fun pushState(state: T) {
        undoStack.add(state)
        if (undoStack.size > maxHistorySize) {
            undoStack.removeAt(0)
        }
        redoStack.clear()
    }

    fun undo(currentState: T): T? {
        if (undoStack.isEmpty()) return null
        redoStack.add(currentState)
        return undoStack.removeAt(undoStack.lastIndex)
    }

    fun redo(currentState: T): T? {
        if (redoStack.isEmpty()) return null
        undoStack.add(currentState)
        return redoStack.removeAt(redoStack.lastIndex)
    }

    val canUndo: Boolean get() = undoStack.isNotEmpty()
    val canRedo: Boolean get() = redoStack.isNotEmpty()

    fun clear() {
        undoStack.clear()
        redoStack.clear()
    }
}

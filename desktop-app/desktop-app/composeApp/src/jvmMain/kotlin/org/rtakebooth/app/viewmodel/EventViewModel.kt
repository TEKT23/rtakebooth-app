package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rtakebooth.app.data.model.Event
import org.rtakebooth.app.data.repository.EventRepository

class EventViewModel {
    private val repository = EventRepository()
    private val scope = CoroutineScope(Dispatchers.Default)

    var events by mutableStateOf<List<Event>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var showCreateDialog by mutableStateOf(false)
        private set

    var newEvent by mutableStateOf(Event())
        private set

    init {
        loadEvents()
    }

    fun loadEvents() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                events = repository.getEvents()
            } catch (e: Exception) {
                errorMessage = "Failed to load events: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun toggleCreateDialog() {
        showCreateDialog = !showCreateDialog
        if (showCreateDialog) {
            newEvent = Event() // Reset form
        }
    }

    fun updateNewEvent(event: Event) {
        newEvent = event
    }

    fun createEvent() {
        scope.launch {
            isLoading = true
            try {
                val success = repository.createEvent(newEvent)
                if (success) {
                    loadEvents()
                    showCreateDialog = false
                } else {
                    errorMessage = "Failed to create event"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteEvent(eventId: String) {
        scope.launch {
            isLoading = true
            try {
                val success = repository.deleteEvent(eventId)
                if (success) {
                    loadEvents()
                } else {
                    errorMessage = "Failed to delete event"
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}

package org.rtakebooth.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.rtakebooth.app.data.model.ShareQueue
import org.rtakebooth.app.data.model.SharingStatusState
import org.rtakebooth.app.data.model.SyncStatus

class SharingStatusViewModel {
    var state by mutableStateOf(SharingStatusState())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadStatus()
    }

    fun loadStatus() {
        isLoading = true
        // Simulasi load data dummy (backend belum ada endpoint-nya)
        state = SharingStatusState(
            totalPending = 3,
            syncStatus = SyncStatus.UP_TO_DATE,
            queues = listOf(
                ShareQueue(channel = "Email", sent = 42, pending = 1),
                ShareQueue(channel = "SMS", sent = 18, pending = 0),
                ShareQueue(channel = "Print", sent = 35, pending = 2),
                ShareQueue(channel = "Upload (Cloud)", sent = 67, pending = 0),
                ShareQueue(channel = "WhatsApp", sent = 23, pending = 0),
            )
        )
        isLoading = false
    }

    fun exportShares() {
        println("Export shares clicked")
    }

    fun deleteAllShares() {
        println("Delete all shares clicked")
        state = state.copy(
            queues = state.queues.map { it.copy(sent = 0, pending = 0) },
            totalPending = 0
        )
    }
}

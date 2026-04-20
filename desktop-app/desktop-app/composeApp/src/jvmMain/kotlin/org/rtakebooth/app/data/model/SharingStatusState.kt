package org.rtakebooth.app.data.model

enum class SyncStatus { UP_TO_DATE, SYNCING, ERROR }

data class SharingStatusState(
    val totalPending: Int = 0,
    val syncStatus: SyncStatus = SyncStatus.UP_TO_DATE,
    val queues: List<ShareQueue> = emptyList()
)

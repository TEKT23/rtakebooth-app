package org.rtakebooth.app.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.data.model.SyncStatus
import org.rtakebooth.app.viewmodel.SharingStatusViewModel

@Composable
fun SharingStatusScreen(viewModel: SharingStatusViewModel = remember { SharingStatusViewModel() }) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Sharing Status Dashboard",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // === Top Section: Summary Card ===
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors()
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = state.totalPending.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Pending Items",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = when (state.syncStatus) {
                        SyncStatus.UP_TO_DATE -> MaterialTheme.colorScheme.primaryContainer
                        SyncStatus.SYNCING -> MaterialTheme.colorScheme.tertiaryContainer
                        SyncStatus.ERROR -> MaterialTheme.colorScheme.errorContainer
                    }
                ) {
                    Text(
                        text = when (state.syncStatus) {
                            SyncStatus.UP_TO_DATE -> "✅ Up to date"
                            SyncStatus.SYNCING -> "🔄 Syncing..."
                            SyncStatus.ERROR -> "❌ Error"
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (state.syncStatus) {
                            SyncStatus.UP_TO_DATE -> MaterialTheme.colorScheme.onPrimaryContainer
                            SyncStatus.SYNCING -> MaterialTheme.colorScheme.onTertiaryContainer
                            SyncStatus.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // === Middle Section: Status Table ===
        Text(
            text = "Queue Details",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Channel", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Sent", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Text("Pending", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Rows
                state.queues.forEach { queue ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(queue.channel, modifier = Modifier.weight(1f), fontSize = 14.sp)
                        Text(
                            queue.sent.toString(), 
                            modifier = Modifier.width(80.dp), 
                            fontSize = 14.sp, 
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            queue.pending.toString(), 
                            modifier = Modifier.width(80.dp), 
                            fontSize = 14.sp, 
                            fontWeight = if (queue.pending > 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (queue.pending > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // === Bottom Section: Action Buttons ===
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.exportShares() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Export Shares")
            }
            Button(
                onClick = { viewModel.deleteAllShares() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete All Shares")
            }
        }
    }
}

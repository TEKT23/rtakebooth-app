package org.rtakebooth.app.ui.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rtakebooth.app.data.model.Event
import org.rtakebooth.app.ui.components.LargeTextAreaRow
import org.rtakebooth.app.ui.components.TextFieldRow
import org.rtakebooth.app.viewmodel.EventViewModel

@Composable
fun EventListScreen(viewModel: EventViewModel = remember { EventViewModel() }) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.toggleCreateDialog() }) {
                Text("+ New Event", modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Events Management",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (viewModel.isLoading && viewModel.events.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.events) { event ->
                        EventCard(
                            event = event,
                            onDelete = { viewModel.deleteEvent(event.id) }
                        )
                    }
                }
            }
        }
    }

    if (viewModel.showCreateDialog) {
        CreateEventDialog(
            event = viewModel.newEvent,
            onEventChange = { viewModel.updateNewEvent(it) },
            onDismiss = { viewModel.toggleCreateDialog() },
            onConfirm = { viewModel.createEvent() }
        )
    }
}

@Composable
fun EventCard(event: Event, onDelete: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = event.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    StatusBadge(event.status)
                }
                Text(
                    text = "${event.date} — ${event.location}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("📷 ${event.photoCount}", fontSize = 12.sp)
                    Text("🖨️ ${event.printCount}", fontSize = 12.sp)
                }
            }

            IconButton(onClick = onDelete) {
                Text("🗑", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = when (status) {
            "active" -> MaterialTheme.colorScheme.primaryContainer
            "completed" -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.tertiaryContainer
        }
    ) {
        Text(
            text = status.uppercase(),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = when (status) {
                "active" -> MaterialTheme.colorScheme.onPrimaryContainer
                "completed" -> MaterialTheme.colorScheme.onSurfaceVariant
                else -> MaterialTheme.colorScheme.onTertiaryContainer
            }
        )
    }
}

@Composable
fun CreateEventDialog(
    event: Event,
    onEventChange: (Event) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Event") },
        text = {
            Column(modifier = Modifier.width(400.dp)) {
                TextFieldRow(
                    label = "Event Name",
                    value = event.name,
                    onValueChange = { onEventChange(event.copy(name = it)) }
                )
                TextFieldRow(
                    label = "Date (YYYY-MM-DD)",
                    value = event.date,
                    onValueChange = { onEventChange(event.copy(date = it)) }
                )
                TextFieldRow(
                    label = "Location",
                    value = event.location,
                    onValueChange = { onEventChange(event.copy(location = it)) }
                )
                LargeTextAreaRow(
                    label = "Description",
                    value = event.description,
                    onValueChange = { onEventChange(event.copy(description = it)) }
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

package org.rtakebooth.app.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import org.rtakebooth.app.data.api.ApiClient
import org.rtakebooth.app.data.model.Event

class EventRepository {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getEvents(): List<Event> {
        return try {
            val response: ApiResponse = ApiClient.httpClient.get(
                ApiClient.buildUrl("/api/v1/events")
            ).body()
            if (response.success && response.data != null) {
                json.decodeFromJsonElement<List<Event>>(response.data)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error fetching events: ${e.message}")
            // Gunakan data dummy sementara backend belum siap
            listOf(
                Event(id = "1", name = "Wedding Reception", date = "2026-04-20", location = "Grand Ballroom", status = "active", photoCount = 142, printCount = 85),
                Event(id = "2", name = "Birthday Party", date = "2026-04-15", location = "Garden Venue", status = "completed", photoCount = 98, printCount = 45),
                Event(id = "3", name = "Corporate Event", date = "2026-04-25", location = "Convention Center", status = "draft", photoCount = 0, printCount = 0),
            )
        }
    }

    suspend fun createEvent(event: Event): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.post(
                ApiClient.buildUrl("/api/v1/events")
            ) {
                contentType(ContentType.Application.Json)
                setBody(event)
            }.body()
            response.success
        } catch (e: Exception) {
            println("Error creating event: ${e.message}")
            false
        }
    }

    suspend fun deleteEvent(eventId: String): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.delete(
                ApiClient.buildUrl("/api/v1/events/$eventId")
            ).body()
            response.success
        } catch (e: Exception) {
            println("Error deleting event: ${e.message}")
            false
        }
    }
}

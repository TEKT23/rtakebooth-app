package org.rtakebooth.app.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.rtakebooth.app.data.api.ApiClient
import org.rtakebooth.app.data.model.GeneralSettings
import org.rtakebooth.app.data.model.CaptureSettings
import org.rtakebooth.app.data.model.CameraSettings
import org.rtakebooth.app.data.model.AttendantSettings
import org.rtakebooth.app.data.model.LayoutSettings
import org.rtakebooth.app.data.model.SharingSettings
import org.rtakebooth.app.data.model.PrintSettings

/**
 * Wrapper class matching the backend's APIResponse structure.
 */
@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: JsonElement? = null
)

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

class SettingsRepository {

    /**
     * GET /api/v1/settings/general
     * Mengambil GeneralSettings dari backend.
     * Jika backend belum punya data (404 atau data null), return default.
     */
    suspend fun getGeneralSettings(): GeneralSettings {
        return try {
            val response: ApiResponse = ApiClient.httpClient.get(
                ApiClient.buildUrl("/api/v1/settings/general")
            ).body()

            if (response.success && response.data != null) {
                json.decodeFromJsonElement<GeneralSettings>(response.data)
            } else {
                GeneralSettings() // Return default jika belum ada
            }
        } catch (e: Exception) {
            println("Error fetching general settings: ${e.message}")
            GeneralSettings() // Return default jika error (backend belum jalan, dll)
        }
    }

    /**
     * PUT /api/v1/settings/general
     * Menyimpan GeneralSettings ke backend.
     * Return true jika berhasil, false jika gagal.
     */
    suspend fun saveGeneralSettings(settings: GeneralSettings): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.put(
                ApiClient.buildUrl("/api/v1/settings/general")
            ) {
                contentType(ContentType.Application.Json)
                setBody(settings)
            }.body()

            response.success
        } catch (e: Exception) {
            println("Error saving general settings: ${e.message}")
            false
        }
    }

    /**
     * GET /api/v1/settings/capture
     */
    suspend fun getCaptureSettings(): CaptureSettings {
        return try {
            val response: ApiResponse = ApiClient.httpClient.get(
                ApiClient.buildUrl("/api/v1/settings/capture")
            ).body()

            if (response.success && response.data != null) {
                json.decodeFromJsonElement<CaptureSettings>(response.data)
            } else {
                CaptureSettings()
            }
        } catch (e: Exception) {
            println("Error fetching capture settings: ${e.message}")
            CaptureSettings()
        }
    }

    /**
     * PUT /api/v1/settings/capture
     */
    suspend fun saveCaptureSettings(settings: CaptureSettings): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.put(
                ApiClient.buildUrl("/api/v1/settings/capture")
            ) {
                contentType(ContentType.Application.Json)
                setBody(settings)
            }.body()

            response.success
        } catch (e: Exception) {
            println("Error saving capture settings: ${e.message}")
            false
        }
    }

    /**
     * GET /api/v1/settings/camera
     */
    suspend fun getCameraSettings(): CameraSettings {
        return try {
            val response: ApiResponse = ApiClient.httpClient.get(
                ApiClient.buildUrl("/api/v1/settings/camera")
            ).body()

            if (response.success && response.data != null) {
                json.decodeFromJsonElement<CameraSettings>(response.data)
            } else {
                CameraSettings()
            }
        } catch (e: Exception) {
            println("Error fetching camera settings: ${e.message}")
            CameraSettings()
        }
    }

    /**
     * PUT /api/v1/settings/camera
     */
    suspend fun saveCameraSettings(settings: CameraSettings): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.put(
                ApiClient.buildUrl("/api/v1/settings/camera")
            ) {
                contentType(ContentType.Application.Json)
                setBody(settings)
            }.body()

            response.success
        } catch (e: Exception) {
            println("Error saving camera settings: ${e.message}")
            false
        }
    }

    /**
     * GET /api/v1/settings/attendant
     */
    suspend fun getAttendantSettings(): AttendantSettings {
        return try {
            val response: ApiResponse = ApiClient.httpClient.get(
                ApiClient.buildUrl("/api/v1/settings/attendant")
            ).body()

            if (response.success && response.data != null) {
                json.decodeFromJsonElement<AttendantSettings>(response.data)
            } else {
                AttendantSettings()
            }
        } catch (e: Exception) {
            println("Error fetching attendant settings: ${e.message}")
            AttendantSettings()
        }
    }

    /**
     * PUT /api/v1/settings/attendant
     */
    suspend fun saveAttendantSettings(settings: AttendantSettings): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.put(
                ApiClient.buildUrl("/api/v1/settings/attendant")
            ) {
                contentType(ContentType.Application.Json)
                setBody(settings)
            }.body()

            response.success
        } catch (e: Exception) {
            println("Error saving attendant settings: ${e.message}")
            false
        }
    }

    /**
     * GET /api/v1/settings/layout
     */
    suspend fun getLayoutSettings(): LayoutSettings {
        return try {
            val response: ApiResponse = ApiClient.httpClient.get(
                ApiClient.buildUrl("/api/v1/settings/layout")
            ).body()

            if (response.success && response.data != null) {
                json.decodeFromJsonElement<LayoutSettings>(response.data)
            } else {
                LayoutSettings()
            }
        } catch (e: Exception) {
            println("Error fetching layout settings: ${e.message}")
            LayoutSettings()
        }
    }

    /**
     * PUT /api/v1/settings/layout
     */
    suspend fun saveLayoutSettings(settings: LayoutSettings): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.put(
                ApiClient.buildUrl("/api/v1/settings/layout")
            ) {
                contentType(ContentType.Application.Json)
                setBody(settings)
            }.body()

            response.success
        } catch (e: Exception) {
            println("Error saving layout settings: ${e.message}")
            false
        }
    }

    /**
     * GET /api/v1/settings/sharing
     */
    suspend fun getSharingSettings(): SharingSettings {
        return try {
            val response: ApiResponse = ApiClient.httpClient.get(
                ApiClient.buildUrl("/api/v1/settings/sharing")
            ).body()

            if (response.success && response.data != null) {
                json.decodeFromJsonElement<SharingSettings>(response.data)
            } else {
                SharingSettings()
            }
        } catch (e: Exception) {
            println("Error fetching sharing settings: ${e.message}")
            SharingSettings()
        }
    }

    /**
     * PUT /api/v1/settings/sharing
     */
    suspend fun saveSharingSettings(settings: SharingSettings): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.put(
                ApiClient.buildUrl("/api/v1/settings/sharing")
            ) {
                contentType(ContentType.Application.Json)
                setBody(settings)
            }.body()

            response.success
        } catch (e: Exception) {
            println("Error saving sharing settings: ${e.message}")
            false
        }
    }

    /**
     * GET /api/v1/settings/print
     */
    suspend fun getPrintSettings(): PrintSettings {
        return try {
            val response: ApiResponse = ApiClient.httpClient.get(
                ApiClient.buildUrl("/api/v1/settings/print")
            ).body()

            if (response.success && response.data != null) {
                json.decodeFromJsonElement<PrintSettings>(response.data)
            } else {
                PrintSettings()
            }
        } catch (e: Exception) {
            println("Error fetching print settings: ${e.message}")
            PrintSettings()
        }
    }

    /**
     * PUT /api/v1/settings/print
     */
    suspend fun savePrintSettings(settings: PrintSettings): Boolean {
        return try {
            val response: ApiResponse = ApiClient.httpClient.put(
                ApiClient.buildUrl("/api/v1/settings/print")
            ) {
                contentType(ContentType.Application.Json)
                setBody(settings)
            }.body()

            response.success
        } catch (e: Exception) {
            println("Error saving print settings: ${e.message}")
            false
        }
    }
}

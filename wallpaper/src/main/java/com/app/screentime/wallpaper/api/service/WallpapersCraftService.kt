package com.app.screentime.wallpaper.api.service

import android.util.Log
import com.app.screentime.core.network.BuildConfig
import com.app.screentime.wallpaper.api.model.CategoriesResponse
import com.app.screentime.wallpaper.api.model.ImagesResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for WallpapersCraft API
 */
@Singleton
class WallpapersCraftService @Inject constructor() {
    private val httpClient: HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = false
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = false
            })
        }
        if (BuildConfig.DEBUG) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://api-uc.wallpaperscraft.com"
    }

    /**
     * Get categories
     * @param screenWidth Screen width (default: 1080)
     * @param screenHeight Screen height (default: 2400)
     * @param limit Limit of results (default: 200)
     * @param newTimeFrom Optional timestamp for new items
     */
    suspend fun getCategories(
        screenWidth: Int = 1080,
        screenHeight: Int = 2400,
        limit: Int = 200,
        newTimeFrom: String? = null
    ): Result<CategoriesResponse> {
        return try {
            val response: HttpResponse = httpClient.get("$BASE_URL/categories") {
                header(HttpHeaders.Accept, "application/json")
                parameter("screen[width]", screenWidth)
                parameter("screen[height]", screenHeight)
                parameter("limit", limit)
                parameter("types[]", "free")
                newTimeFrom?.let { parameter("new_time_from", it) }
            }

            if (response.status.isSuccess()) {
                val categoriesResponse: CategoriesResponse = response.body()
                Result.success(categoriesResponse)
            } else {
                Result.failure(Exception("Failed to fetch categories: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(Exception("Client error: ${e.response.status}", e))
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status}", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get images for a category
     * @param categoryId Category ID
     * @param screenWidth Screen width (default: 1080)
     * @param screenHeight Screen height (default: 2400)
     * @param lang Language code (default: "en")
     * @param limit Limit of results (default: 60)
     * @param offset Offset for pagination (default: 0)
     * @param sort Sort order (default: "date")
     * @param cost Cost filter (default: "all")
     */
    suspend fun getImages(
        categoryId: Int,
        screenWidth: Int = 1080,
        screenHeight: Int = 2400,
        lang: String = "en",
        limit: Int = 60,
        offset: Int = 0,
        sort: String = "date",
        cost: String = "all"
    ): Result<ImagesResponse> {
        return try {
            val response: HttpResponse = httpClient.get("$BASE_URL/images") {
                header(HttpHeaders.Accept, "application/json")
                parameter("screen[width]", screenWidth)
                parameter("screen[height]", screenHeight)
                parameter("lang", lang)
                parameter("limit", limit)
                parameter("offset", offset)
                parameter("category_id", categoryId)
                parameter("sort", sort)
                parameter("cost", cost)
                parameter("types[]", "popular")
                parameter("types[]", "premium")
            }

            if (response.status.isSuccess()) {
                val imagesResponse: ImagesResponse = response.body()
                Result.success(imagesResponse)
            } else {
                Result.failure(Exception("Failed to fetch images: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(Exception("Client error: ${e.response.status}", e))
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status}", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Search images by query
     * @param query Search query
     * @param screenWidth Screen width (default: 1080)
     * @param screenHeight Screen height (default: 2400)
     * @param lang Language code (default: "en")
     * @param limit Limit of results (default: 60)
     * @param offset Offset for pagination (default: 0)
     * @param costVariant Cost variant (default: "free,private")
     */
    suspend fun searchImages(
        query: String,
        screenWidth: Int = 1080,
        screenHeight: Int = 2400,
        lang: String = "en",
        limit: Int = 60,
        offset: Int = 0,
        costVariant: String = "free,private"
    ): Result<ImagesResponse> {
        return try {
            val response: HttpResponse = httpClient.get("$BASE_URL/images") {
                header(HttpHeaders.Accept, "application/json")
                parameter("screen[width]", screenWidth)
                parameter("screen[height]", screenHeight)
                parameter("lang", lang)
                parameter("limit", limit)
                parameter("types[]", costVariant)
                parameter("offset", offset)
                parameter("query", query)
                parameter("cost_variant", costVariant)
            }

            if (response.status.isSuccess()) {
                val imagesResponse: ImagesResponse = response.body()
                Result.success(imagesResponse)
            } else {
                Result.failure(Exception("Failed to search images: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(Exception("Client error: ${e.response.status}", e))
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status}", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get parallax images
     * @param resolution Resolution (default: "fhd")
     * @param offset Offset for pagination (default: 0)
     * @param limit Limit of results (default: 60)
     * @param costVariant Cost variant (default: "free")
     */
    suspend fun getParallaxImages(
        resolution: String = "fhd",
        offset: Int = 0,
        limit: Int = 60,
        costVariant: String = "free"
    ): Result<ImagesResponse> {
        return try {
            val response: HttpResponse = httpClient.get("$BASE_URL/parallax-images") {
                header(HttpHeaders.Accept, "application/json")
                parameter("resolution", resolution)
                parameter("offset", offset)
                parameter("limit", limit)
                parameter("cost_variant", costVariant)
            }

            if (response.status.isSuccess()) {
                val imagesResponse: ImagesResponse = response.body()
                Result.success(imagesResponse)
            } else {
                Result.failure(Exception("Failed to fetch parallax images: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(Exception("Client error: ${e.response.status}", e))
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status}", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get new images
     * @param screenWidth Screen width (default: 1080)
     * @param screenHeight Screen height (default: 2400)
     * @param lang Language code (default: "en")
     * @param limit Limit of results (default: 60)
     * @param offset Offset for pagination (default: 0)
     */
    suspend fun getNewImages(
        screenWidth: Int = 1080,
        screenHeight: Int = 2400,
        lang: String = "en",
        limit: Int = 60,
        offset: Int = 0
    ): Result<ImagesResponse> {
        return try {
            val response: HttpResponse = httpClient.get("$BASE_URL/images/new") {
                header(HttpHeaders.Accept, "application/json")
                parameter("screen[width]", screenWidth)
                parameter("screen[height]", screenHeight)
                parameter("lang", lang)
                parameter("limit", limit)
                parameter("offset", offset)
            }

            if (response.status.isSuccess()) {
                val imagesResponse: ImagesResponse = response.body()
                Result.success(imagesResponse)
            } else {
                Result.failure(Exception("Failed to fetch new images: ${response.status}"))
            }
        } catch (e: ClientRequestException) {
            Result.failure(Exception("Client error: ${e.response.status}", e))
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status}", e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

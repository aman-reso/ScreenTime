package com.app.screentime.feature.profile.domain.usecase

import android.content.Context
import android.net.Uri
import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.UploadImagesResponse
import com.app.screentime.core.network.dto.toUser
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke(): User {
        val phone = sessionManager.userPhone ?: preferencesManager.getPhone() ?: ""
        val name = sessionManager.userName ?: preferencesManager.getUsername() ?: "User"
        val id = sessionManager.userId ?: preferencesManager.getUserId() ?: ""
        val role = sessionManager.userRole
        val walletBalance = if (role == UserRole.USER) 1000.0 else 0.0
        return User(
            id = id,
            phone = phone,
            name = name,
            role = role,
            walletBalance = walletBalance
        )
    }
}

class FetchUserProfileUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(userId: String? = null): Result<User> {
        val token = sessionManager.getToken() ?: ""
        return try {
            val dto = api.getUserProfile(token, userId)
            val user = dto.toUser()
            if (userId.isNullOrBlank() || userId == "me" || userId == sessionManager.userId) {
                if (token.isNotBlank()) {
                    sessionManager.saveSession(token, user)
                }
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class LogoutUseCase @Inject constructor(
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    operator fun invoke() {
        sessionManager.clearSession()
        preferencesManager.clearAuth()
    }
}

class SubmitModelOnboardingUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(bio: String, voiceRate: Double, chatRate: Double): Result<Boolean> {
        val token = sessionManager.getToken() ?: return Result.failure(Exception("Not logged in"))
        return try {
            api.submitOnboarding(
                token = token,
                bio = bio,
                voiceRate = voiceRate,
                chatRate = chatRate
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class UploadImagesUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(uris: List<Uri>): Result<List<String>> = withContext(Dispatchers.IO) {
        val token = sessionManager.getToken() ?: ""
        try {
            val parts = uris.mapIndexedNotNull { index, uri ->
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bytes = stream.readBytes()
                    val detectedMime = context.contentResolver.getType(uri)?.lowercase()
                    val mimeType = when {
                        detectedMime?.contains("webp") == true -> "image/webp"
                        detectedMime?.contains("png") == true -> "image/png"
                        detectedMime?.contains("gif") == true -> "image/gif"
                        detectedMime?.contains("heic") == true || detectedMime?.contains("heif") == true -> "image/heic"
                        // Fallback: check RIFF / WEBP magic bytes (52 49 46 46 ... 57 45 42 50)
                        bytes.size >= 12 && bytes[0] == 0x52.toByte() && bytes[1] == 0x49.toByte() &&
                                bytes[2] == 0x46.toByte() && bytes[3] == 0x46.toByte() &&
                                bytes[8] == 0x57.toByte() && bytes[9] == 0x45.toByte() &&
                                bytes[10] == 0x42.toByte() && bytes[11] == 0x50.toByte() -> "image/webp"
                        // Fallback: check PNG magic bytes (89 50 4E 47)
                        bytes.size >= 8 && bytes[0] == 0x89.toByte() && bytes[1] == 0x50.toByte() &&
                                bytes[2] == 0x4E.toByte() && bytes[3] == 0x47.toByte() -> "image/png"
                        else -> "image/jpeg"
                    }
                    val extension = when (mimeType) {
                        "image/webp" -> "webp"
                        "image/png" -> "png"
                        "image/gif" -> "gif"
                        "image/heic" -> "heic"
                        else -> "jpg"
                    }
                    val fileName = "photo_${System.currentTimeMillis()}_$index.$extension"
                    com.app.screentime.core.network.dto.UploadImagePart(
                        filename = fileName,
                        bytes = bytes,
                        mimeType = mimeType
                    )
                }
            }
            if (parts.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("No valid image files found"))
            }

            val response: UploadImagesResponse = api.uploadImageParts(token, parts)
            val urls = response.getUploadedUrls()
            if (urls.isNotEmpty()) {
                Result.success(urls)
            } else if (response.success) {
                Result.success(emptyList())
            } else {
                Result.failure(Exception(response.message.ifBlank { "Upload failed" }))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadBytes(images: List<Pair<String, ByteArray>>): Result<List<String>> = withContext(Dispatchers.IO) {
        val token = sessionManager.getToken() ?: ""
        try {
            val response: UploadImagesResponse = api.uploadImages(token, images)
            val urls = response.getUploadedUrls()
            if (urls.isNotEmpty()) {
                Result.success(urls)
            } else if (response.success) {
                Result.success(emptyList())
            } else {
                Result.failure(Exception(response.message.ifBlank { "Upload failed" }))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


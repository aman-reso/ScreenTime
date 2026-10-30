package com.app.screentime.feature.models.data

import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.feature.models.model.ModelGirl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModelsRepository @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend fun getModels(page: Int = 1, limit: Int = 30): Result<List<ModelGirl>> {
        return try {
            val token = sessionManager.getToken() ?: ""
            val resp = api.getModels(token, page, limit)
            val list = resp.models.map { dto ->
                val primaryPhoto = dto.avatar_url?.takeIf { it.isNotBlank() }
                    ?: dto.photos.firstOrNull { it.isNotBlank() }
                    ?: ""

                val audioRateVal = when {
                    dto.audio_rate > 0 -> dto.audio_rate.toDouble()
                    dto.voice_rate_per_min > 0 -> dto.voice_rate_per_min
                    else -> 10.0
                }
                val videoRateVal = when {
                    dto.video_rate > 0 -> dto.video_rate.toDouble()
                    dto.video_rate_per_min > 0 -> dto.video_rate_per_min
                    else -> 20.0
                }

                ModelGirl(
                    id = dto.id.ifBlank { dto.phone },
                    name = dto.getResolvedName(),
                    age = dto.age?.takeIf { it > 0 } ?: 22,
                    city = dto.city ?: "",
                    bio = dto.bio ?: dto.about_me ?: "",
                    avatarUrl = primaryPhoto,
                    photos = dto.photos,
                    audioRate = audioRateVal,
                    videoRate = videoRateVal,
                    isOnline = dto.is_online,
                    status = dto.status ?: dto.role,
                    role = dto.role
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMyRates(audioRate: Int, videoRate: Int): Result<Boolean> {
        return try {
            val token = sessionManager.getToken() ?: ""
            val resp = api.updateModelRates(token, audioRate, videoRate)
            Result.success(resp.success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isCurrentUserModel(): Boolean {
        return sessionManager.userRole == UserRole.MODEL
    }

    fun getCurrentUserName(): String? {
        return sessionManager.userName
    }
}

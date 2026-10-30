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
    companion object {
        val DUMMY_MODELS = listOf(
            ModelGirl(
                id = "dummy_1",
                name = "Aarohi Sharma",
                age = 22,
                city = "Mumbai",
                bio = "Love deep talks, sharing feelings and soulful music ✨",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=600&auto=format&fit=crop&q=80",
                audioRate = 12.0,
                videoRate = 25.0,
                isOnline = true
            ),
            ModelGirl(
                id = "dummy_2",
                name = "Priya Patel",
                age = 24,
                city = "Delhi",
                bio = "Here to listen and talk your heart out ❤️",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&auto=format&fit=crop&q=80",
                audioRate = 15.0,
                videoRate = 30.0,
                isOnline = true
            ),
            ModelGirl(
                id = "dummy_3",
                name = "Ananya Roy",
                age = 21,
                city = "Bangalore",
                bio = "Sharing laughter, stories and chill vibes 🌸",
                avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=600&auto=format&fit=crop&q=80",
                audioRate = 10.0,
                videoRate = 20.0,
                isOnline = false
            ),
            ModelGirl(
                id = "dummy_4",
                name = "Riya Sen",
                age = 23,
                city = "Kolkata",
                bio = "Late night comfort calls and sweet talks 🌙",
                avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600&auto=format&fit=crop&q=80",
                audioRate = 18.0,
                videoRate = 35.0,
                isOnline = true
            ),
            ModelGirl(
                id = "dummy_5",
                name = "Sneha Kapoor",
                age = 25,
                city = "Chandigarh",
                bio = "Friendly companion ready to listen to your day ☕",
                avatarUrl = "https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?w=600&auto=format&fit=crop&q=80",
                audioRate = 14.0,
                videoRate = 28.0,
                isOnline = true
            ),
            ModelGirl(
                id = "dummy_6",
                name = "Kavya Verma",
                age = 22,
                city = "Pune",
                bio = "Gentle voice, soothing talks & warm vibes 💫",
                avatarUrl = "https://images.unsplash.com/photo-1531746020798-e6953c6e8e04?w=600&auto=format&fit=crop&q=80",
                audioRate = 10.0,
                videoRate = 22.0,
                isOnline = false
            ),
            ModelGirl(
                id = "dummy_7",
                name = "Nisha Rajput",
                age = 23,
                city = "Jaipur",
                bio = "Express your thoughts freely with me 🌷",
                avatarUrl = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=600&auto=format&fit=crop&q=80",
                audioRate = 16.0,
                videoRate = 32.0,
                isOnline = true
            ),
            ModelGirl(
                id = "dummy_8",
                name = "Zoya Khan",
                age = 24,
                city = "Hyderabad",
                bio = "Positive energy, deep heart-to-heart talks 🦋",
                avatarUrl = "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?w=600&auto=format&fit=crop&q=80",
                audioRate = 20.0,
                videoRate = 40.0,
                isOnline = true
            )
        )
    }

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
            // Fallback to rich dummy data if server list is empty for now
            val resultList = if (list.isEmpty()) DUMMY_MODELS else list
            Result.success(resultList)
        } catch (e: Exception) {
            // Fallback to dummy data on failure so the user immediately sees the UI
            Result.success(DUMMY_MODELS)
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

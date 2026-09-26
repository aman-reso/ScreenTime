package com.app.screentime.feature.profile.domain.usecase

import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.dto.ProfileUpdateRequest
import com.app.screentime.core.network.session.SessionManager
import javax.inject.Inject

class UpdatePreferencesUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(request: ProfileUpdateRequest): Result<Boolean> {
        val token = sessionManager.getToken() ?: return Result.failure(Exception("Not logged in"))
        return try {
            val response = api.updateProfile(token, request)
            if (response.success) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

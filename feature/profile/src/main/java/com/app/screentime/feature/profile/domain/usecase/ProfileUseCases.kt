package com.app.screentime.feature.profile.domain.usecase

import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.dto.toUser
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
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
        return User(
            id = id,
            phone = phone,
            name = name,
            role = role
        )
    }
}

class FetchUserProfileUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(): Result<User> {
        val token = sessionManager.token ?: return Result.failure(Exception("Not logged in"))
        return try {
            val dto = api.getUserProfile(token)
            val user = dto.toUser()
            sessionManager.saveSession(token, user)
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
    private val api: ChattyApi,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke(bio: String, voiceRate: Double, chatRate: Double): Result<Boolean> {
        val token = sessionManager.token ?: return Result.failure(Exception("Not logged in"))
        return try {
            api.submitOnboarding(
                token = token,
                bio = bio,
                voiceRatePerMin = voiceRate,
                groupRatePerMin = voiceRate * 0.7,
                chatRatePerMsg = chatRate
            )
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

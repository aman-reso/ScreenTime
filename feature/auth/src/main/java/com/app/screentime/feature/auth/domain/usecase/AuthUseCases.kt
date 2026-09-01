package com.app.screentime.feature.auth.domain.usecase

import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val api: ChattyApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(phone: String, name: String, role: String): Result<User> {
        return try {
            val response = api.registerOrLogin(phone, name, role)
            val isModelRole = UserRole.fromString(response.user.role) == UserRole.MODEL
            val rawBalance = response.wallet?.balance ?: 0.0
            val defaultBalance = if (!isModelRole && rawBalance <= 0.0) 1000.0 else rawBalance

            val user = User(
                id = response.user.id,
                phone = response.user.phone,
                name = response.user.name,
                role = UserRole.fromString(response.user.role),
                avatarUrl = response.user.avatar_url,
                bio = response.user.bio,
                voiceRatePerMin = response.user.voice_rate_per_min,
                chatRatePerMsg = response.user.chat_rate_per_msg,
                isOnline = response.user.is_online,
                isBusy = response.user.is_busy,
                walletBalance = defaultBalance
            )
            sessionManager.saveSession(response.token, user)
            preferencesManager.setToken(response.token)
            preferencesManager.setUserId(user.id)
            preferencesManager.setUsername(user.name)
            preferencesManager.setPhone(user.phone)
            preferencesManager.setRole(user.role.name.lowercase())
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class RegisterUseCase @Inject constructor(
    private val loginUseCase: LoginUseCase
) {
    suspend operator fun invoke(phone: String, name: String, role: String): Result<User> {
        return loginUseCase(phone, name, role)
    }
}

class CheckAuthStatusUseCase @Inject constructor(
    private val sessionManager: SessionManager
) {
    val isLoggedInFlow: StateFlow<Boolean> = sessionManager.isLoggedInFlow

    operator fun invoke(): Boolean = sessionManager.hasToken()
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

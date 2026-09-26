package com.app.screentime.feature.auth.domain.usecase

import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(phone: String, name: String, role: String): Result<User> {
        return try {
            var token = ""
            var userId = ""
            var userName = name.ifBlank { "User" }
            var userPhone = phone

            try {
                val response = api.registerOrLogin(phone, name, role)
                token = response.token
                userId = response.user.id
                userName = response.user.name
                userPhone = response.user.phone
            } catch (_: Exception) {
                token = "dev_jwt_${System.currentTimeMillis()}"
                userId = "user_${phone.filter { it.isDigit() }.takeLast(4)}"
            }

            val user = User(
                id = userId.ifBlank { "user_demo" },
                phone = userPhone,
                name = userName,
                role = UserRole.USER,
                walletBalance = 1000.0
            )
            sessionManager.saveSession(token, user)
            preferencesManager.setToken(token)
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

/**
 * Google Sign-In authentication where backend is the overall owner.
 * Sends Google ID token & account details to the backend.
 * The backend authenticates/validates the token, creates or updates the user profile,
 * issues the app JWT token, and returns the authoritative session and user data.
 */
class LoginWithGoogleUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(
        googleId: String? = null,
        idToken: String? = null,
        email: String? = null,
        name: String? = null,
        avatarUrl: String? = null,
        role: String = "user"
    ): Result<User> {
        return try {
            val response = api.loginWithGoogle(
                googleId = googleId,
                idToken = idToken,
                email = email,
                name = name,
                photoUrl = avatarUrl,
                role = role
            )
            val userDto = response.user
            val resolvedName = userDto.getResolvedName().ifBlank { name ?: "User" }
            val user = User(
                id = userDto.id.ifBlank { googleId ?: idToken ?: "google_${System.currentTimeMillis()}" },
                phone = userDto.phone.ifBlank { email ?: "" },
                name = resolvedName,
                role = UserRole.USER,
                avatarUrl = userDto.avatar_url ?: avatarUrl,
                walletBalance = response.wallet?.balance ?: 1000.0
            )
            val isNewUser = response.is_new_user
            val onboardingDone = !isNewUser
            sessionManager.isOnboardingCompleted = onboardingDone
            preferencesManager.setOnboardingCompleted(onboardingDone)

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
        sessionManager.isOnboardingCompleted = false
        preferencesManager.setOnboardingCompleted(false)
    }
}

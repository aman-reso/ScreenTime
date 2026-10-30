package com.app.screentime.feature.auth.domain.usecase

import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val api: WinterApi,
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(phone: String): Result<String> {
        return try {
            val response = api.sendOtp(phone)
            val hint = response.data?.hint ?: ""
            Result.success(hint)
        } catch (e: Exception) {
            Result.success("")
        }
    }
}

class VerifyOtpUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(phone: String, code: String): Result<User> {
        val trimmedCode = code.trim()

        return try {
            val response = api.verifyOtp(phone, trimmedCode)
            val userDto = response.user
            val resolvedName = userDto.getResolvedName().ifBlank { "User" }
            val user = User(
                id = userDto.id.ifBlank { "user_demo" },
                phone = userDto.phone.ifBlank { phone },
                name = resolvedName,
                role = UserRole.USER,
                walletBalance = response.wallet?.balance ?: 0.0
            )

            val isProfileComplete =
                (response.profile_complete || response.has_profile || userDto.profile_complete || userDto.has_profile) && !response.is_new_user
            sessionManager.isOnboardingCompleted = isProfileComplete
            preferencesManager.setOnboardingCompleted(isProfileComplete)
            sessionManager.saveSession(response.token, user)
            preferencesManager.setToken(response.token)
            preferencesManager.setUserId(user.id)
            preferencesManager.setUsername(user.name)
            preferencesManager.setPhone(user.phone)
            preferencesManager.setRole(user.role.name.lowercase())
            preferencesManager.setPhoneVerified(true)
            Result.success(user.copy(isPhoneVerified = true))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class LoginUseCase @Inject constructor(
    private val api: WinterApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    suspend operator fun invoke(phone: String, name: String, role: String): Result<User> {
        return try {
            val response = api.registerOrLogin(phone, name, role)
            val userDto = response.user
            val user = User(
                id = userDto.id.ifBlank { "user_demo" },
                phone = userDto.phone,
                name = userDto.name,
                role = UserRole.USER,
                walletBalance = response.wallet?.balance ?: 1000.0
            )

            val isProfileComplete =
                (response.profile_complete || response.has_profile || userDto.profile_complete || userDto.has_profile) && !response.is_new_user
            sessionManager.isOnboardingCompleted = isProfileComplete
            preferencesManager.setOnboardingCompleted(isProfileComplete)

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
                id = userDto.id.ifBlank {
                    googleId ?: idToken ?: "google_${System.currentTimeMillis()}"
                },
                phone = userDto.phone.ifBlank { email ?: "" },
                name = resolvedName,
                role = UserRole.USER,
                avatarUrl = userDto.avatar_url ?: avatarUrl,
                walletBalance = response.wallet?.balance ?: 1000.0
            )

            val isProfileComplete =
                (response.profile_complete || response.has_profile || userDto.profile_complete || userDto.has_profile) && !response.is_new_user
            sessionManager.isOnboardingCompleted = isProfileComplete
            preferencesManager.setOnboardingCompleted(isProfileComplete)

            sessionManager.saveSession(response.token, user)
            preferencesManager.setToken(response.token)
            preferencesManager.setUserId(user.id)
            preferencesManager.setUsername(user.name)
            val isPhoneVerified = userDto.is_phone_verified == true || userDto.phone_verified == true
            preferencesManager.setPhoneVerified(isPhoneVerified)
            val resolvedUser = user.copy(isPhoneVerified = isPhoneVerified)
            Result.success(resolvedUser)
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
        preferencesManager.clearAll()
    }
}

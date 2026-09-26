package com.app.screentime.core.network.session

import android.content.Context
import android.content.SharedPreferences
import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: com.app.screentime.core.network.preferences.PreferencesManager
) {
    companion object {
        const val TOKEN_LIFETIME_MS = 365L * 24 * 60 * 60 * 1000L // Persistent 365-Day Session
        private const val PREFS_NAME = "chatty_session_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_EXPIRES_AT = "token_expires_at"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _isLoggedInFlow = MutableStateFlow(hasValidSession())
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow.asStateFlow()

    fun getToken(): String? {
        return preferencesManager.getToken()
    }

    fun setToken(value: String?) {
        if (!value.isNullOrEmpty()) {
            preferencesManager.setToken(value)
        } else {
            preferencesManager.clearAuth()
        }
        _isLoggedInFlow.value = !value.isNullOrEmpty()
    }

    var userId: String?
        get() = prefs.getString(KEY_USER_ID, null)
            ?: context.getSharedPreferences("screentime_prefs", Context.MODE_PRIVATE)
                .getString("user_id", null)
        set(value) = prefs.edit { putString(KEY_USER_ID, value) }

    var userName: String?
        get() = prefs.getString(KEY_USER_NAME, null)
            ?: context.getSharedPreferences("screentime_prefs", Context.MODE_PRIVATE)
                .getString("username", null)
        set(value) = prefs.edit { putString(KEY_USER_NAME, value) }

    var userPhone: String?
        get() = prefs.getString(KEY_USER_PHONE, null)
            ?: context.getSharedPreferences("screentime_prefs", Context.MODE_PRIVATE)
                .getString("phone", null)
        set(value) = prefs.edit { putString(KEY_USER_PHONE, value) }

    var userRole: UserRole
        get() {
            val roleStr = prefs.getString(KEY_USER_ROLE, null)
                ?: context.getSharedPreferences("screentime_prefs", Context.MODE_PRIVATE)
                    .getString("role", "user")
            return UserRole.fromString(roleStr ?: "user")
        }
        set(value) = prefs.edit { putString(KEY_USER_ROLE, value.name.lowercase()) }

    var tokenExpiresAt: Long
        get() = prefs.getLong(KEY_EXPIRES_AT, 0L)
        set(value) = prefs.edit { putLong(KEY_EXPIRES_AT, value) }

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean(
            KEY_ONBOARDING_COMPLETED,
            false
        ) || context.getSharedPreferences("screentime_prefs", Context.MODE_PRIVATE)
            .getBoolean("onboarding_completed", false)
        set(value) {
            prefs.edit { putBoolean(KEY_ONBOARDING_COMPLETED, value) }
            context.getSharedPreferences("screentime_prefs", Context.MODE_PRIVATE)
                .edit { putBoolean("onboarding_completed", value) }
        }

    var walletBalance: Double
        get() = Double.fromBits(
            prefs.getLong(
                "wallet_balance_bits",
                java.lang.Double.doubleToRawLongBits(1000.0)
            )
        )
        set(value) = prefs.edit {
            putLong(
                "wallet_balance_bits",
                java.lang.Double.doubleToRawLongBits(value)
            )
        }

    var currentUser: User?
        get() {
            val id = userId ?: return null
            return User(
                id = id,
                phone = userPhone ?: "",
                name = userName ?: "User",
                role = userRole,
                walletBalance = walletBalance
            )
        }
        set(value) {
            if (value != null) {
                userId = value.id
                userName = value.name
                userPhone = value.phone
                userRole = value.role
                walletBalance = value.walletBalance
            }
        }

    fun hasToken(): Boolean = !getToken().isNullOrEmpty()

    fun isTokenExpired(): Boolean {
        val expiry = tokenExpiresAt
        return expiry > 0L && System.currentTimeMillis() >= expiry
    }

    fun hasValidSession(): Boolean {
        return hasToken()
    }

    fun saveSession(token: String, user: User) {
        val expiry = System.currentTimeMillis() + TOKEN_LIFETIME_MS
        setToken(token)
        preferencesManager.setUserId(user.id)
        prefs.edit {
            putString(KEY_USER_ID, user.id)
                .putString(KEY_USER_NAME, user.name)
                .putString(KEY_USER_PHONE, user.phone)
                .putString(KEY_USER_ROLE, user.role.name.lowercase())
                .putLong(
                    "wallet_balance_bits",
                    java.lang.Double.doubleToRawLongBits(user.walletBalance)
                )
                .putLong(KEY_EXPIRES_AT, expiry)
        }
        _isLoggedInFlow.value = true
    }

    fun clearSession() {
        setToken(null)
        prefs.edit { clear() }
        _isLoggedInFlow.value = false
    }
}

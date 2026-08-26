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

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("chatty_session_prefs", Context.MODE_PRIVATE)

    private val _isLoggedInFlow = MutableStateFlow(hasToken())
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow.asStateFlow()

    var token: String?
        get() = prefs.getString("jwt_token", null)
        set(value) {
            prefs.edit().putString("jwt_token", value).apply()
            _isLoggedInFlow.value = !value.isNullOrEmpty()
        }

    var userId: String?
        get() = prefs.getString("user_id", null)
        set(value) = prefs.edit().putString("user_id", value).apply()

    var userName: String?
        get() = prefs.getString("user_name", null)
        set(value) = prefs.edit().putString("user_name", value).apply()

    var userPhone: String?
        get() = prefs.getString("user_phone", null)
        set(value) = prefs.edit().putString("user_phone", value).apply()

    var userRole: UserRole
        get() = UserRole.fromString(prefs.getString("user_role", "user") ?: "user")
        set(value) = prefs.edit().putString("user_role", value.name.lowercase()).apply()

    fun hasToken(): Boolean = !prefs.getString("jwt_token", null).isNullOrEmpty()

    fun saveSession(token: String, user: User) {
        this.token = token
        this.userId = user.id
        this.userName = user.name
        this.userPhone = user.phone
        this.userRole = user.role
        _isLoggedInFlow.value = true
    }

    fun clearSession() {
        prefs.edit().clear().apply()
        _isLoggedInFlow.value = false
    }
}

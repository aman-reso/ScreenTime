package com.app.screentime.core.network.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val PREFS_NAME = "screentime_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_PHONE = "phone"
        private const val KEY_ROLE = "role"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_BASE_URL = "custom_base_url"
        const val KEY_LANGUAGE = "language"
        const val LANGUAGE_PREF = "language_pref"
    }

    private val prefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    @Volatile
    private var cachedToken: String? = null

    @Volatile
    private var cachedUserId: String? = null

    private val authLock = Any()

    fun getUserId(): String? {
        cachedUserId?.let { return it }
        return synchronized(authLock) {
            cachedUserId ?: prefs.getString(KEY_USER_ID, null)?.also { cachedUserId = it }
        }
    }

    fun setUserId(userId: String) {
        synchronized(authLock) {
            cachedUserId = userId
            prefs.edit { putString(KEY_USER_ID, userId) }
        }
    }

    fun getToken(): String? {
        cachedToken?.let { return it }
        return synchronized(authLock) {
            cachedToken ?: prefs.getString(KEY_TOKEN, null)?.also { cachedToken = it }
        }
    }

    fun setToken(token: String) {
        synchronized(authLock) {
            cachedToken = token
            prefs.edit { putString(KEY_TOKEN, token) }
        }
    }

    fun getBaseUrl(): String? = prefs.getString(KEY_BASE_URL, null)

    fun setBaseUrl(url: String) {
        prefs.edit { putString(KEY_BASE_URL, url.trimEnd('/')) }
    }

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun setUsername(username: String) {
        prefs.edit { putString(KEY_USERNAME, username) }
    }

    fun getPhone(): String? = prefs.getString(KEY_PHONE, null)

    fun setPhone(phone: String) {
        prefs.edit { putString(KEY_PHONE, phone) }
    }

    fun getRole(): String? = prefs.getString(KEY_ROLE, null)

    fun setRole(role: String) {
        prefs.edit { putString(KEY_ROLE, role) }
    }

    fun clearAuth() {
        synchronized(authLock) {
            cachedToken = null
            cachedUserId = null
            prefs.edit {
                remove(KEY_TOKEN)
                remove(KEY_USER_ID)
                remove(KEY_USERNAME)
                remove(KEY_PHONE)
                remove(KEY_ROLE)
            }
        }
    }

    fun isFirstLaunch(): Boolean = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

    fun setFirstLaunchCompleted() {
        prefs.edit {
            putBoolean(KEY_FIRST_LAUNCH, false)
        }
    }

    fun isOnboardingCompleted(): Boolean = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    fun setOnboardingCompleted(completed: Boolean = true) {
        prefs.edit {
            putBoolean(KEY_ONBOARDING_COMPLETED, completed)
        }
    }

    fun putString(key: String, value: String) {
        prefs.edit {
            putString(key, value)
        }
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return prefs.getString(key, defaultValue)
    }

    fun remove(key: String) {
        prefs.edit {
            remove(key)
        }
    }

    fun setLanguage(language: String): Boolean {
        val regularPrefs = context.getSharedPreferences(LANGUAGE_PREF, Context.MODE_PRIVATE)
        val editor = regularPrefs.edit()
        editor.putString(KEY_LANGUAGE, language)
        val committed = editor.commit()

        try {
            prefs.edit {
                putString(KEY_LANGUAGE, language)
            }
        } catch (e: Exception) {
            // Ignore if encrypted prefs fail
        }

        return committed
    }
}


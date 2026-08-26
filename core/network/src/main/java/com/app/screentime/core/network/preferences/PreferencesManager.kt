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

    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun setUserId(userId: String) {
        prefs.edit { putString(KEY_USER_ID, userId) }
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun setToken(token: String) {
        prefs.edit { putString(KEY_TOKEN, token) }
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
        prefs.edit {
            remove(KEY_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_USERNAME)
            remove(KEY_PHONE)
            remove(KEY_ROLE)
        }
    }

    fun isFirstLaunch(): Boolean = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

    fun setFirstLaunchCompleted() {
        prefs.edit {
            putBoolean(KEY_FIRST_LAUNCH, false)
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


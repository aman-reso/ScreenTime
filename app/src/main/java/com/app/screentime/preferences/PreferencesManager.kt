package com.app.screentime.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.app.screentime.network.model.DeviceRegistrationResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "screentime_prefs"
        private const val KEY_ALIAS = "screentime_key_alias"

        private const val KEY_USER_ID = "user_id"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_IS_REGISTERED = "is_device_registered"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_CONSENT_SCREEN_SHOWN = "consent_screen_shown"
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

    fun getUserId(): String? {
        return try {
            val userRegInfoJson = prefs.getString("user_reg_info", null)
            if (userRegInfoJson != null) {
                val registrationResponse =
                    Json.decodeFromString<DeviceRegistrationResponse>(userRegInfoJson)
                registrationResponse.userId
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun isDeviceRegistered(): Boolean = prefs.getBoolean(KEY_IS_REGISTERED, false)

    /**
     * Get username from stored registration data
     */
    fun getUsername(): String? {
        return try {
            val userRegInfoJson = prefs.getString("user_reg_info", null)
            if (userRegInfoJson != null) {
                val registrationResponse =
                    Json.decodeFromString<DeviceRegistrationResponse>(userRegInfoJson)
                registrationResponse.username
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    fun clearDeviceRegistration() {
        prefs.edit {
            remove(KEY_USER_ID)
            remove(KEY_DEVICE_ID)
            remove(KEY_IS_REGISTERED)
        }
    }

    // First Launch Tracking
    fun isFirstLaunch(): Boolean = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

    fun setFirstLaunchCompleted() {
        prefs.edit {
            putBoolean(KEY_FIRST_LAUNCH, false)
        }
    }

    // Consent Screen Shown Flag
    fun isConsentScreenShown(): Boolean = prefs.getBoolean(KEY_CONSENT_SCREEN_SHOWN, false)

    fun setConsentScreenShown(shown: Boolean = true) {
        prefs.edit {
            putBoolean(KEY_CONSENT_SCREEN_SHOWN, shown)
        }
    }

    // Generic preference helpers
    fun putString(key: String, value: String) {
        prefs.edit {
            putString(key, value)
        }
    }

    fun getString(key: String, defaultValue: String = ""): String =
        prefs.getString(key, defaultValue) ?: defaultValue

    fun putInt(key: String, value: Int) {
        prefs.edit {
            putInt(key, value)
        }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int = prefs.getInt(key, defaultValue)

    fun putLong(key: String, value: Long) {
        prefs.edit {
            putLong(key, value)
        }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long = prefs.getLong(key, defaultValue)

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit {
            putBoolean(key, value)
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        prefs.getBoolean(key, defaultValue)

    fun putFloat(key: String, value: Float) {
        prefs.edit {
            putFloat(key, value)
        }
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float = prefs.getFloat(key, defaultValue)

    // Clear all preferences
    fun clearAll() {
        prefs.edit {
            clear()
        }
    }
}


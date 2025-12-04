package com.app.screentime.preferences

import android.annotation.SuppressLint
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
        private const val KEY_LAST_SYNC_TIME = "last_sync_time"
        private const val KEY_LAST_FOCUS_SYNC_TIME = "last_focus_sync_time"
        private const val USER_REG_INFO = "user_reg_info"
        private const val KEY_USAGE_STATS_PERMISSION_REQUESTED = "usage_stats_permission_requested"
        private const val KEY_USAGE_DATA_DISCLOSURE_SHOWN = "usage_data_disclosure_shown"
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
            val userRegInfoJson = prefs.getString(USER_REG_INFO, null)
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
            val userRegInfoJson = prefs.getString(USER_REG_INFO, null)
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

    fun getUserInformation(): DeviceRegistrationResponse? {
        return try {
            val userRegInfoJson = prefs.getString(USER_REG_INFO, null)
            if (userRegInfoJson != null) {
                val registrationResponse =
                    Json.decodeFromString<DeviceRegistrationResponse>(userRegInfoJson)
                registrationResponse
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get TOTP secret from stored registration data
     */
    fun getTOTPSecret(): String? {
        return try {
            val userRegInfoJson = prefs.getString(USER_REG_INFO, null)
            if (userRegInfoJson != null) {
                val registrationResponse =
                    Json.decodeFromString<DeviceRegistrationResponse>(userRegInfoJson)
                registrationResponse.totpSecret
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    fun saveUserInformation(registrationResponse: DeviceRegistrationResponse) {
        val registrationResponse =
            Json.encodeToString(registrationResponse)
        prefs.edit {
            putString(USER_REG_INFO, registrationResponse)
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

    fun getString(key: String, defaultValue: String? = null): String? {
        return prefs.getString(key, defaultValue)
    }

    fun remove(key: String) {
        prefs.edit {
            remove(key)
        }
    }

    // Last Sync Time Management
    fun getLastSyncTime(): Long = prefs.getLong(KEY_LAST_SYNC_TIME, 0L)

    fun setLastSyncTime(timestamp: Long) {
        prefs.edit {
            putLong(KEY_LAST_SYNC_TIME, timestamp)
        }
    }

    // Last Focus Sync Time Management
    fun getLastFocusSyncTime(): Long? {
        val time = prefs.getLong(KEY_LAST_FOCUS_SYNC_TIME, 0L)
        return if (time > 0L) time else null
    }

    fun setLastFocusSyncTime(timestamp: Long) {
        prefs.edit {
            putLong(KEY_LAST_FOCUS_SYNC_TIME, timestamp)
        }
    }
    // Usage Stats Permission Tracking
    fun isUsageStatsPermissionRequested(): Boolean = prefs.getBoolean(KEY_USAGE_STATS_PERMISSION_REQUESTED, false)

    fun setUsageStatsPermissionRequested(requested: Boolean = true) {
        prefs.edit {
            putBoolean(KEY_USAGE_STATS_PERMISSION_REQUESTED, requested)
        }
    }
    
    // Usage Data Disclosure Tracking (Google Play Compliance)
    fun isUsageDataDisclosureShown(): Boolean = prefs.getBoolean(KEY_USAGE_DATA_DISCLOSURE_SHOWN, false)
    
    fun setUsageDataDisclosureShown(shown: Boolean = true) {
        prefs.edit {
            putBoolean(KEY_USAGE_DATA_DISCLOSURE_SHOWN, shown)
        }
    }
}


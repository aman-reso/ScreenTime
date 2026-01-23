package com.app.screentime.core.network.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.app.screentime.core.network.model.DeviceRegistrationResponse
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
        private const val KEY_USER_ID = "user_id"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_IS_REGISTERED = "is_device_registered"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_CONSENT_SCREEN_SHOWN = "consent_screen_shown"
        private const val KEY_LAST_SYNC_TIME = "last_sync_time"
        private const val KEY_LAST_FOCUS_SYNC_TIME = "last_focus_sync_time"
        private const val KEY_LAST_LOCATION_SYNC_TIME = "last_location_sync_time"
        private const val USER_REG_INFO = "user_reg_info"
        private const val KEY_USAGE_STATS_PERMISSION_REQUESTED = "usage_stats_permission_requested"
        private const val KEY_USAGE_DATA_DISCLOSURE_SHOWN = "usage_data_disclosure_shown"
        private const val KEY_SAVED_CLAIM_DETAILS = "saved_claim_details"
        private const val KEY_DAILY_GOAL_HOURS = "daily_goal_hours"
        private const val KEY_ADS_APP_VERSION_REMOTE_CONFIG = "ads_app_version_remote_config"
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

    fun saveUserInformation(registrationResponse: DeviceRegistrationResponse) {
        val jsonString = Json.encodeToString(DeviceRegistrationResponse.serializer(), registrationResponse)
        prefs.edit {
            putString(USER_REG_INFO, jsonString)
        }
    }

    fun clearDeviceRegistration() {
        prefs.edit {
            remove(KEY_USER_ID)
            remove(KEY_DEVICE_ID)
            remove(KEY_IS_REGISTERED)
        }
    }

    fun isFirstLaunch(): Boolean = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

    fun setFirstLaunchCompleted() {
        prefs.edit {
            putBoolean(KEY_FIRST_LAUNCH, false)
        }
    }

    fun isConsentScreenShown(): Boolean = prefs.getBoolean(KEY_CONSENT_SCREEN_SHOWN, false)

    fun setConsentScreenShown(shown: Boolean = true) {
        prefs.edit {
            putBoolean(KEY_CONSENT_SCREEN_SHOWN, shown)
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

    fun getLastSyncTime(): Long = prefs.getLong(KEY_LAST_SYNC_TIME, 0L)

    fun setLastSyncTime(timestamp: Long) {
        prefs.edit {
            putLong(KEY_LAST_SYNC_TIME, timestamp)
        }
    }

    fun getLastFocusSyncTime(): Long? {
        val time = prefs.getLong(KEY_LAST_FOCUS_SYNC_TIME, 0L)
        return if (time > 0L) time else null
    }

    fun setLastFocusSyncTime(timestamp: Long) {
        prefs.edit {
            putLong(KEY_LAST_FOCUS_SYNC_TIME, timestamp)
        }
    }

    fun getLastLocationSyncTime(): Long = prefs.getLong(KEY_LAST_LOCATION_SYNC_TIME, 0L)

    fun setLastLocationSyncTime(timestamp: Long) {
        prefs.edit {
            putLong(KEY_LAST_LOCATION_SYNC_TIME, timestamp)
        }
    }

    fun isUsageStatsPermissionRequested(): Boolean = prefs.getBoolean(KEY_USAGE_STATS_PERMISSION_REQUESTED, false)

    fun setUsageStatsPermissionRequested(requested: Boolean = true) {
        prefs.edit {
            putBoolean(KEY_USAGE_STATS_PERMISSION_REQUESTED, requested)
        }
    }
    
    fun isUsageDataDisclosureShown(): Boolean = prefs.getBoolean(KEY_USAGE_DATA_DISCLOSURE_SHOWN, false)
    
    fun setUsageDataDisclosureShown(shown: Boolean = true) {
        prefs.edit {
            putBoolean(KEY_USAGE_DATA_DISCLOSURE_SHOWN, shown)
        }
    }

    fun getTOTPSecret(): String? {
        return try {
            val userRegInfoJson = prefs.getString(USER_REG_INFO, null)
            if (userRegInfoJson != null) {
                val registrationResponse = Json.decodeFromString<DeviceRegistrationResponse>(userRegInfoJson)
                registrationResponse.totpSecret
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun isTOTPEnabled(): Boolean {
        return try {
            val userRegInfoJson = prefs.getString(USER_REG_INFO, null)
            if (userRegInfoJson != null) {
                val registrationResponse = Json.decodeFromString<DeviceRegistrationResponse>(userRegInfoJson)
                registrationResponse.totpEnabled
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Generic method to save serializable data
     */
    fun <T> saveSerializable(key: String, data: T, serializer: kotlinx.serialization.KSerializer<T>) {
        try {
            val jsonString = Json.encodeToString(serializer, data)
            prefs.edit {
                putString(key, jsonString)
            }
        } catch (e: Exception) {
            // Handle serialization error
        }
    }

    /**
     * Generic method to get serializable data
     */
    fun <T> getSerializable(key: String, serializer: kotlinx.serialization.KSerializer<T>): T? {
        return try {
            val jsonString = prefs.getString(key, null)
            if (jsonString != null) {
                Json.decodeFromString(serializer, jsonString)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get daily goal in hours (default: 6 hours)
     */
    fun getDailyGoalHours(): Int {
        return prefs.getInt(KEY_DAILY_GOAL_HOURS, 6)
    }

    /**
     * Save daily goal in hours
     */
    fun setDailyGoalHours(hours: Int) {
        prefs.edit {
            putInt(KEY_DAILY_GOAL_HOURS, hours)
        }
    }

    /**
     * Get ads app version from RemoteConfig
     * Returns null if not set
     */
    fun getAdsAppVersionRemoteConfig(): String? {
        return prefs.getString(KEY_ADS_APP_VERSION_REMOTE_CONFIG, null)
    }

    /**
     * Set ads app version from RemoteConfig
     * Set to null to remove
     */
    fun setAdsAppVersionRemoteConfig(version: String?) {
        prefs.edit {
            if (version != null) {
                putString(KEY_ADS_APP_VERSION_REMOTE_CONFIG, version)
            } else {
                remove(KEY_ADS_APP_VERSION_REMOTE_CONFIG)
            }
        }
    }


    /**
     * Set language preference synchronously (uses commit() for immediate write)
     * This is needed for attachBaseContext to read the language before activity recreation
     * Uses regular SharedPreferences (not encrypted) for synchronous commit support
     * @return true if the language was successfully saved, false otherwise
     */
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
            // Ignore if encrypted prefs fail, regular prefs is the source of truth for language
        }
        
        return committed
    }
}

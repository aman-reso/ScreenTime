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
        private const val KEY_API_KEY = "firebase_remote_config_api_key"
        const val DEFAULT_API_KEY = "nusratfatehalikhan"
        private const val KEY_USER_LATITUDE = "user_latitude"
        private const val KEY_USER_LONGITUDE = "user_longitude"
        private const val KEY_PHONE_VERIFIED = "is_phone_verified"
        private const val KEY_USER_CITY = "user_city"

        // Ad configuration keys
        private const val KEY_ADS_ENABLED = "ads_enabled"
        private const val KEY_SWIPE_AD_ENABLED = "swipe_ad_enabled"
        private const val KEY_MESSAGE_AD_ENABLED = "message_ad_enabled"
        private const val KEY_SWIPE_AD_INTERVAL = "swipe_ad_interval"
        private const val KEY_SWIPE_AD_MIN_INTERVAL = "swipe_ad_min_interval"
        private const val KEY_SWIPE_AD_MAX_INTERVAL = "swipe_ad_max_interval"
        private const val KEY_MESSAGE_AD_INTERVAL = "message_ad_interval"

        // App Version & Update configuration keys
        private const val KEY_MIN_VERSION = "min_version"
        private const val KEY_LATEST_VERSION = "latest_version"
        private const val KEY_FORCE_UPDATE = "force_update"
        private const val KEY_UPDATE_URL = "update_url"
        private const val KEY_APP_CONFIG_JSON = "app_config_json"
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

    fun getApiKey(): String {
        return prefs.getString(KEY_API_KEY, null)?.takeIf { it.isNotBlank() } ?: DEFAULT_API_KEY
    }

    fun setApiKey(apiKey: String) {
        if (apiKey.isNotBlank()) {
            prefs.edit { putString(KEY_API_KEY, apiKey) }
        }
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
                remove(KEY_ONBOARDING_COMPLETED)
            }
        }
    }

    fun clearAll() {
        synchronized(authLock) {
            cachedToken = null
            cachedUserId = null
            try {
                prefs.edit { clear() }
            } catch (e: Exception) {
                // Ignore
            }
            try {
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit { clear() }
            } catch (e: Exception) {
                // Ignore
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

    fun getUserLatitude(): Double? {
        val lat = prefs.getString(KEY_USER_LATITUDE, null)
        return lat?.toDoubleOrNull()
    }

    fun getUserLongitude(): Double? {
        val lng = prefs.getString(KEY_USER_LONGITUDE, null)
        return lng?.toDoubleOrNull()
    }

    fun setUserLocation(lat: Double, lng: Double) {
        prefs.edit {
            putString(KEY_USER_LATITUDE, lat.toString())
            putString(KEY_USER_LONGITUDE, lng.toString())
        }
    }

    fun isPhoneVerified(): Boolean = prefs.getBoolean(KEY_PHONE_VERIFIED, false)

    fun setPhoneVerified(verified: Boolean) {
        prefs.edit { putBoolean(KEY_PHONE_VERIFIED, verified) }
    }

    fun getUserCity(): String? = prefs.getString(KEY_USER_CITY, null)

    fun setUserCity(city: String) {
        prefs.edit { putString(KEY_USER_CITY, city) }
    }


    fun getAdsEnabled(): Boolean = prefs.getBoolean(KEY_ADS_ENABLED, true)
    fun setAdsEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_ADS_ENABLED, enabled) }
    }

    fun getSwipeAdEnabled(): Boolean = prefs.getBoolean(KEY_SWIPE_AD_ENABLED, true)
    fun setSwipeAdEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_SWIPE_AD_ENABLED, enabled) }
    }

    fun getMessageAdEnabled(): Boolean = prefs.getBoolean(KEY_MESSAGE_AD_ENABLED, true)
    fun setMessageAdEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_MESSAGE_AD_ENABLED, enabled) }
    }

    fun getSwipeAdInterval(): Int = prefs.getInt(KEY_SWIPE_AD_INTERVAL, 10)
    fun setSwipeAdInterval(interval: Int) {
        prefs.edit { putInt(KEY_SWIPE_AD_INTERVAL, interval) }
    }

    fun getSwipeAdMinInterval(): Int = prefs.getInt(KEY_SWIPE_AD_MIN_INTERVAL, 10)
    fun setSwipeAdMinInterval(interval: Int) {
        prefs.edit { putInt(KEY_SWIPE_AD_MIN_INTERVAL, interval) }
    }

    fun getSwipeAdMaxInterval(): Int = prefs.getInt(KEY_SWIPE_AD_MAX_INTERVAL, 10)
    fun setSwipeAdMaxInterval(interval: Int) {
        prefs.edit { putInt(KEY_SWIPE_AD_MAX_INTERVAL, interval) }
    }

    fun getMessageAdInterval(): Int = prefs.getInt(KEY_MESSAGE_AD_INTERVAL, 5)
    fun setMessageAdInterval(interval: Int) {
        prefs.edit { putInt(KEY_MESSAGE_AD_INTERVAL, interval) }
    }

    fun getMinVersion(): String? = prefs.getString(KEY_MIN_VERSION, null)
    fun setMinVersion(version: String?) {
        prefs.edit { putString(KEY_MIN_VERSION, version) }
    }

    fun getLatestVersion(): String? = prefs.getString(KEY_LATEST_VERSION, null)
    fun setLatestVersion(version: String?) {
        prefs.edit { putString(KEY_LATEST_VERSION, version) }
    }

    fun getForceUpdate(): Boolean = prefs.getBoolean(KEY_FORCE_UPDATE, false)
    fun setForceUpdate(force: Boolean) {
        prefs.edit { putBoolean(KEY_FORCE_UPDATE, force) }
    }

    fun getUpdateUrl(): String? = prefs.getString(KEY_UPDATE_URL, null)
    fun setUpdateUrl(url: String?) {
        prefs.edit { putString(KEY_UPDATE_URL, url) }
    }

    fun getAppConfigJson(): String? = prefs.getString(KEY_APP_CONFIG_JSON, null)
    fun setAppConfigJson(json: String?) {
        prefs.edit { putString(KEY_APP_CONFIG_JSON, json) }
    }
}


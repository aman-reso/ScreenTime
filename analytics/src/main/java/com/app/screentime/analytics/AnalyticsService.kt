package com.app.screentime.analytics

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import com.app.screentime.core.network.preferences.PreferencesManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Analytics service for tracking user events and screen views using Firebase Analytics
 *
 * This service provides a clean interface for tracking analytics events throughout the app.
 * All events are sent to Firebase Analytics for analysis and reporting.
 * Common parameters (app_version, country, username) are automatically added to all events.
 */
@Singleton
class AnalyticsService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    init {
        firebaseAnalytics.setAnalyticsCollectionEnabled(true)
    }
    
    companion object {
        private const val TAG = "FirebaseAnalytics"
        private const val DEBUG = false // Set to true for debug builds
    }

    /**
     * Get app version from package manager
     */
    private fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager?.getPackageInfo(context.packageName, 0)
            packageInfo?.versionName ?: "unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "unknown"
        }
    }

    /**
     * Get country code from device
     * Tries TelephonyManager first, falls back to Locale
     */
    private fun getCountryCode(): String {
        return try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager?
            val simCountryCode = telephonyManager?.simCountryIso?.uppercase()
            val networkCountryCode = telephonyManager?.networkCountryIso?.uppercase()
            val countryCode = simCountryCode ?: networkCountryCode
            if (!countryCode.isNullOrBlank()) {
                countryCode
            } else {
                Locale.getDefault().country.uppercase().takeIf { it.isNotBlank() } ?: "IN"
            }
        } catch (e: Exception) {
            Locale.getDefault().country.uppercase().takeIf { it.isNotBlank() } ?: "IN"
        }
    }

    /**
     * Get common parameters that should be included in all events
     */
    private fun getCommonParameters(): Map<String, String> {
        val params = mutableMapOf<String, String>()
        
        // App version
        params[AnalyticsConstants.PARAM_APP_VERSION] = getAppVersion()
        
        // Country code
        params[AnalyticsConstants.PARAM_COUNTRY] = getCountryCode()
        
        // Username (if available)
        preferencesManager.getUsername()?.let { username ->
            params[AnalyticsConstants.PARAM_USERNAME] = username
        }
        
        return params
    }

    /**
     * Track a screen view
     * Uses Firebase Analytics standard screen_view event
     * Automatically includes common parameters (app_version, country, username)
     *
     * @param screenName The name of the screen being viewed
     */
    fun trackScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
            
            // Add common parameters
            getCommonParameters().forEach { (key, value) ->
                putString(key, value)
            }
        }
        
        // Log to logcat in debug builds
        if (DEBUG) {
            val paramsStr = bundle.keySet().joinToString(", ") { key ->
                "$key=${bundle.get(key)}"
            }
            Log.d(TAG, "📊 Screen View: $screenName | Params: $paramsStr")
        }
        
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    /**
     * Track a custom event
     * Automatically includes common parameters (app_version, country, username)
     *
     * @param eventName The name of the event (should follow Firebase naming conventions:
     *                  lowercase, underscores, max 40 characters)
     * @param parameters Optional map of event parameters (keys should be lowercase,
     *                   underscores, max 40 characters)
     */
    fun trackEvent(eventName: String, parameters: Map<String, String> = emptyMap()) {
        val bundle = Bundle().apply {
            // Add common parameters first (always included)
            getCommonParameters().forEach { (key, value) ->
                putString(key, value)
            }
            
            // Add custom parameters (can override common parameters if same key)
            parameters.forEach { (key, value) ->
                putString(key, value)
            }
        }
        
        // Log to logcat in debug builds
        if (DEBUG) {
            val paramsStr = bundle.keySet().joinToString(", ") { key ->
                "$key=${bundle.get(key)}"
            }
            Log.d(TAG, "📊 Event: $eventName | Params: $paramsStr")
        }
        
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    /**
     * Set a user property
     * User properties are attributes that describe segments of your user base
     *
     * @param name The name of the user property (max 24 characters, alphanumeric + underscore)
     * @param value The value of the user property (max 36 characters)
     */
    fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }

    /**
     * Set user ID for analytics
     *
     * @param userId The user ID to set (null to clear)
     */
    fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }
}








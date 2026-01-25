package com.app.screentime.applock.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing app lock data (locked apps, PIN, etc.)
 */
@Singleton
class AppLockRepository @Inject constructor(
    context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "app_lock_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_LOCKED_APPS = "locked_apps"
        private const val KEY_PIN = "pin"
        private const val KEY_PIN_SET = "pin_set"
        private const val KEY_PATTERN = "pattern"
        private const val KEY_PATTERN_SET = "pattern_set"
        private const val KEY_LOCK_TYPE = "lock_type" // "pin" or "pattern"
        private const val KEY_UNLOCK_TIME_DURATION = "unlock_time_duration"
        private const val KEY_APP_LIMITS = "app_limits" // packageName:limitMinutes format
        private const val KEY_APP_LAUNCH_LIMITS = "app_launch_limits" // packageName:launchCount format
        private const val KEY_APP_DAILY_USAGE = "app_daily_usage" // packageName:usageMinutes:date format
        private const val KEY_APP_DAILY_LAUNCHES = "app_daily_launches" // packageName:launches:date format
        private const val DELIMITER = ","
        private const val ENTRY_DELIMITER = ":"
    }
    
    /**
     * Lock type enum
     */
    enum class LockType {
        PIN, PATTERN
    }

    /**
     * Get all locked app package names
     */
    fun getLockedApps(): Set<String> {
        val lockedAppsString = sharedPreferences.getString(KEY_LOCKED_APPS, "") ?: ""
        return if (lockedAppsString.isEmpty()) {
            emptySet()
        } else {
            lockedAppsString.split(DELIMITER).toSet()
        }
    }

    /**
     * Add an app to locked apps list
     */
    fun addLockedApp(packageName: String) {
        val lockedApps = getLockedApps().toMutableSet()
        lockedApps.add(packageName)
        saveLockedApps(lockedApps)
    }

    /**
     * Remove an app from locked apps list
     */
    fun removeLockedApp(packageName: String) {
        val lockedApps = getLockedApps().toMutableSet()
        lockedApps.remove(packageName)
        saveLockedApps(lockedApps)
    }

    /**
     * Check if an app is locked
     */
    fun isAppLocked(packageName: String): Boolean {
        return getLockedApps().contains(packageName)
    }

    /**
     * Save locked apps set
     */
    private fun saveLockedApps(apps: Set<String>) {
        sharedPreferences.edit()
            .putString(KEY_LOCKED_APPS, apps.joinToString(DELIMITER))
            .apply()
    }

    /**
     * Set PIN for app lock
     */
    fun setPin(pin: String) {
        sharedPreferences.edit()
            .putString(KEY_PIN, pin)
            .putBoolean(KEY_PIN_SET, true)
            .apply()
    }

    /**
     * Get stored PIN
     */
    fun getPin(): String? {
        return sharedPreferences.getString(KEY_PIN, null)
    }

    /**
     * Validate PIN
     */
    fun validatePin(pin: String): Boolean {
        val storedPin = getPin()
        return storedPin != null && storedPin == pin
    }

    /**
     * Check if PIN is set
     */
    fun isPinSet(): Boolean {
        return sharedPreferences.getBoolean(KEY_PIN_SET, false)
    }

    /**
     * Clear PIN
     */
    fun clearPin() {
        sharedPreferences.edit()
            .remove(KEY_PIN)
            .putBoolean(KEY_PIN_SET, false)
            .apply()
    }
    
    /**
     * Set pattern for app lock
     * Pattern is stored as a comma-separated string of dot indices (e.g., "1,2,5,8")
     */
    fun setPattern(pattern: String) {
        sharedPreferences.edit()
            .putString(KEY_PATTERN, pattern)
            .putBoolean(KEY_PATTERN_SET, true)
            .apply()
    }
    
    /**
     * Get stored pattern
     */
    fun getPattern(): String? {
        return sharedPreferences.getString(KEY_PATTERN, null)
    }
    
    /**
     * Validate pattern
     */
    fun validatePattern(pattern: String): Boolean {
        val storedPattern = getPattern()
        return storedPattern != null && storedPattern == pattern
    }
    
    /**
     * Check if pattern is set
     */
    fun isPatternSet(): Boolean {
        return sharedPreferences.getBoolean(KEY_PATTERN_SET, false)
    }
    
    /**
     * Clear pattern
     */
    fun clearPattern() {
        sharedPreferences.edit()
            .remove(KEY_PATTERN)
            .putBoolean(KEY_PATTERN_SET, false)
            .apply()
    }
    
    /**
     * Get current lock type (PIN or PATTERN)
     */
    fun getLockType(): LockType {
        val lockTypeString = sharedPreferences.getString(KEY_LOCK_TYPE, "pin") ?: "pin"
        return try {
            LockType.valueOf(lockTypeString.uppercase())
        } catch (e: Exception) {
            LockType.PIN // Default to PIN
        }
    }
    
    /**
     * Set lock type (PIN or PATTERN)
     */
    fun setLockType(lockType: LockType) {
        sharedPreferences.edit()
            .putString(KEY_LOCK_TYPE, lockType.name.lowercase())
            .apply()
    }
    
    /**
     * Check if any lock method is set (PIN or Pattern)
     */
    fun isLockSet(): Boolean {
        return isPinSet() || isPatternSet()
    }

    /**
     * Get unlock time duration in minutes (grace period after unlocking)
     * Returns 0 if no grace period is set
     */
    fun getUnlockTimeDuration(): Int {
        return sharedPreferences.getInt(KEY_UNLOCK_TIME_DURATION, 0)
    }

    /**
     * Set unlock time duration in minutes (grace period after unlocking)
     * Set to 0 to disable grace period
     */
    fun setUnlockTimeDuration(minutes: Int) {
        sharedPreferences.edit()
            .putInt(KEY_UNLOCK_TIME_DURATION, minutes)
            .apply()
    }

    // ==================== App Time Limits ====================

    /**
     * Data class for app limit
     */
    data class AppLimit(
        val packageName: String,
        val limitMinutes: Int
    )

    /**
     * Data class for app launch limit
     */
    data class AppLaunchLimit(
        val packageName: String,
        val maxLaunches: Int
    )

    /**
     * Set time limit for an app (in minutes)
     */
    fun setAppTimeLimit(packageName: String, limitMinutes: Int) {
        val limits = getAppTimeLimits().toMutableMap()
        if (limitMinutes > 0) {
            limits[packageName] = limitMinutes
        } else {
            limits.remove(packageName)
        }
        saveAppTimeLimits(limits)
    }

    /**
     * Get time limit for a specific app (in minutes)
     * Returns null if no limit is set
     */
    fun getAppTimeLimit(packageName: String): Int? {
        return getAppTimeLimits()[packageName]
    }

    /**
     * Get all app time limits
     */
    fun getAppTimeLimits(): Map<String, Int> {
        val limitsString = sharedPreferences.getString(KEY_APP_LIMITS, "") ?: ""
        if (limitsString.isEmpty()) return emptyMap()
        
        return limitsString.split(DELIMITER)
            .filter { it.contains(ENTRY_DELIMITER) }
            .associate { entry ->
                val parts = entry.split(ENTRY_DELIMITER)
                parts[0] to (parts.getOrNull(1)?.toIntOrNull() ?: 0)
            }
            .filter { it.value > 0 }
    }

    /**
     * Remove time limit for an app
     */
    fun removeAppTimeLimit(packageName: String) {
        setAppTimeLimit(packageName, 0)
    }

    /**
     * Save app time limits
     */
    private fun saveAppTimeLimits(limits: Map<String, Int>) {
        val limitsString = limits.entries
            .filter { it.value > 0 }
            .joinToString(DELIMITER) { "${it.key}$ENTRY_DELIMITER${it.value}" }
        sharedPreferences.edit()
            .putString(KEY_APP_LIMITS, limitsString)
            .apply()
    }

    /**
     * Check if any app has a time limit set
     */
    fun hasAnyAppTimeLimit(): Boolean {
        return getAppTimeLimits().isNotEmpty()
    }

    // ==================== App Launch Limits ====================

    /**
     * Set launch limit for an app
     */
    fun setAppLaunchLimit(packageName: String, maxLaunches: Int) {
        val limits = getAppLaunchLimits().toMutableMap()
        if (maxLaunches > 0) {
            limits[packageName] = maxLaunches
        } else {
            limits.remove(packageName)
        }
        saveAppLaunchLimits(limits)
    }

    /**
     * Get launch limit for a specific app
     * Returns null if no limit is set
     */
    fun getAppLaunchLimit(packageName: String): Int? {
        return getAppLaunchLimits()[packageName]
    }

    /**
     * Get all app launch limits
     */
    fun getAppLaunchLimits(): Map<String, Int> {
        val limitsString = sharedPreferences.getString(KEY_APP_LAUNCH_LIMITS, "") ?: ""
        if (limitsString.isEmpty()) return emptyMap()
        
        return limitsString.split(DELIMITER)
            .filter { it.contains(ENTRY_DELIMITER) }
            .associate { entry ->
                val parts = entry.split(ENTRY_DELIMITER)
                parts[0] to (parts.getOrNull(1)?.toIntOrNull() ?: 0)
            }
            .filter { it.value > 0 }
    }

    /**
     * Remove launch limit for an app
     */
    fun removeAppLaunchLimit(packageName: String) {
        setAppLaunchLimit(packageName, 0)
    }

    /**
     * Save app launch limits
     */
    private fun saveAppLaunchLimits(limits: Map<String, Int>) {
        val limitsString = limits.entries
            .filter { it.value > 0 }
            .joinToString(DELIMITER) { "${it.key}$ENTRY_DELIMITER${it.value}" }
        sharedPreferences.edit()
            .putString(KEY_APP_LAUNCH_LIMITS, limitsString)
            .apply()
    }

    /**
     * Check if any app has a launch limit set
     */
    fun hasAnyAppLaunchLimit(): Boolean {
        return getAppLaunchLimits().isNotEmpty()
    }

    // ==================== Daily Launch Tracking ====================

    /**
     * Get today's date as string (YYYY-MM-DD)
     */
    private fun getTodayDateString(): String {
        val calendar = java.util.Calendar.getInstance()
        return "${calendar.get(java.util.Calendar.YEAR)}-${calendar.get(java.util.Calendar.MONTH) + 1}-${calendar.get(java.util.Calendar.DAY_OF_MONTH)}"
    }

    /**
     * Get daily launch count for an app
     */
    fun getDailyLaunchCount(packageName: String): Int {
        val today = getTodayDateString()
        val key = "launch_count_${packageName}_$today"
        return sharedPreferences.getInt(key, 0)
    }

    /**
     * Increment and get daily launch count for an app
     * Returns the new count after incrementing
     */
    fun incrementDailyLaunchCount(packageName: String): Int {
        val today = getTodayDateString()
        val key = "launch_count_${packageName}_$today"
        val currentCount = sharedPreferences.getInt(key, 0)
        val newCount = currentCount + 1
        sharedPreferences.edit()
            .putInt(key, newCount)
            .apply()
        return newCount
    }

    /**
     * Check if app has exceeded launch limit
     * Returns true if limit exceeded, false otherwise
     */
    fun hasExceededLaunchLimit(packageName: String): Boolean {
        val limit = getAppLaunchLimit(packageName) ?: return false
        val currentCount = getDailyLaunchCount(packageName)
        return currentCount >= limit
    }

    /**
     * Reset daily launch count for an app (for testing)
     */
    fun resetDailyLaunchCount(packageName: String) {
        val today = getTodayDateString()
        val key = "launch_count_${packageName}_$today"
        sharedPreferences.edit()
            .putInt(key, 0)
            .apply()
    }

    // ==================== Check if service should run ====================

    /**
     * Check if the monitoring service should run
     * (either locked apps exist OR any app limits are set)
     */
    fun shouldServiceRun(): Boolean {
        return getLockedApps().isNotEmpty() || hasAnyAppTimeLimit() || hasAnyAppLaunchLimit()
    }
}


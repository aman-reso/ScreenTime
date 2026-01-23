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
        private const val DELIMITER = ","
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
}


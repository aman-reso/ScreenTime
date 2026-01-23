package com.app.screentime.applock.manager

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * In-memory manager for app lock state
 */
object AppLockManager {
    // Track which apps are currently unlocked (temporary unlock state)
    val appUnlockTimes = ConcurrentHashMap<String, Long>()
    
    // Track if lock screen is currently shown
    val isLockScreenShown = AtomicBoolean(false)
    
    /**
     * Unlock an app (temporary unlock)
     */
    fun unlockApp(packageName: String) {
        appUnlockTimes[packageName] = System.currentTimeMillis()
        isLockScreenShown.set(false)
    }
    
    /**
     * Lock an app (clear temporary unlock)
     */
    fun lockApp(packageName: String) {
        appUnlockTimes.remove(packageName)
    }
    
    /**
     * Check if app is temporarily unlocked
     */
    fun isAppUnlocked(packageName: String): Boolean {
        return appUnlockTimes.containsKey(packageName)
    }
    
    /**
     * Clear all unlock states
     */
    fun clearAllUnlocks() {
        appUnlockTimes.clear()
        isLockScreenShown.set(false)
    }
}


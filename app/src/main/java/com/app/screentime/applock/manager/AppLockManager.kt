package com.app.screentime.applock.manager

import java.util.concurrent.ConcurrentHashMap

/**
 * Manages the state of locked apps in memory.
 * Tracks which apps are currently locked and require PIN verification.
 */
object AppLockManager {
    private val lockedApps = ConcurrentHashMap<String, Boolean>() // packageName to isLocked

    /**
     * Locks an app, requiring PIN verification to unlock.
     */
    fun lockApp(packageName: String) {
        lockedApps[packageName] = true
    }

    /**
     * Unlocks an app temporarily (until next app launch).
     */
    fun unlockApp(packageName: String) {
        lockedApps[packageName] = false
    }

    /**
     * Returns a set of package names that are currently locked.
     */
    fun getLockedApps(): Set<String> {
        return lockedApps.filter { it.value }.keys
    }

    /**
     * Checks if a specific app is currently locked.
     */
    fun isAppLocked(packageName: String): Boolean {
        return lockedApps[packageName] == true
    }

    /**
     * Removes an app from the lock list (when user removes lock).
     */
    fun removeAppLock(packageName: String) {
        lockedApps.remove(packageName)
    }

    /**
     * Clears all app locks.
     */
    fun clearAllLocks() {
        lockedApps.clear()
    }
}


package com.app.screentime.blocking.manager

import java.util.concurrent.ConcurrentHashMap

/**
 * Manages the state of blocked apps in memory.
 * Note: This is an in-memory solution and will not persist if the app is killed.
 */
object AppBlockManager {
    private val blockedApps = ConcurrentHashMap<String, Long>() // packageName to blockedUntil timestamp

    /**
     * Blocks an app for a specified duration.
     */
    fun blockApp(packageName: String, durationMillis: Long) {
        val blockedUntil = System.currentTimeMillis() + durationMillis
        blockedApps[packageName] = blockedUntil
    }

    /**
     * Unblocks a previously blocked app.
     */
    fun unblockApp(packageName: String) {
        blockedApps.remove(packageName)
    }

    /**
     * Returns a set of package names that are currently blocked.
     */
    fun getBlockedApps(): Set<String> {
        // Clean up any expired blocks before returning the list
        val now = System.currentTimeMillis()
        val expiredApps = blockedApps.filter { it.value < now }.keys
        expiredApps.forEach { blockedApps.remove(it) }
        return blockedApps.keys
    }

    /**
     * Checks if a specific app is currently blocked.
     */
    fun isAppBlocked(packageName: String): Boolean {
        val blockedUntil = blockedApps[packageName] ?: return false
        return if (System.currentTimeMillis() < blockedUntil) {
            true
        } else {
            // The block has expired, so remove it
            unblockApp(packageName)
            false
        }
    }
}


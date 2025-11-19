package com.app.screentime.service

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

/**
 * Manager for tracking and storing blocked sites in SharedPreferences
 */
object BlockedSitesManager {
    private const val PREFS_NAME = "blocked_sites_prefs"
    private const val KEY_BLOCKED_SITES = "blocked_sites"
    private const val KEY_BLOCKED_COUNT = "blocked_count"
    
    /**
     * Get SharedPreferences instance
     */
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * Add a blocked site to the list
     */
    fun addBlockedSite(context: Context, site: String) {
        val sites = getBlockedSites(context).toMutableSet()
        val normalizedSite = site.lowercase().trim()
        if (normalizedSite.isNotEmpty() && sites.add(normalizedSite)) {
            saveBlockedSites(context, sites)
        }
    }
    
    /**
     * Get all blocked sites
     */
    fun getBlockedSites(context: Context): Set<String> {
        val prefs = getPrefs(context)
        val sitesJson = prefs.getString(KEY_BLOCKED_SITES, null)
        return if (sitesJson != null) {
            try {
                val jsonArray = JSONArray(sitesJson)
                val sites = mutableSetOf<String>()
                for (i in 0 until jsonArray.length()) {
                    sites.add(jsonArray.getString(i))
                }
                sites
            } catch (e: Exception) {
                emptySet()
            }
        } else {
            emptySet()
        }
    }
    
    /**
     * Get count of blocked sites
     */
    fun getBlockedSitesCount(context: Context): Int {
        return getBlockedSites(context).size
    }
    
    /**
     * Clear all blocked sites
     */
    fun clearBlockedSites(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().clear().apply()
    }
    
    /**
     * Remove a specific blocked site
     */
    fun removeBlockedSite(context: Context, site: String) {
        val sites = getBlockedSites(context).toMutableSet()
        val normalizedSite = site.lowercase().trim()
        if (sites.remove(normalizedSite)) {
            saveBlockedSites(context, sites)
        }
    }
    
    /**
     * Save blocked sites to SharedPreferences
     */
    private fun saveBlockedSites(context: Context, sites: Set<String>) {
        val prefs = getPrefs(context)
        val jsonArray = JSONArray()
        sites.forEach { jsonArray.put(it) }
        prefs.edit()
            .putString(KEY_BLOCKED_SITES, jsonArray.toString())
            .putInt(KEY_BLOCKED_COUNT, sites.size)
            .apply()
    }
}


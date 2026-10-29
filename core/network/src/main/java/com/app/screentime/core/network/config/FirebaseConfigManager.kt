package com.app.screentime.core.network.config

import android.content.Context
import com.app.screentime.core.network.preferences.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Backwards-compatible wrapper delegating to AppConfigManager.
 * Firebase Remote Config has been removed in favor of the custom Backend Config API.
 */
@Singleton
class FirebaseConfigManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
    private val appConfigManager: AppConfigManager
) {
    var onConfigUpdated: ((FirebaseConfigManager) -> Unit)? = null

    init {
        appConfigManager.onConfigUpdated = {
            onConfigUpdated?.invoke(this)
        }
    }

    fun getApiKey(): String = appConfigManager.getApiKey()
    suspend fun ensureApiKey(): String = appConfigManager.ensureApiKey()
    fun fetchAndActivateAsync() = appConfigManager.fetchConfigAsync()
    suspend fun fetchAndActivate(): String {
        appConfigManager.fetchConfig()
        return appConfigManager.getApiKey()
    }

    fun areAdsEnabled(): Boolean = appConfigManager.areAdsEnabled()
    fun isSwipeAdEnabled(): Boolean = appConfigManager.isSwipeAdEnabled()
    fun isMessageAdEnabled(): Boolean = appConfigManager.isMessageAdEnabled()
    fun getSwipeAdInterval(): Int = appConfigManager.getSwipeAdInterval()
    fun getSwipeAdMinInterval(): Int = appConfigManager.getSwipeAdMinInterval()
    fun getSwipeAdMaxInterval(): Int = appConfigManager.getSwipeAdMaxInterval()
    fun getMessageAdInterval(): Int = appConfigManager.getMessageAdInterval()
}

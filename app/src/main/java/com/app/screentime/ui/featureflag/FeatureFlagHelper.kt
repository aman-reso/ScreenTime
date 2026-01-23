package com.app.screentime.config.featureflag

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.app.screentime.config.ConfigManager
import com.app.screentime.config.data.Feature
import com.app.screentime.ScreenTimeApplication
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Helper object for feature flag checks
 */
object FeatureFlagHelper {
    /**
     * Check if a feature is enabled
     */
    @Composable
    fun isFeatureEnabled(feature: Feature): Boolean {
        val context = LocalContext.current
        val configManager = EntryPointAccessors.fromApplication(
            context.applicationContext as ScreenTimeApplication,
            ConfigManagerEntryPoint::class.java
        ).configManager()
        return configManager.isFeatureEnabled(feature)
    }
}

/**
 * Entry point for accessing ConfigManager
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface ConfigManagerEntryPoint {
    fun configManager(): ConfigManager
}

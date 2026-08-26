package com.app.screentime.config

import android.content.Context
import android.util.Log
import com.app.screentime.config.data.Feature
import com.app.screentime.core.network.preferences.PreferencesManager
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigRepository @Inject constructor(
    private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getFeatures(): List<Feature> {
        return emptyList()
    }
}

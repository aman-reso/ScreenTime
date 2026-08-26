package com.app.screentime.config

import com.app.screentime.config.data.Feature
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigManager @Inject constructor(
    private val configRepository: ConfigRepository
) {
    fun getFeatures(): List<Feature> = configRepository.getFeatures()
}

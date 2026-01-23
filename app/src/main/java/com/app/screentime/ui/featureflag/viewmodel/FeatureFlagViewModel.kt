package com.app.screentime.ui.featureflag.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.config.ConfigManager
import com.app.screentime.config.data.Feature
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeatureFlagViewModel @Inject constructor(
    private val configManager: ConfigManager
) : ViewModel() {

    /**
     * Get feature enabled state as a StateFlow
     */
    fun isFeatureEnabled(feature: Feature): StateFlow<Boolean> {
        val flow = MutableStateFlow(configManager.isFeatureEnabled(feature))
        
        // Refresh config periodically or on demand
        viewModelScope.launch {
            configManager.refresh()
            flow.value = configManager.isFeatureEnabled(feature)
        }
        
        return flow.asStateFlow()
    }
    
    /**
     * Check if feature is enabled (synchronous)
     */
    fun checkFeatureEnabled(feature: Feature): Boolean {
        return configManager.isFeatureEnabled(feature)
    }
}

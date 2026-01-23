package com.app.screentime.battery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.battery.model.AppBatteryUsage
import com.app.screentime.battery.model.BatteryInfo
import com.app.screentime.battery.usecase.BatteryUseCase
import com.app.screentime.analytics.AnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for battery health information
 */
@HiltViewModel
class BatteryViewModel @Inject constructor(
    private val batteryUseCase: BatteryUseCase,
    private val analyticsUseCase: AnalyticsUseCase
) : ViewModel() {

    fun trackBatteryHealth() {
        analyticsUseCase.trackBatteryHealth()
    }
    
    private val _batteryInfo = MutableStateFlow<BatteryInfo?>(null)
    val batteryInfo: StateFlow<BatteryInfo?> = _batteryInfo.asStateFlow()
    
    private val _appBatteryUsage = MutableStateFlow<List<AppBatteryUsage>>(emptyList())
    val appBatteryUsage: StateFlow<List<AppBatteryUsage>> = _appBatteryUsage.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadBatteryInfo()
        startPeriodicRefresh()
    }
    
    fun loadBatteryInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val info = batteryUseCase.getBatteryInfo()
                _batteryInfo.value = info
            } catch (e: Exception) {
                // Handle error silently or log it
            } finally {
                _isLoading.value = false
            }
        }
    }
    

    private fun startPeriodicRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(30000) // 30 seconds
                loadBatteryInfo()
            }
        }
    }
}


package com.app.screentime.network.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.network.model.NetworkInfo
import com.app.screentime.network.usecase.NetworkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for network health information
 */
@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val networkUseCase: NetworkUseCase
) : ViewModel() {

    private val _networkInfo = MutableStateFlow<NetworkInfo?>(null)
    val networkInfo: StateFlow<NetworkInfo?> = _networkInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadNetworkInfo()
        // Refresh network info periodically (every 10 seconds)
        startPeriodicRefresh()
    }

    fun loadNetworkInfo() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val info = networkUseCase.getNetworkInfo()
                _networkInfo.value = info
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
                kotlinx.coroutines.delay(10000) // 10 seconds
                loadNetworkInfo()
            }
        }
    }
}


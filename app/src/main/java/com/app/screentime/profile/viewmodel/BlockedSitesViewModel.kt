package com.app.screentime.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.profile.model.BlockedSitesUiProps
import com.app.screentime.profile.usecase.BlockedSitesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockedSitesViewModel @Inject constructor(
    private val blockedSitesUseCase: BlockedSitesUseCase
) : ViewModel() {

    private val _uiProps = MutableStateFlow<BlockedSitesUiProps?>(null)
    val uiProps: StateFlow<BlockedSitesUiProps?> = _uiProps.asStateFlow()

    init {
        loadBlockedSites()
    }

    /**
     * Load blocked sites and get UI Props from use case
     */
    fun loadBlockedSites() {
        viewModelScope.launch {
            _uiProps.value = blockedSitesUseCase.getBlockedSitesUiProps(isLoading = true)
            val props = blockedSitesUseCase.getBlockedSitesUiProps(isLoading = false)
            _uiProps.value = props
        }
    }

    /**
     * Clear error
     */
    fun clearError() {
        val currentProps = _uiProps.value
        if (currentProps != null) {
            _uiProps.value = currentProps.copy(error = null)
        }
    }
}


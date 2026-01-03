package com.app.screentime.blocking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.database.entity.BlockedLinkEntity
import com.app.screentime.database.repository.BlockedLinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BlockedLinksUiState(
    val isLoading: Boolean = false,
    val blockedLinks: List<BlockedLinkEntity> = emptyList(),
    val showAddDialog: Boolean = false,
    val isVpnRunning: Boolean = false,
    val hasVpnPermission: Boolean = false,
    val error: String? = null
) {
    val totalBlockCount: Int
        get() = blockedLinks.sumOf { it.blockedCount }
}

@HiltViewModel
class BlockedLinksViewModel @Inject constructor(
    private val blockedLinkRepository: BlockedLinkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlockedLinksUiState())
    val uiState: StateFlow<BlockedLinksUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                blockedLinkRepository.getAllBlockedLinks().collect { links ->
                    _uiState.value = _uiState.value.copy(
                        blockedLinks = links,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadBlockedLinks() {
        // Flow is already being observed in init, this is just for manual refresh
        viewModelScope.launch {
            try {
                val links = blockedLinkRepository.getAllBlockedLinksSync()
                _uiState.value = _uiState.value.copy(
                    blockedLinks = links,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun addBlockedLink(link: String) {
        viewModelScope.launch {
            try {
                blockedLinkRepository.addBlockedLink(link)
                // State will be updated via Flow
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteBlockedLink(id: Long) {
        viewModelScope.launch {
            try {
                blockedLinkRepository.removeBlockedLinkById(id)
                // State will be updated via Flow
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun showAddLinkDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true)
    }

    fun hideAddLinkDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }


    fun checkVpnStatus(context: android.content.Context) {
        val am =
            context.getSystemService(android.content.Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val isRunning = am.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == com.app.screentime.service.ScreenTimeVpnService::class.java.name }

        val vpnPermissionManager = com.app.screentime.service.VpnPermissionManager(context)
        val hasPermission = vpnPermissionManager.hasVpnPermission()

        if (_uiState.value.isVpnRunning != isRunning || _uiState.value.hasVpnPermission != hasPermission) {
            _uiState.value = _uiState.value.copy(
                isVpnRunning = isRunning,
                hasVpnPermission = hasPermission
            )
        }
    }
}


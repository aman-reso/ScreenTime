package com.app.screentime.applock.viewmodel

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.applock.manager.AppLockManager
import com.app.screentime.applock.model.AppLockRule
import com.app.screentime.applock.repository.AppLockRepository
import com.app.screentime.applock.utils.startServiceIfNeeded
import com.app.screentime.applock.utils.stopService
import com.app.screentime.applock.utils.hasActiveRules

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppLockUiState(
    val isLoading: Boolean = false,
    val rules: List<AppLockRule> = emptyList(),
    val error: String? = null,
    val isPINSet: Boolean = false
)

@HiltViewModel
class AppLockViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val repository = AppLockRepository(context)
    private val _uiState = MutableStateFlow(AppLockUiState())
    val uiState: StateFlow<AppLockUiState> = _uiState.asStateFlow()

    init {
        loadRules()
        checkPINStatus()
    }

    fun loadRules() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val rules = repository.getAllRules()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    rules = rules,
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

    fun checkPINStatus() {
        viewModelScope.launch {
            val isPINSet = repository.isPINSet()
            _uiState.value = _uiState.value.copy(isPINSet = isPINSet)
        }
    }

    fun setPIN(pin: String) {
        viewModelScope.launch {
            try {
                repository.savePIN(pin)
                _uiState.value = _uiState.value.copy(isPINSet = true, error = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun verifyPIN(pin: String): Boolean {
        return repository.verifyPIN(pin)
    }

    fun lockApp(packageName: String, appName: String) {
        viewModelScope.launch {
            try {
                val rule = AppLockRule(packageName = packageName, appName = appName, isLocked = true)
                repository.saveRule(rule)
                AppLockManager.lockApp(packageName)
                loadRules()
                
                // Start service if needed (check permissions and start if locked apps exist)
                val hasUsageStats = checkUsageStatsPermission(context)
                val hasOverlay = Settings.canDrawOverlays(context)
                startServiceIfNeeded(context, hasUsageStats, hasOverlay)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    private fun checkUsageStatsPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOps.checkOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == android.app.AppOpsManager.MODE_ALLOWED
    }

    fun unlockApp(packageName: String) {
        viewModelScope.launch {
            try {
                AppLockManager.unlockApp(packageName)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun removeAppLock(packageName: String) {
        viewModelScope.launch {
            try {
                repository.deleteRule(packageName)
                AppLockManager.removeAppLock(packageName)
                loadRules()
                delay(100)
                if (!hasActiveRules(context)) {
                    stopService(context)
                    Log.d("AppLockViewModel", "App lock list is empty, stopped ListenerService")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}


package com.app.screentime.blocking.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.blocking.manager.AppBlockManager
import com.app.screentime.blocking.model.BlockingRule
import com.app.screentime.blocking.repository.BlockingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppBlockingUiState(
    val isLoading: Boolean = false,
    val rules: List<BlockingRule> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class AppBlockingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val repository = BlockingRepository(context)
    private val _uiState = MutableStateFlow(AppBlockingUiState())
    val uiState: StateFlow<AppBlockingUiState> = _uiState.asStateFlow()

    init {
        loadRules()
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

    fun blockAppInstantly(packageName: String, appName: String) {
        viewModelScope.launch {
            try {
                val rule = BlockingRule.InstantBlock(packageName, appName)
                repository.saveRule(rule)
                AppBlockManager.blockApp(packageName, Long.MAX_VALUE) // Block indefinitely
                loadRules()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun blockAppAfterLaunches(packageName: String, appName: String, maxLaunches: Int) {
        viewModelScope.launch {
            try {
                val rule = BlockingRule.LaunchBasedBlock(packageName, appName, maxLaunches, 0)
                repository.saveRule(rule)
                loadRules()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun blockAppAfterDuration(packageName: String, appName: String, maxDurationMinutes: Int) {
        viewModelScope.launch {
            try {
                val rule = BlockingRule.DurationBasedBlock(packageName, appName, maxDurationMinutes, 0L)
                repository.saveRule(rule)
                loadRules()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun removeBlockingRule(packageName: String) {
        viewModelScope.launch {
            try {
                repository.deleteRule(packageName)
                AppBlockManager.unblockApp(packageName)
                loadRules()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}


package com.app.screentime.reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.config.R
import com.app.screentime.reward.mapper.CoinHistoryMapper
import com.app.screentime.reward.model.CoinHistoryFilter
import com.app.screentime.reward.model.CoinHistoryUiState
import com.app.screentime.reward.usecase.CoinHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinHistoryViewModel @Inject constructor(
    private val coinHistoryUseCase: CoinHistoryUseCase,
    @ApplicationContext private val context: android.content.Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinHistoryMapper.toLoadingUiState())
    val uiState: StateFlow<CoinHistoryUiState> = _uiState.asStateFlow()

    init {
        loadCoinHistory()
    }

    fun loadCoinHistory(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh) {
                _uiState.value = CoinHistoryMapper.toLoadingUiState()
            }

            coinHistoryUseCase.getCoinHistory().fold(
                onSuccess = { (totalCoins, coinHistory) ->
                    _uiState.value = CoinHistoryMapper.toUiState(
                        totalCoins = totalCoins,
                        coinHistory = coinHistory,
                        selectedFilter = _uiState.value.selectedFilter
                    )
                },
                onFailure = { exception ->
                    _uiState.value = CoinHistoryMapper.toErrorUiState(
                        error = exception.message ?: context.getString(R.string.failed_to_load_coin_history)
                    )
                }
            )
        }
    }
    
    fun refresh() {
        loadCoinHistory(isRefresh = true)
    }

    fun setFilter(filter: CoinHistoryFilter) {
        val currentState = _uiState.value
        _uiState.value = CoinHistoryMapper.toUiState(
            totalCoins = currentState.totalCoins,
            coinHistory = currentState.coinHistory,
            selectedFilter = filter
        )
    }

    fun clearError() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(error = null)
    }
}






















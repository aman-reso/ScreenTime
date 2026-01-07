package com.app.screentime.reward.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.reward.mapper.CoinHistoryMapper
import com.app.screentime.reward.model.CoinHistoryFilter
import com.app.screentime.reward.model.CoinHistoryUiState
import com.app.screentime.reward.usecase.CoinHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinHistoryViewModel @Inject constructor(
    private val coinHistoryUseCase: CoinHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoinHistoryMapper.toLoadingUiState())
    val uiState: StateFlow<CoinHistoryUiState> = _uiState.asStateFlow()

    init {
        loadCoinHistory()
    }

    fun loadCoinHistory() {
        viewModelScope.launch {
            _uiState.value = CoinHistoryMapper.toLoadingUiState()

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
                        error = exception.message ?: "Failed to load coin history"
                    )
                }
            )
        }
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





















package com.app.screentime.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.Conversation
import com.app.screentime.feature.chat.domain.usecase.GetConversationsUseCase
import com.app.screentime.feature.chat.domain.usecase.RealtimeChatSyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatListUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val realtimeChatSyncManager: RealtimeChatSyncManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState(isLoading = true))
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    init {
        realtimeChatSyncManager.ensureConnected()
        loadConversations(force = true)
    }

    fun loadConversations(force: Boolean = false) {
        if (fetchJob?.isActive == true && !force) return
        fetchJob = viewModelScope.launch {
            val showLoading = _uiState.value.conversations.isEmpty() || force
            if (showLoading) {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            val list = getConversationsUseCase()
            _uiState.value = _uiState.value.copy(
                conversations = list,
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    fun refresh() {
        if (_uiState.value.isRefreshing) return
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            val list = getConversationsUseCase()
            _uiState.value = _uiState.value.copy(
                conversations = list,
                isLoading = false,
                isRefreshing = false
            )
        }
    }
}

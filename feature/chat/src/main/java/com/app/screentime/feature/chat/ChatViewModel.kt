package com.app.screentime.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.network.websocket.WSEventTypes
import com.app.screentime.feature.chat.domain.usecase.GetMessagesUseCase
import com.app.screentime.feature.chat.domain.usecase.ObserveMessagesUseCase
import com.app.screentime.feature.chat.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var activePartnerId: String = ""

    init {
        observeIncomingMessages()
    }

    fun loadChat(partnerId: String) {
        activePartnerId = partnerId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val list = getMessagesUseCase(partnerId)
            _uiState.value = _uiState.value.copy(
                messages = list,
                isLoading = false
            )
        }
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            observeMessagesUseCase().collectLatest { msg ->
                if (msg.type == WSEventTypes.CHAT_MESSAGE || msg.type == WSEventTypes.CHAT_RECEIVED) {
                    val partner = msg.caller_id ?: activePartnerId
                    val incoming = ChatMessage(
                        id = "msg_${System.currentTimeMillis()}",
                        senderId = partner,
                        receiverId = "user",
                        text = msg.payload ?: "",
                        timestamp = System.currentTimeMillis()
                    )
                    observeMessagesUseCase.saveIncoming(partner, incoming)
                    if (partner == activePartnerId) {
                        _uiState.value = _uiState.value.copy(
                            messages = _uiState.value.messages + incoming
                        )
                    }
                }
            }
        }
    }

    fun onInputTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage(modelId: String) {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return

        _uiState.value = _uiState.value.copy(inputText = "", isSending = true)
        viewModelScope.launch {
            sendMessageUseCase(modelId, text).onSuccess { msg ->
                val existing = _uiState.value.messages.any { it.id == msg.id }
                if (!existing) {
                    _uiState.value = _uiState.value.copy(
                        messages = _uiState.value.messages + msg,
                        isSending = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isSending = false)
                }
            }.onFailure {
                _uiState.value = _uiState.value.copy(isSending = false)
            }
        }
    }
}

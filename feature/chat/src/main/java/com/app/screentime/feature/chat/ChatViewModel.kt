package com.app.screentime.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.network.websocket.WSEventTypes
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
    val isSending: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        observeIncomingMessages()
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            observeMessagesUseCase().collectLatest { msg ->
                if (msg.type == WSEventTypes.CHAT_MESSAGE || msg.type == WSEventTypes.CHAT_RECEIVED) {
                    val incoming = ChatMessage(
                        id = "msg_${System.currentTimeMillis()}",
                        senderId = msg.caller_id ?: "remote",
                        receiverId = "user",
                        text = msg.payload ?: "",
                        timestamp = System.currentTimeMillis()
                    )
                    _uiState.value = _uiState.value.copy(
                        messages = _uiState.value.messages + incoming
                    )
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

        val myMessage = ChatMessage(
            id = "my_${System.currentTimeMillis()}",
            senderId = "user",
            receiverId = modelId,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + myMessage,
            inputText = ""
        )

        sendMessageUseCase(modelId, text)
    }
}

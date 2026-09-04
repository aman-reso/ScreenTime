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
                messages = list.filter { it.text.isNotBlank() }.distinctBy { it.id },
                isLoading = false
            )
        }
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            observeMessagesUseCase().collectLatest { msg ->
                // ONLY handle actual incoming chat messages. CHAT_RECEIVED is purely an ACK/delivery receipt.
                if (msg.type == WSEventTypes.CHAT_MESSAGE) {
                    val myId = observeMessagesUseCase.currentUserId
                    val sender = msg.caller_id ?: msg.user_id ?: ""

                    // Ignore echo of our own sent message
                    if (sender.isNotBlank() && sender == myId) {
                        return@collectLatest
                    }

                    val content = (msg.message?.takeIf { it.isNotBlank() } ?: msg.payloadAsString()).trim()
                    if (content.isBlank()) {
                        return@collectLatest
                    }

                    val partner = if (sender.isNotBlank() && sender != myId) sender else activePartnerId
                    val msgId = msg.call_id?.takeIf { it.isNotBlank() }
                        ?: "msg_${System.currentTimeMillis()}_${content.hashCode()}"

                    val incoming = ChatMessage(
                        id = msgId,
                        senderId = partner,
                        receiverId = myId,
                        text = content,
                        timestamp = System.currentTimeMillis()
                    )
                    observeMessagesUseCase.saveIncoming(partner, incoming)
                    if (partner == activePartnerId) {
                        val current = _uiState.value.messages
                        val isDuplicate = current.any {
                            it.id == incoming.id || (it.text == incoming.text && it.senderId == incoming.senderId && Math.abs(it.timestamp - incoming.timestamp) < 4000)
                        }
                        if (!isDuplicate) {
                            _uiState.value = _uiState.value.copy(
                                messages = (current + incoming).distinctBy { it.id }
                            )
                        }
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

        val myUserId = observeMessagesUseCase.currentUserId
        val tempId = "msg_${System.currentTimeMillis()}"
        val optimisticMsg = ChatMessage(
            id = tempId,
            senderId = myUserId,
            receiverId = modelId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        // Optimistically show message on UI immediately
        _uiState.value = _uiState.value.copy(
            inputText = "",
            messages = (_uiState.value.messages + optimisticMsg).distinctBy { it.id },
            isSending = true
        )

        viewModelScope.launch {
            sendMessageUseCase(modelId, text).onSuccess { confirmedMsg ->
                val current = _uiState.value.messages
                val updated = current.map {
                    if (it.id == tempId || (it.text == text && it.senderId == myUserId && Math.abs(it.timestamp - confirmedMsg.timestamp) < 5000)) {
                        confirmedMsg
                    } else {
                        it
                    }
                }.distinctBy { it.id }

                _uiState.value = _uiState.value.copy(
                    messages = updated,
                    isSending = false
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isSending = false)
            }
        }
    }
}

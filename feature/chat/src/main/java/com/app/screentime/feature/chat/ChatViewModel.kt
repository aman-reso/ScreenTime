package com.app.screentime.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.network.websocket.WSEventTypes
import com.app.screentime.feature.chat.domain.usecase.GetMessagesUseCase
import com.app.screentime.feature.chat.domain.usecase.ObserveMessagesUseCase
import com.app.screentime.feature.chat.domain.usecase.RealtimeChatSyncManager
import com.app.screentime.feature.chat.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false,
    val isLoading: Boolean = false,
    val activeConversationId: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val realtimeChatSyncManager: RealtimeChatSyncManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var activePartnerId: String = ""
    private var activeConversationId: String? = null
    private var messagesFlowJob: Job? = null

    init {
        realtimeChatSyncManager.ensureConnected()
        observeIncomingMessages()
    }

    fun loadChat(partnerId: String, conversationId: String? = null) {
        activePartnerId = partnerId
        activeConversationId = conversationId?.takeIf { it.isNotBlank() }
        _uiState.value = _uiState.value.copy(activeConversationId = activeConversationId)

        // 1. Reactively observe local Room DB for real-time live UI updates
        messagesFlowJob?.cancel()
        messagesFlowJob = viewModelScope.launch {
            getMessagesUseCase.getMessagesFlow(partnerId).collect { list ->
                _uiState.value = _uiState.value.copy(
                    messages = list.filter { it.text.isNotBlank() }.distinctBy { it.id }
                )
            }
        }

        // 2. Fetch remote messages history and sync with Room DB
        viewModelScope.launch {
            val showLoading = _uiState.value.messages.isEmpty()
            if (showLoading) {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }
            getMessagesUseCase(partnerId = partnerId, conversationId = activeConversationId)
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            observeMessagesUseCase().collect { msg ->
                if (msg.type == WSEventTypes.CHAT_MESSAGE) {
                    val myId = observeMessagesUseCase.currentUserId
                    val sender = msg.effectiveSenderId ?: msg.sender_id ?: msg.caller_id ?: msg.user_id ?: ""
                    val target = msg.effectiveTargetId ?: msg.target_id ?: msg.receiver_id ?: ""

                    val content = (msg.content?.takeIf { it.isNotBlank() }
                        ?: msg.message?.takeIf { it.isNotBlank() }
                        ?: msg.payloadAsString()).trim()

                    if (content.isBlank()) {
                        return@collect
                    }

                    // Resolve partner user ID for database indexing
                    val partner = when {
                        sender.isNotBlank() && sender != myId -> sender
                        target.isNotBlank() && target != myId -> target
                        activePartnerId.isNotBlank() -> activePartnerId
                        else -> sender.ifBlank { target }
                    }

                    if (partner.isBlank()) {
                        return@collect
                    }

                    val msgId = msg.call_id?.takeIf { it.isNotBlank() }
                        ?: "msg_${msg.timestamp ?: System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}"

                    val isFromMe = (sender.isNotBlank() && sender == myId)

                    val incoming = ChatMessage(
                        id = msgId,
                        senderId = if (isFromMe) myId else partner,
                        receiverId = if (isFromMe) partner else myId,
                        text = content,
                        timestamp = msg.timestamp ?: System.currentTimeMillis(),
                        conversationId = msg.conversation_id ?: msg.room_id ?: activeConversationId,
                        isSending = false,
                        isFailed = false
                    )

                    // Persist to Room DB; reactive flow will update UI instantly on both sides
                    observeMessagesUseCase.saveIncoming(partner, incoming)
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

        _uiState.value = _uiState.value.copy(
            inputText = "",
            isSending = true
        )

        viewModelScope.launch {
            sendMessageUseCase(
                receiverId = modelId,
                text = text,
                conversationId = activeConversationId
            ).onSuccess { confirmedMsg ->
                if (activeConversationId.isNullOrBlank() && !confirmedMsg.conversationId.isNullOrBlank()) {
                    activeConversationId = confirmedMsg.conversationId
                    _uiState.value = _uiState.value.copy(activeConversationId = activeConversationId)
                }
                _uiState.value = _uiState.value.copy(isSending = false)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isSending = false)
            }
        }
    }

    fun retrySendMessage(failedMessage: ChatMessage) {
        viewModelScope.launch {
            sendMessageUseCase(
                receiverId = failedMessage.receiverId,
                text = failedMessage.text,
                conversationId = failedMessage.conversationId ?: activeConversationId
            )
        }
    }
}

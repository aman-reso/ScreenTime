package com.app.screentime.feature.chat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.network.util.ImageCompressor
import com.app.screentime.core.network.websocket.WSEventTypes
import com.app.screentime.feature.chat.domain.usecase.GetMessagesUseCase
import com.app.screentime.feature.chat.domain.usecase.ObserveMessagesUseCase
import com.app.screentime.feature.chat.domain.usecase.RealtimeChatSyncManager
import com.app.screentime.feature.chat.domain.usecase.SendImageMessageUseCase
import com.app.screentime.feature.chat.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

import com.app.screentime.feature.chat.domain.usecase.SendAudioMessageUseCase
import com.app.screentime.feature.chat.util.AudioPlayerManager

import android.app.Activity
import com.app.screentime.core.network.dto.WalletPackDto
import com.app.screentime.feature.wallet.billing.BillingPurchaseResult
import com.app.screentime.feature.wallet.billing.GooglePlayBillingManager
import com.app.screentime.feature.wallet.domain.usecase.GetWalletPacksUseCase
import com.app.screentime.feature.wallet.domain.usecase.VerifyGooglePlayPurchaseUseCase

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingOlder: Boolean = false,
    val hasMore: Boolean = true,
    val currentPage: Int = 1,
    val activeConversationId: String? = null,
    val quotaLimit: com.app.screentime.core.model.QuotaLimitInfo? = null,
    val showPaywallSheet: Boolean = false,
    val walletPacks: List<WalletPackDto> = emptyList(),
    val isVerifyingPayment: Boolean = false,
    val paymentError: String? = null
)

private val DEFAULT_CHAT_PACKS = listOf(
    WalletPackDto(
        id = "product_19_rupees",
        name = "190 Messages",
        amount = 19.0,
        credits = 190,
        description = "Instant 190 messages top-up",
        popular_badge = true
    ),
    WalletPackDto(
        id = "product_49_rupees",
        name = "490 Messages & Profile Views",
        amount = 49.0,
        credits = 490,
        description = "490 messages & unlimited chat pass"
    ),
    WalletPackDto(
        id = "product_99_rupees",
        name = "1000 Messages Mega Pack",
        amount = 99.0,
        credits = 1000,
        description = "Best value for long conversations"
    )
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val sendImageMessageUseCase: SendImageMessageUseCase,
    private val sendAudioMessageUseCase: SendAudioMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val realtimeChatSyncManager: RealtimeChatSyncManager,
    private val quotaLimitManager: com.app.screentime.core.network.quota.QuotaLimitManager,
    private val viewOncePhotoTracker: com.app.screentime.feature.chat.data.local.ViewOncePhotoTracker,
    private val localStorage: com.app.screentime.feature.chat.data.local.LocalChatStorage,
    private val messagesApi: com.app.screentime.core.network.api.MessagesApi,
    private val sessionManager: com.app.screentime.core.network.session.SessionManager,
    private val preferencesManager: com.app.screentime.core.network.preferences.PreferencesManager,
    private val getWalletPacksUseCase: GetWalletPacksUseCase,
    private val verifyGooglePlayPurchaseUseCase: VerifyGooglePlayPurchaseUseCase,
    val googlePlayBillingManager: GooglePlayBillingManager,
    private val e2eCryptoManager: com.app.screentime.core.network.security.E2ECryptoManager
) : ViewModel() {

    init {
        observeBillingEvents()
        loadWalletPacks()
    }

    private fun observeBillingEvents() {
        viewModelScope.launch {
            googlePlayBillingManager.purchaseEvents.collect { result ->
                when (result) {
                    is BillingPurchaseResult.Success -> {
                        handleBillingSuccess(result)
                    }
                    is BillingPurchaseResult.Cancelled -> {
                        _uiState.value = _uiState.value.copy(
                            isVerifyingPayment = false,
                            paymentError = "Payment cancelled"
                        )
                    }
                    is BillingPurchaseResult.Failed -> {
                        _uiState.value = _uiState.value.copy(
                            isVerifyingPayment = false,
                            paymentError = result.message
                        )
                    }
                }
            }
        }
    }

    private fun handleBillingSuccess(result: BillingPurchaseResult.Success) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isVerifyingPayment = true, paymentError = null)
            val currentPacks = _uiState.value.walletPacks.ifEmpty { DEFAULT_CHAT_PACKS }
            val pack = currentPacks.firstOrNull { it.id == result.productId }
            val credits = pack?.effectiveCredits ?: 190
            val amount = pack?.effectivePrice ?: 19.0
            val packId = pack?.id ?: result.productId
            val type = if (result.productId.contains("sub", ignoreCase = true) || packId.contains("sub", ignoreCase = true)) "subs" else "inapp"

            val verifyRes = verifyGooglePlayPurchaseUseCase(
                purchaseToken = result.purchaseToken,
                productId = result.productId,
                type = type,
                orderId = result.orderId,
                packId = packId,
                credits = credits,
                amount = amount
            )

            if (verifyRes.isSuccess) {
                quotaLimitManager.clearChatQuota()
                _uiState.value = _uiState.value.copy(
                    quotaLimit = null,
                    showPaywallSheet = false,
                    isVerifyingPayment = false,
                    paymentError = null
                )
                val token = preferencesManager.getToken().orEmpty().ifBlank { sessionManager.getToken().orEmpty() }
                val myUserId = (sessionManager.userId ?: preferencesManager.getUserId() ?: "").trim()
                if (myUserId.isNotBlank()) {
                    try {
                        messagesApi.claimAdReward(token, myUserId, credits)
                    } catch (_: Exception) {}
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isVerifyingPayment = false,
                    paymentError = verifyRes.exceptionOrNull()?.message ?: "Purchase verification failed"
                )
            }
        }
    }

    fun openPaywall() {
        _uiState.value = _uiState.value.copy(
            showPaywallSheet = true,
            paymentError = null,
            walletPacks = _uiState.value.walletPacks.ifEmpty { DEFAULT_CHAT_PACKS }
        )
        if (_uiState.value.walletPacks.isEmpty()) {
            loadWalletPacks()
        }
    }

    fun dismissPaywall() {
        _uiState.value = _uiState.value.copy(showPaywallSheet = false, paymentError = null)
    }

    private fun loadWalletPacks() {
        viewModelScope.launch {
            try {
                val res = getWalletPacksUseCase()
                val packs = res.getOrNull().orEmpty().ifEmpty { DEFAULT_CHAT_PACKS }
                _uiState.value = _uiState.value.copy(walletPacks = packs)
                googlePlayBillingManager.queryProducts(packs.map { it.id })
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(walletPacks = DEFAULT_CHAT_PACKS)
            }
        }
    }

    fun launchPurchase(activity: Activity, pack: WalletPackDto) {
        _uiState.value = _uiState.value.copy(isVerifyingPayment = true, paymentError = null)
        googlePlayBillingManager.launchBillingFlow(
            activity = activity,
            productId = pack.id,
            onLaunchFailed = { error ->
                _uiState.value = _uiState.value.copy(isVerifyingPayment = false, paymentError = error)
            }
        )
    }

    fun claimAdReward(rewardAmount: Int = 5) {
        viewModelScope.launch {
            try {
                val token = preferencesManager.getToken().orEmpty().ifBlank { sessionManager.getToken().orEmpty() }
                val myUserId = (sessionManager.userId ?: preferencesManager.getUserId() ?: "").trim()
                if (myUserId.isNotBlank()) {
                    messagesApi.claimAdReward(token, myUserId, rewardAmount)
                }
                quotaLimitManager.clearChatQuota()
                _uiState.value = _uiState.value.copy(quotaLimit = null)
            } catch (e: Exception) {
                quotaLimitManager.clearChatQuota()
                _uiState.value = _uiState.value.copy(quotaLimit = null)
            }
        }
    }

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var activePartnerId: String = ""
    private var activeConversationId: String? = null
    private var messagesFlowJob: Job? = null
    private var displayedLimit: Int = 20
    private var allCachedMessages: List<ChatMessage> = emptyList()
    private var currentLoadedPage: Int = 1
    private var serverHasMore: Boolean = true

    init {
        realtimeChatSyncManager.ensureConnected()
        viewModelScope.launch {
            quotaLimitManager.chatQuota.collect { quota ->
                _uiState.value = _uiState.value.copy(quotaLimit = quota)
            }
        }
        AudioPlayerManager.onAudioCompleted = { completedMsgId ->
            consumeViewOnceAudioById(completedMsgId)
        }
    }

    fun loadChat(partnerId: String, conversationId: String? = null) {
        if (partnerId.isBlank()) return
        activePartnerId = partnerId
        activeConversationId = conversationId?.takeIf { it.isNotBlank() }
        displayedLimit = 20
        currentLoadedPage = 1
        serverHasMore = true
        _uiState.value = _uiState.value.copy(
            activeConversationId = activeConversationId,
            currentPage = 1,
            hasMore = true,
            isLoadingOlder = false
        )
        com.app.screentime.core.model.ActiveChatTracker.activePartnerId = partnerId

        // 1. Reactively observe local Room DB as SINGLE SOURCE OF TRUTH
        messagesFlowJob?.cancel()
        messagesFlowJob = viewModelScope.launch {
            getMessagesUseCase.getMessagesFlow(partnerId).collect { dbList ->
                val deduplicated = dbList.filter { msg ->
                    msg.text.isNotBlank() &&
                    !((msg.type == com.app.screentime.core.model.MessageType.IMAGE || msg.type == com.app.screentime.core.model.MessageType.AUDIO) && viewOncePhotoTracker.isViewed(msg.id, msg.clientMessageId, msg.mediaUrl ?: msg.text))
                }.deduplicate()
                allCachedMessages = deduplicated
                val visible = deduplicated.takeLast(displayedLimit)
                val canShowMoreLocal = deduplicated.size > visible.size
                _uiState.value = _uiState.value.copy(
                    messages = visible,
                    hasMore = canShowMoreLocal || serverHasMore
                )
            }
        }

        // 2. Fetch page 1 (latest 20 messages) from remote server and sync with Room DB
        viewModelScope.launch {
            val showLoading = _uiState.value.messages.isEmpty()
            if (showLoading) {
                _uiState.value = _uiState.value.copy(isLoading = true)
            }
            try {
                val result = getMessagesUseCase.fetchMessagesPage(
                    partnerId = partnerId,
                    conversationId = activeConversationId,
                    page = 1,
                    limit = 20
                )
                currentLoadedPage = result.page
                serverHasMore = result.hasMore
                val canShowMoreLocal = allCachedMessages.size > displayedLimit
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentPage = currentLoadedPage,
                    hasMore = canShowMoreLocal || serverHasMore
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun loadOlderMessages() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingOlder || !state.hasMore || activePartnerId.isBlank()) {
            return
        }

        // If local cache has older messages not currently in view, reveal next batch locally first
        if (allCachedMessages.size > displayedLimit) {
            displayedLimit += 20
            val visible = allCachedMessages.takeLast(displayedLimit)
            val canShowMoreLocal = allCachedMessages.size > visible.size
            _uiState.value = _uiState.value.copy(
                messages = visible,
                hasMore = canShowMoreLocal || serverHasMore
            )
            if (canShowMoreLocal) {
                return
            }
        }

        // If no more messages on server, stop
        if (!serverHasMore) {
            _uiState.value = _uiState.value.copy(hasMore = false)
            return
        }

        val nextPage = currentLoadedPage + 1
        val oldestMsg = state.messages.minByOrNull { it.timestamp }
        val beforeParam = oldestMsg?.let {
            try {
                java.time.Instant.ofEpochMilli(it.timestamp).toString()
            } catch (e: Exception) {
                it.timestamp.toString()
            }
        }

        _uiState.value = _uiState.value.copy(isLoadingOlder = true)

        viewModelScope.launch {
            try {
                val result = getMessagesUseCase.fetchMessagesPage(
                    partnerId = activePartnerId,
                    conversationId = activeConversationId,
                    page = nextPage,
                    limit = 20,
                    before = beforeParam
                )
                currentLoadedPage = result.nextPage ?: nextPage
                serverHasMore = result.hasMore && result.messages.isNotEmpty()
                displayedLimit += 20

                val visible = allCachedMessages.takeLast(displayedLimit)
                val canShowMoreLocal = allCachedMessages.size > visible.size

                _uiState.value = _uiState.value.copy(
                    isLoadingOlder = false,
                    currentPage = currentLoadedPage,
                    hasMore = canShowMoreLocal || serverHasMore,
                    messages = visible
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoadingOlder = false)
            }
        }
    }

    fun onInputTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage(modelId: String) {
        if (_uiState.value.quotaLimit?.isLimitReached == true) return
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return

        displayedLimit++
        val tempId = "local_${System.currentTimeMillis()}_${java.util.UUID.randomUUID().toString().take(6)}"

        _uiState.value = _uiState.value.copy(
            inputText = "",
            isSending = true
        )

        viewModelScope.launch {
            sendMessageUseCase(
                receiverId = modelId,
                text = text,
                conversationId = activeConversationId,
                messageId = tempId
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

    fun sendImage(context: Context, uri: Uri, modelId: String) {
        if (_uiState.value.quotaLimit?.isLimitReached == true) return
        val part = ImageCompressor.compressUriToPart(context, uri, 0) ?: return

        displayedLimit++
        val tempId = "local_img_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}"

        viewModelScope.launch {
            sendImageMessageUseCase(
                receiverId = modelId,
                part = part,
                localUriString = uri.toString(),
                conversationId = activeConversationId,
                clientMessageId = tempId
            ).onSuccess { confirmedMsg ->
                if (activeConversationId.isNullOrBlank() && !confirmedMsg.conversationId.isNullOrBlank()) {
                    activeConversationId = confirmedMsg.conversationId
                    _uiState.value = _uiState.value.copy(activeConversationId = activeConversationId)
                }
            }
        }
    }

    fun sendAudio(audioFile: java.io.File, modelId: String) {
        if (_uiState.value.quotaLimit?.isLimitReached == true) return
        if (!audioFile.exists() || audioFile.length() <= 0) return

        displayedLimit++
        val tempId = "local_audio_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}"

        viewModelScope.launch {
            sendAudioMessageUseCase(
                receiverId = modelId,
                audioFile = audioFile,
                conversationId = activeConversationId,
                clientMessageId = tempId
            ).onSuccess { confirmedMsg ->
                if (activeConversationId.isNullOrBlank() && !confirmedMsg.conversationId.isNullOrBlank()) {
                    activeConversationId = confirmedMsg.conversationId
                    _uiState.value = _uiState.value.copy(activeConversationId = activeConversationId)
                }
            }
        }
    }

    fun retrySendMessage(failedMessage: ChatMessage) {
        viewModelScope.launch {
            if (failedMessage.type == com.app.screentime.core.model.MessageType.AUDIO && !failedMessage.mediaUrl.isNullOrBlank()) {
                val file = java.io.File(failedMessage.mediaUrl!!)
                if (file.exists()) {
                    sendAudio(file, failedMessage.receiverId)
                    return@launch
                }
            }
            sendMessageUseCase(
                receiverId = failedMessage.receiverId,
                text = failedMessage.text,
                conversationId = failedMessage.conversationId ?: activeConversationId
            )
        }
    }

    fun consumeViewOncePhoto(message: ChatMessage) {
        val photoUrl = message.mediaUrl?.takeIf { it.isNotBlank() } ?: message.text
        viewOncePhotoTracker.markAsViewed(message.id, message.clientMessageId, photoUrl)

        // Optimistically remove from visible UI immediately
        allCachedMessages = allCachedMessages.filter { it.id != message.id && it.clientMessageId != message.clientMessageId }
        val visible = _uiState.value.messages.filter { it.id != message.id && it.clientMessageId != message.clientMessageId }
        _uiState.value = _uiState.value.copy(messages = visible)

        viewModelScope.launch {
            localStorage.deleteMessage(message.id)
            val cId = message.clientMessageId
            if (!cId.isNullOrBlank()) {
                localStorage.deleteMessage(cId)
            }
        }
    }

    fun consumeViewOnceAudio(message: ChatMessage) {
        consumeViewOncePhoto(message)
    }

    fun consumeViewOnceAudioById(messageId: String) {
        val msg = allCachedMessages.firstOrNull { it.id == messageId || it.clientMessageId == messageId }
            ?: _uiState.value.messages.firstOrNull { it.id == messageId || it.clientMessageId == messageId }
        if (msg != null) {
            consumeViewOncePhoto(msg)
        }
    }

    fun playAudio(context: Context, message: ChatMessage) {
        val partnerId = activePartnerId ?: return
        val isMe = message.senderId != partnerId
        val otherUserId = if (isMe) (message.receiverId ?: partnerId) else message.senderId
        val audioUrl = message.mediaUrl?.takeIf { it.isNotBlank() } ?: message.text
        val token = preferencesManager.getToken().orEmpty().ifBlank { sessionManager.getToken().orEmpty() }

        AudioPlayerManager.togglePlay(
            context = context,
            messageId = message.id,
            audioUrlOrPath = audioUrl,
            otherUserId = otherUserId,
            e2eCryptoManager = e2eCryptoManager,
            token = token
        )
    }

    override fun onCleared() {
        super.onCleared()
        AudioPlayerManager.onAudioCompleted = null
        AudioPlayerManager.stop()
        if (com.app.screentime.core.model.ActiveChatTracker.activePartnerId == activePartnerId) {
            com.app.screentime.core.model.ActiveChatTracker.activePartnerId = null
        }
    }
}

private fun List<ChatMessage>.deduplicate(): List<ChatMessage> {
    val result = mutableListOf<ChatMessage>()
    val seenIds = mutableSetOf<String>()
    val seenClientMsgIds = mutableSetOf<String>()

    for (msg in this) {
        val hasSameId = msg.id.isNotBlank() && seenIds.contains(msg.id)
        val hasSameClientId = !msg.clientMessageId.isNullOrBlank() && seenClientMsgIds.contains(msg.clientMessageId)

        if (!hasSameId && !hasSameClientId) {
            result.add(msg)
            if (msg.id.isNotBlank()) seenIds.add(msg.id)
            if (!msg.clientMessageId.isNullOrBlank()) seenClientMsgIds.add(msg.clientMessageId!!)
        }
    }
    return result
}

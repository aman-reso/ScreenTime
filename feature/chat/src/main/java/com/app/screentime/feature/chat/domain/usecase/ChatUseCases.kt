package com.app.screentime.feature.chat.domain.usecase

import com.app.screentime.core.model.ChatMessage
import com.app.screentime.core.model.Conversation
import com.app.screentime.core.network.api.MessagesApi
import com.app.screentime.core.network.api.WinterApi
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.session.SessionManager
import com.app.screentime.core.network.websocket.WinterWebSocketClient
import com.app.screentime.core.network.websocket.WSMessage
import com.app.screentime.feature.chat.data.local.LocalChatStorage
import com.app.screentime.feature.chat.data.local.ViewOncePhotoTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import com.app.screentime.core.model.MessageType
import com.app.screentime.core.network.dto.UploadImagePart
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

class SendMessageUseCase @Inject constructor(
    private val messagesApi: MessagesApi,
    private val winterApi: WinterApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val wsClient: WinterWebSocketClient,
    private val localStorage: LocalChatStorage,
    private val quotaLimitManager: com.app.screentime.core.network.quota.QuotaLimitManager,
    private val e2eCryptoManager: com.app.screentime.core.network.security.E2ECryptoManager
) {
    suspend operator fun invoke(
        receiverId: String,
        text: String,
        conversationId: String? = null,
        messageId: String? = null
    ): Result<ChatMessage> {
        val trimmed = text.trim()
        if (trimmed.isBlank()) {
            return Result.failure(IllegalArgumentException("Message content cannot be blank"))
        }

        val token = preferencesManager.getToken().orEmpty().ifBlank { sessionManager.getToken().orEmpty() }
        val myUserId = (sessionManager.userId ?: preferencesManager.getUserId() ?: "me").trim()
        val timestamp = System.currentTimeMillis()

        // 1. Optimistic Local Save in Room DB with unique client_message_id
        val clientMessageId = messageId ?: UUID.randomUUID().toString()
        val localMsg = ChatMessage(
            id = clientMessageId,
            senderId = myUserId,
            receiverId = receiverId,
            text = trimmed,
            timestamp = timestamp,
            conversationId = conversationId,
            isSending = true,
            isFailed = false,
            clientMessageId = clientMessageId
        )
        localStorage.saveMessage(receiverId, localMsg)

        if (token.isBlank()) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, clientMessageId, failedMsg)
            return Result.failure(Exception("Authentication token required"))
        }

        // 2. Persistent Server Sync with client_message_id
        val targetConvId = conversationId?.takeIf { it.isNotBlank() } ?: "conv_${receiverId}"
        return try {
            val outgoingContent = e2eCryptoManager.encrypt(trimmed, receiverId, token)
            val response = messagesApi.sendMessage(
                token = token,
                conversationId = targetConvId,
                content = outgoingContent,
                userId = myUserId,
                receiverId = receiverId,
                clientMessageId = clientMessageId
            )
            val quotaInfo = response.toQuotaLimitInfo()
            if (quotaInfo.isLimitReached || quotaInfo.quotaRemaining != null || quotaInfo.quotaLimit != null) {
                quotaLimitManager.updateChatQuota(quotaInfo)
            }
            if (response.effectiveIsLimitReached) {
                val failedMsg = localMsg.copy(isSending = false, isFailed = true)
                localStorage.replaceOrSaveMessage(receiverId, clientMessageId, failedMsg)
                return Result.failure(Exception(response.effectiveLimitMessage ?: "Message limit reached"))
            }
            if (response.success && response.data != null) {
                val dto = response.data!!
                val parsedTime = try {
                    if (!dto.created_at.isNullOrBlank()) {
                        java.time.Instant.parse(dto.created_at).toEpochMilli()
                    } else {
                        timestamp
                    }
                } catch (e: Exception) {
                    timestamp
                }
                val confirmedMsg = ChatMessage(
                    id = dto.id.ifBlank { clientMessageId },
                    senderId = dto.sender_id.ifBlank { myUserId },
                    receiverId = dto.receiver_id?.ifBlank { receiverId } ?: receiverId,
                    text = trimmed,
                    timestamp = parsedTime,
                    conversationId = dto.conversation_id.ifBlank { targetConvId },
                    isSending = false,
                    isFailed = false,
                    clientMessageId = dto.client_message_id ?: clientMessageId
                )
                localStorage.replaceOrSaveMessage(receiverId, clientMessageId, confirmedMsg)
                Result.success(confirmedMsg)
            } else {
                // Fallback to winterApi.sendChatMessage
                try {
                    val dto = winterApi.sendChatMessage(token = token, receiverId = receiverId, content = outgoingContent)
                    val parsedTime = try {
                        if (dto.created_at.isNotBlank()) {
                            java.time.Instant.parse(dto.created_at).toEpochMilli()
                        } else {
                            timestamp
                        }
                    } catch (e: Exception) {
                        timestamp
                    }
                    val confirmedMsg = ChatMessage(
                        id = dto.id.ifBlank { clientMessageId },
                        senderId = dto.sender_id.ifBlank { myUserId },
                        receiverId = dto.receiver_id.ifBlank { receiverId },
                        text = dto.content.ifBlank { trimmed },
                        timestamp = parsedTime,
                        conversationId = conversationId,
                        isSending = false,
                        isFailed = false,
                        clientMessageId = clientMessageId
                    )
                    localStorage.replaceOrSaveMessage(receiverId, clientMessageId, confirmedMsg)
                    Result.success(confirmedMsg)
                } catch (fallbackEx: Exception) {
                    val failedMsg = localMsg.copy(isSending = false, isFailed = true)
                    localStorage.replaceOrSaveMessage(receiverId, clientMessageId, failedMsg)
                    Result.failure(Exception(response.message.ifBlank { fallbackEx.message ?: "Failed to send message" }))
                }
            }
        } catch (e: Exception) {
            try {
                val dto = winterApi.sendChatMessage(token = token, receiverId = receiverId, content = trimmed)
                val parsedTime = try {
                    if (dto.created_at.isNotBlank()) {
                        java.time.Instant.parse(dto.created_at).toEpochMilli()
                    } else {
                        timestamp
                    }
                } catch (pe: Exception) {
                    timestamp
                }
                val confirmedMsg = ChatMessage(
                    id = dto.id.ifBlank { clientMessageId },
                    senderId = dto.sender_id.ifBlank { myUserId },
                    receiverId = dto.receiver_id.ifBlank { receiverId },
                    text = dto.content.ifBlank { trimmed },
                    timestamp = parsedTime,
                    conversationId = conversationId,
                    isSending = false,
                    isFailed = false,
                    clientMessageId = clientMessageId
                )
                localStorage.replaceOrSaveMessage(receiverId, clientMessageId, confirmedMsg)
                Result.success(confirmedMsg)
            } catch (fallbackEx: Exception) {
                val failedMsg = localMsg.copy(isSending = false, isFailed = true)
                localStorage.replaceOrSaveMessage(receiverId, clientMessageId, failedMsg)
                Result.failure(e)
            }
        }
    }

    private fun resolveUserId(token: String): String? {
        val uid = sessionManager.userId ?: preferencesManager.getUserId()
        if (!uid.isNullOrBlank()) return uid
        if (token.startsWith("token_")) {
            return token.removePrefix("token_")
        }
        return null
    }
}

data class GetMessagesResult(
    val messages: List<ChatMessage> = emptyList(),
    val page: Int = 1,
    val hasMore: Boolean = false,
    val nextPage: Int? = null,
    val totalCount: Int = 0
)

class GetMessagesUseCase @Inject constructor(
    private val messagesApi: MessagesApi,
    private val winterApi: WinterApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val localStorage: LocalChatStorage,
    private val quotaLimitManager: com.app.screentime.core.network.quota.QuotaLimitManager,
    private val viewOncePhotoTracker: ViewOncePhotoTracker,
    private val e2eCryptoManager: com.app.screentime.core.network.security.E2ECryptoManager
) {
    val currentUserId: String
        get() = sessionManager.userId ?: preferencesManager.getUserId() ?: "me"

    fun getMessagesFlow(partnerId: String): Flow<List<ChatMessage>> {
        return localStorage.getMessagesFlow(partnerId).map { list ->
            list.filter { msg ->
                !((msg.type == MessageType.IMAGE || msg.type == MessageType.AUDIO) && viewOncePhotoTracker.isViewed(msg.id, msg.clientMessageId, msg.mediaUrl ?: msg.text))
            }
        }
    }

    suspend fun fetchMessagesPage(
        partnerId: String,
        conversationId: String? = null,
        page: Int = 1,
        limit: Int = 20,
        before: String? = null
    ): GetMessagesResult {
        val token = preferencesManager.getToken().orEmpty()
        val userId = preferencesManager.getUserId() ?: sessionManager.userId ?: "user"

        if (token.isBlank() || partnerId.isBlank()) {
            val local = localStorage.getMessages(partnerId)
            return GetMessagesResult(messages = local, page = page, hasMore = false)
        }

        // Preload recipient public key immediately as page opens so sending has 0ms key lookup delay
        e2eCryptoManager.preloadRecipientPublicKey(partnerId, token)

        var resolvedConvId = conversationId?.takeIf { it.isNotBlank() }
        if (resolvedConvId == null) {
            resolvedConvId = localStorage.getMessages(partnerId)
                .lastOrNull { !it.conversationId.isNullOrBlank() }?.conversationId
        }

        if (resolvedConvId == null) {
            try {
                val convsRes = messagesApi.getConversations(token = token, page = 1, limit = 50, userId = userId)
                val matching = convsRes.getItems().firstOrNull { it.partner_user_id == partnerId }
                if (matching != null && matching.id.isNotBlank()) {
                    resolvedConvId = matching.id
                }
            } catch (_: Exception) {}
        }

        val targetConvId = resolvedConvId ?: "conv_$partnerId"
        val remoteList = mutableListOf<ChatMessage>()
        var paginationInfo = com.app.screentime.core.network.dto.MessagePaginationInfo()

        try {
            val res = messagesApi.getConversationMessages(
                token = token,
                conversationId = targetConvId,
                page = page,
                limit = limit,
                before = before,
                userId = userId
            )
            val quotaInfo = res.toQuotaLimitInfo()
            if (quotaInfo.isLimitReached || quotaInfo.quotaRemaining != null || quotaInfo.quotaLimit != null) {
                quotaLimitManager.updateChatQuota(quotaInfo)
            }
            paginationInfo = res.getPaginationInfo()
            val msgs = res.getMessageList()
            if (msgs.isNotEmpty()) {
                for (dto in msgs) {
                    if (dto.content.isBlank()) continue
                    val parsedTime = try {
                        if (!dto.created_at.isNullOrBlank()) {
                            java.time.Instant.parse(dto.created_at).toEpochMilli()
                        } else {
                            System.currentTimeMillis()
                        }
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }
                    val decryptedContent = if (e2eCryptoManager.isE2EE(dto.content)) {
                        e2eCryptoManager.decrypt(dto.content, partnerId, token)
                    } else {
                        dto.content
                    }
                    val (resolvedType, resolvedMediaUrl) = parseMessageTypeAndUrl(decryptedContent)
                    if (resolvedType == MessageType.IMAGE && viewOncePhotoTracker.isViewed(dto.id, dto.client_message_id, decryptedContent)) {
                        continue
                    }
                    remoteList.add(
                        ChatMessage(
                            id = dto.id.ifBlank { "msg_${parsedTime}_${UUID.randomUUID().toString().take(6)}" },
                            senderId = dto.sender_id.ifBlank { partnerId },
                            receiverId = dto.receiver_id ?: if (dto.sender_id == userId) partnerId else userId,
                            text = decryptedContent,
                            timestamp = parsedTime,
                            conversationId = dto.conversation_id ?: targetConvId,
                            type = resolvedType,
                            mediaUrl = resolvedMediaUrl,
                            isSending = false,
                            isFailed = false,
                            clientMessageId = dto.client_message_id
                        )
                    )
                }
            }
        } catch (_: Exception) {}

        // Fallback or secondary sync from winterApi only on first page if remoteList is empty
        if (remoteList.isEmpty() && page == 1 && before.isNullOrBlank()) {
            try {
                val res = winterApi.getChatMessages(token = token, partnerId = partnerId)
                if (res.messages.isNotEmpty()) {
                    for (dto in res.messages) {
                        if (dto.content.isBlank()) continue
                        val parsedTime = try {
                            if (dto.created_at.isNotBlank()) {
                                java.time.Instant.parse(dto.created_at).toEpochMilli()
                            } else {
                                System.currentTimeMillis()
                            }
                        } catch (e: Exception) {
                            System.currentTimeMillis()
                        }
                        val decryptedContent = if (e2eCryptoManager.isE2EE(dto.content)) {
                            e2eCryptoManager.decrypt(dto.content, partnerId, token)
                        } else {
                            dto.content
                        }
                        val (resolvedType, resolvedMediaUrl) = parseMessageTypeAndUrl(decryptedContent)
                        if (resolvedType == MessageType.IMAGE && viewOncePhotoTracker.isViewed(dto.id, null, decryptedContent)) {
                            continue
                        }
                        remoteList.add(
                            ChatMessage(
                                id = dto.id.ifBlank { "msg_${parsedTime}_${UUID.randomUUID().toString().take(6)}" },
                                senderId = dto.sender_id.ifBlank { partnerId },
                                receiverId = dto.receiver_id.ifBlank { userId },
                                text = decryptedContent,
                                timestamp = parsedTime,
                                conversationId = targetConvId,
                                type = resolvedType,
                                mediaUrl = resolvedMediaUrl,
                                isSending = false,
                                isFailed = false
                            )
                        )
                    }
                }
            } catch (_: Exception) {}
        }

        // Save valid remote messages to Room DB
        if (remoteList.isNotEmpty()) {
            localStorage.saveMessages(partnerId, remoteList)
        }

        val hasMore = if (paginationInfo.hasMore) {
            true
        } else if (paginationInfo.nextPage != null) {
            true
        } else {
            remoteList.size >= limit
        }

        return GetMessagesResult(
            messages = remoteList,
            page = paginationInfo.page,
            hasMore = hasMore,
            nextPage = paginationInfo.nextPage,
            totalCount = paginationInfo.totalCount
        )
    }

    suspend operator fun invoke(partnerId: String, conversationId: String? = null): List<ChatMessage> {
        fetchMessagesPage(partnerId = partnerId, conversationId = conversationId, page = 1)
        return localStorage.getMessages(partnerId).filter { msg ->
            !(msg.type == MessageType.IMAGE && viewOncePhotoTracker.isViewed(msg.id, msg.clientMessageId, msg.mediaUrl ?: msg.text))
        }
    }

    private fun resolveUserId(token: String): String? {
        val uid = sessionManager.userId ?: preferencesManager.getUserId()
        if (!uid.isNullOrBlank()) return uid
        if (token.startsWith("token_")) {
            return token.removePrefix("token_")
        }
        return null
    }
}

class ObserveMessagesUseCase @Inject constructor(
    private val wsClient: WinterWebSocketClient,
    private val localStorage: LocalChatStorage,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager
) {
    val currentUserId: String
        get() = sessionManager.userId ?: preferencesManager.getUserId() ?: "user"

    operator fun invoke(): SharedFlow<WSMessage> {
        if (!wsClient.isConnected()) {
            wsClient.connect()
        }
        return wsClient.eventsFlow
    }

    suspend fun saveIncoming(partnerId: String, msg: ChatMessage) {
        if (msg.text.isNotBlank()) {
            localStorage.saveMessage(partnerId, msg)
        }
    }
}

class GetConversationsUseCase @Inject constructor(
    private val messagesApi: MessagesApi,
    private val preferencesManager: PreferencesManager,
    private val localStorage: LocalChatStorage
) {
    suspend operator fun invoke(): List<Conversation> {
        val token = preferencesManager.getToken().orEmpty()
        val userId = preferencesManager.getUserId()

        if (token.isBlank()) {
            return emptyList()
        }

        val list = mutableListOf<Conversation>()

        try {
            val response = messagesApi.getConversations(
                token = token,
                page = 1,
                limit = 20,
                userId = userId
            )
            val items = response.getItems()
            for (item in items) {
                val partnerId = item.partner_user_id
                if (partnerId.isBlank()) continue

                val serverTime = item.last_message_at?.let { timeStr ->
                    try {
                        java.time.Instant.parse(timeStr).toEpochMilli()
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }
                } ?: System.currentTimeMillis()

                val latestLocal = localStorage.getLatestMessage(partnerId)
                val (msg, time) = if (latestLocal != null && latestLocal.timestamp >= serverTime) {
                    latestLocal.text to latestLocal.timestamp
                } else {
                    (item.last_message_text ?: "") to serverTime
                }

                if (msg.isBlank() && item.last_message_text.isNullOrBlank() && latestLocal == null) {
                    continue
                }

                list.add(
                    Conversation(
                        id = item.id.ifBlank { "conv_$partnerId" },
                        modelId = partnerId,
                        modelName = item.partner_name,
                        modelAvatarUrl = item.partner_photo_url.orEmpty(),
                        lastMessage = msg,
                        lastMessageTime = time,
                        unreadCount = item.unread_count,
                        isOnline = false
                    )
                )
            }
        } catch (e: Exception) {
            // Handle error / return current list
        }

        // Sort by most recent conversation first (active chats at top)
        return list.sortedByDescending { it.lastMessageTime }
    }
}

@Singleton
class SendImageMessageUseCase @Inject constructor(
    private val winterApi: WinterApi,
    private val messagesApi: MessagesApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val localStorage: LocalChatStorage,
    private val quotaLimitManager: com.app.screentime.core.network.quota.QuotaLimitManager
) {
    suspend operator fun invoke(
        receiverId: String,
        part: UploadImagePart,
        localUriString: String? = null,
        conversationId: String? = null,
        clientMessageId: String? = null
    ): Result<ChatMessage> {
        val token = preferencesManager.getToken().orEmpty().ifBlank { sessionManager.getToken().orEmpty() }
        val myUserId = (sessionManager.userId ?: preferencesManager.getUserId() ?: "me").trim()
        val timestamp = System.currentTimeMillis()
        val cMsgId = clientMessageId ?: "local_img_${timestamp}_${UUID.randomUUID().toString().take(6)}"

        // 1. Optimistic Local Save in Room DB with local image URI so UI renders immediately
        val localMsg = ChatMessage(
            id = cMsgId,
            senderId = myUserId,
            receiverId = receiverId,
            text = localUriString ?: "[Photo]",
            timestamp = timestamp,
            conversationId = conversationId,
            type = MessageType.IMAGE,
            mediaUrl = localUriString,
            isSending = true,
            isFailed = false,
            clientMessageId = cMsgId
        )
        localStorage.saveMessage(receiverId, localMsg)

        if (token.isBlank()) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            return Result.failure(Exception("Authentication token required"))
        }

        // 2. Upload image to server
        val uploadResult = try {
            winterApi.uploadImageParts(token, listOf(part))
        } catch (e: Exception) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            return Result.failure(e)
        }

        val uploadedUrls = uploadResult.getUploadedUrls()
        val remoteUrl = uploadedUrls.firstOrNull()
        if (remoteUrl.isNullOrBlank()) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            return Result.failure(Exception(uploadResult.message.ifBlank { "Failed to upload image" }))
        }

        // 3. Send message with the uploaded image URL as content
        val targetConvId = conversationId?.takeIf { it.isNotBlank() } ?: "conv_${receiverId}"
        return try {
            val response = messagesApi.sendMessage(
                token = token,
                conversationId = targetConvId,
                content = remoteUrl,
                userId = myUserId,
                receiverId = receiverId,
                clientMessageId = cMsgId
            )
            val quotaInfo = response.toQuotaLimitInfo()
            if (quotaInfo.isLimitReached || quotaInfo.quotaRemaining != null || quotaInfo.quotaLimit != null) {
                quotaLimitManager.updateChatQuota(quotaInfo)
            }
            if (response.effectiveIsLimitReached) {
                val failedMsg = localMsg.copy(isSending = false, isFailed = true)
                localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
                return Result.failure(Exception(response.effectiveLimitMessage ?: "Message limit reached"))
            }

            val confirmedMsg = if (response.success && response.data != null) {
                val dto = response.data!!
                val parsedTime = try {
                    if (!dto.created_at.isNullOrBlank()) {
                        java.time.Instant.parse(dto.created_at).toEpochMilli()
                    } else {
                        timestamp
                    }
                } catch (e: Exception) {
                    timestamp
                }
                ChatMessage(
                    id = dto.id.ifBlank { cMsgId },
                    senderId = dto.sender_id.ifBlank { myUserId },
                    receiverId = dto.receiver_id?.ifBlank { receiverId } ?: receiverId,
                    text = remoteUrl,
                    timestamp = parsedTime,
                    conversationId = dto.conversation_id.ifBlank { targetConvId },
                    type = MessageType.IMAGE,
                    mediaUrl = remoteUrl,
                    isSending = false,
                    isFailed = false,
                    clientMessageId = dto.client_message_id ?: cMsgId
                )
            } else {
                // Fallback to winterApi.sendChatMessage
                val dto = winterApi.sendChatMessage(token = token, receiverId = receiverId, content = remoteUrl)
                val parsedTime = try {
                    if (dto.created_at.isNotBlank()) {
                        java.time.Instant.parse(dto.created_at).toEpochMilli()
                    } else {
                        timestamp
                    }
                } catch (e: Exception) {
                    timestamp
                }
                ChatMessage(
                    id = dto.id.ifBlank { cMsgId },
                    senderId = dto.sender_id.ifBlank { myUserId },
                    receiverId = dto.receiver_id.ifBlank { receiverId },
                    text = remoteUrl,
                    timestamp = parsedTime,
                    conversationId = targetConvId,
                    type = MessageType.IMAGE,
                    mediaUrl = remoteUrl,
                    isSending = false,
                    isFailed = false,
                    clientMessageId = cMsgId
                )
            }
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, confirmedMsg)
            Result.success(confirmedMsg)
        } catch (e: Exception) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            Result.failure(e)
        }
    }
}

@Singleton
class SendAudioMessageUseCase @Inject constructor(
    private val winterApi: WinterApi,
    private val messagesApi: MessagesApi,
    private val sessionManager: SessionManager,
    private val preferencesManager: PreferencesManager,
    private val localStorage: LocalChatStorage,
    private val quotaLimitManager: com.app.screentime.core.network.quota.QuotaLimitManager,
    private val e2eCryptoManager: com.app.screentime.core.network.security.E2ECryptoManager
) {
    suspend operator fun invoke(
        receiverId: String,
        audioFile: java.io.File,
        conversationId: String? = null,
        clientMessageId: String? = null
    ): Result<ChatMessage> {
        val token = preferencesManager.getToken().orEmpty().ifBlank { sessionManager.getToken().orEmpty() }
        val myUserId = (sessionManager.userId ?: preferencesManager.getUserId() ?: "me").trim()
        val timestamp = System.currentTimeMillis()
        val cMsgId = clientMessageId ?: "local_audio_${timestamp}_${UUID.randomUUID().toString().take(6)}"

        // 1. Optimistic Local Save with local file path so UI displays immediately
        val localMsg = ChatMessage(
            id = cMsgId,
            senderId = myUserId,
            receiverId = receiverId,
            text = audioFile.absolutePath,
            timestamp = timestamp,
            conversationId = conversationId,
            type = MessageType.AUDIO,
            mediaUrl = audioFile.absolutePath,
            isSending = true,
            isFailed = false,
            clientMessageId = cMsgId
        )
        localStorage.saveMessage(receiverId, localMsg)

        if (token.isBlank()) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            return Result.failure(Exception("Authentication token required"))
        }

        // 2. Read audio bytes, encrypt with E2EE, and upload
        val bytes = try {
            audioFile.readBytes()
        } catch (e: Exception) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            return Result.failure(e)
        }

        val uploadBytes = try {
            e2eCryptoManager.encryptAudioBytes(bytes, receiverId, token)
        } catch (e: Exception) {
            bytes
        }

        val audioPart = UploadImagePart(
            filename = "voice_note_${timestamp}.m4a",
            bytes = uploadBytes,
            mimeType = "audio/m4a",
            formKey = "images"
        )

        val uploadResult = try {
            winterApi.uploadImageParts(token, listOf(audioPart))
        } catch (e: Exception) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            return Result.failure(e)
        }

        val uploadedUrls = uploadResult.getUploadedUrls()
        val remoteUrl = uploadedUrls.firstOrNull()
        if (remoteUrl.isNullOrBlank()) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            return Result.failure(Exception(uploadResult.message.ifBlank { "Failed to upload voice message" }))
        }

        // 3. Send message with the E2EE-encrypted audio URL
        val targetConvId = conversationId?.takeIf { it.isNotBlank() } ?: "conv_${receiverId}"
        return try {
            val outgoingContent = e2eCryptoManager.encrypt(remoteUrl, receiverId, token)
            val response = messagesApi.sendMessage(
                token = token,
                conversationId = targetConvId,
                content = outgoingContent,
                userId = myUserId,
                receiverId = receiverId,
                clientMessageId = cMsgId
            )
            val quotaInfo = response.toQuotaLimitInfo()
            if (quotaInfo.isLimitReached || quotaInfo.quotaRemaining != null || quotaInfo.quotaLimit != null) {
                quotaLimitManager.updateChatQuota(quotaInfo)
            }
            if (response.effectiveIsLimitReached) {
                val failedMsg = localMsg.copy(isSending = false, isFailed = true)
                localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
                return Result.failure(Exception(response.effectiveLimitMessage ?: "Message limit reached"))
            }

            val confirmedMsg = if (response.success && response.data != null) {
                val dto = response.data!!
                val parsedTime = try {
                    if (!dto.created_at.isNullOrBlank()) {
                        java.time.Instant.parse(dto.created_at).toEpochMilli()
                    } else {
                        timestamp
                    }
                } catch (e: Exception) {
                    timestamp
                }
                ChatMessage(
                    id = dto.id.ifBlank { cMsgId },
                    senderId = dto.sender_id.ifBlank { myUserId },
                    receiverId = dto.receiver_id?.ifBlank { receiverId } ?: receiverId,
                    text = remoteUrl,
                    timestamp = parsedTime,
                    conversationId = dto.conversation_id.ifBlank { targetConvId },
                    type = MessageType.AUDIO,
                    mediaUrl = remoteUrl,
                    isSending = false,
                    isFailed = false,
                    clientMessageId = dto.client_message_id ?: cMsgId
                )
            } else {
                val dto = winterApi.sendChatMessage(token = token, receiverId = receiverId, content = remoteUrl)
                val parsedTime = try {
                    if (dto.created_at.isNotBlank()) {
                        java.time.Instant.parse(dto.created_at).toEpochMilli()
                    } else {
                        timestamp
                    }
                } catch (e: Exception) {
                    timestamp
                }
                ChatMessage(
                    id = dto.id.ifBlank { cMsgId },
                    senderId = dto.sender_id.ifBlank { myUserId },
                    receiverId = dto.receiver_id.ifBlank { receiverId },
                    text = remoteUrl,
                    timestamp = parsedTime,
                    conversationId = targetConvId,
                    type = MessageType.AUDIO,
                    mediaUrl = remoteUrl,
                    isSending = false,
                    isFailed = false,
                    clientMessageId = cMsgId
                )
            }
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, confirmedMsg)
            Result.success(confirmedMsg)
        } catch (e: Exception) {
            val failedMsg = localMsg.copy(isSending = false, isFailed = true)
            localStorage.replaceOrSaveMessage(receiverId, cMsgId, failedMsg)
            Result.failure(e)
        }
    }
}

fun parseMessageTypeAndUrl(content: String): Pair<MessageType, String?> {
    val isHttp = content.startsWith("http", ignoreCase = true)
    val isAudio = (isHttp || content.startsWith("/data/") || content.startsWith("/storage/")) && (
        content.endsWith(".m4a", ignoreCase = true) ||
        content.endsWith(".mp3", ignoreCase = true) ||
        content.endsWith(".aac", ignoreCase = true) ||
        content.endsWith(".wav", ignoreCase = true) ||
        content.endsWith(".ogg", ignoreCase = true) ||
        content.endsWith(".3gp", ignoreCase = true) ||
        content.endsWith(".enc", ignoreCase = true) ||
        content.contains("voice_note", ignoreCase = true) ||
        content.contains("/audio/", ignoreCase = true)
    )
    if (isAudio) {
        return MessageType.AUDIO to content
    }
    val isImage = isHttp && (
        content.endsWith(".jpg", ignoreCase = true) ||
        content.endsWith(".jpeg", ignoreCase = true) ||
        content.endsWith(".png", ignoreCase = true) ||
        content.endsWith(".webp", ignoreCase = true) ||
        content.contains("/uploads/images/", ignoreCase = true) ||
        content.contains("storage.googleapis.com", ignoreCase = true)
    )
    if (isImage) {
        return MessageType.IMAGE to content
    }
    return MessageType.TEXT to null
}



package com.app.screentime.feature.chat.data.local

import com.app.screentime.core.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalChatStorage @Inject constructor(
    private val chatDao: ChatDao
) {
    fun getMessagesFlow(partnerId: String): Flow<List<ChatMessage>> {
        return chatDao.getMessagesFlow(partnerId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getMessages(partnerId: String): List<ChatMessage> {
        return chatDao.getMessages(partnerId).map { it.toDomain() }
    }

    suspend fun getLatestMessage(partnerId: String): ChatMessage? {
        return chatDao.getLatestMessage(partnerId)?.toDomain()
    }

    suspend fun saveMessage(partnerId: String, message: ChatMessage) {
        if (message.text.isBlank()) return
        val entity = ChatMessageEntity.fromDomain(partnerId, message)
        chatDao.insertMessage(entity)
    }

    suspend fun replaceOrSaveMessage(partnerId: String, tempId: String, confirmedMessage: ChatMessage) {
        if (confirmedMessage.text.isBlank()) return
        if (tempId != confirmedMessage.id) {
            chatDao.deleteMessage(tempId)
        }
        val entity = ChatMessageEntity.fromDomain(partnerId, confirmedMessage)
        chatDao.insertMessage(entity)
    }

    suspend fun saveMessages(partnerId: String, newMessages: List<ChatMessage>) {
        val validEntities = newMessages
            .filter { it.text.isNotBlank() }
            .map { ChatMessageEntity.fromDomain(partnerId, it) }
        if (validEntities.isNotEmpty()) {
            chatDao.insertMessages(validEntities)
        }
    }

    suspend fun deleteMessage(id: String) {
        chatDao.deleteMessage(id)
    }

    suspend fun clearMessages(partnerId: String) {
        chatDao.clearMessagesForPartner(partnerId)
    }

    fun purgeExpired() {
        // Room DB handles persistence permanently with unique message IDs.
    }
}

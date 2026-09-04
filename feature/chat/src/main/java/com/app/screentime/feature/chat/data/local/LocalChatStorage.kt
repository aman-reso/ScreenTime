package com.app.screentime.feature.chat.data.local

import android.content.Context
import android.content.SharedPreferences
import com.app.screentime.core.model.ChatMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalChatStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("chatty_ephemeral_chat_prefs", Context.MODE_PRIVATE)

    private val json = Json { ignoreUnknownKeys = true }
    private val ttlMillis: Long = 24 * 60 * 60 * 1000L // 24 Hours

    @Synchronized
    fun getMessages(partnerId: String): List<ChatMessage> {
        val now = System.currentTimeMillis()
        val raw = prefs.getString("chat_$partnerId", null) ?: return emptyList()
        return try {
            val list = json.decodeFromString<List<ChatMessage>>(raw)
            val valid = list.filter { it.text.isNotBlank() && (now - it.timestamp < ttlMillis) }
            deduplicateMessages(valid)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @Synchronized
    fun getLatestMessage(partnerId: String): ChatMessage? {
        return getMessages(partnerId).maxByOrNull { it.timestamp }
    }

    @Synchronized
    fun saveMessage(partnerId: String, message: ChatMessage) {
        if (message.text.isBlank()) return
        val current = getMessages(partnerId).toMutableList()
        if (current.none { it.id == message.id }) {
            current.add(message)
            val deduplicated = deduplicateMessages(current)
            prefs.edit().putString("chat_$partnerId", json.encodeToString(deduplicated)).apply()
        }
    }

    @Synchronized
    fun replaceOrSaveMessage(partnerId: String, tempId: String, confirmedMessage: ChatMessage) {
        if (confirmedMessage.text.isBlank()) return
        val current = getMessages(partnerId).toMutableList()
        val index = current.indexOfFirst { it.id == tempId }
        if (index != -1) {
            current[index] = confirmedMessage
        } else if (current.none { it.id == confirmedMessage.id }) {
            current.add(confirmedMessage)
        }
        val deduplicated = deduplicateMessages(current)
        prefs.edit().putString("chat_$partnerId", json.encodeToString(deduplicated)).apply()
    }

    @Synchronized
    fun saveMessages(partnerId: String, newMessages: List<ChatMessage>) {
        val current = getMessages(partnerId).toMutableList()
        val existingIds = current.map { it.id }.toSet()
        for (m in newMessages) {
            if (m.text.isNotBlank() && m.id !in existingIds) {
                current.add(m)
            }
        }
        val deduplicated = deduplicateMessages(current)
        prefs.edit().putString("chat_$partnerId", json.encodeToString(deduplicated)).apply()
    }

    private fun deduplicateMessages(messages: List<ChatMessage>): List<ChatMessage> {
        val result = mutableListOf<ChatMessage>()
        val seenIds = mutableSetOf<String>()
        for (msg in messages) {
            if (msg.text.isBlank() || msg.id in seenIds) continue
            val duplicate = result.any {
                it.senderId == msg.senderId &&
                it.receiverId == msg.receiverId &&
                it.text == msg.text &&
                Math.abs(it.timestamp - msg.timestamp) < 5000
            }
            if (!duplicate) {
                result.add(msg)
                seenIds.add(msg.id)
            }
        }
        return result.sortedBy { it.timestamp }
    }

    @Synchronized
    fun purgeExpired() {
        val now = System.currentTimeMillis()
        val all = prefs.all
        val editor = prefs.edit()
        for ((key, value) in all) {
            if (key.startsWith("chat_") && value is String) {
                try {
                    val list = json.decodeFromString<List<ChatMessage>>(value)
                    val valid = list.filter { now - it.timestamp < ttlMillis }
                    if (valid.isEmpty()) {
                        editor.remove(key)
                    } else if (valid.size != list.size) {
                        editor.putString(key, json.encodeToString(valid))
                    }
                } catch (e: Exception) {
                    editor.remove(key)
                }
            }
        }
        editor.apply()
    }
}

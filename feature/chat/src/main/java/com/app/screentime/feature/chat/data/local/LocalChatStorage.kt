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
            list.filter { now - it.timestamp < ttlMillis }
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
        val current = getMessages(partnerId).toMutableList()
        if (current.none { it.id == message.id }) {
            current.add(message)
            prefs.edit().putString("chat_$partnerId", json.encodeToString(current)).apply()
        }
    }

    @Synchronized
    fun saveMessages(partnerId: String, newMessages: List<ChatMessage>) {
        val current = getMessages(partnerId).toMutableList()
        val existingIds = current.map { it.id }.toSet()
        for (m in newMessages) {
            if (m.id !in existingIds) {
                current.add(m)
            }
        }
        prefs.edit().putString("chat_$partnerId", json.encodeToString(current)).apply()
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

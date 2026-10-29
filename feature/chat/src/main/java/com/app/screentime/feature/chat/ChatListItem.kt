package com.app.screentime.feature.chat

import com.app.screentime.core.model.ChatMessage

/**
 * Sealed interface representing elements in the chat conversation list.
 * Can be a standard ChatMessage or an in-stream sponsored Native Ad Bubble.
 */
sealed interface ChatListItem {
    val itemId: String

    data class MessageItem(val message: ChatMessage) : ChatListItem {
        override val itemId: String get() = message.id
    }

    data class NativeAdItem(
        val adSlotIndex: Int,
        override val itemId: String = "in_chat_native_ad_$adSlotIndex"
    ) : ChatListItem
}

package com.app.screentime.core.network.mock

import com.app.screentime.core.network.dto.*

/**
 * High-quality offline simulated data for the Dating, Chat, and Call flows.
 * Provides fallback data when the remote backend endpoint returns HTML or is unreachable.
 */
object ChattyMockDataSource {

    val mockUsers = listOf(
        UserDto(
            id = "usr_anastasia",
            name = "Anastasia Romanova",
            email = "anastasia@chatty.app",
            phone = "+919876543210",
            bio = "UX designer & coffee lover ☕ Traveling and exploring aesthetics in everyday life.",
            avatar_url = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80",
            voice_rate_per_min = 15.0,
            group_rate_per_min = 7.0,
            chat_rate_per_msg = 1.0,
            is_online = true,
            is_busy = false
        ),
        UserDto(
            id = "usr_priya",
            name = "Priya Sharma",
            email = "priya@chatty.app",
            phone = "+919812345678",
            bio = "Architect, yoga enthusiast 🧘‍♀️ Architecture by day, indie music listener by night.",
            avatar_url = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&auto=format&fit=crop&q=80",
            voice_rate_per_min = 20.0,
            group_rate_per_min = 10.0,
            chat_rate_per_msg = 2.0,
            is_online = true,
            is_busy = false
        ),
        UserDto(
            id = "usr_elena",
            name = "Elena Rostova",
            email = "elena@chatty.app",
            phone = "+919922334455",
            bio = "Photographer & artist 🎨 Love capturing sunset horizons and spontaneous street moments.",
            avatar_url = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&auto=format&fit=crop&q=80",
            voice_rate_per_min = 25.0,
            group_rate_per_min = 12.0,
            chat_rate_per_msg = 2.0,
            is_online = false,
            is_busy = false
        ),
        UserDto(
            id = "usr_zoya",
            name = "Zoya Khan",
            email = "zoya@chatty.app",
            phone = "+919123456780",
            bio = "Book lover, culinary explorer & classical dancer ✨ Coffee & deep conversations.",
            avatar_url = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=800&auto=format&fit=crop&q=80",
            voice_rate_per_min = 15.0,
            group_rate_per_min = 8.0,
            chat_rate_per_msg = 1.0,
            is_online = true,
            is_busy = false
        ),
        UserDto(
            id = "usr_sophia",
            name = "Sophia Chen",
            email = "sophia@chatty.app",
            phone = "+919345678901",
            bio = "Fintech product manager & weekend mountaineer 🏔️ Let's chat about dreams & goals.",
            avatar_url = "https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?w=800&auto=format&fit=crop&q=80",
            voice_rate_per_min = 18.0,
            group_rate_per_min = 9.0,
            chat_rate_per_msg = 1.5,
            is_online = true,
            is_busy = false
        )
    )

    val mockFavorites = listOf(
        mockUsers[0],
        mockUsers[1]
    )

    fun createMockAuthResponse(idToken: String, email: String?, name: String?): AuthResponse {
        val userName = name?.ifBlank { null } ?: "Aman Kumar"
        val userEmail = email?.ifBlank { null } ?: "user@chatty.app"
        val user = UserDto(
            id = "usr_me_${Math.abs(userEmail.hashCode())}",
            name = userName,
            email = userEmail,
            phone = userEmail,
            role = "user",
            avatar_url = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=800&auto=format&fit=crop&q=80",
            bio = "Member · Connect Chatty",
            is_online = true
        )
        return AuthResponse(
            user = user,
            token = "conn_jwt_simulated_${System.currentTimeMillis()}",
            is_new_user = false,
            wallet = WalletDto(
                user_id = user.id,
                balance = 1000.0,
                bonus_given = 100.0,
                total_spent = 0.0,
                total_earned = 0.0
            ),
            message = "Success"
        )
    }

    val mockConversations = listOf(
        ConversationDto(
            id = "conv_anastasia",
            partner_id = "usr_anastasia",
            user_id = "usr_anastasia",
            partner_name = "Anastasia Romanova",
            partner_avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80",
            last_message = "Hey! Loved your profile! Are you free for a call? 😊",
            last_message_time = System.currentTimeMillis() - 1000 * 60 * 12,
            unread_count = 1,
            is_online = true
        ),
        ConversationDto(
            id = "conv_priya",
            partner_id = "usr_priya",
            user_id = "usr_priya",
            partner_name = "Priya Sharma",
            partner_avatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&auto=format&fit=crop&q=80",
            last_message = "Sounds great! Looking forward to connecting soon ✨",
            last_message_time = System.currentTimeMillis() - 1000 * 60 * 65,
            unread_count = 0,
            is_online = true
        )
    )

    fun getMockMessages(partnerId: String): List<ChatMessageDto> {
        val now = System.currentTimeMillis()
        return listOf(
            ChatMessageDto(
                id = "msg_${partnerId}_1",
                sender_id = partnerId,
                receiver_id = "me",
                content = "Hey there! Nice to connect with you 👋",
                cost = 0.0,
                created_at = java.time.Instant.ofEpochMilli(now - 1000 * 60 * 30).toString()
            ),
            ChatMessageDto(
                id = "msg_${partnerId}_2",
                sender_id = "me",
                receiver_id = partnerId,
                content = "Hi! Great to meet you! How is your week going?",
                cost = 0.0,
                created_at = java.time.Instant.ofEpochMilli(now - 1000 * 60 * 20).toString()
            ),
            ChatMessageDto(
                id = "msg_${partnerId}_3",
                sender_id = partnerId,
                receiver_id = "me",
                content = "Going wonderful! Just finished work. Would love to voice chat if you're free!",
                cost = 0.0,
                created_at = java.time.Instant.ofEpochMilli(now - 1000 * 60 * 10).toString()
            )
        )
    }

    val mockLiveStreams = listOf(
        LiveStreamDto(
            stream_id = "live_stream_01",
            host_id = "usr_anastasia",
            host_name = "Anastasia Romanova",
            host_avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&auto=format&fit=crop&q=80",
            title = "Chill Evening Coffee & Chat ☕✨",
            viewer_count = 42,
            total_earned = 350.0,
            is_active = true,
            is_paid_mode = false,
            coin_rate_per_min = 10.0
        ),
        LiveStreamDto(
            stream_id = "live_stream_02",
            host_id = "usr_priya",
            host_name = "Priya Sharma",
            host_avatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800&auto=format&fit=crop&q=80",
            title = "Acoustic Guitar & Relaxing Vibes 🎸",
            viewer_count = 128,
            total_earned = 980.0,
            is_active = true,
            is_paid_mode = true,
            coin_rate_per_min = 15.0
        )
    )

    val mockWalletPacks = listOf(
        WalletPackDto(
            id = "pack_100",
            coins = 100,
            bonus_coins = 20,
            total_coins = 120,
            price_inr = 99.0,
            badge = "Starter",
            is_popular = false,
            description = "100 + 20 Bonus Coins"
        ),
        WalletPackDto(
            id = "pack_500",
            coins = 500,
            bonus_coins = 150,
            total_coins = 650,
            price_inr = 399.0,
            badge = "Most Popular",
            is_popular = true,
            description = "500 + 150 Bonus Coins"
        ),
        WalletPackDto(
            id = "pack_1500",
            coins = 1500,
            bonus_coins = 500,
            total_coins = 2000,
            price_inr = 999.0,
            badge = "Best Value",
            is_popular = false,
            description = "1500 + 500 Bonus Coins"
        )
    )
}

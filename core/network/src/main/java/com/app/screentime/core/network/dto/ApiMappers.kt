package com.app.screentime.core.network.dto

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.model.WalletTransaction
import com.app.screentime.core.model.TransactionType

fun UserDto.toModelProfile(): ModelProfile {
    return ModelProfile(
        id = this.id,
        name = this.name,
        bio = this.bio ?: "",
        avatarUrl = this.avatar_url ?: "",
        coverUrl = this.avatar_url ?: "",
        ratePerMinute = this.voice_rate_per_min.toInt().coerceAtLeast(1),
        chatRate = this.chat_rate_per_msg.toInt().coerceAtLeast(1),
        isOnline = this.is_online,
        isBusy = this.is_busy
    )
}

fun UserDto.toUser(): User {
    return User(
        id = this.id,
        phone = this.phone,
        name = this.name,
        role = UserRole.fromString(this.role),
        avatarUrl = this.avatar_url,
        bio = this.bio,
        voiceRatePerMin = this.voice_rate_per_min,
        chatRatePerMsg = this.chat_rate_per_msg,
        isOnline = this.is_online,
        isBusy = this.is_busy,
        createdAt = this.created_at
    )
}

fun TransactionDto.toWalletTransaction(): WalletTransaction {
    return WalletTransaction(
        id = this.id,
        type = TransactionType.fromString(this.type),
        amount = this.amount,
        description = this.description ?: "Transaction",
        timestamp = System.currentTimeMillis()
    )
}

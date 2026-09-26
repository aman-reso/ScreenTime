package com.app.screentime.core.network.dto

import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.model.User
import com.app.screentime.core.model.UserRole
import com.app.screentime.core.model.WalletTransaction
import com.app.screentime.core.model.TransactionType

fun UserDto.toModelProfile(): ModelProfile {
    return ModelProfile(
        id = this.id,
        name = this.getResolvedName(),
        bio = this.bio ?: "",
        avatarUrl = this.avatar_url ?: "",
        coverUrl = this.avatar_url ?: "",
        ratePerMinute = this.voice_rate_per_min.toInt().coerceAtLeast(1),
        chatRate = this.chat_rate_per_msg.toInt().coerceAtLeast(1),
        isOnline = this.is_online,
        isBusy = this.is_busy,
        height = this.height ?: this.height_cm?.let { "$it cm" },
        gender = this.gender,
        job = this.job ?: this.occupation,
        education = this.education,
        relationType = this.relation_type ?: this.dating_intent
    )
}

fun UserDto.toUser(): User {
    val roleEnum = UserRole.fromString(this.role)
    val mappedPrompts = this.prompts.map {
        com.app.screentime.core.model.ProfilePrompt(
            id = it.id,
            question = it.question,
            answer = it.answer
        )
    }
    return User(
        id = this.id,
        phone = this.phone,
        name = this.getResolvedName(),
        email = this.email,
        role = roleEnum,
        avatarUrl = this.avatar_url,
        photos = this.photos,
        bio = this.bio,
        aboutMe = this.about_me ?: this.bio,
        age = this.age,
        gender = this.gender,
        interests = this.interests,
        datingIntent = this.dating_intent,
        city = this.city,
        area = this.area,
        qualityScore = this.quality_score,
        job = this.job ?: this.occupation,
        occupation = this.occupation ?: this.job,
        height = this.height ?: this.height_cm?.let { "$it cm" },
        heightCm = this.height_cm,
        education = this.education,
        languages = this.languages,
        relationType = this.relation_type ?: this.dating_intent,
        dateOfBirth = this.date_of_birth ?: this.dob,
        dob = this.dob ?: this.date_of_birth,
        prompts = mappedPrompts,
        voiceRatePerMin = this.voice_rate_per_min,
        chatRatePerMsg = this.chat_rate_per_msg,
        isOnline = this.is_online,
        isBusy = this.is_busy,
        walletBalance = if (roleEnum == UserRole.USER) 1000.0 else 0.0,
        createdAt = this.created_at
    )
}

fun TransactionDto.toWalletTransaction(): WalletTransaction {
    val isPos = this.type.equals("credit", ignoreCase = true) ||
            this.direction.equals("in", ignoreCase = true) ||
            this.amount > 0

    val parsedTime = try {
        if (this.created_at.isNotBlank()) {
            java.time.Instant.parse(this.created_at).toEpochMilli()
        } else {
            System.currentTimeMillis()
        }
    } catch (_: Exception) {
        System.currentTimeMillis()
    }

    return WalletTransaction(
        id = this.id,
        type = TransactionType.fromString(this.transaction_type.ifBlank { this.type }),
        amount = this.amount,
        description = this.description ?: "Transaction",
        timestamp = parsedTime,
        balanceAfter = this.balance_after,
        direction = this.direction,
        transactionType = this.transaction_type,
        category = this.category,
        referenceId = this.reference_id ?: this.call_id ?: this.room_id,
        createdAt = this.created_at,
        isPositive = isPos
    )
}

fun DiscoveryMatchDto.toModelProfile(): ModelProfile {
    val photoUrl = this.primary_photo_url?.ifBlank { null }
        ?: this.avatar_url?.ifBlank { null }
        ?: ""
    val availablePhotos = this.additional_photos ?: this.photos
    val gallery = availablePhotos?.filter { it.isNotBlank() }?.ifEmpty { null }
        ?: if (photoUrl.isNotBlank()) listOf(photoUrl) else emptyList()
    val bioText = this.bio?.ifBlank { null }
        ?: this.score_breakdown?.explanation?.ifBlank { null }
        ?: ""

    val allTags = mutableListOf<String>()
    if (this.dating_intent.isNotBlank()) allTags.add(this.dating_intent)
    if (this.gender.isNotBlank()) allTags.add(this.gender)
    this.profession?.let { if (it.isNotBlank()) allTags.add(it) }
    this.interests?.let { allTags.addAll(it.filter { interest -> interest.isNotBlank() }) }

    val matchPercentage = if (this.compatibility_score > 0) {
        "${(this.compatibility_score * 100).toInt()}% Match"
    } else if ((this.score_breakdown?.overall_score ?: 0) > 0) {
        "${this.score_breakdown?.overall_score}% Match"
    } else {
        ""
    }

    val distStr = this.distance_display.ifBlank {
        if (this.distance_km > 0) "${this.distance_km} km away" else ""
    }

    val resolvedName = this.name?.ifBlank { null }
        ?: this.display_name.ifBlank { null }
        ?: "Member"

    val resolvedHeight = this.height ?: this.height_cm?.let { "$it cm" }
    val resolvedJob = this.job ?: this.occupation ?: this.profession
    val resolvedRelation = this.relation_type ?: this.dating_intent

    return ModelProfile(
        id = this.user_id,
        name = resolvedName,
        age = this.age,
        distance = distStr,
        location = this.city.ifBlank { "" },
        matchedPreferences = matchPercentage,
        bio = bioText,
        avatarUrl = photoUrl,
        coverUrl = photoUrl,
        galleryUrls = gallery,
        ratePerMinute = 10,
        chatRate = 1,
        isOnline = true,
        isBusy = false,
        rating = (this.compatibility_score * 5).toFloat().coerceIn(3.5f, 5.0f),
        reviewCount = (this.compatibility_score * 100).toInt().coerceAtLeast(12),
        height = resolvedHeight,
        gender = this.gender.ifBlank { null },
        job = resolvedJob,
        education = this.education,
        relationType = resolvedRelation.ifBlank { null },
        tags = allTags.distinct(),
        lat = this.fuzzed_lat,
        lng = this.fuzzed_lng
    )
}

fun DiscoveryMatchItemDto.toDiscoveryMatch(): com.app.screentime.core.model.DiscoveryMatch {
    return com.app.screentime.core.model.DiscoveryMatch(
        matchId = this.match_id,
        matchedUserId = this.matched_user_id,
        displayName = this.display_name.ifBlank { "Member" },
        age = this.age,
        city = this.city,
        area = this.area,
        primaryPhotoUrl = this.primary_photo_url.orEmpty(),
        datingIntent = this.dating_intent,
        bio = this.bio,
        matchedAt = this.matched_at
    )
}


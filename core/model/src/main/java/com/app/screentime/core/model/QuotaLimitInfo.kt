package com.app.screentime.core.model

import kotlinx.serialization.Serializable

@Serializable
data class QuotaLimitInfo(
    val isLimitReached: Boolean = false,
    val limitMessage: String? = null,
    val quotaRemaining: Int? = null,
    val quotaLimit: Int? = null,
    val rewardMessages: Int? = null,
    val gateType: String? = null
)

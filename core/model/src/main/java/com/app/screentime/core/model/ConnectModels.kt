package com.app.screentime.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class SiteCategory(val key: String, val displayName: String, val iconEmoji: String) {
    DOCTOR("doctor", "Doctor & Healthcare", "🩺"),
    COACHING("coaching", "Coaching & Tutoring", "📚"),
    SCHOOL("school", "School & Education", "🎓"),
    BUSINESS("business", "Business & Services", "💼"),
    PERSON("person", "Portfolio & Person", "👤"),
    RESTAURANT("restaurant", "Restaurant & Cafe", "🍽️"),
    SALON("salon", "Salon & Beauty", "💇"),
    FITNESS("fitness", "Fitness & Gym", "🏋️"),
    GROCERY("grocery", "Grocery & Market", "🥦");

    companion object {
        fun fromKey(key: String): SiteCategory {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: BUSINESS
        }
    }
}

@Serializable
enum class SiteStatus(val key: String, val label: String) {
    DRAFT("draft", "Draft"),
    LIVE("live", "Live"),
    SUSPENDED("suspended", "Suspended");

    companion object {
        fun fromKey(key: String): SiteStatus {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: DRAFT
        }
    }
}

@Serializable
data class SiteItem(
    val id: String,
    val slug: String,
    val category: SiteCategory,
    val status: SiteStatus,
    val primaryUrl: String,
    val aliasUrl: String,
    val title: String,
    val tagline: String = "",
    val totalViews: Long = 0L,
    val totalUniqueVisitors: Long = 0L,
    val currentMonthUniques: Int = 0,
    val adsEnabled: Boolean = false,
    val isEligibleForAds: Boolean = false,
    val milestoneProgressPercent: Double = 0.0,
    val thumbnailUrl: String? = null
)

@Serializable
data class TemplateItem(
    val id: String,
    val name: String,
    val slug: String,
    val categories: List<String> = emptyList(),
    val description: String = "",
    val thumbnailUrl: String = "",
    val isActive: Boolean = true
)

@Serializable
data class VoiceExtractionResult(
    val sessionId: String,
    val draftJson: String = "",
    val missingKeys: List<String> = emptyList(),
    val followUpQuestion: String? = null,
    val suggestedSlug: String = "",
    val suggestedTemplateId: String = "",
    val isReadyForReview: Boolean = false
)

@Serializable
data class AIParseResult(
    val provider: String = "Gemini",
    val slug: String = "",
    val category: String = "",
    val templateId: String = "",
    val slugAvailable: Boolean = false,
    val draftContentJson: String = "",
    val suggestions: List<String> = emptyList()
)

@Serializable
data class CatalogProduct(
    val id: String,
    val siteId: String,
    val name: String,
    val description: String = "",
    val price: Double = 0.0,
    val currency: String = "₹",
    val imageUrl: String = "",
    val categoryTag: String = "",
    val isAvailable: Boolean = true
)

@Serializable
data class MonetizationStats(
    val siteId: String,
    val totalViews: Long = 0L,
    val totalUniqueVisitors: Long = 0L,
    val currentMonthUniques: Int = 0,
    val adsEnabled: Boolean = false,
    val isEligibleForAds: Boolean = false,
    val milestoneProgressPercent: Double = 0.0,
    val targetVisitors: Int = 1000,
    val grossEarnings: Double = 0.0,
    val creatorShare: Double = 0.0
)

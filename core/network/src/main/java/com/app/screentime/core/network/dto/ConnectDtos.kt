package com.app.screentime.core.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

// ── 1. Auth DTOs ─────────────────────────────────────────────────────────────

@Serializable
data class ConnectOTPRequest(
    val phone: String? = null,
    val email: String? = null
)

@Serializable
data class ConnectOTPVerifyRequest(
    val phone: String? = null,
    val email: String? = null,
    val code: String,
    val name: String? = null
)

@Serializable
data class ConnectOTPResponse(
    val mock_code: String? = null,
    val message: String? = null
)

@Serializable
data class ConnectAuthResponse(
    val tenant: TenantDto? = null,
    val token: String = "",
    val is_new_user: Boolean = false,
    val message: String = ""
)

@Serializable
data class TenantDto(
    val id: String = "",
    val phone: String = "",
    val email: String = "",
    val full_name: String = "",
    val role: String = "creator",
    val status: String = "active",
    val created_at: String = "",
    val updated_at: String = ""
)

// ── 2. Templates DTOs ─────────────────────────────────────────────────────────

@Serializable
data class TemplateDto(
    val id: String = "",
    val name: String = "",
    val slug: String = "",
    val categories: List<String> = emptyList(),
    val description: String = "",
    val thumbnail_url: String = "",
    val is_active: Boolean = true
)

@Serializable
data class TemplateDetailResponse(
    val template: TemplateDto,
    val version: TemplateVersionDto? = null
)

@Serializable
data class TemplateVersionDto(
    val id: String = "",
    val template_id: String = "",
    val version_number: Int = 1,
    val schema: JsonElement? = null,
    val layout: List<String> = emptyList(),
    val theme: Map<String, String> = emptyMap(),
    val ad_slots: List<AdSlotDto> = emptyList()
)

@Serializable
data class AdSlotDto(
    val slot_id: String = "",
    val position: String = "",
    val format: String = ""
)

// ── 3. Sites DTOs ─────────────────────────────────────────────────────────────

@Serializable
data class UserSiteSummaryDto(
    val id: String = "",
    val slug: String = "",
    val category: String = "business",
    val status: String = "draft",
    val primary_url: String = "",
    val alias_url: String = "",
    val day_created: String = "",
    val total_views: Long = 0L,
    val total_unique_visitors: Long = 0L,
    val current_month_uniques: Int = 0,
    val ads_enabled: Boolean = false,
    val is_eligible_for_ads: Boolean = false,
    val milestone_progress_percent: Double = 0.0,
    val thumbnail_url: String? = null,
    val title: String = "",
    val tagline: String? = null
)

@Serializable
data class CheckSlugResponse(
    val slug: String = "",
    val available: Boolean = false,
    val message: String = ""
)

@Serializable
data class CreateSiteRequestDto(
    val template_id: String,
    val slug: String,
    val category: String,
    val content: JsonElement? = null,
    val meta_seo: Map<String, String>? = null,
    val images: List<String>? = null,
    val catalog_items: List<CreateCatalogItemRequestDto>? = null
)

@Serializable
data class CreateSiteResponseDto(
    val site: SiteDto? = null,
    val content: JsonElement? = null,
    val primary_url: String = "",
    val alias_url: String = ""
)

@Serializable
data class SiteDetailResponseDto(
    val site: SiteDto? = null,
    val content: JsonElement? = null,
    val analytics: AnalyticsSummaryDto? = null,
    val primary_url: String = "",
    val alias_url: String = ""
)

@Serializable
data class SiteDto(
    val id: String = "",
    val tenant_id: String = "",
    val template_version_id: String = "",
    val slug: String = "",
    val category: String = "business",
    val status: String = "draft",
    val ads_enabled: Boolean = false,
    val is_published: Boolean = false,
    val created_at: String = "",
    val updated_at: String = ""
)

@Serializable
data class UpdateSiteRequestDto(
    val slug: String? = null,
    val category: String? = null,
    val content: JsonElement? = null,
    val meta_seo: Map<String, String>? = null,
    val ads_enabled: Boolean? = null,
    val is_published: Boolean? = null
)

// ── 4. AI DTOs ───────────────────────────────────────────────────────────────

@Serializable
data class AIParseRequestDto(
    val instruction: String,
    val preferred_category: String? = null,
    val preferred_template_id: String? = null
)

@Serializable
data class AIParseResponseDto(
    val provider: String = "",
    val parsed_site: AIParsedSiteDto? = null,
    val slug_available: Boolean = false,
    val validation_errors: List<String> = emptyList(),
    val suggestions: List<String> = emptyList()
)

@Serializable
data class AIParsedSiteDto(
    val slug: String = "",
    val category: String = "",
    val template_id: String = "",
    val content: JsonElement? = null,
    val catalog_items: List<CreateCatalogItemRequestDto> = emptyList(),
    val meta_seo: Map<String, String> = emptyMap(),
    val images: List<String> = emptyList()
)

@Serializable
data class AIGenerateAndCreateRequestDto(
    val instruction: String,
    val slug_override: String? = null,
    val preferred_category: String? = null,
    val auto_publish: Boolean = true
)

@Serializable
data class AIGenerateAndCreateResponseDto(
    val message: String = "",
    val provider: String = "",
    val site_id: String = "",
    val slug: String = "",
    val category: String = "",
    val primary_url: String = "",
    val alias_url: String = "",
    val preview_url: String = "",
    val site: SiteDto? = null,
    val content: JsonElement? = null,
    val catalog_count: Int = 0
)

// ── 5. Voice DTOs ────────────────────────────────────────────────────────────

@Serializable
data class VoiceSessionStartRequestDto(
    val transcript: String,
    val category: String? = null,
    val template_id: String? = null
)

@Serializable
data class VoiceSessionPatchRequestDto(
    val session_id: String,
    val transcript: String
)

@Serializable
data class VoiceSessionResponseDto(
    val session_id: String = "",
    val extracted_draft: JsonElement? = null,
    val missing_required_keys: List<String> = emptyList(),
    val follow_up_question: String? = null,
    val suggested_slug: String = "",
    val suggested_template_id: String = "",
    val is_ready_for_review: Boolean = false,
    val followUpQuestion: String=""
)

// ── 6. Catalog DTOs ──────────────────────────────────────────────────────────

@Serializable
data class CatalogItemDto(
    val id: String = "",
    val site_id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val currency: String = "₹",
    val image_url: String = "",
    val category_tag: String = "",
    val is_available: Boolean = true,
    val display_order: Int = 0
)

@Serializable
data class CreateCatalogItemRequestDto(
    val name: String,
    val description: String = "",
    val price: Double = 0.0,
    val currency: String = "₹",
    val image_url: String = "",
    val category_tag: String = "",
    val is_available: Boolean = true,
    val display_order: Int = 0
)

@Serializable
data class UpdateCatalogItemRequestDto(
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val currency: String? = null,
    val image_url: String? = null,
    val category_tag: String? = null,
    val is_available: Boolean? = null,
    val display_order: Int? = null
)

// ── 7. Monetization & Payouts DTOs ───────────────────────────────────────────

@Serializable
data class AnalyticsSummaryDto(
    val site_id: String = "",
    val total_views: Long = 0L,
    val total_unique_visitors: Long = 0L,
    val current_month_uniques: Int = 0,
    val ads_enabled: Boolean = false,
    val is_eligible_for_ads: Boolean = false,
    val milestone_progress_percent: Double = 0.0,
    val daily_uniques: Map<String, Int> = emptyMap(),
    val top_referrers: Map<String, Int> = emptyMap(),
    val top_devices: Map<String, Int> = emptyMap()
)

@Serializable
data class AdEarningsLedgerDto(
    val id: String = "",
    val site_id: String = "",
    val date: String = "",
    val impressions: Int = 0,
    val clicks: Int = 0,
    val gross_amount: Double = 0.0,
    val creator_share: Double = 0.0,
    val is_settled: Boolean = false
)

@Serializable
data class PayoutDto(
    val id: String = "",
    val tenant_id: String = "",
    val amount: Double = 0.0,
    val method: String = "upi",
    val status: String = "pending",
    val requested_at: String = "",
    val processed_at: String? = null
)

@Serializable
data class RequestPayoutRequestDto(
    val amount: Double,
    val method: String = "upi",
    val details: Map<String, String> = emptyMap()
)

@Serializable
data class SimulateAdRevenueResponseDto(
    val site_id: String = "",
    val gross_amount: Double = 0.0,
    val creator_credited: Double = 0.0
)

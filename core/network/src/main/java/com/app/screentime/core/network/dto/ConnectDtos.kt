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

@Serializable
data class ConnectUserProfileDto(
    val user_id: String = "",
    val id: String = "",
    val name: String? = null,
    val display_name: String? = null,
    val email: String? = null,
    val masked_email: String? = null,
    val phone: String? = null,
    val masked_phone: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val gender_preference: String? = null,
    val avatar_url: String? = null,
    val photos: List<String> = emptyList(),
    val about_me: String? = null,
    val bio: String? = null,
    val dating_intent: String? = null,
    val relation_type: String? = null,
    val relationship_type: String? = null,
    val relationship_intention: String? = null,
    val city: String? = null,
    val country: String? = null,
    val area: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val job: String? = null,
    val occupation: String? = null,
    val profession: String? = null,
    val height: JsonElement? = null,
    val height_cm: Int? = null,
    val education: String? = null,
    val interests: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val language: String? = null,
    val date_of_birth: String? = null,
    val dob: String? = null,
    val birth_date: String? = null,
    val birthday: String? = null,
    val quality_score: Double = 0.0,
    val min_age_preference: Int = 18,
    val max_age_preference: Int = 60,
    val max_distance_km: Int = 50,
    val discovery_enabled: Boolean = true,
    val visibility: String? = null,
    val profile_complete: Boolean = false,
    val likes_count: Int = 0,
    val matches_count: Int = 0,
    val likes: Int = 0,
    val matches: Int = 0,
    val created_at: String = "",
    val updated_at: String = ""
)

@Serializable
data class ConnectProfilePhotoDto(
    val id: String = "",
    val photo_url: String = "",
    val thumbnail_url: String? = null,
    val url: String = "",
    val display_order: Int? = null,
    val is_primary: Boolean = false,
    val position: Int = 0
)

@Serializable
data class ConnectProfilePromptDto(
    val id: String = "",
    val question: String = "",
    val answer: String = ""
)

@Serializable
data class ConnectProfileDataDto(
    val profile: ConnectUserProfileDto? = null,
    val photos: List<ConnectProfilePhotoDto> = emptyList(),
    val prompts: List<ConnectProfilePromptDto> = emptyList(),
    val likes_count: Int = 0,
    val matches_count: Int = 0,
    val likes: Int = 0,
    val matches: Int = 0,
    val id: String = "",
    val phone: String = "",
    val masked_phone: String? = null,
    val email: String? = null,
    val masked_email: String? = null,
    val name: String = "",
    val display_name: String? = null,
    val avatar_url: String? = null,
    val bio: String? = null,
    val about_me: String? = null,
    val job: String? = null,
    val occupation: String? = null,
    val profession: String? = null,
    val height: JsonElement? = null,
    val height_cm: Int? = null,
    val education: String? = null,
    val interests: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val city: String? = null,
    val country: String? = null,
    val area: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val age: Int? = null,
    val gender: String? = null,
    val relation_type: String? = null,
    val date_of_birth: String? = null,
    val dob: String? = null,
    val birth_date: String? = null,
    val role: String = "user"
) {
    fun toUserDto(): UserDto {
        val prof = profile
        val resolvedId = prof?.user_id?.ifBlank { null } ?: prof?.id?.ifBlank { null } ?: id
        val resolvedName = prof?.display_name?.ifBlank { null }
            ?: prof?.name?.ifBlank { null }
            ?: display_name?.ifBlank { null }
            ?: name.ifBlank { "User" }

        val resolvedEmail = prof?.email?.ifBlank { null }
            ?: prof?.masked_email?.ifBlank { null }
            ?: email?.ifBlank { null }
            ?: masked_email

        val resolvedPhone = prof?.phone?.ifBlank { null }
            ?: prof?.masked_phone?.ifBlank { null }
            ?: phone.ifBlank { null }
            ?: masked_phone.orEmpty()

        val resolvedBio = prof?.about_me ?: prof?.bio ?: about_me ?: bio ?: ""
        val photoList = when {
            prof?.photos?.isNotEmpty() == true -> prof.photos
            photos.isNotEmpty() -> photos.map { it.photo_url.ifBlank { it.url } }
            else -> emptyList()
        }
        val resolvedAvatar = prof?.avatar_url?.ifBlank { null }
            ?: photos.firstOrNull { it.is_primary }?.let { it.photo_url.ifBlank { it.url } }
            ?: photos.firstOrNull()?.let { it.photo_url.ifBlank { it.url } }
            ?: prof?.photos?.firstOrNull()
            ?: avatar_url

        val resolvedJob = prof?.profession ?: prof?.job ?: prof?.occupation ?: profession ?: job ?: occupation

        fun parseHeightString(elem: JsonElement?, fallbackCm: Int?): String? {
            if (elem != null) {
                val str = try {
                    elem.toString().replace("\"", "").trim()
                } catch (_: Exception) {
                    null
                }
                if (!str.isNullOrBlank() && str != "null") {
                    return if (str.contains("cm", ignoreCase = true)) str else "$str cm"
                }
            }
            return fallbackCm?.let { "$it cm" }
        }

        val resolvedHeight = parseHeightString(prof?.height, prof?.height_cm)
            ?: parseHeightString(height, height_cm)

        val resolvedEducation = prof?.education ?: education
        val resolvedInterests = when {
            prof?.interests?.isNotEmpty() == true -> prof.interests
            interests.isNotEmpty() -> interests
            else -> emptyList()
        }
        val resolvedLanguages = when {
            prof?.languages?.isNotEmpty() == true -> prof.languages
            languages.isNotEmpty() -> languages
            !prof?.language.isNullOrBlank() -> listOf(prof!!.language!!)
            else -> emptyList()
        }
        val resolvedRelationType = prof?.dating_intent ?: prof?.relation_type ?: prof?.relationship_type ?: prof?.relationship_intention ?: relation_type
        val resolvedDob = prof?.date_of_birth ?: prof?.dob ?: prof?.birth_date ?: prof?.birthday ?: date_of_birth ?: dob ?: birth_date
        val resolvedLikesCount = when {
            likes_count > 0 -> likes_count
            likes > 0 -> likes
            (prof?.likes_count ?: 0) > 0 -> prof!!.likes_count
            (prof?.likes ?: 0) > 0 -> prof!!.likes
            else -> 0
        }
        val resolvedMatchesCount = when {
            matches_count > 0 -> matches_count
            matches > 0 -> matches
            (prof?.matches_count ?: 0) > 0 -> prof!!.matches_count
            (prof?.matches ?: 0) > 0 -> prof!!.matches
            else -> 0
        }

        return UserDto(
            id = resolvedId,
            phone = resolvedPhone,
            email = resolvedEmail,
            name = resolvedName,
            display_name = prof?.display_name ?: display_name ?: name,
            role = role,
            avatar_url = resolvedAvatar,
            photos = photoList,
            bio = resolvedBio,
            about_me = prof?.about_me ?: resolvedBio,
            age = prof?.age ?: age,
            gender = prof?.gender ?: gender,
            interests = resolvedInterests,
            dating_intent = prof?.dating_intent ?: resolvedRelationType,
            relation_type = resolvedRelationType,
            profession = prof?.profession ?: resolvedJob,
            job = resolvedJob,
            occupation = resolvedJob,
            height = resolvedHeight,
            height_cm = prof?.height_cm ?: height_cm,
            education = resolvedEducation,
            languages = resolvedLanguages,
            date_of_birth = resolvedDob,
            dob = resolvedDob,
            prompts = prompts,
            city = prof?.city ?: city,
            country = prof?.country ?: country,
            area = prof?.area ?: area,
            latitude = prof?.latitude ?: latitude,
            longitude = prof?.longitude ?: longitude,
            quality_score = prof?.quality_score ?: 0.0,
            profile_complete = prof?.profile_complete ?: false,
            discovery_enabled = prof?.discovery_enabled ?: true,
            visibility = prof?.visibility ?: "public",
            likes_count = resolvedLikesCount,
            matches_count = resolvedMatchesCount,
            likes = resolvedLikesCount,
            matches = resolvedMatchesCount,
            created_at = prof?.created_at ?: ""
        )
    }
}

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

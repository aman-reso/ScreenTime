package com.app.screentime.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForYouResponse(
    val success: Boolean = true,
    val trending: List<ForYouModelDto> = emptyList(),
    val trending_profiles: List<ForYouModelDto> = emptyList(),
    val offers: List<ForYouOfferDto> = emptyList(),
    val models: List<ForYouModelDto> = emptyList(),
    val total_models: Int = 0,
    val page: Int = 1,
    val limit: Int = 20,
    val has_more: Boolean = false
)

@Serializable
data class ForYouModelDto(
    val id: String = "",
    val user_id: String = "",
    val name: String = "",
    val age: Int = 22,
    val email: String? = null,
    val description: String? = null,
    val language: String? = null,
    val status: String? = null,
    val role: String? = null,
    val audio_rate: Int = 10,
    val video_rate: Int = 20,
    val voice_rate_per_min: Double = 10.0,
    val video_rate_per_min: Double = 20.0,
    val profile_images: String? = null,
    val photos: List<String> = emptyList(),
    val primary_photo_url: String? = null,
    val avatar_url: String? = null,
    val is_trending: Boolean = false,
    val trending_rank: Int = 0,
    val is_online: Boolean = false
) {
    fun getResolvedPhotoUrl(): String {
        return primary_photo_url?.takeIf { it.isNotBlank() }
            ?: avatar_url?.takeIf { it.isNotBlank() }
            ?: photos.firstOrNull()?.takeIf { it.isNotBlank() }
            ?: profile_images?.split(",")?.firstOrNull()?.trim().orEmpty()
    }
}

@Serializable
data class ForYouOfferDto(
    val id: String = "",
    val title: String = "",
    val subtitle: String? = null,
    val badge_text: String? = null,
    val banner_image_url: String = "",
    val action_type: String = "wallet",
    val action_target: String? = null,
    val discount_pct: Int = 0,
    val is_active: Boolean = true
)

@Serializable
data class ForyouUserDetailResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: ForyouUserDetailDto? = null
)

@Serializable
data class ForyouUserDetailDto(
    val id: String = "",
    val name: String = "",
    val age: Int = 22,
    val followers: Int = 1248,
    val following: Int = 86,
    val about: String = "",
    val languages: String = "Hindi, English",
    val language_list: List<String> = emptyList(),
    val audio_rate: Int = 10,
    val video_rate: Int = 20,
    val profile_images: String? = null,
    val photos: List<String> = emptyList(),
    val photo_url: String? = null,
    val status: String = "online",
    val role: String = "model",
    val is_online: Boolean = false,
    val is_following: Boolean = false
) {
    fun getEffectivePhotoUrl(): String {
        return photo_url?.takeIf { it.isNotBlank() }
            ?: photos.firstOrNull()?.takeIf { it.isNotBlank() }
            ?: profile_images?.split(",")?.firstOrNull()?.trim().orEmpty()
    }
}

@Serializable
data class FollowToggleResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: FollowToggleDto? = null
)

@Serializable
data class FollowToggleDto(
    val target_id: String = "",
    val is_following: Boolean = false,
    val followers: Int = 0
)

@Serializable
data class FollowToggleRequest(
    val follower_id: String? = null,
    val is_following: Boolean? = null
)

@Serializable
data class WinterWalletResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: WinterWalletDataDto? = null
)

@Serializable
data class WinterWalletDataDto(
    val coin_balance: Int = 0,
    val transactions: List<WinterTransactionDto> = emptyList(),
    val packages: List<WinterRechargePackageDto> = emptyList()
)

@Serializable
data class WinterTransactionDto(
    val id: String = "",
    val title: String = "",
    val timestamp_details: String = "",
    val amount_formatted: String = "",
    val amount: Int = 0,
    val is_positive: Boolean = true,
    val category: String = "All",
    val status: String = "Completed"
)

@Serializable
data class WinterRechargePackageDto(
    val id: String = "",
    val name: String = "",
    val credits: Int = 0,
    val price_inr: Int = 0,
    val popular_badge: Boolean = false,
    val benefits: List<String> = emptyList(),
    val description: String = ""
)

@Serializable
data class WalletPurchaseRequestDto(
    val package_id: String,
    val payment_gateway: String = "google_play",
    val purchase_token: String? = null,
    val order_id: String? = null
)

@Serializable
data class WalletPurchaseResponseDto(
    val success: Boolean = true,
    val message: String = "",
    val data: WalletPurchaseResultDto? = null
)

@Serializable
data class WalletPurchaseResultDto(
    val success: Boolean = true,
    val message: String = "",
    val credits_added: Int = 0,
    val credit_balance: Int = 0,
    val transaction_id: String = ""
)


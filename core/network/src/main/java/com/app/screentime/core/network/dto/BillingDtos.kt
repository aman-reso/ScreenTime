package com.app.screentime.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class BillingProductItemDto(
    val id: String = "",
    val google_product_id: String = "",
    val name: String = "",
    val description: String = "",
    val type: String = "inapp",
    val price_inr: Double = 0.0,
    val credits: Int = 0,
    val category: String = "",
    val is_consumable: Boolean = true,
    val popular_badge: Boolean = false,
    val benefits: List<String> = emptyList()
) {
    val effectiveProductId: String
        get() = google_product_id.ifBlank { id }

    fun toWalletPackDto(): WalletPackDto {
        return WalletPackDto(
            id = effectiveProductId,
            name = name,
            amount = price_inr,
            amount_in_paise = (price_inr * 100).toLong(),
            currency = "INR",
            credits = credits,
            benefits = benefits,
            description = description,
            popular_badge = popular_badge,
            price_inr = price_inr,
            is_popular = popular_badge,
            type = type,
            category = category,
            google_product_id = effectiveProductId
        )
    }
}

@Serializable
data class BillingCatalogDataDto(
    val package_name: String = "com.app.screentime",
    val inapp_products: List<BillingProductItemDto> = emptyList(),
    val subscriptions: List<BillingProductItemDto> = emptyList()
)

@Serializable
data class BillingProductsResponse(
    val success: Boolean = false,
    val status: String = "",
    val message: String = "",
    val data: BillingCatalogDataDto? = null
)

@Serializable
data class VerifyGooglePurchaseRequest(
    val package_name: String = "com.app.screentime",
    val product_id: String,
    val purchase_token: String,
    val type: String = "inapp"
)

@Serializable
data class VerifyGooglePurchaseDataDto(
    val success: Boolean = true,
    val transaction_id: String? = null,
    val product_id: String = "",
    val product_name: String = "",
    val credits_added: Int = 0,
    val credit_balance: Int = 0,
    val message: String = ""
)

@Serializable
data class VerifyGooglePurchaseResponse(
    val success: Boolean = false,
    val status: String = "",
    val message: String = "",
    val data: VerifyGooglePurchaseDataDto? = null
)

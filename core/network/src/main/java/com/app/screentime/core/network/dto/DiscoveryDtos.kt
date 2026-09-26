package com.app.screentime.core.network.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

object FlexibleDoubleSerializer : KSerializer<Double> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleDouble", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Double {
        val input = decoder as? JsonDecoder
            ?: return runCatching { decoder.decodeDouble() }.getOrDefault(0.0)
        val element = input.decodeJsonElement()
        return if (element is JsonPrimitive) {
            element.doubleOrNull
                ?: element.longOrNull?.toDouble()
                ?: element.content.toDoubleOrNull()
                ?: 0.0
        } else {
            0.0
        }
    }

    override fun serialize(encoder: Encoder, value: Double) {
        encoder.encodeDouble(value)
    }
}

@Serializable
data class DiscoveryMatchDto(
    val user_id: String,
    val name: String? = null,
    val display_name: String = "",
    val age: Int = 24,
    val gender: String = "",
    val city: String = "",
    val distance_display: String = "",
    @Serializable(with = FlexibleDoubleSerializer::class)
    val distance_km: Double = 0.0,
    @Serializable(with = FlexibleDoubleSerializer::class)
    val fuzzed_lat: Double? = null,
    @Serializable(with = FlexibleDoubleSerializer::class)
    val fuzzed_lng: Double? = null,
    val bio: String? = null,
    val dating_intent: String = "",
    val relation_type: String? = null,
    val profession: String? = null,
    val job: String? = null,
    val occupation: String? = null,
    val education: String? = null,
    val height: String? = null,
    val height_cm: Int? = null,
    val interests: List<String>? = null,
    val primary_photo_url: String? = null,
    val avatar_url: String? = null,
    val photos: List<String>? = null,
    val additional_photos: List<String>? = null,
    @Serializable(with = FlexibleDoubleSerializer::class)
    val compatibility_score: Double = 0.0,
    val score_breakdown: ScoreBreakdownDto? = null
)

@Serializable
data class ScoreBreakdownDto(
    val overall_score: Int = 0,
    val location_score: Int = 0,
    val preference_score: Int = 0,
    val activity_score: Int = 0,
    val quality_score: Int = 0,
    val freshness_score: Int = 0,
    val formula: String = "",
    @Serializable(with = FlexibleDoubleSerializer::class)
    val distance_km: Double = 0.0,
    val distance_display: String = "",
    val common_interests: List<String>? = null,
    val explanation: String = ""
)

@Serializable
data class DiscoveryDataResponse(
    val items: List<DiscoveryMatchDto> = emptyList(),
    val page: Int = 1,
    val limit: Int = 10,
    val total_count: Int = 0,
    val has_more: Boolean = false
)

@Serializable
data class DiscoveryApiResponse(
    val success: Boolean,
    val message: String?,
    val data: DiscoveryDataResponse? = null
)

@Serializable
data class DiscoveryInteractRequest(
    val target_user_id: String,
    val action: String
)

@Serializable
data class DiscoveryInteractData(
    val success: Boolean = true,
    val action: String = "like",
    val is_match: Boolean = false,
    val match_id: String? = null,
    val target_user_id: String? = null,
    val message: String? = null
)

@Serializable
data class DiscoveryInteractResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: DiscoveryInteractData? = null
)

@Serializable
data class DiscoveryMatchItemDto(
    val match_id: String,
    val matched_user_id: String,
    val display_name: String,
    val age: Int = 24,
    val city: String = "",
    val area: String = "",
    val primary_photo_url: String? = null,
    val dating_intent: String = "",
    val bio: String = "",
    val matched_at: String = ""
)

@Serializable
data class DiscoveryMatchesData(
    val items: List<DiscoveryMatchItemDto> = emptyList(),
    val page: Int = 1,
    val limit: Int = 10,
    val total_count: Int = 0,
    val has_more: Boolean = false
)

@Serializable
data class DiscoveryMatchesResponse(
    val success: Boolean = true,
    val message: String = "",
    val data: DiscoveryMatchesData? = null
)

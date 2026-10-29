package com.app.screentime.feature.profile

import com.app.screentime.core.model.User

/**
 * Clean, presentation-ready UI model for [ProfileDetailScreen].
 *
 * All business logic, formatting, fallback rules, and calculations
 * (e.g. zodiac, pronouns, height strings, works format, photos fallback)
 * are prepared beforehand via [toProfileDetailUiModel] mapper,
 * ensuring the UI Composable remains 100% declarative and pure.
 */
data class ProfileDetailUiModel(
    val userId: String = "",
    val displayName: String = "User",
    val headerTitle: String = "User",
    val isVerified: Boolean = false,
    val photos: List<String> = emptyList(),
    val videoUrl: String? = null,
    val lookingForText: String? = null,
    val aboutMeText: String? = null,
    val distanceText: String = "Nearby",
    val heightText: String? = null,
    val worksText: String? = null,
    val livesInText: String? = null,
    val pronounText: String? = null,
    val educationText: String? = null,
    val languagesText: String? = null,
    val drinkingText: String? = null,
    val smokingText: String? = null,
    val zodiacSign: String? = null,
    val essentialItems: List<ProfileTileItemUiModel> = emptyList(),
    val lifestyleItems: List<ProfileTileItemUiModel> = emptyList(),
    val interests: List<String> = emptyList(),
    val prompts: List<ProfilePromptUiModel> = emptyList(),
    val matchPercentage: Int = 90
)

data class ProfileTileItemUiModel(
    val label: String,
    val value: String
)

data class ProfilePromptUiModel(
    val question: String,
    val answer: String
)

/**
 * Pure Mapper function converting Domain [User] into ready-to-render [ProfileDetailUiModel].
 */
fun User?.toProfileDetailUiModel(
    fallbackUserId: String = "",
    fallbackName: String = "",
    fallbackAge: Int = 0,
    fallbackLocation: String = "Online"
): ProfileDetailUiModel {
    val user = this

    // 1. Name & Age
    val resolvedName = user?.name?.takeIf { it.isNotBlank() }
        ?: fallbackName.takeIf { it.isNotBlank() }
        ?: "User"

    val resolvedAge = user?.age
        ?: calculateAgeFromDob(user?.dateOfBirth ?: user?.dob)
        ?: fallbackAge.takeIf { it > 0 }

    val headerTitle = if (resolvedAge != null && resolvedAge > 0) {
        "$resolvedName, $resolvedAge"
    } else {
        resolvedName
    }

    // 2. Photos gallery with fallback to avatar (deduplicated)
    val resolvedPhotos = user?.photos.orEmpty()
        .filter { it.isNotBlank() }
        .distinct()
        .ifEmpty {
            listOfNotNull(user?.avatarUrl?.takeIf { it.isNotBlank() })
        }

    // 3. Location / Distance
    val location = when {
        !user?.area.isNullOrBlank() && !user?.city.isNullOrBlank() -> "${user.area}, ${user.city}"
        !user?.city.isNullOrBlank() -> user.city!!
        !user?.area.isNullOrBlank() -> user.area!!
        fallbackLocation.isNotBlank() && fallbackLocation != "Online" -> fallbackLocation
        else -> "Nearby"
    }

    // 4. Height formatting (e.g. "175 cm (5'9\")")
    val totalInches = user?.heightCm?.let { kotlin.math.round(it / 2.54).toInt() }
    val feet = totalInches?.let { it / 12 }
    val inches = totalInches?.let { it % 12 }
    val heightFormatted = when {
        user?.heightCm != null && feet != null && inches != null -> "${user.heightCm} cm (${feet}'${inches}\")"
        !user?.height.isNullOrBlank() -> user.height!!
        user?.heightCm != null -> "${user.heightCm} cm"
        else -> null
    }

    // 5. Profession / Company
    val jobTitle = user?.job ?: user?.occupation ?: user?.profession
    val company = user?.company
    val worksFormatted = listOfNotNull(
        jobTitle?.takeIf { it.isNotBlank() },
        company?.takeIf { it.isNotBlank() }
    ).joinToString(" at ").takeIf { it.isNotBlank() }

    // 6. City / Lives in
    val cityFormatted = (user?.city ?: user?.area)?.takeIf { it.isNotBlank() }

    // 7. Gender / Pronouns
    val pronounFormatted = when (user?.gender?.trim()?.lowercase()) {
        "female", "woman", "f" -> "she / her"
        "male", "man", "m" -> "he / him"
        "non-binary" -> "they / them"
        else -> user?.gender?.takeIf { it.isNotBlank() }
    }

    // 8. Education
    val educationFormatted = user?.education?.takeIf { it.isNotBlank() }

    // 9. Languages
    val languagesFormatted = user?.languages?.filter { it.isNotBlank() }
        ?.takeIf { it.isNotEmpty() }
        ?.joinToString(", ") { it.replaceFirstChar { char -> char.uppercase() } }

    // 10. Drinking & Smoking (Lifestyle)
    val drinkingFormatted = user?.interests?.find { it.contains("drink", ignoreCase = true) }
        ?.takeIf { it.isNotBlank() }
        ?.replaceFirstChar { it.uppercase() }
    val smokingFormatted = user?.interests?.find { it.contains("smoke", ignoreCase = true) }
        ?.takeIf { it.isNotBlank() }
        ?.replaceFirstChar { it.uppercase() }

    // 11. Zodiac Sign
    val zodiac = calculateZodiacSign(user?.dateOfBirth ?: user?.dob).takeIf { it.isNotBlank() }

    // 12. Looking for / Dating intent
    val lookingFor = (user?.datingIntent ?: user?.relationType)
        ?.takeIf { it.isNotBlank() }
        ?.replaceFirstChar { it.uppercase() }

    // 13. About me / Bio
    val aboutMe = (user?.aboutMe ?: user?.bio)?.takeIf { it.isNotBlank() }

    // 14. Video URL
    val video = user?.videoUrl?.takeIf { it.isNotBlank() }
        ?: user?.videos?.firstOrNull { it.isNotBlank() }

    // 15. Match percentage
    val matchPct = if (user?.qualityScore != null && user.qualityScore > 0.0) {
        (user.qualityScore * 100).toInt().coerceIn(75, 99)
    } else {
        90
    }

    // 16. Prompts mapping
    val mappedPrompts = user?.prompts.orEmpty()
        .filter { it.question.isNotBlank() && it.answer.isNotBlank() }
        .map { ProfilePromptUiModel(question = it.question, answer = it.answer) }

    // 17. Pre-computed Essentials Tile Items (Distance, City, Height, Study, Pronoun, Languages, Work)
    val resolvedEssentialItems = buildList {
        // Distance
        if (location.isNotBlank()) {
            val formattedDistance = if (
                location.contains("away", ignoreCase = true) ||
                location.equals("Nearby", ignoreCase = true) ||
                location.equals("Online", ignoreCase = true)
            ) {
                location
            } else {
                "$location away"
            }
            add(ProfileTileItemUiModel(label = "Distance", value = formattedDistance))
        }
        // City / Lives in
        if (!cityFormatted.isNullOrBlank()) {
            val formattedCity = if (cityFormatted.startsWith("Lives in", ignoreCase = true)) {
                cityFormatted
            } else {
                "Lives in $cityFormatted"
            }
            add(ProfileTileItemUiModel(label = "City", value = formattedCity))
        }
        // Height
        if (!heightFormatted.isNullOrBlank()) {
            add(ProfileTileItemUiModel(label = "Height", value = heightFormatted))
        }
        // Study / Education
        if (!educationFormatted.isNullOrBlank()) {
            add(ProfileTileItemUiModel(label = "Study", value = educationFormatted))
        }
        // She / He (Pronouns)
        if (!pronounFormatted.isNullOrBlank()) {
            add(ProfileTileItemUiModel(label = "Pronoun", value = pronounFormatted.replaceFirstChar { it.uppercase() }))
        }
        // Language speak
        if (!languagesFormatted.isNullOrBlank()) {
            add(ProfileTileItemUiModel(label = "Language speak", value = languagesFormatted))
        }
        // Work / Occupation
        if (!worksFormatted.isNullOrBlank()) {
            add(ProfileTileItemUiModel(label = "Work", value = worksFormatted))
        }
    }

    // 18. Pre-computed Lifestyle Tile Items (Drinking, Smoking, Zodiac)
    val resolvedLifestyleItems = buildList {
        if (!drinkingFormatted.isNullOrBlank()) {
            add(ProfileTileItemUiModel(label = "Drinking", value = drinkingFormatted))
        }
        if (!smokingFormatted.isNullOrBlank()) {
            add(ProfileTileItemUiModel(label = "Smoking", value = smokingFormatted))
        }
        if (!zodiac.isNullOrBlank()) {
            add(ProfileTileItemUiModel(label = "Zodiac", value = zodiac))
        }
    }

    return ProfileDetailUiModel(
        userId = user?.id?.takeIf { it.isNotBlank() } ?: fallbackUserId,
        displayName = resolvedName,
        headerTitle = headerTitle,
        isVerified = user?.isPhoneVerified == true,
        photos = resolvedPhotos,
        videoUrl = video,
        lookingForText = lookingFor,
        aboutMeText = aboutMe,
        distanceText = location,
        heightText = heightFormatted,
        worksText = worksFormatted,
        livesInText = cityFormatted,
        pronounText = pronounFormatted,
        educationText = educationFormatted,
        languagesText = languagesFormatted,
        drinkingText = drinkingFormatted,
        smokingText = smokingFormatted,
        zodiacSign = zodiac,
        essentialItems = resolvedEssentialItems,
        lifestyleItems = resolvedLifestyleItems,
        interests = user?.interests.orEmpty().filter { it.isNotBlank() },
        prompts = mappedPrompts,
        matchPercentage = matchPct
    )
}

/**
 * Calculates astrological zodiac sign from date string (format "YYYY-MM-DD" or similar).
 */
private fun calculateZodiacSign(dateOfBirth: String?): String {
    if (dateOfBirth.isNullOrBlank()) return ""
    return try {
        val parts = dateOfBirth.split("-", "/", ".")
        if (parts.size >= 3) {
            val (month, day) = if (parts[0].length == 4) {
                parts[1].toInt() to parts[2].toInt()
            } else {
                parts[0].toInt() to parts[1].toInt()
            }
            when (month) {
                1 -> if (day < 20) "Capricorn ♑" else "Aquarius ♒"
                2 -> if (day < 19) "Aquarius ♒" else "Pisces ♓"
                3 -> if (day < 21) "Pisces ♓" else "Aries ♈"
                4 -> if (day < 20) "Aries ♈" else "Taurus ♉"
                5 -> if (day < 21) "Taurus ♉" else "Gemini ♊"
                6 -> if (day < 21) "Gemini ♊" else "Cancer ♋"
                7 -> if (day < 23) "Cancer ♋" else "Leo ♌"
                8 -> if (day < 23) "Leo ♌" else "Virgo ♍"
                9 -> if (day < 23) "Virgo ♍" else "Libra ♎"
                10 -> if (day < 23) "Libra ♎" else "Scorpio ♏"
                11 -> if (day < 22) "Scorpio ♏" else "Sagittarius ♐"
                12 -> if (day < 22) "Sagittarius ♐" else "Capricorn ♑"
                else -> ""
            }
        } else ""
    } catch (_: Exception) {
        ""
    }
}

/**
 * Computes age from ISO-8601 or similar DOB format.
 */
private fun calculateAgeFromDob(dob: String?): Int? {
    if (dob.isNullOrBlank()) return null
    return try {
        val parts = dob.split("-")
        if (parts.size >= 3) {
            val y = parts[0].toInt()
            val m = parts[1].toInt()
            val d = parts[2].toInt()
            java.time.Period.between(
                java.time.LocalDate.of(y, m, d),
                java.time.LocalDate.now()
            ).years
        } else null
    } catch (_: Exception) {
        null
    }
}

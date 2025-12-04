package com.app.screentime.challenge.model

/**
 * Enum for challenge display types
 */
enum class ChallengeDisplayType {
    FEATURE,
    TRENDING,
    SPECIAL,
    QUICK_JOIN,
    OTHER;

    companion object {
        /**
         * Parse display type from string, defaulting to OTHER if not recognized
         */
        fun fromString(value: String?): ChallengeDisplayType {
            return when (value?.uppercase()) {
                "FEATURE" -> FEATURE
                "TRENDING" -> TRENDING
                "SPECIAL" -> SPECIAL
                "QUICK_JOIN" -> QUICK_JOIN
                else -> OTHER
            }
        }
    }
}


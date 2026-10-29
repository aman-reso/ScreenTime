package com.app.screentime.feature.preferences.domain.model

/**
 * High-level Onboarding Categories for grouping user profile setup.
 * Category 1: Essential details
 * Category 2: Other details (User Personalization)
 */
enum class OnboardingCategory(
    val id: Int,
    val title: String,
    val subtitle: String,
    val stepIndicatorText: String,
    val subSteps: List<OnboardingSubStep>
) {
    ESSENTIAL_DETAILS(
        id = 1,
        title = "Essential details",
        subtitle = "Start with the essentials about you.",
        stepIndicatorText = "1 of 2",
        subSteps = listOf(
            OnboardingSubStep.NAME,
            OnboardingSubStep.GENDER,
            OnboardingSubStep.AGE,
            OnboardingSubStep.HEIGHT,
            OnboardingSubStep.LANGUAGES,
            OnboardingSubStep.LOCATION
        )
    ),
    OTHER_DETAILS(
        id = 2,
        title = "Other details",
        subtitle = "Your life, your interests, your intentions.",
        stepIndicatorText = "2 of 2",
        subSteps = listOf(
            OnboardingSubStep.EDUCATION,
            OnboardingSubStep.INTERESTS,
            OnboardingSubStep.INTERESTED_IN,
            OnboardingSubStep.PROFILE_VISIBILITY,
            OnboardingSubStep.BIO,
            OnboardingSubStep.DATING_INTENT
        )
    );

    companion object {
        fun fromId(id: Int): OnboardingCategory =
            entries.find { it.id == id } ?: ESSENTIAL_DETAILS
    }
}

/**
 * Sub-steps within each OnboardingCategory.
 */
enum class OnboardingSubStep(
    val key: String,
    val label: String,
    val categoryId: Int
) {
    // Category 1: Essential Details
    NAME("name", "Name", 1),
    GENDER("gender", "Gender", 1),
    AGE("age", "Age", 1),
    HEIGHT("height", "Height", 1),
    LANGUAGES("languages", "Language", 1),
    LOCATION("location", "Location access", 1),

    // Category 2: Other Details / Personalization
    PROFESSION("profession", "Profession", 2),
    EDUCATION("education", "Education", 2),
    INTERESTS("interests", "Interests", 2),
    INTERESTED_IN("interested_in", "Interested in", 2),
    PROFILE_VISIBILITY("visibility", "Profile visibility", 2),
    BIO("bio", "Bio", 2),
    DATING_INTENT("intent", "Dating intent", 2);

    companion object {
        fun fromKey(key: String): OnboardingSubStep? =
            entries.find { it.key == key }

        fun forCategory(categoryId: Int): List<OnboardingSubStep> =
            entries.filter { it.categoryId == categoryId }
    }
}

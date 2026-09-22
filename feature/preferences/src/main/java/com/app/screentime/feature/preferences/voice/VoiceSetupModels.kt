package com.app.screentime.feature.preferences.voice

import androidx.annotation.DrawableRes
import com.telekom.odsystem.R

enum class VoiceSetupField {
    NAME,
    IDENTITY,
    BIRTHDAY,
    LANGUAGES,
    HEIGHT,
    INTERESTED_IN
}

data class VoiceSetupQuestion(
    val field: VoiceSetupField,
    val topic: String,
    val prompt: String,
    val placeholder: String,
    val capturedLabel: String,
    @DrawableRes val capturedIcon: Int,
    val keyboardStep: Int
)

val voiceSetupQuestions: List<VoiceSetupQuestion> = listOf(
    VoiceSetupQuestion(
        field = VoiceSetupField.NAME,
        topic = "Name",
        prompt = "Hi, I'm Zona Assistant. What should we call you on your profile?",
        placeholder = "Say your first and last name",
        capturedLabel = "Name",
        capturedIcon = R.drawable.ic_user,
        keyboardStep = 1
    ),
    VoiceSetupQuestion(
        field = VoiceSetupField.IDENTITY,
        topic = "Identity",
        prompt = "How do you identify? You can say Woman, Man, Non-binary, or another identity.",
        placeholder = "Say how you identify",
        capturedLabel = "Identity",
        capturedIcon = R.drawable.ic_heart,
        keyboardStep = 2
    ),
    VoiceSetupQuestion(
        field = VoiceSetupField.BIRTHDAY,
        topic = "Birthday & Age",
        prompt = "Great! Now, when is your birthday? This helps us find matches in your age group.",
        placeholder = "Say your birthday, like August 24th, 1998",
        capturedLabel = "Birthday",
        capturedIcon = R.drawable.ic_cake,
        keyboardStep = 3
    ),
    VoiceSetupQuestion(
        field = VoiceSetupField.LANGUAGES,
        topic = "Languages",
        prompt = "Which languages do you speak? List as many as you like.",
        placeholder = "Say languages, like English and Hindi",
        capturedLabel = "Languages",
        capturedIcon = R.drawable.ic_globe,
        keyboardStep = 4
    ),
    VoiceSetupQuestion(
        field = VoiceSetupField.HEIGHT,
        topic = "Height",
        prompt = "What is your height? You can say it in centimeters or feet and inches.",
        placeholder = "Say your height, like 175 centimeters",
        capturedLabel = "Height",
        capturedIcon = R.drawable.ic_sliders,
        keyboardStep = 5
    ),
    VoiceSetupQuestion(
        field = VoiceSetupField.INTERESTED_IN,
        topic = "Interested In",
        prompt = "Who are you hoping to meet? You can say Everyone, Men, or Women.",
        placeholder = "Say who you want to meet",
        capturedLabel = "Interested In",
        capturedIcon = R.drawable.ic_heart_pulse,
        keyboardStep = 7
    )
)

fun VoiceSetupQuestion.normalizeSpoken(raw: String): String {
    val spoken = raw.trim().replace(Regex("\\s+"), " ")
    if (spoken.isBlank()) return spoken
    return when (field) {
        VoiceSetupField.IDENTITY -> normalizeIdentity(spoken)
        VoiceSetupField.INTERESTED_IN -> normalizeInterestedIn(spoken)
        VoiceSetupField.HEIGHT -> normalizeHeight(spoken)
        else -> spoken
    }
}

private fun normalizeIdentity(spoken: String): String {
    val lower = spoken.lowercase()
    return when {
        "non-binary" in lower || "non binary" in lower || "nonbinary" in lower -> "Non-binary"
        "genderqueer" in lower -> "Genderqueer"
        "agender" in lower -> "Agender"
        "woman" in lower || "female" in lower -> "Woman"
        "man" in lower || "male" in lower -> "Man"
        else -> spoken.replaceFirstChar { it.uppercase() }
    }
}

private fun normalizeInterestedIn(spoken: String): String {
    val lower = spoken.lowercase()
    return when {
        "everyone" in lower || "anybody" in lower || "anyone" in lower -> "Everyone"
        "women" in lower || "woman" in lower -> "Women"
        "men" in lower || "man" in lower -> "Men"
        else -> spoken.replaceFirstChar { it.uppercase() }
    }
}

private fun normalizeHeight(spoken: String): String {
    val cm = Regex("(\\d{2,3})").find(spoken)?.groupValues?.get(1)
    return if (cm != null) "$cm cm" else spoken
}

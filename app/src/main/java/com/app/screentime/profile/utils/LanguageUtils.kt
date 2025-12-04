package com.app.screentime.profile.utils

import androidx.annotation.DrawableRes
import com.app.screentime.R

data object LanguageUtils {
    val languages = listOf(
        LanguageOption(R.string.language_english, "en"),
        LanguageOption(R.string.language_hindi, "hi"),
        LanguageOption(R.string.language_bengali, "bn")
    )
}

data class LanguageOption(
    @DrawableRes
    val displayName: Int,
    val value: String
)
package com.app.screentime.profile.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.app.screentime.config.R

data object LanguageUtils {
    val languages = listOf(
        // English
        LanguageOption(R.string.language_english, "en"),
        
        // Indian Languages
        LanguageOption(R.string.language_hindi, "hi"),
        LanguageOption(R.string.language_bengali, "bn"),
        LanguageOption(R.string.language_tamil, "ta"),
        LanguageOption(R.string.language_telugu, "te"),
        LanguageOption(R.string.language_marathi, "mr"),
        LanguageOption(R.string.language_gujarati, "gu"),
        LanguageOption(R.string.language_kannada, "kn"),
        LanguageOption(R.string.language_malayalam, "ml"),
        LanguageOption(R.string.language_punjabi, "pa"),
        LanguageOption(R.string.language_urdu, "ur"),
        
        // European Languages
        LanguageOption(R.string.language_french, "fr"),
        LanguageOption(R.string.language_spanish, "es"),
        LanguageOption(R.string.language_german, "de"),
        LanguageOption(R.string.language_italian, "it"),
        LanguageOption(R.string.language_dutch, "nl"),
        LanguageOption(R.string.language_portuguese, "pt"),
        LanguageOption(R.string.language_portuguese_brazil, "pt-rBR"),
        LanguageOption(R.string.language_russian, "ru"),
        
        // Asian Languages
        LanguageOption(R.string.language_chinese_simplified, "zh-rCN"),
        LanguageOption(R.string.language_chinese_traditional, "zh-rTW"),
        LanguageOption(R.string.language_japanese, "ja"),
        LanguageOption(R.string.language_korean, "ko"),
        LanguageOption(R.string.language_indonesian, "id"),
        LanguageOption(R.string.language_thai, "th"),
        LanguageOption(R.string.language_vietnamese, "vi"),
        
        // Middle Eastern Languages
        LanguageOption(R.string.language_arabic, "ar"),
        LanguageOption(R.string.language_hebrew, "iw"),
        LanguageOption(R.string.language_persian, "fa")
    )
    
    /**
     * Get languages sorted alphabetically by their display name
     */
    fun getSortedLanguages(context: Context): List<LanguageOption> {
        return languages.sortedBy { language ->
            context.getString(language.displayName)
        }
    }
}

data class LanguageOption(
    @StringRes
    val displayName: Int,
    val value: String
)
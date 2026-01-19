package com.app.screentime.config.language

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.core.network.preferences.PreferencesManager.Companion.KEY_LANGUAGE
import com.app.screentime.core.network.preferences.PreferencesManager.Companion.LANGUAGE_PREF
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject


class AppLanguageManager @Inject constructor(@ApplicationContext private val context: Context) {

    fun changeLanguage(context: Context, languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }

    fun getLanguageCode(context: Context): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                ?.applicationLocales
                ?.get(0)
        } else {
            AppCompatDelegate.getApplicationLocales().get(0)
        }
        
        // Return full language tag including region if present (e.g., "pt-rBR", "zh-rCN")
        // This matches the format used in LanguageUtils
        return locale?.let {
            val language = it.language
            val country = it.country
            if (country.isNotEmpty()) {
                // Format as "language-rCountry" to match LanguageUtils format (e.g., "pt-rBR")
                "$language-r${country.uppercase()}"
            } else {
                language
            }
        } ?: getDefaultLanguageCode()
    }

    fun getDefaultLanguageCode(): String {
        return "en"
    }

}
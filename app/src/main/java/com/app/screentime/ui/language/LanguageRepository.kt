package com.app.screentime.ui.language

import android.content.Context
import com.app.screentime.config.language.AppLanguageManager
import com.app.screentime.core.network.preferences.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LanguageRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appLanguageManager: AppLanguageManager
) {
    // Use a StateFlow to observe language changes
    // Initialize with current language from AppLanguageManager (new way, not SharedPreferences)
    private val _language = MutableStateFlow(appLanguageManager.getLanguageCode(context))
    val language: Flow<String> = _language.asStateFlow()
    
    /**
     * Update the current language
     */
    fun updateLanguage(languageCode: String) {
        _language.value = languageCode
    }
    
    /**
     * Refresh language from AppLanguageManager
     */
    fun refreshLanguage() {
        _language.value = appLanguageManager.getLanguageCode(context)
    }
    
    /**
     * Get current language synchronously
     */
    fun getCurrentLanguage(): String {
        return appLanguageManager.getLanguageCode(context)
    }
}

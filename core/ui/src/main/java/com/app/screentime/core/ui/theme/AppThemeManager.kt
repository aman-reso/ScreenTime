package com.app.screentime.core.ui.theme

import android.content.Context
import android.content.res.Configuration
import com.telekom.odsystem.ODSystem
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Global reactive theme manager supporting System Default, Light Theme, and Dark Theme.
 * Uses only Zona ODS themes (zonaLightODSTheme and zonaDarkODSTheme) and persists choices.
 */
object AppThemeManager {

    private const val PREFS_NAME = "app_theme_prefs"
    private const val KEY_THEME = "selected_theme"

    private var appContext: Context? = null
    private var isSystemDark: Boolean = false

    private val _currentThemeName = MutableStateFlow("System default")
    val currentThemeName: StateFlow<String> = _currentThemeName.asStateFlow()

    private val _currentTheme = MutableStateFlow(zonaLightODSTheme)
    val currentTheme: StateFlow<ODSTheme> = _currentTheme.asStateFlow()

    fun init(context: Context) {
        appContext = context.applicationContext
        val isNight = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        isSystemDark = isNight

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedTheme = prefs.getString(KEY_THEME, "System default") ?: "System default"
        _currentThemeName.value = savedTheme
        val resolved = resolveTheme(savedTheme, isSystemDark)
        _currentTheme.value = resolved
        ODSystem.colors.value = resolved
    }

    fun updateSystemDarkMode(isDark: Boolean) {
        isSystemDark = isDark
        if (_currentThemeName.value == "System default") {
            val resolved = resolveTheme("System default", isDark)
            _currentTheme.value = resolved
            ODSystem.colors.value = resolved
        }
    }

    fun setTheme(themeName: String) {
        _currentThemeName.value = themeName
        val resolved = resolveTheme(themeName, isSystemDark)
        _currentTheme.value = resolved
        ODSystem.colors.value = resolved

        appContext?.let { ctx ->
            ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_THEME, themeName)
                .apply()
        }
    }

    private fun resolveTheme(themeName: String, systemDark: Boolean): ODSTheme {
        return when (themeName) {
            "Dark", "Dark Mode", "Dark (Onyx)" -> zonaDarkODSTheme
            "Light", "Light Mode" -> zonaLightODSTheme
            else -> if (systemDark) zonaDarkODSTheme else zonaLightODSTheme
        }
    }

    fun isDarkTheme(): Boolean {
        return when (_currentThemeName.value) {
            "Dark", "Dark Mode", "Dark (Onyx)" -> true
            "Light", "Light Mode" -> false
            else -> isSystemDark
        }
    }
}


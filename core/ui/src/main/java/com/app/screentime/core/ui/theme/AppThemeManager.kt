package com.app.screentime.core.ui.theme

import com.telekom.odsystem.ODSystem
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.darkMode
import com.telekom.odsystem.tokens.tokens.lightMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Global reactive theme manager supporting Dark Theme, Light Theme, and curated palette modes.
 */
object AppThemeManager {

    // Preset theme definitions
    val DarkOnyxTheme: ODSTheme = darkMode.copy(
        name = "Dark (Onyx)",
        basicAccent = HexColor(0xffffffff),
        basicTextOnAccent = HexColor(0xff111216),
        basicAccentSecondary = HexColor(0xff2e323b),
        basicTextDominant = HexColor(0xffffffff),
        basicTextLink = HexColor(0xffffffff),
        basicBackground = HexColor(0xff111216),
        basicBackgroundCard = HexColor(0xff1c1d22),
        basicBackgroundCardSubtle = HexColor(0xff16171c),
        basicBackgroundSubtle = HexColor(0xff24262e),
        basicText = HexColor(0xffffffff),
        basicTextRecessive = HexColor(0xff94a3b8),
        basicStroke = HexColor(0xff2e323b),
        basicStrokeSubtle = HexColor(0xff262931)
    )

    val VelvetOrchidTheme: ODSTheme = darkMode.copy(
        name = "Velvet Orchid",
        basicAccent = HexColor(0xffd946ef),
        basicBackground = HexColor(0xff0f0a17),
        basicBackgroundCard = HexColor(0xff1a1228),
        basicBackgroundCardSubtle = HexColor(0xff140d20),
        basicBackgroundSubtle = HexColor(0xff241838),
        basicText = HexColor(0xffffffff),
        basicTextRecessive = HexColor(0xffc084fc),
        basicStroke = HexColor(0xff3b1d5c),
        basicStrokeSubtle = HexColor(0xff2a1542)
    )

    val AmberSunsetTheme: ODSTheme = darkMode.copy(
        name = "Amber Sunset",
        basicAccent = HexColor(0xfff59e0b),
        basicBackground = HexColor(0xff140f09),
        basicBackgroundCard = HexColor(0xff22180d),
        basicBackgroundCardSubtle = HexColor(0xff1a130a),
        basicBackgroundSubtle = HexColor(0xff302112),
        basicText = HexColor(0xffffffff),
        basicTextRecessive = HexColor(0xfffbbf24),
        basicStroke = HexColor(0xff4a3215),
        basicStrokeSubtle = HexColor(0xff33220e)
    )

    val OceanicBreezeTheme: ODSTheme = darkMode.copy(
        name = "Oceanic Breeze",
        basicAccent = HexColor(0xff06b6d4),
        basicBackground = HexColor(0xff08101a),
        basicBackgroundCard = HexColor(0xff0f1f33),
        basicBackgroundCardSubtle = HexColor(0xff0b1726),
        basicBackgroundSubtle = HexColor(0xff162e4c),
        basicText = HexColor(0xffffffff),
        basicTextRecessive = HexColor(0xff38bdf8),
        basicStroke = HexColor(0xff1e3a5f),
        basicStrokeSubtle = HexColor(0xff152943)
    )

    val LightTheme: ODSTheme = lightMode.copy(
        name = "Light Mode",
        basicBackground = HexColor(0xfff8fafc),
        basicBackgroundCard = HexColor(0xffffffff),
        basicBackgroundCardSubtle = HexColor(0xfff1f5f9),
        basicBackgroundSubtle = HexColor(0xfff1f5f9),
        basicText = HexColor(0xff0f172a),
        basicTextRecessive = HexColor(0xff64748b),
        basicStroke = HexColor(0xffe2e8f0),
        basicStrokeSubtle = HexColor(0xffe2e8f0)
    )

    private val _currentThemeName = MutableStateFlow("Dark (Onyx)")
    val currentThemeName: StateFlow<String> = _currentThemeName.asStateFlow()

    private val _currentTheme = MutableStateFlow(DarkOnyxTheme)
    val currentTheme: StateFlow<ODSTheme> = _currentTheme.asStateFlow()

    fun setTheme(themeName: String) {
        _currentThemeName.value = themeName
        val newTheme = when (themeName) {
            "Light Mode" -> LightTheme
            "Velvet Orchid" -> VelvetOrchidTheme
            "Amber Sunset" -> AmberSunsetTheme
            "Oceanic Breeze" -> OceanicBreezeTheme
            else -> DarkOnyxTheme
        }
        _currentTheme.value = newTheme
        ODSystem.colors.value = newTheme
    }

    fun isDarkTheme(): Boolean {
        return _currentThemeName.value != "Light Mode"
    }
}

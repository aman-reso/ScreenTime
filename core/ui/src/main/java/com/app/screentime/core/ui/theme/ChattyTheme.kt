package com.app.screentime.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.telekom.odsystem.R

// Core colors from Evermore / Chatty design
val ChattyPrimary         = Color(0xFF0D2033)
val ChattyOnPrimary       = Color(0xFFFFFFFF)
val ChattySecondary       = Color(0xFF2B3A4A)
val ChattyBackground      = Color(0xFFF8FAFC)
val ChattySurface         = Color(0xFFFFFFFF)
val ChattyCard            = Color(0xFFFFFFFF)
val ChattyOnSurface       = Color(0xFF0D2033)
val ChattySubtext         = Color(0xFF64748B)
val ChattyDivider         = Color(0xFFE2E8F0)
val ChattyError           = Color(0xFFD32F2F)
val ChattySuccess         = Color(0xFF2E7D32)

// Dark Palette
val DarkBackground        = Color(0xFF111216)
val DarkSurface           = Color(0xFF1C1D22)
val DarkOnSurface         = Color(0xFFFFFFFF)
val DarkSubtext           = Color(0xFF94A3B8)
val DarkDivider           = Color(0xFF2E323B)
val DarkPrimary           = Color(0xFFFFFFFF)
val DarkOnPrimary         = Color(0xFF111216)

// Pastel organic blob accent colors
val PastelLavender        = Color(0xFFDCD6F7)
val PastelPeach           = Color(0xFFFBC4AB)
val PastelSky             = Color(0xFFCBE3FB)
val PastelLilac           = Color(0xFFECE2FF)
val PastelMint            = Color(0xFFC8E6C9)
val PastelRose            = Color(0xFFF8AD9D)

// Pompiere Font Family
val PompiereFontFamily = FontFamily(
    Font(R.font.pompiere_regular)
)

private val ChattyLightColorScheme = lightColorScheme(
    primary          = ChattyPrimary,
    onPrimary        = ChattyOnPrimary,
    secondary        = ChattySecondary,
    tertiary         = PastelPeach,
    background       = ChattyBackground,
    surface          = ChattySurface,
    onBackground     = ChattyOnSurface,
    onSurface        = ChattyOnSurface,
    error            = ChattyError,
    surfaceVariant   = ChattyBackground,
    outline          = ChattyDivider
)

private val ChattyDarkColorScheme = darkColorScheme(
    primary          = DarkPrimary,
    onPrimary        = DarkOnPrimary,
    secondary        = Color(0xFF334155),
    tertiary         = Color(0xFF475569),
    background       = DarkBackground,
    surface          = DarkSurface,
    onBackground     = DarkOnSurface,
    onSurface        = DarkOnSurface,
    error            = Color(0xFFFF6B6B),
    surfaceVariant   = DarkSurface,
    outline          = DarkDivider
)

@Composable
fun ChattyTheme(
    darkTheme: Boolean = AppThemeManager.isDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeName by AppThemeManager.currentThemeName.collectAsState()
    val isDark = themeName != "Light Mode"

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                // In dark mode, set light status bars = false so icons are white
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    val colorScheme = if (isDark) ChattyDarkColorScheme else ChattyLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

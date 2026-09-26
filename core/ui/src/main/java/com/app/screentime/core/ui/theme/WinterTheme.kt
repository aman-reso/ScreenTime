package com.app.screentime.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import com.telekom.odsystem.R

// Pompiere Font Family
val PompiereFontFamily = FontFamily(
    Font(R.font.pompiere_regular)
)

// Funnel Sans Font Family
val FunnelSansFontFamily = FontFamily(
    Font(R.font.funnelsans_regular, FontWeight.Normal),
    Font(R.font.funnelsans_medium, FontWeight.Medium),
    Font(R.font.funnelsans_semibold, FontWeight.SemiBold),
    Font(R.font.funnelsans_bold, FontWeight.Bold)
)

private val WinterLightColorScheme = lightColorScheme(
    primary          = Color(0xFFD81B60),
    onPrimary        = Color(0xFFFFFFFF),
    secondary        = Color(0xFFFCE4EC),
    tertiary         = Color(0xFFF06292),
    background       = Color(0xFFFFF1F6),
    surface          = Color(0xFFFFFFFF),
    onBackground     = Color(0xFF1A1A2E),
    onSurface        = Color(0xFF1A1A2E),
    error            = Color(0xFFC62828),
    surfaceVariant   = Color(0xFFFFF9FB),
    outline          = Color(0xFFDED4D8)
)

private val WinterDarkColorScheme = darkColorScheme(
    primary          = Color(0xFFD81B60),
    onPrimary        = Color(0xFFFFF7FA),
    secondary        = Color(0xFF4A1830),
    tertiary         = Color(0xFF261B24),
    background       = Color(0xFF160E14),
    surface          = Color(0xFF1C151B),
    onBackground     = Color(0xFFFFF7FA),
    onSurface        = Color(0xFFFFF7FA),
    error            = Color(0xFFEF5350),
    surfaceVariant   = Color(0xFF261B24),
    outline          = Color(0xFF4A3943)
)

@Composable
fun WinterTheme(
    darkTheme: Boolean = AppThemeManager.isDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeName by AppThemeManager.currentThemeName.collectAsState()
    val isDark = when (themeName) {
        "Light", "Light Mode" -> false
        "Dark", "Dark Mode" -> true
        else -> if (themeName.contains("Dark")) true else isSystemInDarkTheme()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    val zonaTokens = if (isDark) ZonaDarkTokens else ZonaLightTokens
    val colorScheme = if (isDark) WinterDarkColorScheme else WinterLightColorScheme

    CompositionLocalProvider(
        LocalZonaColors provides zonaTokens
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

@Composable
fun ChattyTheme(
    darkTheme: Boolean = AppThemeManager.isDarkTheme(),
    content: @Composable () -> Unit
) = WinterTheme(darkTheme = darkTheme, content = content)

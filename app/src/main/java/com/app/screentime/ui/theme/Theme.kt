package com.app.screentime.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel

val LocalThemeMode = compositionLocalOf { false }

@Composable
fun ScreenTimeTheme(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val themeString by themeViewModel.theme.collectAsState()

    // Parse theme string to ThemeType
    val themeType = remember(themeString) {
        if (themeString == "System") {
            if (isSystemDark) {
                ThemeType.CLASSIC_DARK
            } else {
                ThemeType.CLASSIC_LIGHT
            }
        } else {
            ThemeType.fromString(themeString)
        }
    }

    val useDarkTheme = themeType.isDark
    val appColors = remember(themeType) {
        getThemeColors(themeType)
    }

    CompositionLocalProvider(
        LocalThemeMode provides useDarkTheme,
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}


package com.app.screentime.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.customisation.manager.CustomisationRefreshManager
import com.telekom.odsystem.ODSystem
import com.telekom.odsystem.ODSThemeLiveDataHolder
import com.telekom.odsystem.ODSThemeType
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme

val LocalThemeMode = compositionLocalOf { false }
val headerTheme = compositionLocalOf<ODSTheme> { neutralScheme }

@Composable
fun ScreenTimeTheme(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isSystemDark = isSystemInDarkTheme()
    val themeString by themeViewModel.theme.collectAsState()
    val currentHeaderScheme by themeViewModel.headerScheme.collectAsState()

    val odsThemeLiveData = remember { ODSThemeLiveDataHolder.getODSThemeLiveData() }

    // Sync theme with ODSystem when themeString changes
    LaunchedEffect(themeString) {
        val odsThemeType = when (themeString.lowercase()) {
            "light" -> ODSThemeType.LIGHT
            "dark" -> ODSThemeType.DARK
            else -> ODSThemeType.SYSTEM
        }
        ODSystem.setTheme(context, odsThemeType)
    }

    // Update system theme when system dark mode changes (for SYSTEM theme type)
    LaunchedEffect(isSystemDark) {
        if (ODSystem.getCurrentThemeType() == ODSThemeType.SYSTEM) {
            ODSystem.setSystemTheme(context)
        }
    }

    // Listen for customisation refresh events
    LaunchedEffect(Unit) {
        CustomisationRefreshManager.refreshTrigger.collect {
            themeViewModel.refreshHeaderScheme()
        }
    }

    CompositionLocalProvider(
        LocalThemeMode provides isSystemDark,
        headerTheme provides currentHeaderScheme
    ) {
        MaterialTheme(
            shapes = Shapes,
            content = content
        )
    }
}


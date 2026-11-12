package com.app.screentime.feature.newdesign.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import com.app.screentime.feature.newdesign.theme.AppColorScheme
import com.app.screentime.feature.newdesign.theme.LocalAppColorScheme

/**
 * WinterTheme wraps all new design screens with the real AppColorScheme tokens from Figma
 * (supporting both Dark & Light modes) and the app's native fonts (Funnel Sans & Pacifico).
 */
@Composable
fun WinterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: AppColorScheme = if (darkTheme) AppColorScheme.Dark else AppColorScheme.Light,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalTextStyle provides TextStyle(fontFamily = FunnelSans)
    ) {
        MaterialTheme(
            typography = WinterTypography,
            content = content
        )
    }
}

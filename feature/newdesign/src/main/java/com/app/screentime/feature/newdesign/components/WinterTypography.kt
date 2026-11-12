package com.app.screentime.feature.newdesign.components

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R

/**
 * Funnel Sans font family present in our app design system.
 */
val FunnelSans = FontFamily(
    Font(R.font.funnelsans_light, FontWeight.Light),
    Font(R.font.funnelsans_regular, FontWeight.Normal),
    Font(R.font.funnelsans_medium, FontWeight.Medium),
    Font(R.font.funnelsans_semibold, FontWeight.SemiBold),
    Font(R.font.funnelsans_bold, FontWeight.Bold)
)

/**
 * Pacifico script font family present in our app design system for brand logos ("winter", "Luma").
 */
val Pacifico = FontFamily(
    Font(R.font.pacifico_regular, FontWeight.Normal),
    Font(R.font.pacifico_regular, FontWeight.Bold)
)

object WinterFonts {
    val body: FontFamily = FunnelSans
    val headline: FontFamily = FunnelSans
    val script: FontFamily = Pacifico
}

/**
 * Material 3 Typography configured with the app's Funnel Sans font family.
 */
val WinterTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FunnelSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)


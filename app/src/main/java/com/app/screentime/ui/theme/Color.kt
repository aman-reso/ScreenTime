package com.app.screentime.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


// ==================== SURFACE / BACKGROUND ====================

val LightBackground = Color(0xFFFFFFFF)
val DarkBackground = Color(0xFF0E161E)

val LightCard = Color(0xFFF5F5F5)
val DarkCard = Color(0xFF1F242C)

// ==================== TEXT COLORS ====================

val LightTextPrimary = Color(0xFF0E161E)
val DarkTextPrimary = Color(0xFFFFFFFF)

val LightTextSecondary = Color(0xFF1C1C1C)
val DarkTextSecondary = Color(0xFFE0E0E0)

val LightTextMuted = Color(0xFF777777)
val DarkTextMuted = Color(0xFF9E9E9E)

val LightTextLight = Color(0xFF5E6871)
val DarkTextLight = Color(0xFF9BA4B0)

val LightTextHint = Color(0xFF79869B)
val DarkTextHint = Color(0xFF8B949E)

// ==================== BRAND / ACCENT ====================

val LightPrimary = Color(0xFF28AE60)
val DarkPrimary = Color(0xFF42C77A)

val LightError = Color(0xFFDC4453)
val DarkError = Color(0xFFCF6679)

val LightBorder = Color(0x1A000000)
val DarkBorder = Color(0x29FFFFFF)

// Icon tint colors
val LightTint = Color(0xFF0E161E) // Same as textPrimary for light mode
val DarkTint = Color(0xFFFFFFFF) // Same as textPrimary for dark mode

// Special accent colors (same for light and dark)
val KakaoYellow = Color(0xFFB8ED55)

// ==================== COMPOSABLE HELPERS ====================

@Composable
fun appColor(light: Color, dark: Color): Color {
    val useDarkTheme = LocalThemeMode.current
    return if (useDarkTheme) dark else light
}

// Commonly used adaptive colors
@Composable
fun backgroundColor() = appColor(LightBackground, DarkBackground)
@Composable
fun cardColor() = appColor(LightCard, DarkCard)
@Composable
fun textPrimaryColor() = appColor(LightTextPrimary, DarkTextPrimary)
@Composable
fun textSecondaryColor() = appColor(LightTextSecondary, DarkTextSecondary)
@Composable
fun textMutedColor() = appColor(LightTextMuted, DarkTextMuted)
@Composable
fun textLightColor() = appColor(LightTextLight, DarkTextLight)
@Composable
fun textHintColor() = appColor(LightTextHint, DarkTextHint)
@Composable
fun primaryColor() = appColor(LightPrimary, DarkPrimary)
@Composable
fun errorColor() = appColor(LightError, DarkError)
@Composable
fun borderColor() = appColor(LightBorder, DarkBorder)
@Composable
fun tintColor() = appColor(LightTint, DarkTint)

// ==================== APP COLOR MODEL ====================

data class AppColors(
    val background: Color,
    val card: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textLight: Color,
    val textHint: Color,
    val border: Color,
    val success: Color,
    val error: Color,
    val accent: Color, // KakaoYellow or other accent color
    val tint: Color, // Icon tint color
    // Rank colors (for leaderboard)
    val rankGold: Color = success.copy(alpha = 0.9f), // Use success color as gold
    val rankSilver: Color = textMuted, // Use muted color as silver
    val rankBronze: Color = error.copy(alpha = 0.7f), // Use error color as bronze
    // Text on colored backgrounds
    val textOnPrimary: Color = textPrimary, // Text color on primary/success background
    // Chart colors palette
    val chartColors: List<Color> = listOf(
        success,
        accent,
        error.copy(alpha = 0.8f),
        textSecondary.copy(alpha = 0.6f),
        textMuted.copy(alpha = 0.8f)
    )
)

val LocalAppColors = staticCompositionLocalOf<AppColors?> { null }


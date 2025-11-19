package com.app.screentime.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Theme color definitions with multiple variations
 * All colors are eye-friendly (no harsh/bright colors)
 */

// ==================== LIGHT THEMES ====================

// Light Theme 1: Classic Light (Soft white with gentle grays)
object ClassicLightTheme {
    val Background = Color(0xFFFAFAFA) // Soft off-white, easier on eyes
    val Card = Color(0xFFFFFFFF) // Pure white for cards
    val TextPrimary = Color(0xFF1A1A1A) // Soft black, not pure black
    val TextSecondary = Color(0xFF4A4A4A) // Medium gray
    val TextMuted = Color(0xFF6B6B6B) // Muted gray
    val TextLight = Color(0xFF8A8A8A) // Light gray
    val TextHint = Color(0xFF9E9E9E) // Hint gray
    val Border = Color(0x1A000000) // Subtle border
    val Primary = Color(0xFF2E7D32) // Soft green (eye-friendly)
    val Error = Color(0xFFC62828) // Soft red
    val Accent = Color(0xFF1976D2) // Soft blue
}

// Light Theme 2: Warm Light (Warm beige tones)
object WarmLightTheme {
    val Background = Color(0xFFF5F3F0) // Warm beige background
    val Card = Color(0xFFFFFBF7) // Warm white
    val TextPrimary = Color(0xFF2C2418) // Warm dark brown
    val TextSecondary = Color(0xFF4A3E2E) // Warm brown-gray
    val TextMuted = Color(0xFF6B5D4A) // Muted warm brown
    val TextLight = Color(0xFF8A7A66) // Light warm brown
    val TextHint = Color(0xFF9E8E7A) // Hint warm brown
    val Border = Color(0x1A2C2418) // Warm border
    val Primary = Color(0xFF558B2F) // Warm green
    val Error = Color(0xFFBF360C) // Warm red
    val Accent = Color(0xFFE65100) // Warm orange
}

// Light Theme 3: Cool Light (Cool blue-gray tones)
object CoolLightTheme {
    val Background = Color(0xFFF0F2F5) // Cool light blue-gray
    val Card = Color(0xFFFFFFFF) // White
    val TextPrimary = Color(0xFF1A1F2E) // Cool dark blue-gray
    val TextSecondary = Color(0xFF3A4150) // Cool gray
    val TextMuted = Color(0xFF5A6270) // Muted cool gray
    val TextLight = Color(0xFF7A8290) // Light cool gray
    val TextHint = Color(0xFF8E96A4) // Hint cool gray
    val Border = Color(0x1A1A1F2E) // Cool border
    val Primary = Color(0xFF1565C0) // Cool blue
    val Error = Color(0xFFB71C1C) // Cool red
    val Accent = Color(0xFF0277BD) // Cool cyan-blue
}

// Light Theme 4: Soft Light (Very soft pastel tones)
object SoftLightTheme {
    val Background = Color(0xFFF8F9FA) // Very soft gray
    val Card = Color(0xFFFFFFFF) // White
    val TextPrimary = Color(0xFF2D3436) // Soft charcoal
    val TextSecondary = Color(0xFF636E72) // Soft gray
    val TextMuted = Color(0xFF74B9FF) // Soft blue-gray
    val TextLight = Color(0xFFA0A0A0) // Very light gray
    val TextHint = Color(0xFFB2BEC3) // Hint soft gray
    val Border = Color(0x0D2D3436) // Very subtle border
    val Primary = Color(0xFF00B894) // Soft teal-green
    val Error = Color(0xFFD63031) // Soft red
    val Accent = Color(0xFF0984E3) // Soft blue
}

// ==================== DARK THEMES ====================

// Dark Theme 1: Classic Dark (True dark with good contrast)
object ClassicDarkTheme {
    val Background = Color(0xFF121212) // Material dark (not pure black)
    val Card = Color(0xFF1E1E1E) // Slightly lighter dark
    val TextPrimary = Color(0xFFE0E0E0) // Soft white
    val TextSecondary = Color(0xFFB0B0B0) // Light gray
    val TextMuted = Color(0xFF808080) // Medium gray
    val TextLight = Color(0xFF606060) // Dark gray
    val TextHint = Color(0xFF404040) // Very dark gray
    val Border = Color(0x33FFFFFF) // Subtle white border
    val Primary = Color(0xFF4CAF50) // Soft green
    val Error = Color(0xFFEF5350) // Soft red
    val Accent = Color(0xFF42A5F5) // Soft blue
}

// Dark Theme 2: Warm Dark (Warm dark brown tones)
object WarmDarkTheme {
    val Background = Color(0xFF1A1612) // Warm dark brown
    val Card = Color(0xFF252018) // Warm dark card
    val TextPrimary = Color(0xFFE8E0D6) // Warm off-white
    val TextSecondary = Color(0xFFC8B8A8) // Warm light beige
    val TextMuted = Color(0xFFA89888) // Warm medium beige
    val TextLight = Color(0xFF887868) // Warm dark beige
    val TextHint = Color(0xFF685848) // Warm very dark beige
    val Border = Color(0x33E8E0D6) // Warm border
    val Primary = Color(0xFF66BB6A) // Warm green
    val Error = Color(0xFFE57373) // Warm red
    val Accent = Color(0xFFFFB74D) // Warm orange
}

// Dark Theme 3: Cool Dark (Cool blue-gray dark)
object CoolDarkTheme {
    val Background = Color(0xFF0D1117) // Cool dark blue-gray
    val Card = Color(0xFF161B22) // Cool dark card
    val TextPrimary = Color(0xFFD0D7DE) // Cool off-white
    val TextSecondary = Color(0xFFB1BAC4) // Cool light gray
    val TextMuted = Color(0xFF8B949E) // Cool medium gray
    val TextLight = Color(0xFF6E7681) // Cool dark gray
    val TextHint = Color(0xFF484F58) // Cool very dark gray
    val Border = Color(0x33D0D7DE) // Cool border
    val Primary = Color(0xFF58A6FF) // Cool blue
    val Error = Color(0xFFF85149) // Cool red
    val Accent = Color(0xFF79C0FF) // Cool cyan-blue
}

// Dark Theme 4: Soft Dark (Very soft dark, reduced contrast)
object SoftDarkTheme {
    val Background = Color(0xFF1E1E1E) // Soft dark gray (not too dark)
    val Card = Color(0xFF2A2A2A) // Soft dark card
    val TextPrimary = Color(0xFFE5E5E5) // Very soft white
    val TextSecondary = Color(0xFFC5C5C5) // Soft light gray
    val TextMuted = Color(0xFF959595) // Soft medium gray
    val TextLight = Color(0xFF757575) // Soft dark gray
    val TextHint = Color(0xFF555555) // Soft very dark gray
    val Border = Color(0x26E5E5E5) // Very soft border
    val Primary = Color(0xFF81C784) // Very soft green
    val Error = Color(0xFFE57373) // Very soft red
    val Accent = Color(0xFF64B5F6) // Very soft blue
}

// ==================== THEME TYPE ENUM ====================

enum class ThemeType(val displayName: String, val isDark: Boolean) {
    // Light themes
    CLASSIC_LIGHT("Classic Light", false),
    WARM_LIGHT("Warm Light", false),
    COOL_LIGHT("Cool Light", false),
    SOFT_LIGHT("Soft Light", false),
    
    // Dark themes
    CLASSIC_DARK("Classic Dark", true),
    WARM_DARK("Warm Dark", true),
    COOL_DARK("Cool Dark", true),
    SOFT_DARK("Soft Dark", true);
    
    companion object {
        fun fromString(value: String): ThemeType {
            return entries.find { it.name == value } ?: CLASSIC_LIGHT
        }
    }
}

// ==================== THEME COLOR GETTER ====================

fun getThemeColors(themeType: ThemeType): AppColors {
    return when (themeType) {
        ThemeType.CLASSIC_LIGHT -> {
            val primary = ClassicLightTheme.Primary
            val error = ClassicLightTheme.Error
            val accent = ClassicLightTheme.Accent
            val textMuted = ClassicLightTheme.TextMuted
            val textSecondary = ClassicLightTheme.TextSecondary
            AppColors(
                background = ClassicLightTheme.Background,
                card = ClassicLightTheme.Card,
                textPrimary = ClassicLightTheme.TextPrimary,
                textSecondary = ClassicLightTheme.TextSecondary,
                textMuted = ClassicLightTheme.TextMuted,
                textLight = ClassicLightTheme.TextLight,
                textHint = ClassicLightTheme.TextHint,
                border = ClassicLightTheme.Border,
                success = primary,
                error = error,
                accent = accent,
                tint = ClassicLightTheme.TextPrimary,
                rankGold = primary.copy(alpha = 0.9f),
                rankSilver = textMuted,
                rankBronze = error.copy(alpha = 0.7f),
                textOnPrimary = ClassicLightTheme.Card, // White/light text on primary background
                chartColors = listOf(primary, accent, error.copy(alpha = 0.8f), textSecondary.copy(alpha = 0.6f), textMuted.copy(alpha = 0.8f))
            )
        }
        ThemeType.WARM_LIGHT -> {
            val primary = WarmLightTheme.Primary
            val error = WarmLightTheme.Error
            val accent = WarmLightTheme.Accent
            val textMuted = WarmLightTheme.TextMuted
            val textSecondary = WarmLightTheme.TextSecondary
            AppColors(
                background = WarmLightTheme.Background,
                card = WarmLightTheme.Card,
                textPrimary = WarmLightTheme.TextPrimary,
                textSecondary = WarmLightTheme.TextSecondary,
                textMuted = WarmLightTheme.TextMuted,
                textLight = WarmLightTheme.TextLight,
                textHint = WarmLightTheme.TextHint,
                border = WarmLightTheme.Border,
                success = primary,
                error = error,
                accent = accent,
                tint = WarmLightTheme.TextPrimary,
                rankGold = primary.copy(alpha = 0.9f),
                rankSilver = textMuted,
                rankBronze = error.copy(alpha = 0.7f),
                textOnPrimary = WarmLightTheme.Card, // White/light text on primary background
                chartColors = listOf(primary, accent, error.copy(alpha = 0.8f), textSecondary.copy(alpha = 0.6f), textMuted.copy(alpha = 0.8f))
            )
        }
        ThemeType.COOL_LIGHT -> {
            val primary = CoolLightTheme.Primary
            val error = CoolLightTheme.Error
            val accent = CoolLightTheme.Accent
            val textMuted = CoolLightTheme.TextMuted
            val textSecondary = CoolLightTheme.TextSecondary
            AppColors(
                background = CoolLightTheme.Background,
                card = CoolLightTheme.Card,
                textPrimary = CoolLightTheme.TextPrimary,
                textSecondary = CoolLightTheme.TextSecondary,
                textMuted = CoolLightTheme.TextMuted,
                textLight = CoolLightTheme.TextLight,
                textHint = CoolLightTheme.TextHint,
                border = CoolLightTheme.Border,
                success = primary,
                error = error,
                accent = accent,
                tint = CoolLightTheme.TextPrimary,
                rankGold = primary.copy(alpha = 0.9f),
                rankSilver = textMuted,
                rankBronze = error.copy(alpha = 0.7f),
                textOnPrimary = CoolLightTheme.Card, // White/light text on primary background
                chartColors = listOf(primary, accent, error.copy(alpha = 0.8f), textSecondary.copy(alpha = 0.6f), textMuted.copy(alpha = 0.8f))
            )
        }
        ThemeType.SOFT_LIGHT -> {
            val primary = SoftLightTheme.Primary
            val error = SoftLightTheme.Error
            val accent = SoftLightTheme.Accent
            val textMuted = SoftLightTheme.TextMuted
            val textSecondary = SoftLightTheme.TextSecondary
            AppColors(
                background = SoftLightTheme.Background,
                card = SoftLightTheme.Card,
                textPrimary = SoftLightTheme.TextPrimary,
                textSecondary = SoftLightTheme.TextSecondary,
                textMuted = SoftLightTheme.TextMuted,
                textLight = SoftLightTheme.TextLight,
                textHint = SoftLightTheme.TextHint,
                border = SoftLightTheme.Border,
                success = primary,
                error = error,
                accent = accent,
                tint = SoftLightTheme.TextPrimary,
                rankGold = primary.copy(alpha = 0.9f),
                rankSilver = textMuted,
                rankBronze = error.copy(alpha = 0.7f),
                textOnPrimary = SoftLightTheme.Card, // White/light text on primary background
                chartColors = listOf(primary, accent, error.copy(alpha = 0.8f), textSecondary.copy(alpha = 0.6f), textMuted.copy(alpha = 0.8f))
            )
        }
        ThemeType.CLASSIC_DARK -> {
            val primary = ClassicDarkTheme.Primary
            val error = ClassicDarkTheme.Error
            val accent = ClassicDarkTheme.Accent
            val textMuted = ClassicDarkTheme.TextMuted
            val textSecondary = ClassicDarkTheme.TextSecondary
            AppColors(
                background = ClassicDarkTheme.Background,
                card = ClassicDarkTheme.Card,
                textPrimary = ClassicDarkTheme.TextPrimary,
                textSecondary = ClassicDarkTheme.TextSecondary,
                textMuted = ClassicDarkTheme.TextMuted,
                textLight = ClassicDarkTheme.TextLight,
                textHint = ClassicDarkTheme.TextHint,
                border = ClassicDarkTheme.Border,
                success = primary,
                error = error,
                accent = accent,
                tint = ClassicDarkTheme.TextPrimary,
                rankGold = primary.copy(alpha = 0.9f),
                rankSilver = textMuted,
                rankBronze = error.copy(alpha = 0.7f),
                textOnPrimary = ClassicDarkTheme.TextPrimary, // Already light text for dark theme
                chartColors = listOf(primary, accent, error.copy(alpha = 0.8f), textSecondary.copy(alpha = 0.6f), textMuted.copy(alpha = 0.8f))
            )
        }
        ThemeType.WARM_DARK -> {
            val primary = WarmDarkTheme.Primary
            val error = WarmDarkTheme.Error
            val accent = WarmDarkTheme.Accent
            val textMuted = WarmDarkTheme.TextMuted
            val textSecondary = WarmDarkTheme.TextSecondary
            AppColors(
                background = WarmDarkTheme.Background,
                card = WarmDarkTheme.Card,
                textPrimary = WarmDarkTheme.TextPrimary,
                textSecondary = WarmDarkTheme.TextSecondary,
                textMuted = WarmDarkTheme.TextMuted,
                textLight = WarmDarkTheme.TextLight,
                textHint = WarmDarkTheme.TextHint,
                border = WarmDarkTheme.Border,
                success = primary,
                error = error,
                accent = accent,
                tint = WarmDarkTheme.TextPrimary,
                rankGold = primary.copy(alpha = 0.9f),
                rankSilver = textMuted,
                rankBronze = error.copy(alpha = 0.7f),
                textOnPrimary = WarmDarkTheme.TextPrimary, // Already light text for dark theme
                chartColors = listOf(primary, accent, error.copy(alpha = 0.8f), textSecondary.copy(alpha = 0.6f), textMuted.copy(alpha = 0.8f))
            )
        }
        ThemeType.COOL_DARK -> {
            val primary = CoolDarkTheme.Primary
            val error = CoolDarkTheme.Error
            val accent = CoolDarkTheme.Accent
            val textMuted = CoolDarkTheme.TextMuted
            val textSecondary = CoolDarkTheme.TextSecondary
            AppColors(
                background = CoolDarkTheme.Background,
                card = CoolDarkTheme.Card,
                textPrimary = CoolDarkTheme.TextPrimary,
                textSecondary = CoolDarkTheme.TextSecondary,
                textMuted = CoolDarkTheme.TextMuted,
                textLight = CoolDarkTheme.TextLight,
                textHint = CoolDarkTheme.TextHint,
                border = CoolDarkTheme.Border,
                success = primary,
                error = error,
                accent = accent,
                tint = CoolDarkTheme.TextPrimary,
                rankGold = primary.copy(alpha = 0.9f),
                rankSilver = textMuted,
                rankBronze = error.copy(alpha = 0.7f),
                textOnPrimary = CoolDarkTheme.TextPrimary, // Already light text for dark theme
                chartColors = listOf(primary, accent, error.copy(alpha = 0.8f), textSecondary.copy(alpha = 0.6f), textMuted.copy(alpha = 0.8f))
            )
        }
        ThemeType.SOFT_DARK -> {
            val primary = SoftDarkTheme.Primary
            val error = SoftDarkTheme.Error
            val accent = SoftDarkTheme.Accent
            val textMuted = SoftDarkTheme.TextMuted
            val textSecondary = SoftDarkTheme.TextSecondary
            AppColors(
                background = SoftDarkTheme.Background,
                card = SoftDarkTheme.Card,
                textPrimary = SoftDarkTheme.TextPrimary,
                textSecondary = SoftDarkTheme.TextSecondary,
                textMuted = SoftDarkTheme.TextMuted,
                textLight = SoftDarkTheme.TextLight,
                textHint = SoftDarkTheme.TextHint,
                border = SoftDarkTheme.Border,
                success = primary,
                error = error,
                accent = accent,
                tint = SoftDarkTheme.TextPrimary,
                rankGold = primary.copy(alpha = 0.9f),
                rankSilver = textMuted,
                rankBronze = error.copy(alpha = 0.7f),
                textOnPrimary = SoftDarkTheme.TextPrimary, // Already light text for dark theme
                chartColors = listOf(primary, accent, error.copy(alpha = 0.8f), textSecondary.copy(alpha = 0.6f), textMuted.copy(alpha = 0.8f))
            )
        }
    }
}


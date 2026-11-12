package com.app.screentime.feature.newdesign.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.app.screentime.feature.newdesign.theme.AppColorScheme
import com.app.screentime.feature.newdesign.theme.AppTheme
import com.app.screentime.feature.newdesign.theme.LocalAppColorScheme

// Typealias for backward compatibility across all existing screens and components
typealias WinterDesignSystemTokens = AppColorScheme

val LocalWinterTokens = LocalAppColorScheme

/**
 * Convenience accessor for Winter design tokens and AppColorScheme.
 * Can be used inside @Composable as [WinterTokens.surfaceCard] or statically via [WinterTokens.Default.surfaceCard],
 * or via [AppTheme.colorScheme].
 */
object WinterTokens {
    val tokens: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current

    val Default: AppColorScheme = AppColorScheme.Default
    val Dark: AppColorScheme = AppColorScheme.Dark
    val Light: AppColorScheme = AppColorScheme.Light

    val isDark: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.isDark

    // 1. Backgrounds & Surfaces (Image 1)
    val backgroundCanvas: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.backgroundCanvas

    val surfaceHeader: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceHeader

    val surfaceCard: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceCard

    val surfaceNavigation: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceNavigation

    val surfaceControl: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceControl

    val surfaceInput: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceInput

    val surfaceInk: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceInk

    // Surfaces backward-compatibility aliases
    val surfaceRaised: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceRaised

    val surfaceFloatingNav: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceFloatingNav

    val surfaceExtended10: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceExtended10

    val surfaceBadgeGlass: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceBadgeGlass

    val surfaceInverse: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceInverse

    val surfaceTransparentBase: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.surfaceTransparentBase

    // 2. Text (Image 2)
    val textPrimary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.textPrimary

    val textSecondary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.textSecondary

    val textAccent: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.textAccent

    val textOnAccent: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.textOnAccent

    val textOnMedia: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.textOnMedia

    val textOnPhotoSecondary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.textOnPhotoSecondary

    // 3. Icons (Image 2)
    val iconPrimary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.iconPrimary

    val iconSecondary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.iconSecondary

    val iconMuted: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.iconMuted

    val iconAccent: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.iconAccent

    val iconOnAccent: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.iconOnAccent

    val iconOnMedia: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.iconOnMedia

    // 4. Buttons, States & Borders (Image 3)
    val actionPrimary: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.actionPrimary

    val actionDestructive: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.actionDestructive

    val statusPositive: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.statusPositive

    val statusSuccess: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.statusSuccess

    val statusError: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.statusError

    val statusErrorSurface: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.statusErrorSurface

    val borderDefault: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderDefault

    val borderSubtle: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderSubtle

    val borderMuted: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderMuted

    val borderSelected: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderSelected

    val borderInput: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderInput

    val borderAccent: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderAccent

    val borderExtended4: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderExtended4

    val borderNeutral: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderNeutral

    val borderOnAccent: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.borderOnAccent

    val controlIndicator: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.controlIndicator

    val systemHomeIndicator: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.systemHomeIndicator

    val controlMuted: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.controlMuted

    val systemHomeIndicatorMuted: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.systemHomeIndicatorMuted

    val controlThumb: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.controlThumb

    // 5. Search, Overlays & Effects (Image 4)
    val searchPulse: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.searchPulse

    val overlayScrim: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.overlayScrim

    val mediaWhite: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.mediaWhite

    val effectNavigationShadow: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.effectNavigationShadow

    val overlayAvailability: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.overlayAvailability

    val overlayPortraitClear: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.overlayPortraitClear

    val overlayPortraitDark: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.overlayPortraitDark

    val brandGoogleMark: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.brandGoogleMark

    // Core primitive palette
    val lavender200: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.lavender200

    val lavender400: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.lavender400

    val mauve100: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.mauve100

    val mauve300: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.mauve300

    val mauve400: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.mauve400

    val mint300: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.mint300

    val neutral0: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.neutral0

    val neutral1000: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.neutral1000

    val neutralTransparent: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.neutralTransparent

    val pearl50: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.pearl50

    val plum650: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.plum650

    val plum750: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.plum750

    val plum800: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.plum800

    val plum850: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.plum850

    val plum900: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.plum900

    val plum950: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.plum950

    val rose200: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.rose200

    val rose300: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.rose300

    val rose900: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.rose900

    val violet500: Color
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current.violet500
}

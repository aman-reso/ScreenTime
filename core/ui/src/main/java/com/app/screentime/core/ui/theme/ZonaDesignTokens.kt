package com.app.screentime.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.darkMode
import com.telekom.odsystem.tokens.tokens.lightMode

/**
 * DATING APP DESIGN SYSTEM
 * UPDATED SEPTEMBER 2026
 *
 * Light <-> Dark Color Mapping
 * Each semantic token encapsulates its corresponding light and dark value.
 */
data class ZonaColorTokens(
    val isDark: Boolean,

    // ── Primary Gradient Canvas ─────────────────────────────────────────────
    val backgroundStart: HexColor,       // Light: #FFF1F6 | Dark: #160E14 (Primary gradient start)
    val backgroundMid: HexColor,         // Light: #FFF9FB | Dark: #100B10 (Primary gradient midpoint)
    val backgroundEnd: HexColor,         // Light: #FFFFFF | Dark: #0B080B (Primary gradient end)

    // ── Surfaces ────────────────────────────────────────────────────────────
    val surface: HexColor,               // Light: #FFFFFF | Dark: #1C151B (Cards and primary surfaces)
    val surfaceElevated: HexColor,       // Light: #FFFFFF | Dark: #261B24 (Inputs and elevated controls)

    // ── Typography & Content ────────────────────────────────────────────────
    val textPrimary: HexColor,           // Light: #1A1A2E | Dark: #FFF7FA (Primary content and icons)
    val textSecondary: HexColor,         // Light: #6F5260 | Dark: #C7B6BF (Secondary copy and inactive icons)

    // ── Borders ─────────────────────────────────────────────────────────────
    val border: HexColor,                // Light: #DED4D8 | Dark: #4A3943 (Borders and dividers only)

    // ── Actions ─────────────────────────────────────────────────────────────
    val actionPrimary: HexColor,         // Light: #D81B60 | Dark: #D81B60 (Primary actions and selected states)
    val actionSoft: HexColor,            // Light: #FCE4EC | Dark: #4A1830 (Soft selected and decorative surfaces)

    // ── Feedback ────────────────────────────────────────────────────────────
    val feedbackDanger: HexColor,        // Light: #C62828 | Dark: #EF5350 (Destructive actions)

    // ── Media & Overlays ────────────────────────────────────────────────────
    val mediaOverlay: HexColor,          // Light: #000000 | Dark: #000000 (Camera, video and media overlays)
    val backgroundAlternate: HexColor,   // Light: #FFF0F5 | Dark: #1C151B (Google-login supporting background)
    val overlayLegacy: HexColor,         // Light: #0B0512 | Dark: #261B24 (User-details media gradient)

    // ── Loading & Error States ──────────────────────────────────────────────
    val loadingShimmer: HexColor,        // Light: #F3E8EC | Dark: #261B24 (Loading shimmer base)
    val loadingShimmerHighlight: HexColor, // Light: #FFFFFF | Dark: #352433 (Loading shimmer highlight)
    val errorIllustration: HexColor,     // Light: #FF4365 | Dark: #261B24 (API error illustration gradient)

    // ── Gender Selection Gradients ──────────────────────────────────────────
    val genderStrong: HexColor,          // Light: #E91E8C | Dark: #261B24 (Gender-selection gradient strong)
    val genderMid: HexColor,             // Light: #F06292 | Dark: #261B24 (Gender-selection gradient middle)
    val genderSoft: HexColor,            // Light: #F8BBD9 | Dark: #261B24 (Gender-selection gradient soft)

    // ── Semantic Convenience Aliases ────────────────────────────────────────
    val colorBorder: HexColor = border,
    val colorSurfaceRaised: HexColor = surfaceElevated,
    val background: HexColor = backgroundStart,
    val surfaceRaised: HexColor = surfaceElevated,
    val actionSecondary: HexColor = actionSoft,
    val textInverse: HexColor = if (isDark) HexColor("#1A1A2E") else HexColor("#FFF7FA"),
    val accent: HexColor = actionPrimary,
    val textAccent: HexColor = textSecondary,
    val lavenderAlt: HexColor = textSecondary,
    val lavenderMuted: HexColor = textSecondary,
    val lavenderLight: HexColor = actionSoft,
    val activeLime: HexColor = actionPrimary,
    val navIcon: HexColor = textSecondary,
    val navActive: HexColor = actionPrimary,
    val feedbackSuccess: HexColor = if (isDark) HexColor("#34C759") else HexColor("#2E7D32"),
    val activeIndicator: HexColor = feedbackSuccess,
    val navViolet: HexColor = surfaceElevated,
    val navMid: HexColor = surface,
    val canvasDark: HexColor = backgroundStart,
    val neutralWhite: HexColor = textPrimary,
    val black: HexColor = mediaOverlay
)

/**
 * Light Token Definitions.
 */
val ZonaLightTokens = ZonaColorTokens(
    isDark = false,
    backgroundStart = HexColor("#FFFFFF"),
    backgroundMid = HexColor("#FFF9FB"),
    backgroundEnd = HexColor("#FFFFFF"),
    surface = HexColor("#FFFFFF"),
    surfaceElevated = HexColor("#FFFFFF"),
    textPrimary = HexColor("#000000"),
    textSecondary = HexColor("#6F5260"),
    border = HexColor("#DED4D8"),
    actionPrimary = HexColor("#A7344D"),
    actionSoft = HexColor("#FCE4EC"),
    feedbackDanger = HexColor("#C62828"),
    mediaOverlay = HexColor("#000000"),
    backgroundAlternate = HexColor("#FFF0F5"),
    overlayLegacy = HexColor("#0B0512"),
    loadingShimmer = HexColor("#F3E8EC"),
    loadingShimmerHighlight = HexColor("#FFFFFF"),
    errorIllustration = HexColor("#FF4365"),
    genderStrong = HexColor("#E91E8C"),
    genderMid = HexColor("#F06292"),
    genderSoft = HexColor("#F8BBD9")
)

/**
 * Dark Token Definitions.
 */
val ZonaDarkTokens = ZonaColorTokens(
    isDark = true,
    backgroundStart = HexColor("#160E14"),
    backgroundMid = HexColor("#100B10"),
    backgroundEnd = HexColor("#0B080B"),
    surface = HexColor("#1C151B"),
    surfaceElevated = HexColor("#261B24"),
    textPrimary = HexColor("#FFF7FA"),
    textSecondary = HexColor("#C7B6BF"),
    border = HexColor("#4A3943"),
    actionPrimary = HexColor("#D81B60"),
    actionSoft = HexColor("#4A1830"),
    feedbackDanger = HexColor("#EF5350"),
    mediaOverlay = HexColor("#000000"),
    backgroundAlternate = HexColor("#1C151B"),
    overlayLegacy = HexColor("#261B24"),
    loadingShimmer = HexColor("#261B24"),
    loadingShimmerHighlight = HexColor("#352433"),
    errorIllustration = HexColor("#261B24"),
    genderStrong = HexColor("#261B24"),
    genderMid = HexColor("#261B24"),
    genderSoft = HexColor("#261B24")
)

/**
 * CompositionLocal providing current active ZonaColorTokens.
 */
val LocalZonaColors = staticCompositionLocalOf { ZonaDarkTokens }

/**
 * Dynamic ZonaColors token access.
 * Automatically resolves to the current Light or Dark token value in Composable contexts.
 */
object ZonaColors {
    val Light: ZonaColorTokens get() = ZonaLightTokens
    val Dark: ZonaColorTokens get() = ZonaDarkTokens

    val current: ZonaColorTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalZonaColors.current

    // Semantic tokens
    val BackgroundStart: HexColor @Composable @ReadOnlyComposable get() = current.backgroundStart
    val BackgroundMid: HexColor @Composable @ReadOnlyComposable get() = current.backgroundMid
    val BackgroundEnd: HexColor @Composable @ReadOnlyComposable get() = current.backgroundEnd
    val Surface: HexColor @Composable @ReadOnlyComposable get() = current.surface
    val SurfaceElevated: HexColor @Composable @ReadOnlyComposable get() = current.surfaceElevated
    val TextPrimary: HexColor @Composable @ReadOnlyComposable get() = current.textPrimary
    val TextSecondary: HexColor @Composable @ReadOnlyComposable get() = current.textSecondary
    val Border: HexColor @Composable @ReadOnlyComposable get() = current.border
    val ActionPrimary: HexColor @Composable @ReadOnlyComposable get() = current.actionPrimary
    val ActionSoft: HexColor @Composable @ReadOnlyComposable get() = current.actionSoft
    val FeedbackDanger: HexColor @Composable @ReadOnlyComposable get() = current.feedbackDanger
    val MediaOverlay: HexColor @Composable @ReadOnlyComposable get() = current.mediaOverlay
    val BackgroundAlternate: HexColor @Composable @ReadOnlyComposable get() = current.backgroundAlternate
    val OverlayLegacy: HexColor @Composable @ReadOnlyComposable get() = current.overlayLegacy
    val LoadingShimmer: HexColor @Composable @ReadOnlyComposable get() = current.loadingShimmer
    val loadingShimmerHighlight: HexColor @Composable @ReadOnlyComposable get() = current.loadingShimmerHighlight
    val ErrorIllustration: HexColor @Composable @ReadOnlyComposable get() = current.errorIllustration
    val GenderStrong: HexColor @Composable @ReadOnlyComposable get() = current.genderStrong
    val GenderMid: HexColor @Composable @ReadOnlyComposable get() = current.genderMid
    val GenderSoft: HexColor @Composable @ReadOnlyComposable get() = current.genderSoft

    // Backward-compatible semantic aliases
    val colorBorder: HexColor @Composable @ReadOnlyComposable get() = current.colorBorder
    val colorSurfaceRaised: HexColor @Composable @ReadOnlyComposable get() = current.colorSurfaceRaised
    val Background: HexColor @Composable @ReadOnlyComposable get() = current.background
    val SurfaceRaised: HexColor @Composable @ReadOnlyComposable get() = current.surfaceElevated
    val ActionSecondary: HexColor @Composable @ReadOnlyComposable get() = current.actionSoft
    val TextInverse: HexColor @Composable @ReadOnlyComposable get() = current.textInverse
    val Accent: HexColor @Composable @ReadOnlyComposable get() = current.accent
    val TextAccent: HexColor @Composable @ReadOnlyComposable get() = current.textAccent
    val LavenderAlt: HexColor @Composable @ReadOnlyComposable get() = current.lavenderAlt
    val LavenderMuted: HexColor @Composable @ReadOnlyComposable get() = current.lavenderMuted
    val LavenderLight: HexColor @Composable @ReadOnlyComposable get() = current.lavenderLight
    val ActiveLime: HexColor @Composable @ReadOnlyComposable get() = current.activeLime
    val FeedbackSuccess: HexColor @Composable @ReadOnlyComposable get() = current.feedbackSuccess
    val ActiveIndicator: HexColor @Composable @ReadOnlyComposable get() = current.activeIndicator
    val NavIcon: HexColor @Composable @ReadOnlyComposable get() = current.navIcon
    val NavActive: HexColor @Composable @ReadOnlyComposable get() = current.navActive
    val NavViolet: HexColor @Composable @ReadOnlyComposable get() = current.navViolet
    val NavMid: HexColor @Composable @ReadOnlyComposable get() = current.navMid
    val CanvasDark: HexColor @Composable @ReadOnlyComposable get() = current.canvasDark
    val NeutralWhite: HexColor @Composable @ReadOnlyComposable get() = current.neutralWhite
    val Black: HexColor @Composable @ReadOnlyComposable get() = current.black
}

/**
 * Jetpack Compose Color accessors mapping to ZonaColors.
 */
object ZonaComposeColors {
    val BackgroundStart: Color @Composable @ReadOnlyComposable get() = ZonaColors.BackgroundStart.getColor()
    val BackgroundMid: Color @Composable @ReadOnlyComposable get() = ZonaColors.BackgroundMid.getColor()
    val BackgroundEnd: Color @Composable @ReadOnlyComposable get() = ZonaColors.BackgroundEnd.getColor()
    val Surface: Color @Composable @ReadOnlyComposable get() = ZonaColors.Surface.getColor()
    val SurfaceElevated: Color @Composable @ReadOnlyComposable get() = ZonaColors.SurfaceElevated.getColor()
    val TextPrimary: Color @Composable @ReadOnlyComposable get() = ZonaColors.TextPrimary.getColor()
    val TextSecondary: Color @Composable @ReadOnlyComposable get() = ZonaColors.TextSecondary.getColor()
    val Border: Color @Composable @ReadOnlyComposable get() = ZonaColors.Border.getColor()
    val ActionPrimary: Color @Composable @ReadOnlyComposable get() = ZonaColors.ActionPrimary.getColor()
    val ActionSoft: Color @Composable @ReadOnlyComposable get() = ZonaColors.ActionSoft.getColor()
    val FeedbackDanger: Color @Composable @ReadOnlyComposable get() = ZonaColors.FeedbackDanger.getColor()
    val FeedbackSuccess: Color @Composable @ReadOnlyComposable get() = ZonaColors.FeedbackSuccess.getColor()
    val ActiveIndicator: Color @Composable @ReadOnlyComposable get() = ZonaColors.ActiveIndicator.getColor()
    val MediaOverlay: Color @Composable @ReadOnlyComposable get() = ZonaColors.MediaOverlay.getColor()
    val BackgroundAlternate: Color @Composable @ReadOnlyComposable get() = ZonaColors.BackgroundAlternate.getColor()
    val OverlayLegacy: Color @Composable @ReadOnlyComposable get() = ZonaColors.OverlayLegacy.getColor()
    val LoadingShimmer: Color @Composable @ReadOnlyComposable get() = ZonaColors.LoadingShimmer.getColor()
    val LoadingShimmerHighlight: Color @Composable @ReadOnlyComposable get() = ZonaColors.loadingShimmerHighlight.getColor()
    val ErrorIllustration: Color @Composable @ReadOnlyComposable get() = ZonaColors.ErrorIllustration.getColor()
    val GenderStrong: Color @Composable @ReadOnlyComposable get() = ZonaColors.GenderStrong.getColor()
    val GenderMid: Color @Composable @ReadOnlyComposable get() = ZonaColors.GenderMid.getColor()
    val GenderSoft: Color @Composable @ReadOnlyComposable get() = ZonaColors.GenderSoft.getColor()

    // Backward-compatible aliases
    val Background: Color @Composable @ReadOnlyComposable get() = ZonaColors.Background.getColor()
    val SurfaceRaised: Color @Composable @ReadOnlyComposable get() = ZonaColors.SurfaceRaised.getColor()
    val ActionSecondary: Color @Composable @ReadOnlyComposable get() = ZonaColors.ActionSecondary.getColor()
    val TextInverse: Color @Composable @ReadOnlyComposable get() = ZonaColors.TextInverse.getColor()
    val Accent: Color @Composable @ReadOnlyComposable get() = ZonaColors.Accent.getColor()
    val TextAccent: Color @Composable @ReadOnlyComposable get() = ZonaColors.TextAccent.getColor()
    val LavenderAlt: Color @Composable @ReadOnlyComposable get() = ZonaColors.LavenderAlt.getColor()
    val LavenderMuted: Color @Composable @ReadOnlyComposable get() = ZonaColors.LavenderMuted.getColor()
    val ActiveLime: Color @Composable @ReadOnlyComposable get() = ZonaColors.ActiveLime.getColor()
    val NavIcon: Color @Composable @ReadOnlyComposable get() = ZonaColors.NavIcon.getColor()
    val NavActive: Color @Composable @ReadOnlyComposable get() = ZonaColors.NavActive.getColor()
    val CanvasDark: Color @Composable @ReadOnlyComposable get() = ZonaColors.CanvasDark.getColor()
    val NeutralWhite: Color @Composable @ReadOnlyComposable get() = ZonaColors.NeutralWhite.getColor()
    val Black: Color @Composable @ReadOnlyComposable get() = ZonaColors.Black.getColor()
}

/**
 * Extension helpers for clean Telekom ODS integration without boilerplate.
 */
fun HexColor.toColorModel(): ODSColorModel = ODSColorModel(hexColor = this)
fun HexColor.toColorList(): List<ODSColorModel> = listOf(ODSColorModel(hexColor = this))
val HexColor.ods: ODSColorModel get() = ODSColorModel(hexColor = this)

/**
 * Standard Design System Gradients.
 */
object ZonaGradients {
    @Composable
    fun primaryBackground(): Brush {
        return Brush.verticalGradient(
            listOf(
                ZonaColors.BackgroundStart.getColor(),
                ZonaColors.BackgroundMid.getColor(),
                ZonaColors.BackgroundEnd.getColor()
            )
        )
    }

    @Composable
    fun genderSelection(): Brush {
        return Brush.horizontalGradient(
            listOf(
                ZonaColors.GenderStrong.getColor(),
                ZonaColors.GenderMid.getColor(),
                ZonaColors.GenderSoft.getColor()
            )
        )
    }
}

/**
 * Telekom ODSTheme instance configured for Dark Mode.
 */
val zonaDarkODSTheme: ODSTheme = darkMode.copy(
    name = "zonaDarkTheme",
    basicAccent = ZonaDarkTokens.actionPrimary,
    basicAccentSecondary = ZonaDarkTokens.actionSoft,
    basicBackground = ZonaDarkTokens.backgroundStart,
    basicBackgroundCard = ZonaDarkTokens.surfaceElevated,
    basicBackgroundCardSubtle = ZonaDarkTokens.surface,
    basicBackgroundSubtle = ZonaDarkTokens.backgroundMid,
    basicModalOverlay = HexColor(0x80000000),
    basicStroke = ZonaDarkTokens.border,
    basicStrokeSubtle = ZonaDarkTokens.border,
    basicText = ZonaDarkTokens.textPrimary,
    basicTextDominant = ZonaDarkTokens.textPrimary,
    basicTextLink = ZonaDarkTokens.actionPrimary,
    basicTextOnAccent = HexColor("#FFFFFF"),
    basicTextOnAccentSecondary = ZonaDarkTokens.textPrimary,
    basicTextRecessive = ZonaDarkTokens.textSecondary,
    functionalDestructiveStandard = ZonaDarkTokens.feedbackDanger,
    functionalDestructiveHovered = ZonaDarkTokens.feedbackDanger,
    functionalDestructivePressed = ZonaDarkTokens.feedbackDanger,
    functionalDestructiveSubtle = ZonaDarkTokens.actionSoft,
    functionalSuccessStandard = ZonaDarkTokens.actionPrimary,
    functionalSuccessHovered = ZonaDarkTokens.actionPrimary,
    functionalSuccessPressed = ZonaDarkTokens.actionPrimary,
    functionalSuccessSubtle = ZonaDarkTokens.actionSoft,
    functionalWarningStandard = HexColor("#FF9800"),
    functionalWarningHovered = HexColor("#FF9800"),
    functionalWarningPressed = HexColor("#FF9800"),
    functionalWarningSubtle = ZonaDarkTokens.actionSoft,
    functionalInformationalStandard = ZonaDarkTokens.actionPrimary,
    functionalInformationalHovered = ZonaDarkTokens.actionPrimary,
    functionalInformationalPressed = ZonaDarkTokens.actionPrimary,
    functionalInformationalSubtle = ZonaDarkTokens.actionSoft,
    functionalNotificationNotification = ZonaDarkTokens.actionPrimary,
    functionalNotificationTextOnNotification = HexColor("#FFFFFF"),
    interactionStatesDisabledAccentDisabled = ZonaDarkTokens.border,
    interactionStatesDisabledAccentSecondaryDisabled = ZonaDarkTokens.actionSoft,
    interactionStatesDisabledBackgroundCardDisabled = ZonaDarkTokens.surface,
    interactionStatesDisabledBackgroundDisabled = ZonaDarkTokens.backgroundStart,
    interactionStatesDisabledBackgroundSubtleDisabled = ZonaDarkTokens.backgroundMid,
    interactionStatesDisabledStrokeDisabled = ZonaDarkTokens.border,
    interactionStatesDisabledStrokeSubtleDisabled = ZonaDarkTokens.border,
    interactionStatesDisabledTextDisabled = ZonaDarkTokens.textSecondary,
    interactionStatesDisabledTextDominantDisabled = ZonaDarkTokens.textSecondary,
    interactionStatesDisabledTextLinkDisabled = ZonaDarkTokens.textSecondary,
    interactionStatesDisabledTextOnAccentDisabled = ZonaDarkTokens.textSecondary,
    interactionStatesDisabledTextOnAccentSecondaryDisabled = ZonaDarkTokens.textSecondary,
    interactionStatesDisabledTextRecessiveDisabled = ZonaDarkTokens.textSecondary,
    interactionStatesFocusFocus = ZonaDarkTokens.actionPrimary,
    interactionStatesFocusStrokeActive = ZonaDarkTokens.actionPrimary,
    interactionStatesHoverAccentHover = ZonaDarkTokens.actionPrimary,
    interactionStatesHoverAccentSecondaryHover = ZonaDarkTokens.actionSoft,
    interactionStatesHoverBackgroundHover = ZonaDarkTokens.surface,
    interactionStatesHoverBackgroundSubtleHover = ZonaDarkTokens.backgroundMid,
    interactionStatesHoverStrokeHover = ZonaDarkTokens.actionPrimary,
    interactionStatesHoverStrokeSubtleHover = ZonaDarkTokens.border,
    interactionStatesHoverTextDominantHover = ZonaDarkTokens.textPrimary,
    interactionStatesHoverTextHover = ZonaDarkTokens.textPrimary,
    interactionStatesHoverTextLinkHover = ZonaDarkTokens.actionPrimary,
    interactionStatesHoverTextOnAccentHover = HexColor("#FFFFFF"),
    interactionStatesHoverTextOnAccentSecondaryHover = ZonaDarkTokens.textPrimary,
    interactionStatesHoverTextRecessiveHover = ZonaDarkTokens.textSecondary,
    interactionStatesPressedAccentPressed = ZonaDarkTokens.actionPrimary,
    interactionStatesPressedAccentSecondaryPressed = ZonaDarkTokens.actionSoft,
    interactionStatesPressedBackgroundPressed = ZonaDarkTokens.surface,
    interactionStatesPressedBackgroundSubtlePressed = ZonaDarkTokens.backgroundMid,
    interactionStatesPressedStrokePressed = ZonaDarkTokens.actionPrimary,
    interactionStatesPressedStrokeSubtlePressed = ZonaDarkTokens.border,
    interactionStatesPressedTextDominantPressed = ZonaDarkTokens.textPrimary,
    interactionStatesPressedTextLinkPressed = ZonaDarkTokens.actionPrimary,
    interactionStatesPressedTextOnAccentPressed = HexColor("#FFFFFF"),
    interactionStatesPressedTextOnAccentSecondaryPressed = ZonaDarkTokens.textPrimary,
    interactionStatesPressedTextPressed = ZonaDarkTokens.textPrimary,
    interactionStatesPressedTextRecessivePressed = ZonaDarkTokens.textSecondary,
    interactionStatesVisitedTextLinkVisited = ZonaDarkTokens.actionPrimary,
    interactionStatesVisitedTextVisited = ZonaDarkTokens.textPrimary,
    shadesAccentShadesAccentDominant = ZonaDarkTokens.actionPrimary,
    shadesAccentShadesAccentExtraDominant = ZonaDarkTokens.actionPrimary,
    shadesAccentShadesAccentExtraRecessive = ZonaDarkTokens.actionSoft,
    shadesAccentShadesAccentRecessive = ZonaDarkTokens.actionSoft,
    shadesAccentShadesAccentSubtle = ZonaDarkTokens.actionSoft,
    shadesNeutralShades100 = ZonaDarkTokens.backgroundStart,
    shadesNeutralShades200 = ZonaDarkTokens.backgroundMid,
    shadesNeutralShades300 = ZonaDarkTokens.surface,
    shadesNeutralShades400 = ZonaDarkTokens.surfaceElevated,
    shadesNeutralShades500 = ZonaDarkTokens.border,
    shadesNeutralShades600 = ZonaDarkTokens.textSecondary,
    shadesNeutralShades700 = ZonaDarkTokens.textPrimary,
    shadesNeutralShades800 = ZonaDarkTokens.textPrimary,
    shadesNeutralShades900 = ZonaDarkTokens.textPrimary,
    shadesSecondaryAccentShadesSecondaryAccentDominant = ZonaDarkTokens.actionSoft,
    shadesSecondaryAccentShadesSecondaryAccentExtraDominant = ZonaDarkTokens.actionSoft,
    shadesSecondaryAccentShadesSecondaryAccentExtraRecessive = ZonaDarkTokens.actionSoft,
    shadesSecondaryAccentShadesSecondaryAccentRecessive = ZonaDarkTokens.actionSoft,
    shadesSecondaryAccentShadesSecondaryAccentSubtle = ZonaDarkTokens.actionSoft
)

/**
 * Telekom ODSTheme instance configured for Light Mode.
 */
val zonaLightODSTheme: ODSTheme = lightMode.copy(
    name = "zonaLightTheme",
    basicAccent = ZonaLightTokens.actionPrimary,
    basicAccentSecondary = ZonaLightTokens.actionSoft,
    basicBackground = ZonaLightTokens.backgroundStart,
    basicBackgroundCard = ZonaLightTokens.surfaceElevated,
    basicBackgroundCardSubtle = ZonaLightTokens.surface,
    basicBackgroundSubtle = ZonaLightTokens.backgroundMid,
    basicModalOverlay = HexColor(0x80000000),
    basicStroke = ZonaLightTokens.border,
    basicStrokeSubtle = ZonaLightTokens.border,
    basicText = ZonaLightTokens.textPrimary,
    basicTextDominant = ZonaLightTokens.textPrimary,
    basicTextLink = ZonaLightTokens.actionPrimary,
    basicTextOnAccent = HexColor("#FFFFFF"),
    basicTextOnAccentSecondary = ZonaLightTokens.textPrimary,
    basicTextRecessive = ZonaLightTokens.textSecondary,
    functionalDestructiveStandard = ZonaLightTokens.feedbackDanger,
    functionalDestructiveHovered = ZonaLightTokens.feedbackDanger,
    functionalDestructivePressed = ZonaLightTokens.feedbackDanger,
    functionalDestructiveSubtle = ZonaLightTokens.actionSoft,
    functionalSuccessStandard = ZonaLightTokens.actionPrimary,
    functionalSuccessHovered = ZonaLightTokens.actionPrimary,
    functionalSuccessPressed = ZonaLightTokens.actionPrimary,
    functionalSuccessSubtle = ZonaLightTokens.actionSoft,
    functionalWarningStandard = HexColor("#FF9800"),
    functionalWarningHovered = HexColor("#FF9800"),
    functionalWarningPressed = HexColor("#FF9800"),
    functionalWarningSubtle = ZonaLightTokens.actionSoft,
    functionalInformationalStandard = ZonaLightTokens.actionPrimary,
    functionalInformationalHovered = ZonaLightTokens.actionPrimary,
    functionalInformationalPressed = ZonaLightTokens.actionPrimary,
    functionalInformationalSubtle = ZonaLightTokens.actionSoft,
    functionalNotificationNotification = ZonaLightTokens.actionPrimary,
    functionalNotificationTextOnNotification = HexColor("#FFFFFF"),
    interactionStatesDisabledAccentDisabled = ZonaLightTokens.border,
    interactionStatesDisabledAccentSecondaryDisabled = ZonaLightTokens.actionSoft,
    interactionStatesDisabledBackgroundCardDisabled = ZonaLightTokens.surface,
    interactionStatesDisabledBackgroundDisabled = ZonaLightTokens.backgroundStart,
    interactionStatesDisabledBackgroundSubtleDisabled = ZonaLightTokens.backgroundMid,
    interactionStatesDisabledStrokeDisabled = ZonaLightTokens.border,
    interactionStatesDisabledStrokeSubtleDisabled = ZonaLightTokens.border,
    interactionStatesDisabledTextDisabled = ZonaLightTokens.textSecondary,
    interactionStatesDisabledTextDominantDisabled = ZonaLightTokens.textSecondary,
    interactionStatesDisabledTextLinkDisabled = ZonaLightTokens.textSecondary,
    interactionStatesDisabledTextOnAccentDisabled = ZonaLightTokens.textSecondary,
    interactionStatesDisabledTextOnAccentSecondaryDisabled = ZonaLightTokens.textSecondary,
    interactionStatesDisabledTextRecessiveDisabled = ZonaLightTokens.textSecondary,
    interactionStatesFocusFocus = ZonaLightTokens.actionPrimary,
    interactionStatesFocusStrokeActive = ZonaLightTokens.actionPrimary,
    interactionStatesHoverAccentHover = ZonaLightTokens.actionPrimary,
    interactionStatesHoverAccentSecondaryHover = ZonaLightTokens.actionSoft,
    interactionStatesHoverBackgroundHover = ZonaLightTokens.surface,
    interactionStatesHoverBackgroundSubtleHover = ZonaLightTokens.backgroundMid,
    interactionStatesHoverStrokeHover = ZonaLightTokens.actionPrimary,
    interactionStatesHoverStrokeSubtleHover = ZonaLightTokens.border,
    interactionStatesHoverTextDominantHover = ZonaLightTokens.textPrimary,
    interactionStatesHoverTextHover = ZonaLightTokens.textPrimary,
    interactionStatesHoverTextLinkHover = ZonaLightTokens.actionPrimary,
    interactionStatesHoverTextOnAccentHover = HexColor("#FFFFFF"),
    interactionStatesHoverTextOnAccentSecondaryHover = ZonaLightTokens.textPrimary,
    interactionStatesHoverTextRecessiveHover = ZonaLightTokens.textSecondary,
    interactionStatesPressedAccentPressed = ZonaLightTokens.actionPrimary,
    interactionStatesPressedAccentSecondaryPressed = ZonaLightTokens.actionSoft,
    interactionStatesPressedBackgroundPressed = ZonaLightTokens.surface,
    interactionStatesPressedBackgroundSubtlePressed = ZonaLightTokens.backgroundMid,
    interactionStatesPressedStrokePressed = ZonaLightTokens.actionPrimary,
    interactionStatesPressedStrokeSubtlePressed = ZonaLightTokens.border,
    interactionStatesPressedTextDominantPressed = ZonaLightTokens.textPrimary,
    interactionStatesPressedTextLinkPressed = ZonaLightTokens.actionPrimary,
    interactionStatesPressedTextOnAccentPressed = HexColor("#FFFFFF"),
    interactionStatesPressedTextOnAccentSecondaryPressed = ZonaLightTokens.textPrimary,
    interactionStatesPressedTextPressed = ZonaLightTokens.textPrimary,
    interactionStatesPressedTextRecessivePressed = ZonaLightTokens.textSecondary,
    interactionStatesVisitedTextLinkVisited = ZonaLightTokens.actionPrimary,
    interactionStatesVisitedTextVisited = ZonaLightTokens.textPrimary,
    shadesAccentShadesAccentDominant = ZonaLightTokens.actionPrimary,
    shadesAccentShadesAccentExtraDominant = ZonaLightTokens.actionPrimary,
    shadesAccentShadesAccentExtraRecessive = ZonaLightTokens.actionSoft,
    shadesAccentShadesAccentRecessive = ZonaLightTokens.actionSoft,
    shadesAccentShadesAccentSubtle = ZonaLightTokens.actionSoft,
    shadesNeutralShades100 = ZonaLightTokens.backgroundStart,
    shadesNeutralShades200 = ZonaLightTokens.backgroundMid,
    shadesNeutralShades300 = ZonaLightTokens.surface,
    shadesNeutralShades400 = ZonaLightTokens.surfaceElevated,
    shadesNeutralShades500 = ZonaLightTokens.border,
    shadesNeutralShades600 = ZonaLightTokens.textSecondary,
    shadesNeutralShades700 = ZonaLightTokens.textPrimary,
    shadesNeutralShades800 = ZonaLightTokens.textPrimary,
    shadesNeutralShades900 = ZonaLightTokens.textPrimary,
    shadesSecondaryAccentShadesSecondaryAccentDominant = ZonaLightTokens.actionSoft,
    shadesSecondaryAccentShadesSecondaryAccentExtraDominant = ZonaLightTokens.actionSoft,
    shadesSecondaryAccentShadesSecondaryAccentExtraRecessive = ZonaLightTokens.actionSoft,
    shadesSecondaryAccentShadesSecondaryAccentRecessive = ZonaLightTokens.actionSoft,
    shadesSecondaryAccentShadesSecondaryAccentSubtle = ZonaLightTokens.actionSoft
)

/**
 * Default ODSTheme export (defaults to Dark Mode).
 */
val zonaODSTheme: ODSTheme = zonaLightODSTheme
val winterODSTheme: ODSTheme = zonaODSTheme

/**
 * Resolves current ODSTheme based on active theme setting.
 */
@Composable
fun rememberZonaODSTheme(isDark: Boolean = LocalZonaColors.current.isDark): ODSTheme {
    return if (isDark) zonaDarkODSTheme else zonaLightODSTheme
}

@Composable
fun rememberWinterODSTheme(isDark: Boolean = LocalZonaColors.current.isDark): ODSTheme {
    return rememberZonaODSTheme(isDark)
}

typealias WinterColors = ZonaColors
typealias WinterColorTokens = ZonaColorTokens
typealias WinterGradients = ZonaGradients
val LocalWinterColors = LocalZonaColors


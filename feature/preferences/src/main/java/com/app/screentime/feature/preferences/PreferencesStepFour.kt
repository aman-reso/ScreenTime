package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step 4: Language Preferences.
 * Recreated with 100% ODS components per Figma node-id 10-155.
 * Strictly adheres to project rules:
 * - 100% ODS components & ODSWrap.
 * - Colors picked from `scheme: ODSTheme`.
 * - Max text size 16sp with Funnel Sans.
 * - Padding, margins, and gaps from ODSVariables.
 * - Broken down into modular, maintainable sub-components.
 */

val DEFAULT_PREFERENCES_LANGUAGES = listOf(
    "Hindi",
    "English",
    "Gujarati",
    "Bengali",
    "Punjabi",
    "Tamil",
    "Marathi",
    "Malayalam",
    "Telugu"
)

private const val MAX_LANGUAGES_SELECTION = 5

/**
 * Header component for Step 4 (Languages).
 */
@Composable
fun LanguageHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent3
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "What languages do you speak?",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Select up to 5 languages to show on your profile and match with like-minded speakers.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

/**
 * Selection counter badge component showing current vs max selected.
 */
@Composable
fun LanguageSelectionCounter(
    selectedCount: Int,
    maxCount: Int,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier,
        padding = ODSPadding(
            top = ODSVariables.spacingComponent2,
            bottom = ODSVariables.spacingComponent2,
            left = ODSVariables.spacingComponent4,
            right = ODSVariables.spacingComponent4
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
    ) {
        ODSText(
            text = "$selectedCount of $maxCount selected",
            style = ODSTextStyles.microcopyBold,
            color = scheme.basicTextDominant
        )
    }
}

/**
 * Individual language selectable chip component.
 */
@Composable
fun LanguageChip(
    language: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val dropShadowEffect = if (isSelected) {
        ODSEffect(
            elevations = listOf(
                ODSElevation(
                    x = 0,
                    y = 4,
                    blur = 10,
                    spread = 0,
                    color = HexColor(0x24000000),
                    type = ODSElevationType.DROP_SHADOW
                )
            )
        )
    } else null

    ODSRow(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onToggle
        ),
        gap = ODSVariables.spacingComponent3,
        padding = ODSPadding(
            top = ODSVariables.spacingComponent4,
            bottom = ODSVariables.spacingComponent4,
            left = ODSVariables.spacingLayout1,
            right = ODSVariables.spacingLayout1
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        border = if (isSelected) null else ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        background = listOf(
            ODSColorModel(
                hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
            )
        ),
        effect = dropShadowEffect
    ) {
        ODSText(
            text = language,
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextDominant
        )

        if (isSelected) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_check,
                    contentDescription = "Selected"
                ),
                tint = scheme.basicAccent.getColor(),
                modifier = Modifier.size(ODSVariables.spacingComponent5)
            )
        }
    }
}

/**
 * Grid/Wrap container for displaying all language chips.
 */
@Composable
fun LanguageChipsWrap(
    allLanguages: List<String>,
    selectedLanguages: List<String>,
    onToggleLanguage: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSWrap(
        modifier = modifier.fillMaxWidth(),
        horizontalGap = ODSVariables.spacingComponent3,
        verticalGap = ODSVariables.spacingComponent3,
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        allLanguages.forEach { language ->
            val isSelected = selectedLanguages.contains(language)
            LanguageChip(
                language = language,
                isSelected = isSelected,
                onToggle = { onToggleLanguage(language) },
                scheme = scheme
            )
        }
    }
}

/**
 * Complete Step 4: Languages Screen.
 */
@Composable
fun PreferencesStepFour(
    selectedLanguages: List<String>,
    scheme: ODSTheme,
    onToggleLanguage: (String) -> Unit,
    modifier: Modifier = Modifier,
    allLanguages: List<String> = DEFAULT_PREFERENCES_LANGUAGES
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        // ── 1. Heading & Instructions ──────────────────────────────────────
        LanguageHeader(scheme = scheme)

        // ── 2. Selection Badge ─────────────────────────────────────────────
        LanguageSelectionCounter(
            selectedCount = selectedLanguages.size,
            maxCount = MAX_LANGUAGES_SELECTION,
            scheme = scheme
        )

        // ── 3. Language Chips Wrap ─────────────────────────────────────────
        LanguageChipsWrap(
            allLanguages = allLanguages,
            selectedLanguages = selectedLanguages,
            onToggleLanguage = { language ->
                if (selectedLanguages.contains(language)) {
                    if (selectedLanguages.size > 1) {
                        onToggleLanguage(language)
                    }
                } else if (selectedLanguages.size < MAX_LANGUAGES_SELECTION) {
                    onToggleLanguage(language)
                }
            },
            scheme = scheme
        )
    }
}

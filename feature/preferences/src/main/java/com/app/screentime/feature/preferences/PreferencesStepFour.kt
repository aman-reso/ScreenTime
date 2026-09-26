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
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

val DEFAULT_PREFERENCES_LANGUAGES = listOf(
    "English",
    "Hindi",
    "Spanish",
    "French",
    "German",
    "Punjabi",
    "Bengali",
    "Gujarati",
    "Tamil",
    "Telugu",
    "Marathi"
)

private const val MAX_LANGUAGES_SELECTION = 5

/**
 * Step 7: Language Preferences.
 * Matches unified Figma design system layout:
 * - "What languages do you speak?"
 * - "Select up to 5 languages to show on your profile."
 * - Selectable language chips.
 */
@Composable
fun PreferencesStepFour(
    selectedLanguages: List<String>,
    scheme: ODSTheme,
    onToggleLanguage: (String) -> Unit,
    modifier: Modifier = Modifier,
    allLanguages: List<String> = DEFAULT_PREFERENCES_LANGUAGES
) {
    val primaryTextColor = scheme.basicTextDominant
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent
    val inactiveBorderColor = scheme.basicStrokeSubtle

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 28.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // ── 1. Heading & Subtitle ──────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "What languages do you speak?",
                style = ODSTextStyles.titleL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Select up to $MAX_LANGUAGES_SELECTION languages to show on your profile (${selectedLanguages.size}/$MAX_LANGUAGES_SELECTION).",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Language Chips Wrap ─────────────────────────────────────────
        ODSWrap(
            modifier = Modifier.fillMaxWidth(),
            horizontalGap = 10.dp,
            verticalGap = 10.dp,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            allLanguages.forEach { language ->
                val isSelected = selectedLanguages.contains(language)
                ODSRow(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            if (isSelected) {
                                if (selectedLanguages.size > 1) {
                                    onToggleLanguage(language)
                                }
                            } else if (selectedLanguages.size < MAX_LANGUAGES_SELECTION) {
                                onToggleLanguage(language)
                            }
                        }
                    ),
                    gap = 8.dp,
                    padding = ODSPadding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                    cornerRadius = ODSCorners(all = 14.dp),
                    border = ODSBorder(
                        width = if (isSelected) 2.dp else 1.dp,
                        colorList = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) accentColor else inactiveBorderColor
                            )
                        )
                    ),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundCard
                        )
                    )
                ) {
                    ODSText(
                        text = language,
                        style = ODSTextStyles.bodySBold,
                        color = if (isSelected) accentColor else primaryTextColor
                    )

                    if (isSelected) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_check,
                                contentDescription = "Selected"
                            ),
                            tint = accentColor.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

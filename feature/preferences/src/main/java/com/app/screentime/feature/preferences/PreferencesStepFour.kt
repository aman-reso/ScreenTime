package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.togglechip.ODSToggleChip
import com.telekom.odsystem.atoms.togglechip.ODSToggleChipProps
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
 * Language Preferences Step using Telekom ODS ODSToggleChip components.
 * 100% Telekom ODS compliant.
 */
@Composable
fun PreferencesStepFour(
    selectedLanguages: List<String>,
    scheme: ODSTheme,
    onToggleLanguage: (String) -> Unit,
    modifier: Modifier = Modifier,
    allLanguages: List<String> = DEFAULT_PREFERENCES_LANGUAGES
) {
    val primaryTextColor = scheme.basicText
    val secondaryTextColor = scheme.basicTextRecessive

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
                style = ODSTextStyles.bodyL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Select up to $MAX_LANGUAGES_SELECTION languages to show on your profile (${selectedLanguages.size}/$MAX_LANGUAGES_SELECTION).",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. ODSToggleChip Language Wrap ─────────────────────────────────
        ODSWrap(
            modifier = Modifier.fillMaxWidth(),
            horizontalGap = 8.dp,
            verticalGap = 8.dp,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            allLanguages.forEach { language ->
                val isSelected = selectedLanguages.contains(language)
                ODSToggleChip(
                    props = ODSToggleChipProps(
                        label = language,
                        selected = isSelected
                    ),
                    onToggle = {
                        if (isSelected) {
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
    }
}

package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step 9: Bio / About Me Preference.
 * Matches unified Figma design system layout:
 * - "Write a short bio"
 * - "Introduce yourself in a few words to stand out to potential matches."
 * - Multiline text box with character counter.
 */
@Composable
fun PreferencesStepBio(
    bio: String,
    onBioChange: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val primaryTextColor = scheme.basicTextDominant
    val secondaryTextColor = scheme.basicTextRecessive
    val accentColor = scheme.basicAccent
    val borderColor = if (error != null) scheme.functionalDestructiveStandard else accentColor

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
                text = "Write a short bio",
                style = ODSTextStyles.titleL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Introduce yourself in a few words to stand out to potential matches.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Bio Input Box ───────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp
        ) {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                padding = ODSPadding(all = 16.dp),
                cornerRadius = ODSCorners(all = 16.dp),
                border = ODSBorder(
                    width = 2.dp,
                    colorList = listOf(ODSColorModel(hexColor = borderColor))
                ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (bio.isEmpty()) {
                        ODSText(
                            text = "Love coffee, weekend hikes & discovering indie music...",
                            style = ODSTextStyles.bodyMRegular,
                            color = secondaryTextColor
                        )
                    }

                    BasicTextField(
                        value = bio,
                        onValueChange = { if (it.length <= 250) onBioChange(it) },
                        textStyle = ODSTextStyles.bodyMRegular.toTextStyle().copy(
                            color = primaryTextColor.getColor()
                        ),
                        cursorBrush = SolidColor(accentColor.getColor()),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (error != null) {
                    ODSText(
                        text = error,
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.functionalDestructiveStandard
                    )
                } else {
                    ODSText(
                        text = "Be authentic & kind",
                        style = ODSTextStyles.microcopyRegular,
                        color = secondaryTextColor
                    )
                }

                ODSText(
                    text = "${bio.length}/250",
                    style = ODSTextStyles.microcopyRegular,
                    color = secondaryTextColor
                )
            }
        }
    }
}

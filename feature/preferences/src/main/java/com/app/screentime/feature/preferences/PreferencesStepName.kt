package com.app.screentime.feature.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step 1: Name Preference.
 * Matches Figma node-id 23-6:
 * - "What's your name?"
 * - "We'll show this on your profile. You can't change this later."
 * - Rounded input card with 16.dp corners, 2.dp border #A7344D.
 */
@Composable
fun PreferencesStepName(
    name: String,
    onNameChange: (String) -> Unit,
    onSubmit: () -> Unit,
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
                text = "What's your name?",
                style = ODSTextStyles.titleL,
                color = primaryTextColor
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "We'll show this on your profile. You can't change this later.",
                style = ODSTextStyles.bodyMRegular,
                color = secondaryTextColor
            )
        }

        // ── 2. Name Input Card ─────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 6.dp
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(all = 18.dp),
                cornerRadius = ODSCorners(all = 16.dp),
                border = ODSBorder(
                    width = 2.dp,
                    colorList = listOf(ODSColorModel(hexColor = borderColor))
                ),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (name.isEmpty()) {
                        ODSText(
                            text = "Enter your first name",
                            style = ODSTextStyles.bodyL,
                            color = secondaryTextColor
                        )
                    }
                    BasicTextField(
                        value = name,
                        onValueChange = onNameChange,
                        textStyle = ODSTextStyles.bodyLBold.toTextStyle().copy(
                            color = primaryTextColor.getColor()
                        ),
                        cursorBrush = SolidColor(accentColor.getColor()),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (error != null) {
                ODSText(
                    text = error,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.functionalDestructiveStandard
                )
            }
        }
    }
}

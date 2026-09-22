package com.app.screentime.feature.chat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Chat Input Bar Component (Matching media_1789923373115.png & Figma 10-1469).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Compact button sizing.
 * 4. Maximum text size 14sp for input text.
 * 5. Padding and dimensions using `ODSVariables`.
 */
@Composable
fun ChatInputBar(
    inputText: String,
    scheme: ODSTheme = zonaODSTheme,
    onInputTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    onAttachClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = ODSVariables.spacingLayout1,
                vertical = ODSVariables.spacingComponent4
            ),
        gap = ODSVariables.spacingComponent3,
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        // ── 1. Plus Button (Rounded card) ────────────────────────────────────
        ODSBox(
            modifier = Modifier
                .size(44.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAttachClick
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_plus,
                    contentDescription = "Attach"
                ),
                tint = scheme.basicText.getColor(),
                modifier = Modifier.size(ODSVariables.sizingComponent8)
            )
        }

        // ── 2. Input Pill Container with Smiley Emoji ─────────────────────────
        ODSRow(
            modifier = Modifier.weight(1f),
            gap = ODSVariables.spacingComponent3,
            padding = ODSPadding(
                top = ODSVariables.spacingComponent4,
                bottom = ODSVariables.spacingComponent4,
                left = ODSVariables.spacingLayout1,
                right = ODSVariables.spacingLayout1
            ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        ) {
            // Input TextField
            ODSBox(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (inputText.isEmpty()) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Type something...",
                        style = ODSTextStyles.bodySBold, // 14sp
                        color = scheme.basicTextRecessive
                    )
                }

                BasicTextField(
                    value = inputText,
                    onValueChange = onInputTextChanged,
                    textStyle = TextStyle(
                        color = scheme.basicText.getColor(),
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Smiley Icon 🙂
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_smile,
                    contentDescription = "Emoji"
                ),
                tint = scheme.basicTextRecessive.getColor(),
                modifier = Modifier.size(ODSVariables.sizingComponent8)
            )
        }

        // ── 3. Magenta Send Button ───────────────────────────────────────────
        ODSBox(
            modifier = Modifier
                .size(44.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onSendMessage
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_send,
                    contentDescription = "Send"
                ),
                tint = scheme.basicTextOnAccent.getColor(),
                modifier = Modifier.size(ODSVariables.sizingComponent8)
            )
        }
    }
}

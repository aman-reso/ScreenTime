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
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.util.Locale

/**
 * Chat Input Bar Component with Text, Photo, Emoji and Voice Audio recording support.
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Compact button sizing.
 * 4. Maximum text size 16sp.
 * 5. Padding and dimensions using `ODSVariables`.
 */
@Composable
fun ChatInputBar(
    inputText: String,
    scheme: ODSTheme = zonaODSTheme,
    onInputTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    isEmojiPickerVisible: Boolean = false,
    isLimitReached: Boolean = false,
    onEmojiToggle: () -> Unit = {},
    onPickPhotoClick: () -> Unit = {},
    onInputClick: () -> Unit = {},
    isRecordingAudio: Boolean = false,
    recordingDurationSeconds: Int = 0,
    onStartRecordAudio: () -> Unit = {},
    onCancelRecordAudio: () -> Unit = {},
    onSendRecordedAudio: () -> Unit = {},
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
        if (isRecordingAudio) {
            // Active Voice Recording UI
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
                    colorList = listOf(ODSColorModel(hexColor = HexColor("#FF4D4F", 0.5f)))
                ),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                background = listOf(ODSColorModel(hexColor = HexColor("#FFF1F0", 1.0f)))
            ) {
                // Pulsing red recording dot
                ODSBox(
                    modifier = Modifier.size(10.dp),
                    cornerRadius = ODSCorners(all = 5.dp),
                    background = listOf(ODSColorModel(hexColor = HexColor("#FF4D4F", 1.0f)))
                )

                val mins = recordingDurationSeconds / 60
                val secs = recordingDurationSeconds % 60
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", mins, secs)

                ODSText(
                    text = formattedTime,
                    style = ODSTextStyles.bodySBold,
                    color = HexColor("#FF4D4F", 1.0f)
                )

                ODSText(
                    modifier = Modifier.weight(1f),
                    text = "Recording voice note...",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )

                // Cancel recording button (cross)
                ODSBox(
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent8)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onCancelRecordAudio
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_close,
                            contentDescription = "Cancel recording"
                        ),
                        tint = HexColor("#FF4D4F", 1.0f).getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent8)
                    )
                }
            }

            // Send voice note button
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSendRecordedAudio
                    ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_send,
                        contentDescription = "Send voice note"
                    ),
                    tint = scheme.basicTextOnAccent.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent8)
                )
            }
        } else {
            // Standard Text / Media input bar
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
                background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            ) {
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onInputClick
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (inputText.isEmpty()) {
                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = if (isLimitReached) "Limit reached" else "Type something...",
                            style = ODSTextStyles.bodySBold, // 14sp
                            color = scheme.basicTextRecessive
                        )
                    }

                    BasicTextField(
                        value = inputText,
                        onValueChange = onInputTextChanged,
                        enabled = !isLimitReached,
                        textStyle = TextStyle(
                            color = if (isLimitReached) scheme.basicTextRecessive.getColor() else scheme.basicText.getColor(),
                            fontSize = 14.sp
                        ),
                        cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                }

                ODSBox(
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent8)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { if (!isLimitReached) onPickPhotoClick() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_camera,
                            contentDescription = "Photo"
                        ),
                        tint = if (isLimitReached) scheme.basicStroke.getColor() else scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent8)
                    )
                }

                ODSBox(
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent8)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { if (!isLimitReached) onEmojiToggle() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_smile,
                            contentDescription = "Emoji"
                        ),
                        tint = if (isLimitReached) scheme.basicStroke.getColor() else if (isEmojiPickerVisible) scheme.basicAccent.getColor() else scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent8)
                    )
                }
            }

            // Action Button: Send if inputText has content, otherwise Mic to record audio
            val isSendMode = inputText.trim().isNotEmpty()
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            if (!isLimitReached) {
                                if (isSendMode) {
                                    onSendMessage()
                                } else {
                                    onStartRecordAudio()
                                }
                            }
                        }
                    ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                background = listOf(ODSColorModel(hexColor = if (isLimitReached) scheme.basicStroke else scheme.basicAccent)),
                contentAlignment = Alignment.Center
            ) {
                if (isSendMode) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_send,
                            contentDescription = "Send"
                        ),
                        tint = if (isLimitReached) scheme.basicTextRecessive.getColor() else scheme.basicTextOnAccent.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent8)
                    )
                } else {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_mic,
                            contentDescription = "Record Audio"
                        ),
                        tint = if (isLimitReached) scheme.basicTextRecessive.getColor() else scheme.basicTextOnAccent.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent8)
                    )
                }
            }
        }
    }
}

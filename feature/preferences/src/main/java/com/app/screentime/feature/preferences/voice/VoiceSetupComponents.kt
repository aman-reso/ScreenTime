package com.app.screentime.feature.preferences.voice

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
internal fun Modifier.odsClick(onClick: () -> Unit): Modifier {
    val source = remember { MutableInteractionSource() }
    return clickable(interactionSource = source, indication = null, onClick = onClick)
}

@Composable
fun VoiceSetupIconButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.odsClick(onClick),
        padding = ODSPadding(all = ODSVariables.spacingComponent4),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        border = ODSBorder(
            width = ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        contentAlignment = Alignment.Center
    ) {
        ODSIcon(
            iconModel = ODSIconModel(drawableRes = iconRes, contentDescription = contentDescription),
            tint = scheme.basicTextDominant.getColor(),
            width = ODSVariables.sizingComponent9,
            height = ODSVariables.sizingComponent9
        )
    }
}

@Composable
fun VoiceSetupSoftChip(
    label: String,
    onClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.odsClick(onClick),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingComponent4,
            vertical = ODSVariables.spacingComponent3
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ODSText(
            text = label,
            style = ODSTextStyles.microcopyBold,
            color = scheme.basicAccent
        )
    }
}

@Composable
fun VoiceSetupTopBar(
    title: String,
    keyboardLabel: String,
    onBack: () -> Unit,
    onKeyboardClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        padding = ODSPadding(horizontal = ODSVariables.spacingLayout1),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        VoiceSetupIconButton(
            iconRes = R.drawable.ic_arrow_left,
            contentDescription = "Back",
            onClick = onBack,
            scheme = scheme
        )
        ODSText(
            text = title,
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        VoiceSetupSoftChip(
            label = keyboardLabel,
            onClick = onKeyboardClick,
            scheme = scheme
        )
    }
}

@Composable
fun VoiceSetupProgress(
    currentQuestion: Int,
    totalQuestions: Int,
    topic: String,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val progress = (currentQuestion.toFloat() / totalQuestions.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent3,
        padding = ODSPadding(horizontal = ODSVariables.spacingLayout1)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = "QUESTION $currentQuestion OF $totalQuestions",
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
            ODSText(
                text = topic,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicAccent
            )
        }
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            height = ODSVariables.radiusExtraSmall,
            background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            clipContent = true
        ) {
            ODSBox(
                modifier = Modifier.fillMaxWidth(fraction = progress),
                height = ODSVariables.radiusExtraSmall,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull)
            )
        }
    }
}

@Composable
fun VoiceAssistantHeader(
    name: String,
    isActive: Boolean,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent4,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        ODSBox(
            width = ODSVariables.sizingComponent12,
            height = ODSVariables.sizingComponent12,
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_zap,
                    contentDescription = name
                ),
                tint = scheme.basicTextOnAccent.getColor(),
                width = ODSVariables.sizingComponent7,
                height = ODSVariables.sizingComponent7
            )
        }
        ODSText(
            text = name,
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextDominant
        )
        if (isActive) {
            ODSRow(
                padding = ODSPadding(
                    horizontal = ODSVariables.spacingComponent2,
                    vertical = ODSVariables.spacingComponent1
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusExtraSmall),
                background = listOf(ODSColorModel(hexColor = scheme.functionalInformationalSubtle))
            ) {
                ODSText(
                    text = "ACTIVE",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.functionalInformationalStandard
                )
            }
        }
    }
}

@Composable
fun VoicePromptCard(
    prompt: String,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        padding = ODSPadding(all = ODSVariables.spacingLayout1),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
        ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = prompt,
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
    }
}

@Composable
fun VoiceHearingCard(
    transcript: String,
    placeholder: String,
    isKeyboardMode: Boolean,
    isListening: Boolean,
    scheme: ODSTheme,
    onTranscriptChange: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val caretPulse = rememberInfiniteTransition(label = "voiceCaret")
    val caretOpacity by caretPulse.animateFloat(
        initialValue = 1f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "caretOpacity"
    )
    val displayText = transcript.ifBlank { placeholder }
    val hint = when {
        isKeyboardMode -> "Type your answer, then tap Done."
        isListening -> "Listening… say \"Change\" to replace this."
        else -> "Say \"Change\" or tap keyboard to edit."
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent3,
        padding = ODSPadding(all = ODSVariables.spacingLayout1),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
    ) {
        ODSText(
            text = "WHAT WE ARE HEARING:",
            style = ODSTextStyles.microcopyBold,
            color = scheme.basicAccent
        )
        if (isKeyboardMode) {
            ODSTextField(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSTextFieldProps(
                    label = "Answer",
                    inputText = transcript,
                    size = ODSTextFieldSize.SMALL,
                    placeholderText = placeholder
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onKeyboardDone() }),
                onValueChange = onTranscriptChange
            )
        } else {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent2,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = "\"$displayText\"",
                    style = if (transcript.isBlank()) ODSTextStyles.bodyMRegular else ODSTextStyles.bodyMRegular,
                    color = if (transcript.isBlank()) scheme.basicTextRecessive else scheme.basicTextDominant
                )
                ODSBox(
                    width = ODSVariables.strokes2,
                    height = ODSVariables.sizingComponent8,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    opacity = if (isListening || transcript.isNotBlank()) caretOpacity else 0f
                )
            }
        }
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = hint,
            style = ODSTextStyles.microcopyRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun VoiceCapturedRow(
    label: String,
    value: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .odsClick(onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSRow(
            gap = ODSVariables.spacingComponent4,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSBox(
                width = ODSVariables.sizingComponent10,
                height = ODSVariables.sizingComponent10,
                cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = iconRes, contentDescription = label),
                    tint = scheme.basicAccent.getColor(),
                    width = ODSVariables.sizingComponent6,
                    height = ODSVariables.sizingComponent6
                )
            }
            ODSText(
                text = label,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
        }
        ODSText(
            text = value,
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextDominant
        )
    }
}

@Composable
fun VoiceCapturedList(
    items: List<Triple<String, String, Int>>,
    onItemClick: (Int) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent4
    ) {
        ODSText(
            text = "ALREADY CAPTURED",
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextRecessive
        )
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3,
            padding = ODSPadding(all = ODSVariables.spacingComponent4),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        ) {
            items.forEachIndexed { index, (label, value, icon) ->
                if (index > 0) {
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        height = ODSVariables.strokes1,
                        background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    )
                }
                VoiceCapturedRow(
                    label = label,
                    value = value,
                    iconRes = icon,
                    onClick = { onItemClick(index) },
                    scheme = scheme
                )
            }
        }
    }
}

@Composable
fun VoicePrivacyNote(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.ic_shield_check,
                contentDescription = "Private"
            ),
            tint = scheme.basicTextRecessive.getColor(),
            width = ODSVariables.sizingComponent6,
            height = ODSVariables.sizingComponent6
        )
        ODSText(
            text = "Voice processing is secure and kept private on-device.",
            style = ODSTextStyles.microcopyRegular,
            color = scheme.basicTextRecessive,
            textAlign = TextAlign.Center
        )
    }
}

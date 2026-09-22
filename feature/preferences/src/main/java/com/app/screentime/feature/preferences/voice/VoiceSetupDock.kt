package com.app.screentime.feature.preferences.voice

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun VoiceWaveform(
    isListening: Boolean,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val pulse = rememberInfiniteTransition(label = "voiceWave")
    val scales = List(6) { index ->
        pulse.animateFloat(
            initialValue = 0.35f,
            targetValue = if (isListening) 1f else 0.45f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 420 + (index * 70)),
                repeatMode = RepeatMode.Reverse
            ),
            label = "wave$index"
        )
    }

    ODSRow(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2,
        height = ODSVariables.sizingComponent10,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        scales.forEach { scaleState ->
            val scale by scaleState
            ODSBox(
                width = ODSVariables.strokes2,
                height = ODSVariables.sizingComponent8 * scale,
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
            )
        }
    }
}

@Composable
fun VoiceDockCircleButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.odsClick(onClick),
        padding = ODSPadding(all = ODSVariables.spacingComponent4),
        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
        border = ODSBorder(
            width = ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        contentAlignment = Alignment.Center
    ) {
        ODSIcon(
            iconModel = ODSIconModel(drawableRes = iconRes, contentDescription = contentDescription),
            tint = scheme.basicTextDominant.getColor(),
            width = ODSVariables.sizingComponent8,
            height = ODSVariables.sizingComponent8
        )
    }
}

@Composable
fun VoiceMicButton(
    isListening: Boolean,
    onClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.odsClick(onClick),
        padding = ODSPadding(all = ODSVariables.spacingComponent3),
        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
        background = listOf(ODSColorModel(hexColor = scheme.basicAccent.copy(alpha = 0.13f)))
    ) {
        ODSBox(
            padding = ODSPadding(all = ODSVariables.spacingLayout1),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
            contentAlignment = Alignment.Center,
            effect = ODSEffect(
                elevations = listOf(
                    ODSElevation(
                        x = 0,
                        y = 8,
                        blur = 16,
                        spread = 0,
                        color = HexColor(scheme.basicAccent.getHexColor(), 0.25f),
                        type = ODSElevationType.DROP_SHADOW
                    )
                )
            )
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = if (isListening) R.drawable.ic_mic_off else R.drawable.ic_mic,
                    contentDescription = if (isListening) "Stop listening" else "Start listening"
                ),
                tint = scheme.basicTextOnAccent.getColor(),
                width = ODSVariables.sizingComponent11,
                height = ODSVariables.sizingComponent11
            )
        }
    }
}

@Composable
fun VoiceSetupDock(
    isListening: Boolean,
    onPrevious: () -> Unit,
    onMicClick: () -> Unit,
    onConfirm: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingLayout1,
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout1,
            top = ODSVariables.spacingLayout1,
            bottom = ODSVariables.spacingComponent3
        ),
        verticalAlignment = Alignment.CenterVertically,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VoiceWaveform(isListening = isListening, scheme = scheme)
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent7,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VoiceDockCircleButton(
                iconRes = R.drawable.ic_chevron_left,
                contentDescription = "Previous question",
                onClick = onPrevious,
                scheme = scheme
            )
            VoiceMicButton(
                isListening = isListening,
                onClick = onMicClick,
                scheme = scheme
            )
            VoiceDockCircleButton(
                iconRes = R.drawable.ic_check,
                contentDescription = "Confirm answer",
                onClick = onConfirm,
                scheme = scheme
            )
        }
        VoicePrivacyNote(scheme = scheme)
    }
}

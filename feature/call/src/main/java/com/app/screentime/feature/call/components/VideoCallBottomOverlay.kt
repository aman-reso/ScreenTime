package com.app.screentime.feature.call.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.CallUiState
import com.telekom.odsystem.R as ODSR
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
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * VideoCallBottomOverlay
 *
 * Glassmorphic floating bottom overlay dock housing all call action controls.
 * Floating over the full-screen video feed with navigationBarsPadding.
 *
 * Action Buttons Included:
 * 1. Mute / Unmute Microphone
 * 2. Toggle Camera On / Off
 * 3. Flip Camera (Front / Rear)
 * 4. Toggle Speakerphone Output
 * 5. End / Hangup Call (High-contrast red circular action button)
 *
 * @param callState Current state of the active call
 * @param modelName Name of the remote participant
 * @param scheme Telekom ODS Theme tokens
 * @param onToggleMute Toggles microphone mute state
 * @param onToggleCamera Toggles video camera stream on/off
 * @param onFlipCamera Flips camera between front and rear lenses
 * @param onToggleSpeaker Toggles audio between speakerphone and earpiece
 * @param onEndCall Terminates the active call
 */
@Composable
fun VideoCallBottomOverlay(
    callState: CallUiState,
    modelName: String,
    scheme: ODSTheme,
    onToggleMute: () -> Unit,
    onToggleCamera: () -> Unit,
    onFlipCamera: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        padding = ODSPadding(16.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 12.dp
        ) {
            if (callState.status == CallStatus.DIALING) {
                ODSColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = 4.dp
                ) {
                    ODSText(
                        text = "Calling ${modelName.ifBlank { "User" }}...",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = "Connecting secure video stream",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicAccent
                    )
                }
            }

            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VideoCallActionButton(
                    iconRes = if (callState.isMuted) ODSR.drawable.ic_mic_off else ODSR.drawable.ic_mic,
                    isActive = callState.isMuted,
                    activeBackgroundColor = scheme.basicAccent,
                    activeIconTint = scheme.basicTextOnAccent.getColor(),
                    scheme = scheme,
                    contentDescription = if (callState.isMuted) "Unmute Microphone" else "Mute Microphone",
                    onClick = onToggleMute
                )

                VideoCallActionButton(
                    iconRes = ODSR.drawable.video,
                    isActive = callState.isCameraOn,
                    activeBackgroundColor = scheme.basicAccent,
                    activeIconTint = scheme.basicTextOnAccent.getColor(),
                    scheme = scheme,
                    contentDescription = if (callState.isCameraOn) "Turn Camera Off" else "Turn Camera On",
                    onClick = onToggleCamera
                )

                VideoCallActionButton(
                    iconRes = ODSR.drawable.ic_volume_2,
                    isActive = callState.isSpeaker,
                    activeBackgroundColor = scheme.basicAccent,
                    activeIconTint = scheme.basicTextOnAccent.getColor(),
                    scheme = scheme,
                    contentDescription = if (callState.isSpeaker) "Switch to Earpiece" else "Switch to Speaker",
                    onClick = onToggleSpeaker
                )

                ODSBox(
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { onEndCall() },
                    cornerRadius = ODSCorners(all = 32.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_phone_off),
                        tint = scheme.basicTextOnAccent.getColor(),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

/**
 * Reusable Circular Action Button for the Video Call Bottom Dock.
 */
@Composable
private fun VideoCallActionButton(
    iconRes: Int,
    isActive: Boolean,
    scheme: ODSTheme,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeBackgroundColor: HexColor = scheme.basicAccent,
    activeIconTint: androidx.compose.ui.graphics.Color = scheme.basicTextOnAccent.getColor()
) {
    ODSBox(
        modifier = modifier
            .size(52.dp)
            .clickable { onClick() },
        cornerRadius = ODSCorners(all = 26.dp),
        background = listOf(
            ODSColorModel(
                hexColor = if (isActive) activeBackgroundColor else scheme.basicBackgroundSubtle
            )
        ),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        ),
        contentAlignment = Alignment.Center
    ) {
        ODSIcon(
            iconModel = ODSIconModel(drawableRes = iconRes),
            tint = if (isActive) activeIconTint else scheme.basicText.getColor(),
            modifier = Modifier.size(22.dp)
        )
    }
}

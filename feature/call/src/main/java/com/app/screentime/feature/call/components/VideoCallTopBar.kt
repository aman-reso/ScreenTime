package com.app.screentime.feature.call.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.CallUiState
import com.telekom.odsystem.R as ODSR
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
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * VideoCallTopBar
 *
 * Modern floating top bar overlay for active video calls.
 * Displays:
 * 1. Status Pill (Left): Remote partner's name, pulsing green active dot, and call timer or dialing status.
 * 2. Camera Flip Shortcut (Right): Quick one-tap camera front/back switch.
 *
 * @param callState Current state of the active call
 * @param modelName Name of the remote participant
 * @param isRemoteVideoActive Whether the remote peer's video is connected and active
 * @param scheme Telekom ODS Theme tokens
 * @param onFlipCamera Action triggered when camera flip shortcut is tapped
 */
@Composable
fun VideoCallTopBar(
    callState: CallUiState,
    modelName: String,
    isRemoteVideoActive: Boolean,
    scheme: ODSTheme,
    onFlipCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = callState.durationSec / 60
    val seconds = callState.durationSec % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ODSRow(
            cornerRadius = ODSCorners(all = 20.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            gap = 8.dp
        ) {
            ODSBox(
                modifier = Modifier.size(8.dp),
                cornerRadius = ODSCorners(all = 4.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
            )

            val pillText = when {
                callState.status == CallStatus.DIALING -> "${modelName.ifBlank { "User" }} • Ringing..."
                isRemoteVideoActive -> "${modelName.ifBlank { "User" }} • $formattedTime"
                else -> "${modelName.ifBlank { "User" }} • $formattedTime"
            }

            ODSText(
                text = pillText,
                style = ODSTextStyles.bodySBold,
                color = scheme.basicText
            )
        }

        // ── Top-Right Camera Flip Quick Action ──
        ODSBox(
            modifier = Modifier
                .size(42.dp)
                .clickable { onFlipCamera() },
            cornerRadius = ODSCorners(all = 21.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_refresh_cw),
                tint = scheme.basicText.getColor(),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

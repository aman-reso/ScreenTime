package com.app.screentime.feature.call.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.feature.call.CallUiState
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * VideoCallEndedScreen
 *
 * Summary screen presented when a video call terminates.
 * Displays call duration, total coins charged, reason for termination, and a Close button.
 *
 * @param callState Current state of the active call
 * @param scheme Telekom ODS Theme tokens
 * @param onClose Action triggered when the user closes the summary screen
 */
@Composable
fun VideoCallEndedScreen(
    callState: CallUiState,
    scheme: ODSTheme,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
        contentAlignment = Alignment.Center
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 20.dp
        ) {
            ODSBox(
                modifier = Modifier.size(80.dp),
                cornerRadius = ODSCorners(all = 40.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_phone_off),
                    tint = HexColor("#FF4D4F", 1.0f).getColor(),
                    modifier = Modifier.size(36.dp)
                )
            }

            ODSText(
                text = stringResource(R.string.call_video_ended),
                style = ODSTextStyles.bodyL,
                color = scheme.basicText
            )

            val durStr = "%02d:%02d".format(callState.durationSec / 60, callState.durationSec % 60)
            ODSText(
                text = callState.endReason ?: stringResource(R.string.call_duration_format, durStr),
                style = ODSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive
            )

            if (callState.cost > 0) {
                ODSText(
                    text = stringResource(R.string.call_total_charged, callState.cost),
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicAccent
                )
            }

            Spacer(Modifier.height(16.dp))

            ODSButton(
                modifier = Modifier.fillMaxWidth(0.6f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = stringResource(R.string.call_action_close),
                    variant = ODSButtonVariant.PRIMARY
                ),
                onClick = onClose
            )
        }
    }
}

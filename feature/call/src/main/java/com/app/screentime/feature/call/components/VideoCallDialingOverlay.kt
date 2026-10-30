package com.app.screentime.feature.call.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.feature.call.CallStatus
import com.app.screentime.feature.call.CallUiState
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * VideoCallDialingOverlay
 *
 * Centered overlay displayed while waiting for the remote party to answer the video call.
 * Shows remote user's avatar in an accented circular border, remote user's name,
 * and a status pill indicating "Calling..." or "Ringing...".
 *
 * @param callState Current state of the active call
 * @param modelName Name of the remote participant
 * @param avatarUrl Avatar URL of the remote participant
 * @param scheme Telekom ODS Theme tokens
 */
@Composable
fun VideoCallDialingOverlay(
    callState: CallUiState,
    modelName: String,
    avatarUrl: String,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = 16.dp
    ) {
        if (avatarUrl.isNotBlank()) {
            ODSBox(
                modifier = Modifier.size(96.dp),
                cornerRadius = ODSCorners(all = 48.dp),
                border = ODSBorder(
                    width = 2.5.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                ),
                clipContent = true
            ) {
                ODSImage(
                    imageModel = ODSImageModel(url = avatarUrl, contentDescription = modelName),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        ODSText(
            text = modelName.ifBlank { "User" },
            style = ODSTextStyles.bodyL,
            color = scheme.basicText
        )

        ODSRow(
            cornerRadius = ODSCorners(all = 16.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            padding = ODSPadding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            gap = 8.dp
        ) {
            ODSBox(
                modifier = Modifier.size(8.dp),
                cornerRadius = ODSCorners(all = 4.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
            )
            ODSText(
                text = if (callState.status == CallStatus.DIALING) "Ringing..." else "Calling...",
                style = ODSTextStyles.bodySBold,
                color = scheme.basicText
            )
        }
    }
}

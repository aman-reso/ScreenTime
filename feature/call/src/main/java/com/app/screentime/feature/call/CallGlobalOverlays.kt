package com.app.screentime.feature.call

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.link.ODSLinkType
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Global incoming call inline notification overlay that appears at the top section of ANY screen when an incoming call arrives,
 * rendered using the design system's ODSInlineNotification component with INFORMATIVE mode.
 */
@Composable
fun IncomingCallGlobalOverlay(
    modifier: Modifier = Modifier,
    callState: CallUiState,
    isModel: Boolean,
    scheme: ODSTheme = neutralScheme,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
) {
    val callSubtitle = buildString {
        append(if (callState.callType == CallType.VIDEO) "Incoming Video Call..." else "Incoming Voice Call...")
        if (isModel && callState.ratePerMin > 0.0) {
            append(" • 🪙 ${callState.ratePerMin.toInt()}/m")
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 0.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        ODSInlineNotification(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(12.dp)),
            scheme = scheme,
            props = ODSInlineNotificationProps(
                mode = ODSInlineNotificationMode.INFORMATIVE,
                title = callState.remoteUserName.ifBlank { "Someone is calling you" },
                text = callSubtitle,
                showCloseButton = true,
                link1Props = ODSLinkProps(
                    label = "Accept",
                    type = ODSLinkType.PRIMARY
                ),
                link2Props = ODSLinkProps(
                    label = "Decline",
                    type = ODSLinkType.SECONDARY
                )
            ),
            onFirstLinkClicked = onAccept,
            onSecondLinkClicked = onDecline,
            onDismiss = onDecline
        )
    }
}

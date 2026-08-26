package com.app.screentime.core.ui.security

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Full-screen ODS Biometric Lock screen shown when Fingerprint Lock is enabled.
 */
@Composable
fun BiometricLockScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onUnlocked: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity

    fun triggerAuth() {
        activity?.let {
            BiometricAuthManager.authenticate(
                activity = it,
                title = "Unlock Evermore",
                subtitle = "Touch the fingerprint sensor to unlock",
                onSuccess = onUnlocked,
                onError = { /* wait for retry */ }
            )
        }
    }

    LaunchedEffect(Unit) {
        triggerAuth()
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
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
            // Lock Icon Badge
            ODSBox(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Lock),
                    tint = scheme.basicAccent.getColor()
                )
            }

            // Title
            ODSColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 6.dp
            ) {
                PompiereTitle(
                    text = "EVERM♥RE",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereDisplayL
                )
                ODSText(
                    text = "Evermore is Locked",
                    style = ODSTextStyles.bodyLBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = "Touch the fingerprint sensor to access your chats and connections.",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }

            Spacer(Modifier.height(12.dp))

            // Large Interactive Fingerprint Icon
            ODSBox(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .clickable { triggerAuth() },
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Fingerprint),
                    tint = scheme.basicTextOnAccent.getColor()
                )
            }

            Spacer(Modifier.height(12.dp))

            // Unlock Button
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Unlock with Fingerprint",
                    variant = ODSButtonVariant.PRIMARY,
                    size = ODSButtonSize.SMALL
                ),
                onClick = { triggerAuth() }
            )
        }
    }
}

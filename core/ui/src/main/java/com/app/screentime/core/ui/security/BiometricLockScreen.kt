package com.app.screentime.core.ui.security

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
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
 * Full-screen App Locked Screen (Matching media_1789926553637.png).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Max text size 16sp (bodyMBold).
 * 4. All spacing, padding, and radii backed by `ODSVariables`.
 */
@Composable
fun BiometricLockScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onUnlocked: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity

    fun triggerAuth(allowDeviceCredential: Boolean = false) {
        activity?.let {
            BiometricAuthManager.authenticate(
                activity = it,
                title = "App Locked",
                subtitle = "Confirm your identity to continue safely",
                allowDeviceCredential = allowDeviceCredential,
                onSuccess = onUnlocked,
                onError = { /* wait for user retry */ }
            )
        }
    }

    LaunchedEffect(Unit) {
        triggerAuth(allowDeviceCredential = false)
    }

    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = ODSVariables.spacingLayout2,
                    vertical = ODSVariables.spacingLayout1
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Top Header: "zona lock" tilted badge + "App Locked" title ──
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.Top
            ) {
                Spacer(modifier = Modifier.height(ODSVariables.spacingComponent6))

                ODSRow(
                    padding = ODSPadding(
                        top = ODSVariables.spacingComponent2,
                        bottom = ODSVariables.spacingComponent2,
                        left = ODSVariables.spacingComponent3,
                        right = ODSVariables.spacingComponent3
                    ),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    rotate = -3f
                ) {
                    ODSText(
                        text = "zona lock",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicBackground
                    )
                }

                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = "App Locked",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText,
                    textAlign = TextAlign.Center
                )
            }

            // ── Center Content: Pink Circle with User/Shield Icon + Explanation ──
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingLayout1,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Soft pink circle container (84x84dp)
                ODSBox(
                    modifier = Modifier.size(84.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.ic_shield_check),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(38.dp)
                    )
                }

                ODSText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    text = "Confirm your identity with Face ID or your fingerprint to continue safely. Your matches are private.",
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive,
                    textAlign = TextAlign.Center
                )
            }

            // ── Bottom Actions Dock: Unlock with Biometrics & Use Phone Passcode ──
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = ODSVariables.spacingComponent5),
                gap = ODSVariables.spacingComponent4,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Primary Action Button: "Unlock with Biometrics"
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { triggerAuth(allowDeviceCredential = false) }
                        ),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    height = 48.dp
                ) {
                    ODSText(
                        text = "Unlock with Biometrics",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicBackground
                    )
                }

                // Secondary Action Button: "Use Phone Passcode"
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { triggerAuth(allowDeviceCredential = true) }
                        ),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    height = 48.dp
                ) {
                    ODSText(
                        text = "Use Phone Passcode",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText
                    )
                }
            }
        }
    }
}

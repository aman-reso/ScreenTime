package com.app.screentime.profile.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.security.TOTP
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import androidx.compose.ui.res.stringResource
import com.app.screentime.R
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet

import com.app.screentime.utils.DateUtils
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.delay

private const val TIME_STEP_SECONDS = 60L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTOTPBottomSheetContent(
    onDismiss: () -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current

    var otp by remember { mutableStateOf("------") }
    var remainingSeconds by remember { mutableIntStateOf(60) }
    var progress by remember { mutableFloatStateOf(1f) }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "TOTP Countdown"
    )

    // Get TOTP secret from preferences
    val preferencesManager = remember { PreferencesManager(context) }
    val totpSecret = remember { preferencesManager.getTOTPSecret() }

    LaunchedEffect(Unit) {
        while (true) {
            val now = DateUtils.now()
            val epochSeconds = now.millis / 1000
            val remaining = (60 - (epochSeconds % 60)).toInt()
            remainingSeconds = remaining
            otp = TOTP.generateTOTP(totpSecret)

            for (i in remaining downTo 0) {
                remainingSeconds = i
                progress = i.toFloat() / 60
                delay(1000)
            }
        }
    }

    ODSBottomSheet(
        showBottomSheet = true,
        onDismissRequest = onDismiss,
        titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = stringResource(R.string.one_time_password),
                    style = DSTextStyles.titleM,
                    color = scheme.basicText
                )
                ODSText(
                    text = stringResource(R.string.otp_code_description),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent5
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(DSVariables.spacingComponent4)
                ) {
                    ODSBox(
                        modifier = Modifier.size(DSVariables.sizingComponent13), // 40.dp
                        contentAlignment = Alignment.Center
                    ) {
                        // Circular progress
                        CircularProgressIndicator(
                            progress = animatedProgress,
                            modifier = Modifier.matchParentSize(),
                            color = scheme.functionalSuccessStandard.getColor(),
                            strokeWidth = 3.dp,
                            trackColor = scheme.basicTextRecessive.getColor().copy(alpha = 0.3f)
                        )

                        ODSText(
                            text = stringResource(R.string.remaining_seconds, remainingSeconds),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    ODSColumn {
                        ODSText(
                            text = otp,
                            style = DSTextStyles.bodyL,
                            color = scheme.functionalSuccessStandard
                        )
                        ODSBox(height = DSVariables.spacingComponent1) {}
                        ODSText(
                            text = stringResource(R.string.otp_regenerate_message),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        },
        onCloseClicked = onDismiss
    )
}

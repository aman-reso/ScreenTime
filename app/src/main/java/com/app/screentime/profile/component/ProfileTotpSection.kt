package com.app.screentime.profile.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.R
import com.app.screentime.common.component.ODSCountdownTimer
import com.app.screentime.common.component.ODSCountdownTimerFormat
import com.app.screentime.common.component.ODSCountdownTimerProps
import com.app.screentime.common.component.ODSCountdownTimerStyle
import com.app.screentime.profile.viewmodel.ProfileViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

private const val TOTP_PERIOD_SECONDS = 60

@Composable
fun ProfileTotpSection(
    viewModel: ProfileViewModel,
    scheme: ODSTheme = neutralScheme,
) {
    val totpState by viewModel.totpState.collectAsState()

    ODSCardBasic(
        modifier = Modifier
            .fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSCountdownTimer(
                    modifier = Modifier.wrapContentHeight(),
                    scheme = scheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = TOTP_PERIOD_SECONDS,
                        format = ODSCountdownTimerFormat.SECONDS_ONLY,
                        style = ODSCountdownTimerStyle.CIRCULAR,
                        circularSize = DSVariables.sizingComponent13,
                        circularStrokeWidth = 3.dp,
                        progressColor = scheme.functionalSuccessStandard,
                        textColor = scheme.basicText
                    ),
                    isRunning = totpState.isRunning,
                    remainingSeconds = totpState.remainingSeconds
                )
                ODSColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = totpState.otp,
                        style = DSTextStyles.bodyMBold,
                        color = scheme.functionalSuccessStandard
                    )
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.otp_regenerate_message),
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        })
}
package com.app.screentime.feature.call.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * VideoCallInsufficientBalanceScreen
 *
 * Screen displayed when the user does not have sufficient wallet balance/coins
 * to start or continue a video call. Offers actions to Top-Up or Cancel.
 *
 * @param callState Current state of the active call
 * @param isCurrentUserModel Whether the current logged-in user is a model
 * @param modelName Name of the user being called
 * @param scheme Telekom ODS Theme tokens
 * @param onCancel Action when Cancel button is clicked
 * @param onRecharge Action when Recharge button is clicked
 */
@Composable
fun VideoCallInsufficientBalanceScreen(
    callState: CallUiState,
    isCurrentUserModel: Boolean,
    modelName: String,
    scheme: ODSTheme,
    onCancel: () -> Unit,
    onRecharge: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
        contentAlignment = Alignment.Center
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 16.dp
        ) {
            ODSBox(
                modifier = Modifier.size(88.dp),
                cornerRadius = ODSCorners(all = 44.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_zap),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(40.dp)
                )
            }

            ODSText(
                text = if (isCurrentUserModel) {
                    stringResource(R.string.call_caller_insufficient_balance)
                } else {
                    stringResource(R.string.call_insufficient_balance)
                },
                style = ODSTextStyles.bodyL,
                color = scheme.basicText
            )

            ODSText(
                text = if (isCurrentUserModel) {
                    callState.balanceMessage.ifBlank { stringResource(R.string.call_insufficient_balance_caller_msg) }
                } else {
                    callState.balanceMessage.ifBlank {
                        stringResource(
                            R.string.call_insufficient_balance_msg,
                            callState.minRequiredBalance.toInt(),
                            modelName,
                            callState.currentBalance.toInt()
                        )
                    }
                },
                style = ODSTextStyles.bodyMRegular,
                color = scheme.basicTextRecessive
            )

            Spacer(Modifier.height(16.dp))

            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSButton(
                    modifier = Modifier.weight(1f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.call_action_cancel),
                        variant = ODSButtonVariant.SECONDARY
                    ),
                    onClick = onCancel
                )

                ODSButton(
                    modifier = Modifier.weight(1f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.call_action_recharge),
                        variant = ODSButtonVariant.PRIMARY
                    ),
                    onClick = onRecharge
                )
            }
        }
    }
}

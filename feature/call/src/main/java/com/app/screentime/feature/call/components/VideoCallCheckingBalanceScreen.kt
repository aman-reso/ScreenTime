package com.app.screentime.feature.call.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * VideoCallCheckingBalanceScreen
 *
 * Full-screen loading indicator displayed while verifying caller's coin balance
 * before connecting the video call.
 *
 * @param modelName Name of the user being called
 * @param ratePerMin Coin cost per minute for this call
 * @param scheme Telekom ODS Theme tokens
 */
@Composable
fun VideoCallCheckingBalanceScreen(
    modelName: String,
    ratePerMin: Double,
    scheme: ODSTheme,
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
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 16.dp
        ) {
            ODSLoadingSpinner(
                scheme = scheme,
                props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
            )

            ODSText(
                text = "Checking Wallet Balance...",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicText
            )
        }
    }
}

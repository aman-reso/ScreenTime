package com.app.screentime.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.components.EvermoreTopBar
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthLandingScreen(
    scheme: ODSTheme,
    onGetStartedClick: () -> Unit,
    onGuestClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            EvermoreTopBar(
                title = "CONNECT",
                scheme = scheme,
                onMenuClick = onGetStartedClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 14.dp
            ) {
                ODSText(
                    text = "Real Conversations • Zero Barriers",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextRecessive
                )

                PompiereTitle(
                    text = "Connect with top creators.\nVoice & video anytime.",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereDisplay,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Get Started",
                            variant = ODSButtonVariant.PRIMARY,
                        ),
                        onClick = onGetStartedClick
                    )

                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Explore as Guest",
                            variant = ODSButtonVariant.SECONDARY,
                        ),
                        onClick = onGuestClick
                    )
                }
            }
        }
    }
}

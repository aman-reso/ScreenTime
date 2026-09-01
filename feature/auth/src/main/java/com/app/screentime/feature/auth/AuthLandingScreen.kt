package com.app.screentime.feature.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.components.EvermoreTopBar
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun AuthLandingScreen(
    scheme: ODSTheme,
    onContinueAsUser: () -> Unit,
    onContinueAsModel: () -> Unit,
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
                onMenuClick = onContinueAsUser
            )

            Spacer(modifier = Modifier.height(28.dp))

            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 16.dp
            ) {
                // Cyber Lime Pill Tag (4th color)
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = HexColor(0xffd7ff81))),
                    cornerRadius = ODSCorners(all = 20.dp),
                    padding = ODSPadding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    ODSText(
                        text = "✦ Real Conversations • Zero Barriers",
                        style = ODSTextStyles.microcopyBold,
                        color = HexColor(0xff371f7d)
                    )
                }

                ODSText(
                    text = "Connect with top creators.\nVoice & video anytime.",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText,
                    textAlign = TextAlign.Center
                )

                // Feature Highlights Card with ODS (Background #2b1764, Icons #ff4365)
                ODSBox(
                    modifier = Modifier.fillMaxWidth(),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                    ),
                    cornerRadius = ODSCorners(all = 20.dp),
                    padding = ODSPadding(all = 16.dp)
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FeatureBadge(
                            icon = Icons.Filled.PhoneInTalk,
                            label = "1-on-1 Voice",
                            scheme = scheme,
                            badgeColor = HexColor(0xffff4365) // 2nd color: Icon color
                        )
                        FeatureBadge(
                            icon = Icons.Filled.Videocam,
                            label = "Live Video",
                            scheme = scheme,
                            badgeColor = HexColor(0xffff4365) // 2nd color: Icon color
                        )
                        FeatureBadge(
                            icon = Icons.Filled.VolunteerActivism,
                            label = "Private Tips",
                            scheme = scheme,
                            badgeColor = HexColor(0xffff4365) // 2nd color: Icon color
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Vertical Actions Container
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 12.dp
                ) {
                    // 1. Continue as User Button (Vertical)
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Continue as User",
                            buttonIcon = ODSIconModel(imageVector = Icons.Filled.Person),
                            leftIcon = true,
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onContinueAsUser
                    )

                    // 2. Continue as Model Button (Vertical)
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Continue as Model",
                            buttonIcon = ODSIconModel(imageVector = Icons.Filled.AutoAwesome),
                            leftIcon = true,
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onContinueAsModel
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 3. Guest Exploration Option
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onGuestClick),
                        contentAlignment = Alignment.Center,
                        padding = ODSPadding(vertical = 10.dp)
                    ) {
                        ODSText(
                            text = "Explore as Guest →",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    scheme: ODSTheme,
    badgeColor: HexColor
) {
    ODSColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        gap = 6.dp
    ) {
        ODSBox(
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
            cornerRadius = ODSCorners(all = 12.dp),
            padding = ODSPadding(all = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = badgeColor.getColor(),
                modifier = Modifier.size(22.dp)
            )
        }
        ODSText(
            text = label,
            style = ODSTextStyles.microcopyBold,
            color = scheme.basicText
        )
    }
}

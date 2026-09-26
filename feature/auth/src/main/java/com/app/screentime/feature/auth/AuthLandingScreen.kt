package com.app.screentime.feature.auth

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ZONA / Winter Onboarding & Sign-In Screen.
 * 100% constructed with Telekom ODS components.
 */
@Composable
fun AuthLandingScreen(
    scheme: ODSTheme = zonaODSTheme,
    uiState: AuthUiState = AuthUiState(),
    onGoogleSignInClick: () -> Unit,
    onPhoneSignInClick: () -> Unit = {},
    onRetryClick: () -> Unit = onGoogleSignInClick,
    onPhoneChange: (String) -> Unit = {},
    onOtpChange: (String) -> Unit = {},
    onVerifyAndLogin: () -> Unit = {},
    onGoogleLogin: () -> Unit = onGoogleSignInClick,
    onGuestClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = HexColor("#BCBCF0")))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Top Content Section ─────────────────────────────────────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 12.dp
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.login_scene,
                        contentDescription = "Winter Illustration"
                    ),
                    tint = HexColor.None.getColor(),
                    modifier = Modifier
                )

                // Headline & Subtitle
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 8.dp,
                    horizontalAlignment = Alignment.Start
                ) {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 4.dp,
                        horizontalAlignment = Alignment.Start
                    ) {
                        ODSText(
                            text = "Discover, Chat,",
                            style = ODSTextStyles.titleL,
                            color = HexColor("#11111A")
                        )
                        ODSRow(
                            gap = 8.dp,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.Start
                        ) {
                            ODSText(
                                text = "& Find Your",
                                style = ODSTextStyles.titleL,
                                color = HexColor("#11111A")
                            )

                            // Highlighted "Spark." Pill Badge
                            ODSBox(
                                padding = ODSPadding(horizontal = 16.dp, vertical = 4.dp),
                                cornerRadius = ODSCorners(all = 14.dp),
                                background = listOf(ODSColorModel(hexColor = HexColor("#11111A"))),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = "Spark.",
                                    style = ODSTextStyles.titleL,
                                    color = HexColor("#FFFFFF")
                                )
                            }
                        }
                    }

                    ODSText(
                        text = "Unveil your quirks, passions, and adventures. Let's match, chat, and explore.",
                        style = ODSTextStyles.bodyMBold,
                        color = HexColor("#4D4D66")
                    )
                }

                if (!uiState.error.isNullOrBlank()) {
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        background = listOf(ODSColorModel(hexColor = HexColor("#FFFFFF"))),
                        cornerRadius = ODSCorners(all = 14.dp),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = HexColor("#A7344D")))
                        ),
                        padding = ODSPadding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = 8.dp,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ODSText(
                                text = uiState.error,
                                style = ODSTextStyles.bodySRegular,
                                color = HexColor("#A7344D"),
                                textAlign = TextAlign.Center
                            )
                            ODSButton(
                                scheme = scheme,
                                props = ODSButtonProps(
                                    label = "Retry",
                                    variant = ODSButtonVariant.SECONDARY,
                                    size = ODSButtonSize.SMALL
                                ),
                                onClick = onRetryClick
                            )
                        }
                    }
                }
            }

            // ── Bottom Action Section (Always anchored at the bottom) ────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = 10.dp,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.Top
            ) {
                if (uiState.isLoading) {
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        height = 48.dp,
                        background = listOf(ODSColorModel(hexColor = HexColor("#A7344D"))),
                        cornerRadius = ODSCorners(all = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            gap = 10.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = com.telekom.odsystem.R.drawable.ic_refresh_cw,
                                    contentDescription = "Loading"
                                ),
                                tint = HexColor("#FFFFFF").getColor(),
                                modifier = Modifier.size(20.dp)
                            )
                            ODSText(
                                text = "Connecting...",
                                style = ODSTextStyles.bodyMBold,
                                color = HexColor("#FFFFFF")
                            )
                        }
                    }
                } else {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 8.dp,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 1. Magenta "Sign in with Google" Pill Button
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onGoogleSignInClick
                                ),
                            height = 48.dp,
                            background = listOf(ODSColorModel(hexColor = HexColor("#A7344D"))),
                            cornerRadius = ODSCorners(all = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSRow(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                gap = 10.dp
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_google_logo
                                    ),
                                    tint = HexColor.None.getColor(),
                                    modifier = Modifier.size(20.dp)
                                )
                                ODSText(
                                    text = "Sign in with Google",
                                    style = ODSTextStyles.bodyMBold,
                                    color = HexColor("#FFFFFF")
                                )
                            }
                        }

                        // 2. "Continue with Phone Number" Pill Button
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onPhoneSignInClick
                                ),
                            height = 48.dp,
                            background = listOf(ODSColorModel(hexColor = HexColor("#11111A"))),
                            cornerRadius = ODSCorners(all = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSRow(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                gap = 10.dp
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = com.telekom.odsystem.R.drawable.ic_phone
                                    ),
                                    tint = HexColor("#FFFFFF").getColor(),
                                    modifier = Modifier.size(18.dp)
                                )
                                ODSText(
                                    text = "Continue with Phone Number",
                                    style = ODSTextStyles.bodyMBold,
                                    color = HexColor("#FFFFFF")
                                )
                            }
                        }
                    }
                }

                // Security & Legal Terms Footnote
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 2.dp,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ODSText(
                        text = "By tapping Login, you agree to our Terms of Service.",
                        style = ODSTextStyles.bodySRegular,
                        color = HexColor("#4D4D66"),
                        textAlign = TextAlign.Center
                    )
                    ODSText(
                        text = "Learn how we process your data in our Privacy Policy.",
                        style = ODSTextStyles.bodySRegular,
                        color = HexColor("#4D4D66"),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

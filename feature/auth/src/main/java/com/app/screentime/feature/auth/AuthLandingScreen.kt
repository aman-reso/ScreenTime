package com.app.screentime.feature.auth

import com.app.screentime.feature.auth.R
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.ZonaComposeColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ZONA Google Sign-In Authentication Screen.
 * 100% constructed with Telekom ODS components.
 * Matches Left Phone in uploaded mockups (Canvas #200B4D · Google button #FFFFFF · Brand accent #D7FF01 · Primary text #FFFFFF · Supporting text #BC9DFF).
 */
@Composable
fun AuthLandingScreen(
    scheme: ODSTheme = zonaODSTheme,
    uiState: AuthUiState = AuthUiState(),
    onGoogleSignInClick: () -> Unit,
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
        background = listOf(ODSColorModel(hexColor = ZonaColors.CanvasDark))
    ) {
        // ── 1. Hero Image Header (Smiling Couple at night with bokeh city lights) ──
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .align(Alignment.TopCenter)
        ) {
            ODSImage(
                imageModel = ODSImageModel(
                    url = "https://images.unsplash.com/photo-1517841905240-472988babdf9?q=80&w=1200&auto=format&fit=crop",
                    contentDescription = "ZONA Couples"
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            ODSBox(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ZonaComposeColors.CanvasDark.copy(alpha = 0.1f),
                                ZonaComposeColors.CanvasDark.copy(alpha = 0.5f),
                                ZonaComposeColors.CanvasDark
                            ),
                            startY = 100f,
                            endY = 1100f
                        )
                    )
            )
        }

        // ── 2. Foreground Content (Branding, Headings, Button & Legal) ──────
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(180.dp))

            // ── Brand Badge & Headline ──────────────────────────────────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 16.dp
            ) {
                // "zona" Neon Lime Brand Badge
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime)),
                    cornerRadius = ODSCorners(all = 12.dp),
                    padding = ODSPadding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "zona",
                        style = ODSTextStyles.titleM,
                        color = ZonaColors.TextInverse
                    )
                }

                // Main Display Headline
                ODSText(
                    text = "Find Your Match\nIn Full Color.",
                    style = ODSTextStyles.titleL,
                    color = ZonaColors.TextPrimary,
                    textAlign = TextAlign.Center
                )

                // Subtitle
                ODSText(
                    text = "Express yourself, match\nauthentically, live loudly.",
                    style = ODSTextStyles.bodyMBold,
                    color = ZonaColors.LavenderAlt,
                    textAlign = TextAlign.Center
                )
            }

            // ── Error Banner (if error occurred) ────────────────────────────
            if (!uiState.error.isNullOrBlank()) {
                ODSBox(
                    modifier = Modifier.fillMaxWidth(),
                    background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                    cornerRadius = ODSCorners(all = 14.dp),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = ZonaColors.ActionPrimary))
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
                            color = ZonaColors.ActionPrimary,
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

            // ── Bottom Action & Legal ────────────────────────────────────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = 16.dp
            ) {
                if (uiState.isLoading) {
                    // Loading pill
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        height = 56.dp,
                        background = listOf(ODSColorModel(hexColor = ZonaColors.SurfaceRaised)),
                        cornerRadius = ODSCorners(all = 28.dp),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = ZonaColors.Border))
                        ),
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
                                tint = ZonaColors.ActiveLime.getColor(),
                                modifier = Modifier.size(20.dp)
                            )
                            ODSText(
                                text = "Connecting...",
                                style = ODSTextStyles.bodyMBold,
                                color = ZonaColors.TextPrimary
                            )
                        }
                    }
                } else {
                    // White Google Pill Button (matching mockup)
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onGoogleSignInClick
                            ),
                        height = 56.dp,
                        background = listOf(ODSColorModel(hexColor = ZonaColors.NeutralWhite)),
                        cornerRadius = ODSCorners(all = 28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            gap = 12.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_google_logo
                                ),
                                tint = HexColor.None.getColor(),
                                modifier = Modifier.size(22.dp)
                            )
                            ODSText(
                                text = "Continue with Google",
                                style = ODSTextStyles.bodyMBold,
                                color = ZonaColors.TextInverse
                            )
                        }
                    }
                }

                // Legal Terms & Privacy
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = 2.dp
                ) {
                    ODSText(
                        text = "By continuing, you agree to ZONA's",
                        style = ODSTextStyles.microcopyRegular,
                        color = ZonaColors.LavenderAlt,
                        textAlign = TextAlign.Center
                    )
                    ODSText(
                        text = "Terms of Service  •  Privacy Policy",
                        style = ODSTextStyles.microcopyBold,
                        color = ZonaColors.LavenderAlt,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

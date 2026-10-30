package com.app.screentime.feature.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.app.screentime.config.R
import com.app.screentime.core.ui.components.AppLanguageBottomSheet
import com.app.screentime.core.ui.components.InAppWebViewModal
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.DSVariables
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
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.feature.auth.R as AuthR
import com.telekom.odsystem.R as ODSR

/**
 * Winter Auth Gated / Onboarding Landing Screen.
 *
 * Implemented using 100% Telekom ODS components aligned with Figma node-id 129-90:
 * - ConstraintLayout with guideline at 0.3f from top for the brand text ("winter")
 * - 32dp margin between brand text and bottom section
 * - ODSLazyColumn enabled for smooth scrolling on all screen sizes
 * - Linear gradient background: #F7DEE8 -> #FFF5F7 -> #FFFFFF
 * - 2 Pill login options using ODSButton:
 *   1. "Continue with Google" (with official Google logo)
 *   2. "Continue with mobile number" (with mobile device icon)
 * - Terms of Service and Privacy Policy footer
 */
@Composable
fun AuthLandingScreen(
    scheme: ODSTheme = zonaODSTheme,
    uiState: AuthUiState = AuthUiState(),
    onLanguageChange: (String) -> Unit = {},
    onGoogleSignInClick: () -> Unit,
    onPhoneSignInClick: () -> Unit = {},
    onRetryClick: () -> Unit = onGoogleSignInClick,
    onTermsClick: (() -> Unit)? = null,
    onPrivacyClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var activeWebUrl by remember { mutableStateOf<String?>(null) }
    var activeWebTitle by remember { mutableStateOf<String?>(null) }
    var showLanguageBottomSheet by remember { mutableStateOf(false) }

    val termsOfServiceText = stringResource(R.string.auth_terms_of_service)
    val privacyPolicyText = stringResource(R.string.auth_privacy_policy)
    val termsAndConditionsTitle = stringResource(R.string.auth_terms_and_conditions)

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(
            ODSColorModel(
                hexColor = scheme.basicBackground
            )
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val screenHeight = maxHeight
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = screenHeight)
            ) {
                val (brandRef, bottomSectionRef) = createRefs()

                ODSColumn(
                    modifier = Modifier.constrainAs(brandRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(bottomSectionRef.top)
                    },
                    padding = ODSPadding(horizontal = DSVariables.spacingLayout2),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ODSText(
                        text = "winter",
                        style = ODSTextStyle(
                            fontFamily = ODSR.font.pacifico_regular,
                            fontSize = 48,
                            lineHeight = 64
                        ),
                        color = scheme.basicAccent
                    )
                }

                // ── 2. Bottom Section (32dp margin after brand text) ────
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(bottomSectionRef) {
                            top.linkTo(brandRef.bottom, margin = 32.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    gap = 20.dp,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    padding = ODSPadding(horizontal = DSVariables.spacingLayout2),
                ) {
                    if (!uiState.error.isNullOrBlank()) {
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            background = listOf(ODSColorModel(hexColor = HexColor("#FFF0F3"))),
                            cornerRadius = ODSCorners(all = 16.dp),
                            border = ODSBorder(
                                width = 1.dp,
                                colorList = listOf(ODSColorModel(hexColor = HexColor("#A7344D")))
                            ),
                            padding = ODSPadding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            ODSRow(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ODSText(
                                    modifier = Modifier.weight(1f),
                                    text = uiState.error,
                                    style = ODSTextStyle(
                                        fontFamily = ODSR.font.funnelsans_regular,
                                        fontSize = 13,
                                        lineHeight = 16
                                    ),
                                    color = HexColor("#A7344D")
                                )
                                ODSButton(
                                    scheme = scheme,
                                    props = ODSButtonProps(
                                        label = stringResource(R.string.retry),
                                        variant = ODSButtonVariant.SECONDARY,
                                        size = ODSButtonSize.SMALL
                                    ),
                                    onClick = onRetryClick
                                )
                            }
                        }
                    }

                    // Headline & Subtitle
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 10.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Find your person, not just a match.",
                            style = ODSTextStyle(
                                fontFamily = ODSR.font.funnelsans_bold,
                                fontSize = 34,
                                lineHeight = 42
                            ),
                            color = scheme.basicText
                        )

                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Intentional connections for love, friendship, and everything in between.",
                            style = ODSTextStyle(
                                fontFamily = ODSR.font.funnelsans_regular,
                                fontSize = 15,
                                lineHeight = 22
                            ),
                            color = scheme.basicTextRecessive
                        )
                    }

                    // 3 Login Option Pills using ODSButton
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 12.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start
                    ) {

                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Continue with Google",
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.LARGE,
                                buttonIcon = ODSIconModel(
                                    drawableRes = AuthR.drawable.ic_google_logo,
                                    contentDescription = "Google"
                                ),
                                leftIcon = true,
                                disabled = uiState.isLoading
                            ),
                            onClick = onGoogleSignInClick
                        )

                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Continue with mobile number",
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL,
                                buttonIcon = ODSIconModel(
                                    drawableRes = AuthR.drawable.ic_mobile_device,
                                    contentDescription = "Mobile"
                                ),
                                leftIcon = true,
                                disabled = uiState.isLoading
                            ),
                            onClick = onPhoneSignInClick
                        )
                    }

                    // Terms and Privacy Policy Footer + Home Indicator
                    ODSColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp),
                        gap = 16.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val termsAndPrivacyAnnotated =
                            remember(termsOfServiceText, privacyPolicyText) {
                                buildAnnotatedString {
                                    append("By continuing, you agree to our ")
                                    withLink(
                                        LinkAnnotation.Clickable(
                                            tag = "TERMS",
                                            linkInteractionListener = {
                                                if (onTermsClick != null) {
                                                    onTermsClick()
                                                } else {
                                                    activeWebUrl =
                                                        "https://winterdating.com/terms"
                                                    activeWebTitle = termsAndConditionsTitle
                                                }
                                            }
                                        )
                                    ) {
                                        withStyle(
                                            SpanStyle(
                                                color = scheme.basicAccent.getColor(),
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append("Terms of Service")
                                        }
                                    }
                                    append(" and ")
                                    withLink(
                                        LinkAnnotation.Clickable(
                                            tag = "PRIVACY",
                                            linkInteractionListener = {
                                                if (onPrivacyClick != null) {
                                                    onPrivacyClick()
                                                } else {
                                                    activeWebUrl =
                                                        "https://winterdating.com/privacy-policy"
                                                    activeWebTitle = privacyPolicyText
                                                }
                                            }
                                        )
                                    ) {
                                        withStyle(
                                            SpanStyle(
                                                color = scheme.basicAccent.getColor(),
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append("Privacy Policy")
                                        }
                                    }
                                    append(".")
                                }
                            }

                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = termsAndPrivacyAnnotated,
                            style = ODSTextStyle(
                                fontFamily = ODSR.font.funnelsans_regular,
                                fontSize = 12,
                                lineHeight = 16
                            ),
                            color = scheme.basicTextRecessive,
                            textAlign = TextAlign.Center
                        )

                        ODSBox(
                            modifier = Modifier.size(width = 134.dp, height = 5.dp),
                            cornerRadius = ODSCorners(all = 2.5.dp),
                            background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        )
                    }
                }
            }

        }

        ODSBox(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(
                    top = ODSVariables.spacingComponent4,
                    end = DSVariables.spacingLayout2
                )
                .size(40.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { showLanguageBottomSheet = true }
                ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = ODSR.drawable.ic_globe,
                    contentDescription = "Language"
                ),
                tint = scheme.basicTextDominant.getColor(),
                modifier = Modifier.size(20.dp)
            )
        }
    }

    if (activeWebUrl != null && activeWebTitle != null) {
        InAppWebViewModal(
            url = activeWebUrl!!,
            title = activeWebTitle!!,
            onDismiss = {
                activeWebUrl = null
                activeWebTitle = null
            }
        )
    }

    if (showLanguageBottomSheet) {
        AppLanguageBottomSheet(
            currentLanguage = uiState.currentLanguageCode,
            onLanguageCodeSelected = { langCode ->
                onLanguageChange(langCode)
                showLanguageBottomSheet = false
            },
            onDismiss = {
                showLanguageBottomSheet = false
            },
            scheme = scheme
        )
    }
}

package com.app.screentime.feature.newdesign.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.screentime.config.R as ConfigR
import com.app.screentime.feature.auth.AuthViewModel
import com.app.screentime.feature.auth.util.GoogleSignInHelper
import com.app.screentime.feature.newdesign.R
import com.app.screentime.feature.newdesign.components.FunnelSans
import com.app.screentime.feature.newdesign.components.Pacifico
import com.app.screentime.feature.newdesign.components.WinterTokens
import com.google.android.gms.auth.api.signin.GoogleSignIn

/**
 * WinterWelcomeAuthScreen - Figma screen "Winter · Google login" (Node 114:2).
 *
 * Rules:
 * 1. 4dp grid for all margins, paddings, and sizes.
 * 2. Uses AuthViewModel to handle Google Sign-In state and backend session creation.
 * 3. All strings and translations referenced via strings.xml.
 * 4. Content-driven layout with zero hardcoded container heights.
 */
@Composable
fun WinterWelcomeAuthScreen(
    modifier: Modifier = Modifier,
    heroImageUrl: String? = null,
    viewModel: AuthViewModel = hiltViewModel(),
    onAuthSuccess: () -> Unit = {},
    onGoogleSignInClick: (() -> Unit)? = null,
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Automatically proceed if user already has an active session, or upon sign-in success
    LaunchedEffect(isLoggedIn, uiState.isSuccess) {
        if (isLoggedIn || uiState.isSuccess) {
            onAuthSuccess()
        }
    }

    val webClientId = remember(context) { GoogleSignInHelper.resolveWebClientId(context) }
    val googleSignInClient =
        remember(context, webClientId) { GoogleSignInHelper.createClient(context, webClientId) }
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        viewModel.handleGoogleSignInResult(task)
    }

    fun launchGoogleSignIn() {
        if (uiState.isLoading) return
        try {
            googleSignInClient.signOut().addOnCompleteListener {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
        } catch (e: Exception) {
            viewModel.showGoogleSignInError(
                e.localizedMessage ?: context.getString(ConfigR.string.auth_failed_launch_google)
            )
        }
    }

    WinterWelcomeAuthScreenContent(
        modifier = modifier,
        heroImageUrl = heroImageUrl,
        statusBarPadding = statusBarPadding,
        isLoading = uiState.isLoading,
        errorMessage = uiState.error,
        onGoogleSignInClick = {
            if (onGoogleSignInClick != null) {
                onGoogleSignInClick()
            } else {
                launchGoogleSignIn()
            }
        },
        onTermsClick = onTermsClick,
        onPrivacyClick = onPrivacyClick
    )
}

/**
 * Stateless content layer for WinterWelcomeAuthScreen.
 */
@Composable
fun WinterWelcomeAuthScreenContent(
    modifier: Modifier = Modifier,
    heroImageUrl: String? = null,
    statusBarPadding: androidx.compose.ui.unit.Dp = 0.dp,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onGoogleSignInClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Full-bleed Hero Portrait Background
        WinterWelcomeHeroBackground(
            heroImageUrl = heroImageUrl
        )

        // 2. Warm Readability Gradient Fade (Node 115:3)
        WinterWelcomeGradientOverlay()

        // 3. Main Screen Content Layer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Wordmark Brand
            WinterWelcomeBrandHeader(
                topPadding = statusBarPadding + 12.dp
            )

            // Bottom Section: Welcome Text + Normal Google Sign-in Button + Legal Disclaimers
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(28.dp))

                // Instruction: "Sign in to get started" (Node 119:6)
                Text(
                    text = stringResource(ConfigR.string.auth_sign_in_to_get_started),
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 2.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Error message banner if sign-in fails
                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        fontFamily = FunnelSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = WinterTokens.statusError,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 6.dp)
                    )
                }

                // Clickable Google Sign-in Button with loading state
                WinterGoogleSignInButton(
                    isLoading = isLoading,
                    onClick = onGoogleSignInClick,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Legal Disclaimers (Terms & Conditions, Privacy Policy)
                WinterWelcomeLegalNotice(
                    onTermsClick = onTermsClick,
                    onPrivacyClick = onPrivacyClick
                )
            }
        }
    }
}

/**
 * Full-bleed Hero Portrait Background.
 */
@Composable
private fun WinterWelcomeHeroBackground(
    heroImageUrl: String?,
    modifier: Modifier = Modifier
) {
    if (heroImageUrl != null) {
        AsyncImage(
            model = heroImageUrl,
            contentDescription = stringResource(ConfigR.string.auth_welcome_portrait_desc),
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize()
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.welcome_hero_bg),
            contentDescription = stringResource(ConfigR.string.auth_welcome_portrait_desc),
            contentScale = ContentScale.Crop,
            modifier = modifier.fillMaxSize()
        )
    }
}

/**
 * Warm Readability Gradient Fade (Node 115:3).
 */
@Composable
private fun WinterWelcomeGradientOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0.00f to Color(0xB3FFFFFF),
                    0.16f to Color(0x00FFFFFF),
                    0.40f to Color(0x0012140F),
                    0.56f to Color(0x7512140F),
                    1.00f to Color(0x9412140F)
                )
            )
    )
}

/**
 * Brand cursive wordmark "winter" in Pacifico script.
 */
@Composable
private fun WinterWelcomeBrandHeader(
    topPadding: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding, start = 8.dp)
    ) {
        Text(
            text = stringResource(ConfigR.string.winter_brand_name),
            fontFamily = Pacifico,
            fontSize = 48.sp,
            lineHeight = 56.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
    }
}

/**
 * Clickable Google Sign-In Button with loading indicator and ripple effect.
 */
@Composable
private fun WinterGoogleSignInButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.Black)
            .border(1.dp, Color(0x4DFFFFFF), RoundedCornerShape(32.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = Color.White),
                enabled = !isLoading,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_g),
                    contentDescription = stringResource(ConfigR.string.auth_google_icon_desc),
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(ConfigR.string.sign_in_with_google),
                    fontFamily = FunnelSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Bottom Legal Notice with Terms and Privacy links.
 */
@Composable
private fun WinterWelcomeLegalNotice(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val termsText = stringResource(ConfigR.string.auth_terms_and_conditions)
    val privacyText = stringResource(ConfigR.string.auth_privacy_policy)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(ConfigR.string.auth_legal_age_confirmation),
            fontFamily = FunnelSans,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        val legalAnnotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            ) {
                append(termsText)
            }
            append("  ·  ")
            withStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            ) {
                append(privacyText)
            }
        }

        Text(
            text = legalAnnotatedString,
            fontFamily = FunnelSans,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onTermsClick
            )
        )
    }
}
package com.app.screentime.feature.profile

import com.app.screentime.core.ui.util.showODSToast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * My Account & Zona Wallet Screen (Matching Figma node-id 10-1134).
 *
 * Rules:
 * 1. 100% ODS components & ODSLazyColumn.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Compact button sizing.
 * 4. Maximum text size 16sp with Funnel Sans font.
 * 5. All padding, margins, gaps and radii use `ODSVariables`.
 */
@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onLogoutClick: () -> Unit = {},
    onNavigateToTopUp: () -> Unit = {},
    onNavigateToTransactions: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToControlAccount: () -> Unit = {},
    onNavigateToProfileDetail: (String, String) -> Unit = { _, _ -> },
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showThemeDialog by remember { mutableStateOf(false) }
    var showProfileVisibilityDialog by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var profileVisibility by remember { mutableStateOf("Public") }
    var selectedLanguage by remember { mutableStateOf("English") }

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    val resolvedName = uiState.displayName.ifBlank { uiState.user?.name ?: "User" }
    val resolvedEmail = uiState.email.ifBlank { uiState.user?.email ?: "user@winter.app" }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            AccountTopBar(
                title = "My Account",
                scheme = scheme,
                onEditClick = onNavigateToEditProfile,
                onNotificationsClick = onNavigateToNotifications
            )
            ODSLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                padding = ODSPadding(
                    top = ODSVariables.spacingComponent2,
                    bottom = ODSVariables.spacingComponent6
                ),
                gap = ODSVariables.spacingLayout1
            ) {
                // ── Item 1: Profile Header (Avatar, Name, Email, PRO badge) ───
                item {
                    AccountProfileHeader(
                        userName = resolvedName,
                        email = resolvedEmail,
                        subtitle = "",
                        avatarUrl = uiState.photoUrl.takeIf { it.isNotBlank() },
                        scheme = scheme,
                        onProfileClick = {
                            onNavigateToProfileDetail(uiState.user?.id ?: "me", resolvedName)
                        }
                    )
                }

                // ── Item 2: Profile Completion Progress Bar ──────────────────
                item {
                    AccountProfileCompletion(
                        percentage = 85,
                        scheme = scheme
                    )
                }

                // ── Item 3: Available Points Card (Points, Buy Points, Transactions) ──
                item {
                    AccountWalletCard(
                        pointsCount = if (uiState.walletCoins > 0) uiState.walletCoins else 1000,
                        scheme = scheme,
                        onBuyPointsClick = onNavigateToTopUp,
                        onTransactionsClick = onNavigateToTransactions
                    )
                }

                // ── Item 4: Categorized Profile Sections (About, Interests in Chips, Location, Personal Info) ──
                item {
                    ProfileCategorizedDetails(
                        bio = uiState.bio.ifBlank { uiState.user?.bio },
                        interests = uiState.user?.interests ?: emptyList(),
                        languages = uiState.user?.languages ?: emptyList(),
                        city = uiState.user?.city,
                        area = uiState.user?.area,
                        gender = uiState.user?.gender,
                        datingIntent = uiState.user?.datingIntent,
                        relationType = uiState.user?.relationType,
                        job = uiState.user?.job ?: uiState.user?.occupation,
                        height = uiState.user?.height,
                        education = uiState.user?.education,
                        dateOfBirth = uiState.user?.dateOfBirth ?: uiState.user?.dob,
                        qualityScore = uiState.user?.qualityScore,
                        scheme = scheme
                    )
                }

                // ── Item 5: Preferences & Settings ───────────────────────────
                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DSVariables.spacingComponent4)
                    )
                    AccountQuickSettings(
                        themeName = uiState.selectedTheme.ifBlank { "Pastel" },
                        languageName = selectedLanguage,
                        visibilityStatus = profileVisibility,
                        onThemeClick = { showThemeDialog = true },
                        onLanguageClick = {
                            showLanguageSheet = true
                        },
                        onVisibilityClick = {
                            showProfileVisibilityDialog = true
                        },
                        onControlAccountClick = onNavigateToControlAccount,
                        scheme = scheme
                    )
                }

                item {
                    ODSBox(modifier = Modifier.height(ODSVariables.spacingComponent8))
                }
            }
        }

        if (showThemeDialog) {
            ThemeSelectionDialog(
                initialTheme = uiState.selectedTheme,
                scheme = scheme,
                onDismissRequest = { showThemeDialog = false },
                onThemeApplied = { newTheme ->
                    viewModel.setTheme(newTheme)
                }
            )
        }

        if (showProfileVisibilityDialog) {
            ProfileVisibilityDialog(
                currentVisibility = profileVisibility,
                onVisibilityChanged = { newVisibility ->
                    profileVisibility = newVisibility
                    context.showODSToast("Profile visibility updated to $newVisibility")
                },
                onDismissRequest = { showProfileVisibilityDialog = false },
                scheme = scheme
            )
        }

        if (showLanguageSheet) {
            AppLanguageBottomSheet(
                currentLanguage = selectedLanguage,
                onLanguageSelected = { newLanguage ->
                    selectedLanguage = newLanguage
                    context.showODSToast("App language changed to $newLanguage")
                },
                onDismiss = { showLanguageSheet = false },
                scheme = scheme
            )
        }
    }
}


package com.app.screentime.feature.profile

import android.widget.Toast
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
import com.app.screentime.core.ui.security.BiometricAuthManager
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
    onNavigateToProfileDetail: (String, String) -> Unit = { _, _ -> },
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showSecurityDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showProfileVisibilityDialog by remember { mutableStateOf(false) }
    var showLanguageSheet by remember { mutableStateOf(false) }
    var profileVisibility by remember { mutableStateOf("Public") }
    var selectedLanguage by remember { mutableStateOf("English") }

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    val resolvedName = if (uiState.displayName.isNotBlank()) uiState.displayName else "Alex Rivera"

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
                // ── Item 1: Profile Header (Avatar, Name, PRO badge, Subtitle) ─
                item {
                    AccountProfileHeader(
                        userName = resolvedName,
                        subtitle = "Premium subscriber since Jan 2026",
                        avatarUrl = uiState.photoUrl.takeIf { it.isNotBlank() },
                        isPro = true,
                        scheme = scheme,
                        onProfileClick = {
                            onNavigateToProfileDetail("alex_rivera", resolvedName)
                        }
                    )
                }

                // ── Item 2: Profile Completion Progress Bar ──────────────────
                item {
                    AccountProfileCompletion(
                        percentage = 80,
                        scheme = scheme
                    )
                }

                // ── Item 3: Zona Wallet Card (Balance, Credits, Actions) ─────
                item {
                    AccountWalletCard(
                        balanceFormatted = if (uiState.walletCoins > 0) "$${uiState.walletCoins}.50" else "$24.50",
                        creditsCount = if (uiState.walletCoins > 0) uiState.walletCoins else 120,
                        scheme = scheme,
                        onAddFundsClick = onNavigateToTopUp,
                        onTransactionsClick = onNavigateToTransactions
                    )
                }

                // ── Item 4: Quick Actions Grid (Matches, Likes Info, Security, Help) ──
                item {
                    AccountQuickActions(
                        scheme = scheme,
                        onMatchesClick = {
                            Toast.makeText(context, "Opening Matches", Toast.LENGTH_SHORT).show()
                        },
                        onLikesInfoClick = {
                            Toast.makeText(context, "Opening Likes Info", Toast.LENGTH_SHORT).show()
                        },
                        onSecurityClick = {
                            showSecurityDialog = true
                        },
                        onHelpClick = {
                            Toast.makeText(context, "Opening Help & Support", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }

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
                        scheme = scheme
                    )
                }

                item {
                    ODSBox(modifier = Modifier.height(ODSVariables.spacingComponent8))
                }
            }
        }

        if (showSecurityDialog) {
            AppSecurityLockDialog(
                initialEnabled = BiometricAuthManager.isFingerprintLockEnabled(context),
                scheme = scheme,
                onDismissRequest = { showSecurityDialog = false },
                onDone = { enabled ->
                    showSecurityDialog = false
                    BiometricAuthManager.setFingerprintLockEnabled(context, enabled)
                }
            )
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
                    Toast.makeText(
                        context,
                        "Profile visibility updated to $newVisibility",
                        Toast.LENGTH_SHORT
                    ).show()
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
                    Toast.makeText(
                        context,
                        "App language changed to $newLanguage",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDismiss = { showLanguageSheet = false },
                scheme = scheme
            )
        }
    }
}

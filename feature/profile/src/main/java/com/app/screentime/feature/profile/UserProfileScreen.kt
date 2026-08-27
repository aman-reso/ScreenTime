package com.app.screentime.feature.profile

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.feature.wallet.WalletPacksBottomSheet
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.*
import androidx.fragment.app.FragmentActivity
import com.app.screentime.core.ui.security.BiometricAuthManager
import com.app.screentime.core.ui.security.BiometricStatus
import com.app.screentime.feature.wallet.WalletPacksBottomSheet

import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    onLogoutClick: () -> Unit = {},
    onNavigateToTopUp: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showWalletPacksSheet by remember { mutableStateOf(false) }
    var isSettingsOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Main Profile Screen Content
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        ) {
            // 1. Top Bar
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PompiereTitle(
                    text = "Profile",
                    scheme = scheme,
                    style = ODSTextStyles.pompiereTitleXL
                )
                IconButton(onClick = { isSettingsOpen = true }) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Outlined.Settings),
                        tint = scheme.basicText.getColor()
                    )
                }
            }

            // 2. Profile User Info Card (Avatar, Name, Email, Bio & Edit Profile Button)
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = 16.dp),
                padding = ODSPadding(all = 20.dp)
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 14.dp
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 14.dp,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Avatar
                            ODSBox(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape),
                                background = listOf(ODSColorModel(hexColor = cheddarSecondaryScheme.basicBackgroundSubtle)),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSText(
                                    text = uiState.displayName.firstOrNull()?.uppercase() ?: "U",
                                    style = ODSTextStyles.pompiereDisplayL,
                                    color = scheme.basicText
                                )
                            }

                            // Name & Details
                            ODSColumn(gap = 2.dp) {
                                ODSText(
                                    text = uiState.displayName,
                                    style = ODSTextStyles.bodyLBold,
                                    color = scheme.basicText
                                )
                                ODSText(
                                    text = "+91 98765 43210",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }

                        // Edit Profile Button in Card Header
                        ODSButton(
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Edit",
                                variant = ODSButtonVariant.OUTLINE,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { showEditProfileDialog = true }
                        )
                    }

                    ODSDivider(
                        scheme = scheme,
                        props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
                    )

                    // Email & Bio info rows
                    ODSColumn(gap = 8.dp) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 8.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(imageVector = Icons.Outlined.Email),
                                tint = scheme.basicAccent.getColor()
                            )
                            ODSText(
                                text = uiState.email,
                                style = ODSTextStyles.microcopyMedium,
                                color = scheme.basicText
                            )
                        }

                        if (uiState.bio.isNotBlank()) {
                            ODSRow(
                                verticalAlignment = Alignment.Top,
                                gap = 8.dp
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Info),
                                    tint = scheme.basicTextRecessive.getColor()
                                )
                                ODSText(
                                    text = uiState.bio,
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // 3. Separate Distinct Colored Wallet Card with "+ Add Money" button
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { showWalletPacksSheet = true },
                background = listOf(ODSColorModel(hexColor = orchidSecondaryScheme.basicBackgroundSubtle)),
                cornerRadius = ODSCorners(all = 16.dp),
                padding = ODSPadding(all = 18.dp)
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSColumn(gap = 6.dp) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 8.dp
                        ) {
                            ODSBox(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = Icons.Outlined.AccountBalanceWallet),
                                    tint = scheme.basicText.getColor()
                                )
                            }
                            ODSText(
                                text = "Wallet Balance",
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicText
                            )
                        }

                        ODSRow(
                            verticalAlignment = Alignment.Bottom,
                            gap = 6.dp
                        ) {
                            ODSText(
                                text = "✦ ${uiState.walletCoins}",
                                style = ODSTextStyles.pompiereTitleL,
                                color = scheme.basicText
                            )
                            ODSText(
                                text = "coins",
                                style = ODSTextStyles.microcopyRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }

                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "+ Add Money",
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = { showWalletPacksSheet = true }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // 4. Profile Stats
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                gap = 12.dp
            ) {
                ProfileStatCard(
                    value = "12",
                    label = "Calls Made",
                    scheme = scheme,
                    modifier = Modifier.weight(1f)
                )
                ProfileStatCard(
                    value = "48m",
                    label = "Talk Time",
                    scheme = scheme,
                    modifier = Modifier.weight(1f)
                )
                ProfileStatCard(
                    value = "${uiState.favoritesCount}",
                    label = "Favorites",
                    scheme = scheme,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(18.dp))

            // 5. Menu Card
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = 16.dp)
            ) {
                ODSColumn(modifier = Modifier.fillMaxWidth()) {
                    ProfileMenuItem(
                        icon = Icons.Outlined.Settings,
                        label = "Settings & Preferences",
                        scheme = scheme,
                        onClick = { isSettingsOpen = true }
                    )
                    ProfileMenuItem(
                        icon = Icons.Outlined.Security,
                        label = "Privacy & Security",
                        scheme = scheme,
                        onClick = { isSettingsOpen = true }
                    )
                    ProfileMenuItem(
                        icon = Icons.Outlined.Description,
                        label = "Terms of Service",
                        scheme = scheme,
                        onClick = {
                            Toast.makeText(
                                context,
                                "Evermore Terms & Conditions v9.3",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    ProfileMenuItem(
                        icon = Icons.AutoMirrored.Outlined.HelpOutline,
                        label = "Help & Support",
                        scheme = scheme,
                        onClick = {
                            Toast.makeText(
                                context,
                                "Support: support@chattyevermore.com",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    ProfileMenuItem(
                        icon = Icons.Outlined.Logout,
                        label = "Log Out",
                        scheme = scheme,
                        onClick = {
                            viewModel.logout()
                            onLogoutClick()
                        }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // App Version
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = 16.dp),
                padding = ODSPadding(all = 14.dp)
            ) {
                ODSText(
                    text = "Chatty · Version 9.3 (Evermore Edition)",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }

            Spacer(Modifier.height(32.dp))
        }

        // ── 6. Settings Right Panel (50% screen width) ──────────────────────
        if (isSettingsOpen) {
            // Scrim overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable { isSettingsOpen = false }
            )

            // Right Slide Panel
            AnimatedVisibility(
                visible = isSettingsOpen,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                SettingsRightPanel(
                    scheme = scheme,
                    uiState = uiState,
                    onClose = { isSettingsOpen = false },
                    onThemeSelected = { viewModel.setTheme(it) },
                    onLanguageSelected = { viewModel.setLanguage(it) },
                    onToggleFingerprint = { enabled ->
                        val activity = context as? FragmentActivity
                        if (activity != null) {
                            when (val status = BiometricAuthManager.checkBiometricStatus(context)) {
                                is BiometricStatus.Available -> {
                                    BiometricAuthManager.authenticate(
                                        activity = activity,
                                        title = if (enabled) "Enable Fingerprint Lock" else "Disable Fingerprint Lock",
                                        subtitle = "Touch the fingerprint sensor to confirm",
                                        onSuccess = {
                                            viewModel.toggleFingerprintLock(enabled)
                                            val msg =
                                                if (enabled) "Fingerprint lock enabled" else "Fingerprint lock disabled"
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        },
                                        onError = { err ->
                                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                                is BiometricStatus.NoneEnrolled -> {
                                    Toast.makeText(
                                        context,
                                        "No fingerprint enrolled in device settings. Please enroll a fingerprint in Android Settings.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                is BiometricStatus.Unavailable -> {
                                    Toast.makeText(context, status.reason, Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            viewModel.toggleFingerprintLock(enabled)
                            val msg =
                                if (enabled) "Fingerprint lock enabled" else "Fingerprint lock disabled"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }

        // ── 7. Full Edit Profile Dialog (ODS Styled) ────────────────────────
        if (showEditProfileDialog) {
            EditProfileDialog(
                currentName = uiState.displayName,
                currentEmail = uiState.email,
                currentBio = uiState.bio,
                scheme = scheme,
                onDismiss = { showEditProfileDialog = false },
                onSave = { name, email, bio ->
                    viewModel.updateProfile(name = name, email = email, bio = bio)
                    showEditProfileDialog = false
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }

        // ── 8. Wallet Packs Interactive Grid BottomSheet ────────────────────
        if (showWalletPacksSheet) {
            WalletPacksBottomSheet(
                onDismissRequest = { showWalletPacksSheet = false },
                scheme = scheme,
                onRechargeSuccess = {
                    viewModel.loadUser()
                    Toast.makeText(context, "Wallet recharged successfully! 🪙", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

/**
 * Slide-in Settings Right Panel (taking ~50% of the screen width).
 */
@Composable
private fun SettingsRightPanel(
    scheme: ODSTheme,
    uiState: ProfileUiState,
    onClose: () -> Unit,
    onThemeSelected: (String) -> Unit,
    onLanguageSelected: (String) -> Unit,
    onToggleFingerprint: (Boolean) -> Unit
) {
    val themes =
        listOf("Dark (Onyx)", "Velvet Orchid", "Amber Sunset", "Oceanic Breeze", "Light Mode")
    val languages = listOf("English", "हिन्दी (Hindi)", "Español", "Français", "Deutsch")

    ODSColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.55f)
            .background(scheme.basicBackgroundCard.getColor())
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        gap = 16.dp
    ) {
        // Header
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PompiereTitle(
                text = "Settings",
                scheme = scheme,
                style = ODSTextStyles.pompiereTitleM
            )
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(32.dp)
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Default.Close),
                    tint = scheme.basicText.getColor()
                )
            }
        }

        ODSDivider(scheme = scheme, props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL))

        // 1. Theme Section
        ODSColumn(gap = 8.dp) {
            ODSRow(verticalAlignment = Alignment.CenterVertically, gap = 6.dp) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.ColorLens),
                    tint = scheme.basicAccent.getColor()
                )
                ODSText(
                    text = "App Theme",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicText
                )
            }

            themes.forEach { themeName ->
                val isSelected = uiState.selectedTheme == themeName
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) scheme.basicAccent.getColor().copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                        .clickable { onThemeSelected(themeName) }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = themeName,
                        style = if (isSelected) ODSTextStyles.microcopyBold else ODSTextStyles.microcopyRegular,
                        color = if (isSelected) scheme.basicAccent else scheme.basicText
                    )
                    if (isSelected) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Default.Check),
                            tint = scheme.basicAccent.getColor()
                        )
                    }
                }
            }
        }

        ODSDivider(scheme = scheme, props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL))

        // 2. Language Section
        ODSColumn(gap = 8.dp) {
            ODSRow(verticalAlignment = Alignment.CenterVertically, gap = 6.dp) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Language),
                    tint = scheme.basicAccent.getColor()
                )
                ODSText(
                    text = "App Language",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicText
                )
            }

            languages.forEach { lang ->
                val isSelected = uiState.selectedLanguage == lang
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) scheme.basicAccent.getColor().copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                        .clickable { onLanguageSelected(lang) }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = lang,
                        style = if (isSelected) ODSTextStyles.microcopyBold else ODSTextStyles.microcopyRegular,
                        color = if (isSelected) scheme.basicAccent else scheme.basicText
                    )
                    if (isSelected) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Default.Check),
                            tint = scheme.basicAccent.getColor()
                        )
                    }
                }
            }
        }

        ODSDivider(scheme = scheme, props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL))

        // 3. Android Fingerprint / Biometric Lock with ODSSwitch
        ODSColumn(gap = 8.dp) {
            ODSRow(verticalAlignment = Alignment.CenterVertically, gap = 6.dp) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Outlined.Fingerprint),
                    tint = scheme.basicAccent.getColor()
                )
                ODSText(
                    text = "Security",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicText
                )
            }

            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        cheddarSecondaryScheme.basicBackgroundSubtle.getColor().copy(alpha = 0.15f)
                    )
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSColumn(modifier = Modifier.weight(1f), gap = 2.dp) {
                    ODSText(
                        text = "Fingerprint Lock",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = "Protect app on open",
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                ODSSwitch(
                    scheme = scheme,
                    props = ODSSwitchProps(selected = uiState.isFingerprintLockEnabled),
                    onCheckedChange = { onToggleFingerprint(it) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSButtonProps(
                label = "Done",
                variant = ODSButtonVariant.PRIMARY,
                size = ODSButtonSize.SMALL
            ),
            onClick = onClose
        )
    }
}

/**
 * Full ODS-Styled Edit Profile Dialog (Name, Email Address, and Bio).
 */
@Composable
private fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    currentBio: String,
    scheme: ODSTheme,
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, bio: String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var bio by remember { mutableStateOf(currentBio) }

    Dialog(onDismissRequest = onDismiss) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 16.dp),
            padding = ODSPadding(all = 20.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = 14.dp
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PompiereTitle(
                        text = "Edit Profile",
                        scheme = scheme,
                        style = ODSTextStyles.pompiereTitleM
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Default.Close),
                            tint = scheme.basicTextRecessive.getColor()
                        )
                    }
                }

                // 1. Name Field (ODSTextField)
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        size = ODSTextFieldSize.SMALL,
                        label = "Display Name",
                        placeholderText = "Enter your display name",
                        inputText = name,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.Person)
                    ),
                    onValueChange = { name = it }
                )

                // 2. Email Address Field (ODSTextField)
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = "Email Address",
                        placeholderText = "e.g. yourname@example.com",
                        inputText = email,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.Email)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = { email = it }
                )

                // 3. Bio Field (ODSTextField)
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = "About / Bio",
                        placeholderText = "Tell others about yourself",
                        inputText = bio,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.EditNote)
                    ),
                    onValueChange = { bio = it }
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    gap = 8.dp
                ) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Cancel",
                            variant = ODSButtonVariant.GHOST,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onDismiss
                    )
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Save Changes",
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(name.trim(), email.trim(), bio.trim())
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStatCard(
    value: String,
    label: String,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = 16.dp)
    ) {
        ODSColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
            gap = 4.dp
        ) {
            ODSText(
                text = value,
                style = ODSTextStyles.pompiereTitleM,
                color = scheme.basicText
            )
            ODSText(
                text = label,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        gap = 16.dp
    ) {
        ODSIcon(
            iconModel = ODSIconModel(imageVector = icon),
            tint = scheme.basicText.getColor()
        )
        ODSText(
            text = label,
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicText,
            modifier = Modifier.weight(1f)
        )
        ODSIcon(
            iconModel = ODSIconModel(imageVector = Icons.Filled.ChevronRight),
            tint = scheme.basicTextRecessive.getColor()
        )
    }
}

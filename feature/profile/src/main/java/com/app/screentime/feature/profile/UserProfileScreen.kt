package com.app.screentime.feature.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.security.BiometricAuthManager
import com.app.screentime.core.ui.security.BiometricStatus
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

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
    var showLanguageSheet by remember { mutableStateOf(false) }
    var showModelVerificationSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Box(modifier = modifier.fillMaxSize()) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        ) {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = "Profile",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                IconButton(onClick = { showLanguageSheet = true }) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Outlined.Settings),
                        tint = scheme.basicText.getColor()
                    )
                }
            }

            // 2. User Info Header (Avatar, Name, Bio, User/Model Tag, Edit Button)
            UserProfileHeader(
                displayName = uiState.displayName,
                bio = uiState.bio,
                role = uiState.role,
                scheme = scheme,
                onEditClick = { showEditProfileDialog = true }
            )

            // 3. Yellow Creator Verification Card (When logged in as Model & details pending)
            if (uiState.role == com.app.screentime.core.model.UserRole.MODEL && !uiState.isModelDetailsVerified) {
                Spacer(Modifier.height(12.dp))
                ModelVerificationPendingCard(
                    scheme = scheme,
                    onClickComplete = { showModelVerificationSheet = true }
                )
            }

            Spacer(Modifier.height(14.dp))

            // 4. Stats Row (Calls Made, Talk Time, Favorites)
            ProfileStatsRow(
                callsMade = "12",
                talkTime = "48m",
                favoritesCount = uiState.favoritesCount,
                scheme = scheme
            )

            Spacer(Modifier.height(12.dp))

            BiometricSecurityCard(
                isEnabled = uiState.isFingerprintLockEnabled,
                scheme = scheme,
                onToggle = { enabled ->
                    val activity = context as? FragmentActivity
                    if (activity != null) {
                        when (val status = BiometricAuthManager.checkBiometricStatus(context)) {
                            is BiometricStatus.Available -> {
                                BiometricAuthManager.authenticate(
                                    activity = activity,
                                    title = if (enabled) "Enable Biometric Lock" else "Disable Biometric Lock",
                                    subtitle = "Authenticate to confirm biometric security",
                                    onSuccess = {
                                        viewModel.toggleFingerprintLock(enabled)
                                        Toast.makeText(
                                            context,
                                            if (enabled) "Biometric lock enabled" else "Biometric lock disabled",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = { err ->
                                        Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }

                            is BiometricStatus.NoneEnrolled -> {
                                Toast.makeText(
                                    context,
                                    "No biometrics enrolled in device settings.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            is BiometricStatus.Unavailable -> {
                                Toast.makeText(context, status.reason, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        viewModel.toggleFingerprintLock(enabled)
                    }
                }
            )

            Spacer(Modifier.height(14.dp))

            ProfileMenuCard(
                scheme = scheme,
                onLanguageClick = { showLanguageSheet = true },
                onPrivacyClick = {
                    Toast.makeText(
                        context,
                        "Privacy: End-to-end encrypted chats & calls",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onTermsClick = {
                    Toast.makeText(
                        context,
                        "Chatty Terms & Privacy v9.3",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onHelpClick = {
                    Toast.makeText(
                        context,
                        "Support: support@chattyconnect.com",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onLogoutClick = {
                    viewModel.logout()
                    onLogoutClick()
                }
            )

            Spacer(Modifier.height(16.dp))

            // 6. App Version Tag
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = 12.dp),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                padding = ODSPadding(all = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = "Chatty Connect · Version 9.3 (Evermore)",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }

            Spacer(Modifier.height(96.dp))
        }

        // Language Selection Bottom Sheet
        if (showLanguageSheet) {
            LanguageSelectionBottomSheet(
                selectedLanguage = uiState.selectedLanguage,
                scheme = scheme,
                onLanguageSelected = { viewModel.setLanguage(it) },
                onDismiss = { showLanguageSheet = false }
            )
        }

        // Creator Model Verification Bottom Sheet
        if (showModelVerificationSheet) {
            ModelVerificationBottomSheet(
                initialName = uiState.displayName,
                initialAge = uiState.age,
                initialCountry = uiState.country,
                scheme = scheme,
                onSubmit = { name, age, country, photoUrl ->
                    viewModel.submitModelVerificationDetails(name, age, country, photoUrl)
                    showModelVerificationSheet = false
                    Toast.makeText(
                        context,
                        "Creator details verified successfully! ✨",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDismiss = { showModelVerificationSheet = false }
            )
        }

        // Edit Profile Dialog
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
    }
}

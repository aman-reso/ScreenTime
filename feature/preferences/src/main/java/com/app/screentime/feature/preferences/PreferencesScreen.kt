package com.app.screentime.feature.preferences

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.core.ui.util.LocationHelper
import com.app.screentime.core.ui.util.showODSToast
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Preferences / Onboarding Flow after Sign-in.
 * 1. 9-Step Personal & Match Basics (Name, Gender, Birthday, Height, Interested In, Intent, Languages, Income, Bio)
 * 2. Photos Selection & Upload
 * 3. Location Settings -> Followed by Notification Permission Sheet
 *
 * 100% constructed with Telekom ODS components.
 * Strictly adheres to project design tokens and Figma mockups.
 */
@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    initialStep: Int = 1,
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {},
    onHelp: () -> Unit = {},
    viewModel: PreferencesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showNotificationSheet by remember { mutableStateOf(false) }

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            viewModel.updateLocation(city = "Detecting location...", isEnabled = true)
            LocationHelper.fetchCurrentLocation(context) { _, _, cityName ->
                viewModel.updateLocation(city = cityName, isEnabled = true)
            }
        } else {
            viewModel.updateLocation(city = "Location Off", isEnabled = false)
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            LocationHelper.requestEnableLocationService(
                context = context,
                settingsLauncher = locationSettingsLauncher,
                onAlreadyEnabled = {
                    viewModel.updateLocation(city = "Detecting location...", isEnabled = true)
                    LocationHelper.fetchCurrentLocation(context) { _, _, cityName ->
                        viewModel.updateLocation(city = cityName, isEnabled = true)
                    }
                },
                onFailed = {
                    LocationHelper.openLocationSettings(context)
                }
            )
        } else {
            viewModel.updateLocation(city = "Location Off", isEnabled = false)
            val activity = context as? android.app.Activity
            val showRationale = activity?.let {
                androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } ?: true
            if (!showRationale) {
                LocationHelper.openAppSettings(context)
            } else {
                context.showODSToast("Location permission is required to detect nearby matches.")
            }
        }
    }

    LaunchedEffect(uiState.currentStep) {
        if (uiState.currentStep == 3) {
            if (LocationHelper.hasLocationPermission(context)) {
                LocationHelper.requestEnableLocationService(
                    context = context,
                    settingsLauncher = locationSettingsLauncher,
                    onAlreadyEnabled = {
                        viewModel.updateLocation(city = "Detecting location...", isEnabled = true)
                        LocationHelper.fetchCurrentLocation(context) { _, _, cityName ->
                            viewModel.updateLocation(city = cityName, isEnabled = true)
                        }
                    },
                    onFailed = {
                        viewModel.updateLocation(city = "Location Off", isEnabled = false)
                    }
                )
            }
        }
    }

    // When on Step 1: Render the 9-step single-screen flow
    if (uiState.currentStep == 1) {
        PreferencesNineStepsFlow(
            uiState = uiState,
            scheme = scheme,
            onNameChange = { viewModel.updateName(it) },
            onGenderSelect = { viewModel.updateGender(it) },
            onBirthDayChange = { viewModel.updateBirthDay(it) },
            onBirthMonthChange = { viewModel.updateBirthMonth(it) },
            onBirthYearChange = { viewModel.updateBirthYear(it) },
            onHeightChange = { viewModel.updateHeight(it) },
            onInterestedInChange = { viewModel.updateInterestedIn(it) },
            onRelationshipIntentChange = { viewModel.updateRelationshipIntent(it) },
            onToggleLanguage = { viewModel.toggleLanguage(it) },
            onIncomeSelect = { viewModel.updateIncome(it) },
            onBioChange = { viewModel.updateBio(it) },
            onStepSelect = { viewModel.setPreferenceSubStep(it) },
            onContinue = { viewModel.nextPreferenceSubStep() },
            onBack = { viewModel.previousPreferenceSubStep(onBack) },
            modifier = modifier
        )
    } else {
        // Steps 2 & 3: Photos and Location setup
        val ctaText = when (uiState.currentStep) {
            2 -> "Save Photos →"
            3 -> if (uiState.isSubmitting) "Saving Preferences..." else "Finish & Find Matches →"
            else -> "Continue →"
        }

        ODSBox(
            modifier = modifier
                .fillMaxSize()
                .imePadding(),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        ) {
            ODSColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                PreferencesTopBar(
                    currentStep = uiState.currentStep,
                    totalSteps = uiState.totalSteps,
                    scheme = scheme,
                    onBack = { viewModel.previousStep(onBack) },
                    onHelp = onHelp,
                    modifier = Modifier.padding(
                        start = ODSVariables.spacingLayout1,
                        end = ODSVariables.spacingLayout1,
                        top = ODSVariables.spacingComponent4,
                        bottom = ODSVariables.spacingComponent2
                    )
                )

                ODSLazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingLayout1,
                        top = ODSVariables.spacingComponent3,
                        bottom = ODSVariables.spacingComponent5
                    ),
                    gap = ODSVariables.spacingComponent5
                ) {
                    item {
                        when (uiState.currentStep) {
                            2 -> PreferencesStepPhotos(
                                uploadedPhotos = uiState.uploadedPhotos,
                                isUploading = uiState.isUploadingPhoto,
                                scheme = scheme,
                                onAddPhotos = { uris ->
                                    viewModel.uploadPhotos(uris)
                                },
                                onRemovePhoto = { index ->
                                    viewModel.removePhoto(index)
                                }
                            )

                            3 -> PreferencesStepLocation(
                                currentCity = uiState.currentCity,
                                isLocationEnabled = uiState.isLocationEnabled,
                                onEnableLocation = {
                                    LocationHelper.performLocationAction(
                                        context = context,
                                        permissionLauncher = locationPermissionLauncher,
                                        settingsLauncher = locationSettingsLauncher
                                    ) { _, _, cityName ->
                                        viewModel.updateLocation(city = cityName, isEnabled = true)
                                    }
                                },
                                onSkip = {
                                    viewModel.nextStep(onComplete = {
                                        showNotificationSheet = true
                                    })
                                },
                                scheme = scheme
                            )
                        }
                    }
                }

                // ── Bottom Action Dock (Pill CTA) ──────────────────────────
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = ODSVariables.spacingLayout1,
                            vertical = ODSVariables.spacingComponent4
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (!uiState.isSubmitting) {
                                    viewModel.nextStep(onComplete = {
                                        showNotificationSheet = true
                                    })
                                }
                            }
                        ),
                    height = 54.dp,
                    background = listOf(
                        ODSColorModel(
                            hexColor = if (uiState.isSubmitting) scheme.basicBackgroundSubtle else scheme.basicAccent
                        )
                    ),
                    cornerRadius = ODSCorners(all = 27.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        gap = ODSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = ctaText,
                            style = ODSTextStyles.bodyMBold,
                            color = if (uiState.isSubmitting) scheme.basicTextRecessive else scheme.basicTextOnAccent
                        )
                    }
                }
            }

            // ── Notification Permission Sheet (after Location) ─────────────
            if (showNotificationSheet) {
                NotificationPermissionBottomSheet(
                    onDismiss = {
                        showNotificationSheet = false
                        onComplete()
                    },
                    onPermissionGranted = {
                        showNotificationSheet = false
                        onComplete()
                    },
                    scheme = scheme
                )
            }
        }
    }
}

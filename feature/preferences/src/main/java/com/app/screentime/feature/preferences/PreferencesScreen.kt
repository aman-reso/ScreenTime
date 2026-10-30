package com.app.screentime.feature.preferences

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
 * Onboarding Flow with 2 major enum categories + Photos:
 * Category 1 (Essential details) & Category 2 (Other details) both feature
 * active sub-step question flow collapsing into summary cards with edit pencil icons.
 *
 * 100% constructed using Telekom ODS UI components & token values.
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

    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            if (err.isNotBlank()) {
                context.showODSToast(err)
            }
        }
    }

    BackHandler(enabled = true) {
        viewModel.previousStep(onBack = onBack)
    }

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            viewModel.updateLocation(city = "Detecting location...", isEnabled = false)
            LocationHelper.fetchCurrentLocation(context) { lat, lng, cityName ->
                viewModel.updateLocation(city = cityName, lat = lat, lng = lng, isEnabled = true)
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
                    viewModel.updateLocation(city = "Detecting location...", isEnabled = false)
                    LocationHelper.fetchCurrentLocation(context) { lat, lng, cityName ->
                        viewModel.updateLocation(city = cityName, lat = lat, lng = lng, isEnabled = true)
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

    when (uiState.currentStep.coerceIn(1, 3)) {
        1 -> {
            // Category 1: Essential Details (One-by-one sub-step flow)
            PreferencesCategoryOneFlow(
                uiState = uiState,
                scheme = scheme,
                onNameChange = { viewModel.updateName(it) },
                onGenderSelect = { viewModel.updateGender(it) },
                onAgeYearsChange = { viewModel.updateAgeYears(it) },
                onHeightChange = { viewModel.updateHeight(it) },
                onToggleLanguage = { viewModel.toggleLanguage(it) },
                onEnableLocation = {
                    viewModel.updateLocation(city = "Detecting location...", isEnabled = false)
                    LocationHelper.performLocationAction(
                        context = context,
                        permissionLauncher = locationPermissionLauncher,
                        settingsLauncher = locationSettingsLauncher
                    ) { lat, lng, cityName ->
                        viewModel.updateLocation(city = cityName, lat = lat, lng = lng, isEnabled = true)
                    }
                },
                onStepSelect = { subStep -> viewModel.setPreferenceSubStep(subStep) },
                onContinue = { viewModel.nextStep() },
                onBack = { viewModel.previousStep(onBack) },
                modifier = modifier
            )
        }

        2 -> {
            // Step 2: Upload Photos (ML Kit face check)
            ODSBox(
                modifier = modifier
                    .fillMaxSize()
                    .imePadding(),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    PreferencesTopBar(
                        currentStep = 2,
                        totalSteps = 3,
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
                            PreferencesStepPhotos(
                                uploadedPhotos = uiState.uploadedPhotos,
                                isUploading = uiState.isUploadingPhoto,
                                error = uiState.photoError ?: uiState.error,
                                scheme = scheme,
                                onAddPhotos = { uris ->
                                    viewModel.uploadPhotos(uris)
                                },
                                onRemovePhoto = { index ->
                                    viewModel.removePhoto(index)
                                }
                            )
                        }
                    }

                    // Bottom Pill Button for Photos step -> Next to Face Verification
                    val hasPhotos = uiState.uploadedPhotos.isNotEmpty()
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
                                    if (!uiState.isUploadingPhoto) {
                                        viewModel.nextStep()
                                    }
                                }
                            ),
                        height = 54.dp,
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (hasPhotos && !uiState.isUploadingPhoto) scheme.basicAccent else scheme.basicBackgroundSubtle
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
                                text = if (uiState.isUploadingPhoto) "Uploading Photos..." else "Continue to Face Verification →",
                                style = ODSTextStyles.bodyMBold,
                                color = if (hasPhotos && !uiState.isUploadingPhoto) scheme.basicTextOnAccent else scheme.basicTextRecessive
                            )
                        }
                    }
                }
            }
        }

        3 -> {
            // Step 3: Live Selfie Face Verification using ML Kit
            ODSBox(
                modifier = modifier
                    .fillMaxSize()
                    .imePadding(),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    PreferencesTopBar(
                        currentStep = 3,
                        totalSteps = 3,
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
                            PreferencesStepLivePhoto(
                                livePhotoUri = uiState.livePhotoUri,
                                isVerifyingFace = uiState.isVerifyingFace,
                                isFaceVerified = uiState.isFaceVerified,
                                verificationError = uiState.faceVerificationError,
                                onCaptureLivePhoto = { uriStr ->
                                    viewModel.verifyLivePhoto(uriStr)
                                },
                                scheme = scheme
                            )
                        }
                    }

                    // Bottom Pill Button for Finalizing Profile
                    val canFinish = !uiState.isSubmitting && !uiState.isVerifyingFace && uiState.isFaceVerified && !uiState.livePhotoUri.isNullOrBlank()
                    val buttonBackground = if (canFinish) scheme.basicAccent else scheme.basicBackgroundSubtle
                    val buttonTextColor = if (canFinish) scheme.basicTextOnAccent else scheme.basicTextRecessive
                    val buttonText = when {
                        uiState.isSubmitting -> "Saving Preferences..."
                        uiState.isVerifyingFace -> "Verifying Selfie..."
                        !uiState.isFaceVerified || uiState.livePhotoUri.isNullOrBlank() -> "Take selfie to continue"
                        else -> "Finish & Find Matches →"
                    }

                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = ODSVariables.spacingLayout1,
                                vertical = ODSVariables.spacingComponent4
                            )
                            .clickable(
                                enabled = canFinish,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    viewModel.nextStep(onComplete = {
                                        showNotificationSheet = true
                                    })
                                }
                            ),
                        height = 54.dp,
                        background = listOf(ODSColorModel(hexColor = buttonBackground)),
                        cornerRadius = ODSCorners(all = 27.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            gap = ODSVariables.spacingComponent2
                        ) {
                            ODSText(
                                text = buttonText,
                                style = ODSTextStyles.bodyMBold,
                                color = buttonTextColor
                            )
                        }
                    }
                }

                // Notification Permission Sheet
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
}

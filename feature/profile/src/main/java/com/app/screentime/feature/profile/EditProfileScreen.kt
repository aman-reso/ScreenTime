package com.app.screentime.feature.profile

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.components.GenericHeader
import com.app.screentime.core.ui.components.GenericHeaderProps
import com.app.screentime.core.ui.components.HeaderTitleAlignment
import com.app.screentime.core.ui.theme.FunnelSansFontFamily
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.core.ui.util.LocationHelper
import com.app.screentime.core.ui.util.showODSToast
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelect
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectMode
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectOptions
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectProps
import com.telekom.odsystem.molecules.dropdownselect.ODSDropdownSelectSize
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

private val GENDER_OPTIONS = listOf(
    "Male",
    "Female",
    "Non-binary",
    "Other"
)

private val EDUCATION_OPTIONS = listOf(
    "High School",
    "Bachelor’s degree",
    "Master’s degree",
    "Doctorate",
    "Trade School",
    "In College",
    "Prefer not to say"
)

private val PROFILE_LANGUAGE_OPTIONS = listOf(
    "English",
    "Hindi",
    "Spanish",
    "French",
    "German",
    "Punjabi",
    "Bengali",
    "Gujarati",
    "Tamil",
    "Telugu",
    "Marathi",
    "Urdu",
    "Kannada",
    "Malayalam",
    "Russian",
    "Chinese",
    "Japanese",
    "Arabic"
)

/**
 * Edit Profile Screen ("Personal Info").
 *
 * Designed to faithfully match the design mockup:
 * - Top header with Back arrow, "Personal Info" title, and "Save" action
 * - Circular avatar with camera badge overlay and "Change Photo" action
 * - Rounded input cards for all personal information fields
 * - Delete Account destructive action at bottom
 *
 * Rules:
 * 1. 100% Telekom ODS components & design system tokens.
 * 2. Color tokens derived from `scheme: ODSTheme`.
 * 3. Padding, gaps, and sizes mapped to `ODSVariables`.
 * 4. Architecture strictly follows MVVM Clean Architecture (ViewModel -> UseCase -> Repository).
 */
@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onSaved: () -> Unit = onBack,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val funnelSansFontFamily = FunnelSansFontFamily

    var isGenderExpanded by remember { mutableStateOf(false) }
    val genderOptions = remember {
        GENDER_OPTIONS.map { ODSDropdownSelectOptions(label = it) }
    }
    var showEducationSheet by remember { mutableStateOf(false) }
    var showLanguagesSheet by remember { mutableStateOf(false) }
    var tempSelectedLanguages by remember { mutableStateOf(setOf<String>()) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // ── Auto-fill location if empty and device already has permission ────────
    LaunchedEffect(Unit) {
        if (uiState.editLocation.isBlank() && LocationHelper.hasLocationPermission(context) && LocationHelper.isLocationServiceEnabled(
                context
            )
        ) {
            LocationHelper.fetchCurrentLocation(context) { lat, lng, cityName ->
                if (!cityName.isNullOrBlank()) {
                    viewModel.updateLocation(cityName, lat, lng)
                }
            }
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            if (err.isNotBlank()) {
                context.showODSToast(err)
            }
        }
    }

    // ── Photo Picker Launcher ────────────────────────────────────────────────
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.uploadProfilePhotos(uris = uris, isAvatar = true) { uploadedUrls ->
                if (uploadedUrls.isNotEmpty()) {
                    context.showODSToast("Photo uploaded successfully!")
                }
            }
        }
    }

    // ── Location Launchers ───────────────────────────────────────────────────
    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            LocationHelper.fetchCurrentLocation(context) { lat, lng, cityName ->
                if (!cityName.isNullOrBlank()) {
                    viewModel.updateLocation(cityName, lat, lng)
                    context.showODSToast("Location updated: $cityName")
                } else {
                    viewModel.setDetectingLocation(false)
                }
            }
        } else {
            viewModel.setDetectingLocation(false)
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
                    LocationHelper.fetchCurrentLocation(context) { lat, lng, cityName ->
                        if (!cityName.isNullOrBlank()) {
                            viewModel.updateLocation(cityName, lat, lng)
                            context.showODSToast("Location updated: $cityName")
                        } else {
                            viewModel.setDetectingLocation(false)
                        }
                    }
                },
                onFailed = {
                    viewModel.setDetectingLocation(false)
                    LocationHelper.openLocationSettings(context)
                }
            )
        } else {
            viewModel.setDetectingLocation(false)
        }
    }

    ODSBox(
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            GenericHeader(
                props = GenericHeaderProps(
                    title = "Personal Info",
                    showBackArrow = true,
                    onBackClick = onBack,
                    titleAlignment = HeaderTitleAlignment.CENTER
                ),
                scheme = scheme
            )

            // ── Scrollable Form Fields ───────────────────────────────────────
            ODSLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                padding = ODSPadding(
                    left = ODSVariables.spacingLayout1,
                    right = ODSVariables.spacingLayout1,
                    top = ODSVariables.spacingComponent2,
                ),
                gap = ODSVariables.spacingComponent4
            ) {
                // ── Avatar & "Change Photo" Section ──────────────────────────
                item {
                    ODSColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = ODSVariables.spacingComponent2),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val currentAvatarUrl = uiState.photoUrl.ifBlank {
                            uiState.user?.avatarUrl.orEmpty()
                        }

                        // Circular Avatar with Badge
                        ODSBox(
                            modifier = Modifier
                                .size(108.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { galleryLauncher.launch("image/*") }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            // Main Circular Image
                            if (currentAvatarUrl.isNotBlank()) {
                                ODSImage(
                                    imageModel = ODSImageModel(
                                        url = currentAvatarUrl,
                                        contentDescription = "Profile Photo"
                                    ),
                                    width = 104.dp,
                                    height = 104.dp,
                                    cornerRadius = ODSCorners(all = 52.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                ODSBox(
                                    modifier = Modifier.size(104.dp),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                                    cornerRadius = ODSCorners(all = 52.dp),
                                    border = ODSBorder(
                                        width = ODSVariables.strokes1,
                                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = R.drawable.ic_user,
                                            contentDescription = "Default Avatar"
                                        ),
                                        tint = scheme.basicTextRecessive.getColor(),
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            }

                            // Camera Badge Icon at bottom right
                            ODSBox(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(32.dp),
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                cornerRadius = ODSCorners(all = 16.dp),
                                border = ODSBorder(
                                    width = 2.dp,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_camera,
                                        contentDescription = "Change Photo"
                                    ),
                                    tint = scheme.basicBackgroundCard.getColor(),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(ODSVariables.spacingComponent2))

                        // "Change Photo" Action Text
                        ODSText(
                            text = if (uiState.isUploadingPhoto) "Uploading..." else "Change Photo",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicAccent,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { galleryLauncher.launch("image/*") }
                            )
                        )
                    }
                }

                // ── 1. Full Name ─────────────────────────────────────────────
                item {
                    PersonalInfoInputField(
                        label = "Full Name",
                        value = uiState.editFullName,
                        onValueChange = { viewModel.onEditFullNameChange(it) },
                        scheme = scheme,
                        fontFamily = funnelSansFontFamily
                    )
                }

                // ── 2. Age ───────────────────────────────────────────────────
                item {
                    PersonalInfoInputField(
                        label = "Age",
                        value = uiState.editAge,
                        onValueChange = { viewModel.onEditAgeChange(it) },
                        scheme = scheme,
                        fontFamily = funnelSansFontFamily,
                        keyboardType = KeyboardType.Number
                    )
                }

                // ── 3. Gender (ODS Flyout Dropdown) ───────────────────────────
                item {
                    ODSDropdownSelect(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSDropdownSelectProps(
                            label = "Gender",
                            expanded = isGenderExpanded,
                            selectedValue = if (uiState.editGender.isBlank()) null else ODSDropdownSelectOptions(label = uiState.editGender),
                            options = genderOptions,
                            mode = ODSDropdownSelectMode.STANDARD,
                            size = ODSDropdownSelectSize.SMALL
                        ),
                        onClick = { isGenderExpanded = !isGenderExpanded },
                        selectedOption = { option ->
                            option.label?.let { viewModel.onEditGenderChange(it) }
                            isGenderExpanded = false
                        }
                    )
                }


                // ── 4. Phone Number (Read-only / Account-linked) ──────────
                item {
                    val isPhoneVerified = uiState.isPhoneVerified &&
                            (uiState.editPhone.isNotBlank() || !uiState.user?.phone.isNullOrBlank())
                    PersonalInfoInputField(
                        label = "Phone Number",
                        value = uiState.editPhone.ifBlank { uiState.user?.phone.orEmpty() },
                        onValueChange = {},
                        scheme = scheme,
                        fontFamily = funnelSansFontFamily,
                        keyboardType = KeyboardType.Phone,
                        readOnly = true,
                        enabled = false,
                        helperText = "Phone number is linked to your login account and cannot be changed",
                        trailingIcon = if (isPhoneVerified) {
                            {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_check,
                                        contentDescription = "Verified"
                                    ),
                                    tint = HexColor("#22C55E").getColor(),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else null
                    )
                }

                // ── 5. Email (Read-only / Account-linked) ────────────────────
                item {
                    val resolvedEmail = uiState.editEmail.ifBlank { uiState.user?.email.orEmpty() }
                    PersonalInfoInputField(
                        label = "Email",
                        value = resolvedEmail,
                        onValueChange = {},
                        scheme = scheme,
                        fontFamily = funnelSansFontFamily,
                        keyboardType = KeyboardType.Email,
                        readOnly = true,
                        enabled = false,
                        helperText = "Email address is linked to your login account and cannot be changed",
                        trailingIcon = if (resolvedEmail.isNotBlank()) {
                            {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_check,
                                        contentDescription = "Verified"
                                    ),
                                    tint = HexColor("#22C55E").getColor(),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else null
                    )
                }

                // ── 6. Location (Pin Icon & Update Option) ────────────────────
                item {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 6.dp
                    ) {
                        PersonalInfoInputField(
                            label = "Location",
                            value = uiState.editLocation,
                            onValueChange = { viewModel.onEditLocationChange(it) },
                            scheme = scheme,
                            fontFamily = funnelSansFontFamily,
                            trailingIcon = {
                                ODSBox(
                                    modifier = Modifier.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            viewModel.setDetectingLocation(true)
                                            LocationHelper.performLocationAction(
                                                context = context,
                                                permissionLauncher = locationPermissionLauncher,
                                                settingsLauncher = locationSettingsLauncher
                                            ) { lat, lng, cityName ->
                                                if (!cityName.isNullOrBlank()) {
                                                    viewModel.updateLocation(cityName, lat, lng)
                                                    context.showODSToast("Location updated: $cityName")
                                                } else {
                                                    viewModel.setDetectingLocation(false)
                                                }
                                            }
                                        }
                                    )
                                ) {
                                    if (uiState.isDetectingLocation) {
                                        ODSLoadingSpinner(
                                            props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL),
                                            scheme = scheme
                                        )
                                    } else {
                                        ODSIcon(
                                            iconModel = ODSIconModel(
                                                drawableRes = R.drawable.ic_map_pin,
                                                contentDescription = "Detect Location"
                                            ),
                                            tint = scheme.basicAccent.getColor(),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        )

                        // Option to Update Location
                        ODSRow(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        viewModel.setDetectingLocation(true)
                                        LocationHelper.performLocationAction(
                                            context = context,
                                            permissionLauncher = locationPermissionLauncher,
                                            settingsLauncher = locationSettingsLauncher
                                        ) { lat, lng, cityName ->
                                            if (!cityName.isNullOrBlank()) {
                                                viewModel.updateLocation(cityName, lat, lng)
                                                context.showODSToast("Location updated: $cityName")
                                            } else {
                                                viewModel.setDetectingLocation(false)
                                            }
                                        }
                                    }
                                )
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_map_pin,
                                    contentDescription = "Update Location"
                                ),
                                tint = scheme.basicAccent.getColor(),
                                modifier = Modifier.size(14.dp)
                            )
                            ODSText(
                                text = if (uiState.isDetectingLocation) "Detecting current location..." else "Update Current Location",
                                style = ODSTextStyles.bodySRegular,
                                color = scheme.basicAccent
                            )
                        }
                    }
                }

                // ── 7. Bio (Multiline) ───────────────────────────────────────
                item {
                    PersonalInfoInputField(
                        label = "Bio",
                        value = uiState.editBio,
                        onValueChange = { viewModel.onEditBioChange(it) },
                        scheme = scheme,
                        fontFamily = funnelSansFontFamily,
                        singleLine = false,
                        minLines = 3,
                        maxLines = 5
                    )
                }

                // ── 8. Job Title ─────────────────────────────────────────────
                item {
                    PersonalInfoInputField(
                        label = "Job Title",
                        value = uiState.editJob,
                        onValueChange = { viewModel.onEditJobChange(it) },
                        scheme = scheme,
                        fontFamily = funnelSansFontFamily
                    )
                }

                // ── 9. Height ────────────────────────────────────────────────
                item {
                    PersonalInfoInputField(
                        label = "Height",
                        value = uiState.editHeight,
                        onValueChange = { viewModel.onEditHeightChange(it) },
                        scheme = scheme,
                        fontFamily = funnelSansFontFamily,
                        keyboardType = KeyboardType.Number,
                        error = uiState.editHeightError,
                        trailingIcon = {
                            ODSText(
                                text = "cm",
                                style = ODSTextStyles.bodySRegular,
                                color = scheme.basicTextRecessive
                            )
                        }
                    )
                }

                // ── 10. Education (Select Field) ─────────────────────────────
                item {
                    PersonalInfoSelectField(
                        label = "Education",
                        value = uiState.editEducation.ifBlank { "Select Education" },
                        onClick = { showEducationSheet = true },
                        trailingIcon = {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_chevron_down,
                                    contentDescription = "Select Education"
                                ),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        scheme = scheme
                    )
                }

                // ── 11. Languages Spoken (Select Field) ─────────────────────
                item {
                    PersonalInfoSelectField(
                        label = "Languages Spoken",
                        value = uiState.editLanguages.ifBlank { "Select Languages" },
                        onClick = {
                            tempSelectedLanguages = uiState.editLanguages
                                .split(",", ";")
                                .map { it.trim() }
                                .filter { it.isNotBlank() }
                                .toSet()
                            showLanguagesSheet = true
                        },
                        trailingIcon = {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_chevron_down,
                                    contentDescription = "Select Languages"
                                ),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        scheme = scheme
                    )
                }

                // ── 11. Delete Account Action ────────────────────────────────
                item {
                    Spacer(modifier = Modifier.height(ODSVariables.spacingComponent3))

                    ODSColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = ODSVariables.spacingComponent4),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ODSText(
                            text = "Delete Account",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.functionalDestructiveStandard,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { showDeleteConfirmDialog = true }
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(ODSVariables.spacingLayout1))
                }
            }

            // ── Sticky Save Button at Bottom ─────────────────────────────────
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ODSVariables.spacingLayout1,
                    ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = if (uiState.isSavingProfile) "Saving..." else "Save Changes",
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.LARGE,
                        disabled = uiState.isSavingProfile
                    ),
                    onClick = {
                        viewModel.validateAndSaveProfile(
                            onSuccess = {
                                context.showODSToast("Profile saved successfully")
                                onSaved()
                            },
                            onError = { errorMsg ->
                                context.showODSToast(errorMsg)
                            }
                        )
                    }
                )
            }
        }

        // ── Education Selection Bottom Sheet ─────────────────────────────────
        if (showEducationSheet) {
            ODSBottomSheet(
                scheme = scheme,
                showBottomSheet = true,
                props = ODSBottomSheetProps(showHandle = true),
                onDismissRequest = { showEducationSheet = false },
                onCloseClicked = { showEducationSheet = false },
                titleSlot = {
                    ODSBottomSheetHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp),
                        scheme = scheme,
                        props = ODSBottomSheetHeaderProps(
                            smallHeading = "Select Education",
                            size = ODSBottomSheetHeaderSize.SMALL
                        )
                    )
                },
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = ODSVariables.spacingLayout1,
                                vertical = ODSVariables.spacingComponent2
                            ),
                        gap = ODSVariables.spacingComponent2
                    ) {
                        EDUCATION_OPTIONS.forEach { option ->
                            val isSelected = uiState.editEducation.equals(option, ignoreCase = true)
                            ODSRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            viewModel.onEditEducationChange(option)
                                            showEducationSheet = false
                                        }
                                    ),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent4,
                                    vertical = ODSVariables.spacingComponent3
                                ),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                background = listOf(
                                    ODSColorModel(
                                        hexColor = if (isSelected) scheme.basicBackgroundSubtle else scheme.basicBackground
                                    )
                                ),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(
                                        ODSColorModel(
                                            hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                                        )
                                    )
                                ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSText(
                                    text = option,
                                    style = if (isSelected) ODSTextStyles.bodyMBold else ODSTextStyles.bodyMRegular,
                                    color = if (isSelected) scheme.basicAccent else scheme.basicTextDominant
                                )

                                if (isSelected) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = R.drawable.ic_check,
                                            contentDescription = "Selected"
                                        ),
                                        tint = scheme.basicAccent.getColor(),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }

        // ── Languages Selection Bottom Sheet ─────────────────────────────────
        if (showLanguagesSheet) {
            ODSBottomSheet(
                scheme = scheme,
                showBottomSheet = true,
                props = ODSBottomSheetProps(showHandle = true),
                onDismissRequest = { showLanguagesSheet = false },
                onCloseClicked = { showLanguagesSheet = false },
                titleSlot = {
                    ODSBottomSheetHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp),
                        scheme = scheme,
                        props = ODSBottomSheetHeaderProps(
                            smallHeading = "Select Languages Spoken",
                            size = ODSBottomSheetHeaderSize.SMALL
                        )
                    )
                },
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = ODSVariables.spacingLayout1,
                                vertical = ODSVariables.spacingComponent2
                            ),
                        gap = ODSVariables.spacingComponent2
                    ) {
                        PROFILE_LANGUAGE_OPTIONS.forEach { lang ->
                            val isSelected = tempSelectedLanguages.contains(lang)
                            ODSRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            tempSelectedLanguages = if (isSelected) {
                                                tempSelectedLanguages - lang
                                            } else {
                                                tempSelectedLanguages + lang
                                            }
                                        }
                                    ),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent4,
                                    vertical = ODSVariables.spacingComponent3
                                ),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                background = listOf(
                                    ODSColorModel(
                                        hexColor = if (isSelected) scheme.basicBackgroundSubtle else scheme.basicBackground
                                    )
                                ),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(
                                        ODSColorModel(
                                            hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                                        )
                                    )
                                ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSText(
                                    text = lang,
                                    style = if (isSelected) ODSTextStyles.bodyMBold else ODSTextStyles.bodyMRegular,
                                    color = if (isSelected) scheme.basicAccent else scheme.basicTextDominant
                                )

                                if (isSelected) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = R.drawable.ic_check,
                                            contentDescription = "Selected"
                                        ),
                                        tint = scheme.basicAccent.getColor(),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                actionSlot = {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = ODSVariables.spacingLayout1,
                                vertical = ODSVariables.spacingComponent3
                            )
                    ) {
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = if (tempSelectedLanguages.isEmpty()) "Done" else "Done (${tempSelectedLanguages.size})",
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = {
                                val result = tempSelectedLanguages.joinToString(", ")
                                viewModel.onEditLanguagesChange(result)
                                showLanguagesSheet = false
                            }
                        )
                    }
                }
            )
        }

        // ── Delete Account Confirmation Dialog ───────────────────────────────
        if (showDeleteConfirmDialog) {
            ODSDialog(
                scheme = scheme,
                onDismissRequest = { showDeleteConfirmDialog = false },
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                props = ODSDialogProps(
                    showCloseButton = true,
                    title = "Delete Account",
                    bodyText = "Are you sure you want to delete your account? All your personal profile information, photos, and messages will be permanently deleted. This action cannot be undone."
                ),
                actionSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = if (uiState.isDeletingAccount) "Deleting..." else "Yes, Delete Account",
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = {
                                viewModel.deleteAccount(
                                    onSuccess = {
                                        showDeleteConfirmDialog = false
                                        context.showODSToast("Account deleted successfully")
                                        onBack()
                                    },
                                    onError = { error ->
                                        showDeleteConfirmDialog = false
                                        context.showODSToast(error)
                                    }
                                )
                            }
                        )

                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Cancel",
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { showDeleteConfirmDialog = false }
                        )
                    }
                }
            )
        }
    }
}

/**
 * Clean reusable input field component matching the personal info card style in the mockup.
 */
@Composable
private fun PersonalInfoInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    scheme: ODSTheme,
    fontFamily: androidx.compose.ui.text.font.FontFamily,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    helperText: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 6.dp
    ) {
        ODSText(
            text = label,
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextRecessive
        )

        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            padding = ODSPadding(
                left = ODSVariables.spacingComponent4,
                right = ODSVariables.spacingComponent4,
                top = if (singleLine) 12.dp else 14.dp,
                bottom = if (singleLine) 12.dp else 14.dp
            ),
            cornerRadius = ODSCorners(all = 12.dp),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(
                    ODSColorModel(
                        hexColor = if (!error.isNullOrBlank()) scheme.functionalDestructiveStandard else scheme.basicStroke
                    )
                )
            ),
            background = listOf(ODSColorModel(hexColor = if (enabled) scheme.basicBackground else scheme.basicBackgroundSubtle)),
            verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSBox(
                modifier = Modifier.weight(1f)
            ) {
                if (value.isBlank() && !enabled) {
                    ODSText(
                        text = "Not connected",
                        style = ODSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                } else {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        readOnly = readOnly,
                        enabled = enabled,
                        textStyle = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (enabled) scheme.basicTextDominant.getColor() else scheme.basicTextRecessive.getColor()
                        ),
                        cursorBrush = if (enabled) SolidColor(scheme.basicText.getColor()) else SolidColor(androidx.compose.ui.graphics.Color.Transparent),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = singleLine,
                        minLines = minLines,
                        maxLines = maxLines,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
                    )
                }
            }

            trailingIcon?.let { icon ->
                ODSBox(
                    modifier = Modifier.padding(start = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
        }

        if (!error.isNullOrBlank()) {
            ODSText(
                text = error,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.functionalDestructiveStandard
            )
        } else if (!helperText.isNullOrBlank()) {
            ODSText(
                text = helperText,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}

/**
 * Clickable selection field component (e.g. for Gender dropdown) matching the mockup style.
 */
@Composable
private fun PersonalInfoSelectField(
    label: String,
    value: String,
    onClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = 6.dp
    ) {
        ODSText(
            text = label,
            style = ODSTextStyles.bodySBold,
            color = scheme.basicTextRecessive
        )

        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            padding = ODSPadding(
                left = ODSVariables.spacingComponent4,
                right = ODSVariables.spacingComponent4,
                top = 12.dp,
                bottom = 12.dp
            ),
            cornerRadius = ODSCorners(all = 12.dp),
            border = ODSBorder(
                width = ODSVariables.strokes1,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSText(
                text = value,
                style = ODSTextStyles.bodyMRegular,
                color = scheme.basicTextDominant
            )

            trailingIcon?.let { icon ->
                ODSBox(
                    modifier = Modifier.padding(start = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
        }
    }
}

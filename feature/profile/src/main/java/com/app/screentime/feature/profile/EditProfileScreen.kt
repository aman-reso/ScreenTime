package com.app.screentime.feature.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.FunnelSansFontFamily
import com.app.screentime.core.ui.theme.zonaODSTheme
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
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Sections in Edit Profile that can be expanded or collapsed accordion-style.
 */
enum class EditProfileSection {
    FULL_NAME,
    BIO,
    GENDER,
    DOB,
    HEIGHT,
    LANGUAGES,
    JOB,
    EDUCATION,
    INTENT,
    INTERESTED_IN,
    LOCATION
}

/**
 * Edit Profile Screen with collapsible summary cards matching the onboarding completed step style.
 *
 * All sections start collapsed as compact summary cards:
 * `Label` • `Value` • ✏️
 * Tapping any section expands its dedicated editor inline with quick-selection chips.
 *
 * Rules:
 * 1. 100% ODS components & design system tokens.
 * 2. Colors derived from `scheme: ODSTheme`.
 * 3. Padding, gaps, and sizes mapped to `ODSVariables`.
 * 4. State driven by ProfileViewModel.
 */
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit = onBack,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val funnelSansFontFamily = FunnelSansFontFamily

    var expandedSection by remember { mutableStateOf<EditProfileSection?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            context.showODSToast("Uploading photo...")
            viewModel.uploadProfilePhotos(uris = uris, isAvatar = true) { uploadedUrls ->
                if (uploadedUrls.isNotEmpty()) {
                    context.showODSToast("Photo uploaded successfully!")
                } else {
                    context.showODSToast("Photo upload completed")
                }
            }
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        clipContent = true,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        0.00f to scheme.basicBackground,
                        0.52f to scheme.basicBackgroundSubtle,
                        1.00f to scheme.basicBackgroundCard
                    ),
                    opacity = 1.00f,
                    angleInDegrees = 180f
                )
            )
        )
    ) {
        // ── 1. Scrollable List of Collapsible Section Cards ──────────────────
        ODSLazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding(),
            gap = ODSVariables.spacingComponent3
        ) {
            // Top Bar
            item {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    padding = ODSPadding(
                        top = ODSVariables.spacingComponent3,
                        bottom = ODSVariables.spacingComponent3,
                        left = ODSVariables.spacingLayout1,
                        right = ODSVariables.spacingLayout1
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ODSRow(
                        padding = ODSPadding(all = ODSVariables.spacingComponent4),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBack
                        )
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_arrow_left,
                                contentDescription = "Back"
                            ),
                            tint = scheme.basicTextDominant.getColor(),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    ODSText(
                        text = "Edit Profile",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant
                    )

                    ODSBox(modifier = Modifier.size(44.dp))
                }
            }

            // ── Manage Photos Section ────────────────────────────────────────
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout1),
                    gap = ODSVariables.spacingLayout1,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    val currentAvatarUrl = uiState.photoUrl.ifBlank {
                        uiState.user?.avatarUrl?.ifBlank { null }
                    }

                    ODSRow(
                        cornerRadius = ODSCorners(all = 40.dp),
                        border = ODSBorder(
                            width = ODSVariables.strokes2,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                        width = 80.dp,
                        height = 80.dp
                    ) {
                        if (!currentAvatarUrl.isNullOrBlank()) {
                            ODSImage(
                                imageModel = ODSImageModel(
                                    url = currentAvatarUrl,
                                    contentDescription = "user-avatar"
                                ),
                                width = 72.dp,
                                height = 72.dp,
                                cornerRadius = ODSCorners(all = 36.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_user,
                                    contentDescription = "user-avatar"
                                ),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    ODSColumn(
                        gap = 6.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ODSText(
                            text = "Profile Photo",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )

                        ODSRow(
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { galleryLauncher.launch("image/*") }
                            ),
                            padding = ODSPadding(
                                top = 6.dp,
                                bottom = 6.dp,
                                left = ODSVariables.spacingComponent4,
                                right = ODSVariables.spacingComponent4
                            ),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                            border = ODSBorder(
                                width = ODSVariables.strokes1,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                            ),
                            horizontalAlignment = Alignment.Start,
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start,
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                        ) {
                            ODSText(
                                text = if (uiState.isUploadingPhoto) "Uploading..." else "Upload New",
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicTextDominant
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(ODSVariables.spacingComponent2))
            }

            // ── 1. Full Name Section ─────────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Full Name",
                    value = uiState.editFullName,
                    isExpanded = expandedSection == EditProfileSection.FULL_NAME,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.FULL_NAME) null else EditProfileSection.FULL_NAME
                    },
                    hasError = uiState.editFullNameError != null,
                    errorMessage = uiState.editFullNameError,
                    scheme = scheme
                ) {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent4,
                            bottom = ODSVariables.spacingComponent4,
                            left = ODSVariables.spacingLayout1,
                            right = ODSVariables.spacingLayout1
                        ),
                        cornerRadius = ODSCorners(all = 14.dp),
                        border = ODSBorder(
                            width = ODSVariables.strokes2,
                            colorList = listOf(
                                ODSColorModel(
                                    hexColor = if (uiState.editFullNameError != null) scheme.functionalDestructiveStandard else scheme.basicStroke
                                )
                            )
                        ),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                    ) {
                        BasicTextField(
                            value = uiState.editFullName,
                            onValueChange = { viewModel.onEditFullNameChange(it) },
                            textStyle = TextStyle(
                                fontFamily = funnelSansFontFamily,
                                fontSize = 15.sp,
                                color = scheme.basicTextDominant.getColor()
                            ),
                            cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }

            // ── 2. About Me / Bio Section ────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "About Me",
                    value = uiState.editBio,
                    isExpanded = expandedSection == EditProfileSection.BIO,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.BIO) null else EditProfileSection.BIO
                    },
                    hasError = uiState.editBioError != null,
                    errorMessage = uiState.editBioError,
                    scheme = scheme
                ) {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp),
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent4,
                            bottom = ODSVariables.spacingComponent4,
                            left = ODSVariables.spacingLayout1,
                            right = ODSVariables.spacingLayout1
                        ),
                        cornerRadius = ODSCorners(all = 14.dp),
                        border = ODSBorder(
                            width = ODSVariables.strokes2,
                            colorList = listOf(
                                ODSColorModel(
                                    hexColor = if (uiState.editBioError != null) scheme.functionalDestructiveStandard else scheme.basicStroke
                                )
                            )
                        ),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                    ) {
                        BasicTextField(
                            value = uiState.editBio,
                            onValueChange = { viewModel.onEditBioChange(it) },
                            textStyle = TextStyle(
                                fontFamily = funnelSansFontFamily,
                                fontSize = 15.sp,
                                color = scheme.basicTextDominant.getColor(),
                                lineHeight = 20.sp
                            ),
                            cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // ── 3. Gender Section ────────────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Gender",
                    value = uiState.editGender.ifBlank { "Male" },
                    isExpanded = expandedSection == EditProfileSection.GENDER,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.GENDER) null else EditProfileSection.GENDER
                    },
                    scheme = scheme
                ) {
                    val genders = listOf("Male", "Female", "Non-binary")
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        genders.forEach { gender ->
                            OptionChip(
                                text = gender,
                                isSelected = uiState.editGender.equals(gender, ignoreCase = true),
                                onClick = { viewModel.onEditGenderChange(gender) },
                                scheme = scheme
                            )
                        }
                    }
                }
            }

            // ── 4. Date of Birth Section ─────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Birthday",
                    value = uiState.editDob.ifBlank { "1998-10-12" },
                    isExpanded = expandedSection == EditProfileSection.DOB,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.DOB) null else EditProfileSection.DOB
                    },
                    hasError = uiState.editDobError != null,
                    errorMessage = uiState.editDobError,
                    scheme = scheme
                ) {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent4,
                            bottom = ODSVariables.spacingComponent4,
                            left = ODSVariables.spacingLayout1,
                            right = ODSVariables.spacingLayout1
                        ),
                        cornerRadius = ODSCorners(all = 14.dp),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                    ) {
                        BasicTextField(
                            value = uiState.editDob,
                            onValueChange = { viewModel.onEditDobChange(it) },
                            textStyle = TextStyle(
                                fontFamily = funnelSansFontFamily,
                                fontSize = 15.sp,
                                color = scheme.basicTextDominant.getColor()
                            ),
                            cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }

            // ── 5. Height Section ────────────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Height",
                    value = uiState.editHeight.ifBlank { "178 cm" },
                    isExpanded = expandedSection == EditProfileSection.HEIGHT,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.HEIGHT) null else EditProfileSection.HEIGHT
                    },
                    hasError = uiState.editHeightError != null,
                    errorMessage = uiState.editHeightError,
                    scheme = scheme
                ) {
                    val heights = listOf("160 cm", "165 cm", "170 cm", "175 cm", "178 cm", "182 cm", "185 cm", "190 cm")
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        heights.forEach { h ->
                            OptionChip(
                                text = h,
                                isSelected = uiState.editHeight == h,
                                onClick = { viewModel.onEditHeightChange(h) },
                                scheme = scheme
                            )
                        }
                    }
                }
            }

            // ── 6. Languages Section ─────────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Languages",
                    value = uiState.editLanguages.ifBlank { "English" },
                    isExpanded = expandedSection == EditProfileSection.LANGUAGES,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.LANGUAGES) null else EditProfileSection.LANGUAGES
                    },
                    scheme = scheme
                ) {
                    val popularLangs = listOf("English", "Hindi", "Spanish", "French", "German", "Punjabi", "Bengali")
                    val currentSelected = uiState.editLanguages.split(",").map { it.trim() }.toSet()

                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        popularLangs.forEach { lang ->
                            val isSelected = lang in currentSelected
                            OptionChip(
                                text = lang,
                                isSelected = isSelected,
                                onClick = {
                                    val updated = if (isSelected) {
                                        currentSelected - lang
                                    } else {
                                        currentSelected + lang
                                    }
                                    viewModel.onEditLanguagesChange(updated.joinToString(", "))
                                },
                                scheme = scheme
                            )
                        }
                    }
                }
            }

            // ── 7. Job / Profession Section ──────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Job / Profession",
                    value = uiState.editJob.ifBlank { "Software Engineer" },
                    isExpanded = expandedSection == EditProfileSection.JOB,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.JOB) null else EditProfileSection.JOB
                    },
                    scheme = scheme
                ) {
                    val jobs = listOf("Software Engineer", "Product Designer", "Founder", "Doctor", "Consultant", "Student", "Artist")
                    ODSColumn(gap = ODSVariables.spacingComponent3) {
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            gap = ODSVariables.spacingComponent3
                        ) {
                            jobs.forEach { job ->
                                OptionChip(
                                    text = job,
                                    isSelected = uiState.editJob.equals(job, ignoreCase = true),
                                    onClick = { viewModel.onEditJobChange(job) },
                                    scheme = scheme
                                )
                            }
                        }
                        // Custom text field input
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingLayout1,
                                vertical = ODSVariables.spacingComponent4
                            ),
                            cornerRadius = ODSCorners(all = 14.dp),
                            border = ODSBorder(
                                width = ODSVariables.strokes1,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                        ) {
                            BasicTextField(
                                value = uiState.editJob,
                                onValueChange = { viewModel.onEditJobChange(it) },
                                textStyle = TextStyle(
                                    fontFamily = funnelSansFontFamily,
                                    fontSize = 15.sp,
                                    color = scheme.basicTextDominant.getColor()
                                ),
                                cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            // ── 8. Education Section ─────────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Education",
                    value = uiState.editEducation.ifBlank { "Graduate Degree" },
                    isExpanded = expandedSection == EditProfileSection.EDUCATION,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.EDUCATION) null else EditProfileSection.EDUCATION
                    },
                    scheme = scheme
                ) {
                    val degrees = listOf("High School", "Undergraduate", "Graduate Degree", "Post Graduate", "PhD / Doctorate")
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        degrees.forEach { deg ->
                            OptionChip(
                                text = deg,
                                isSelected = uiState.editEducation.equals(deg, ignoreCase = true),
                                onClick = { viewModel.onEditEducationChange(deg) },
                                scheme = scheme
                            )
                        }
                    }
                }
            }

            // ── 9. Relationship Intent Section ───────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Looking for",
                    value = uiState.editIntent.ifBlank { "Long-term" },
                    isExpanded = expandedSection == EditProfileSection.INTENT,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.INTENT) null else EditProfileSection.INTENT
                    },
                    scheme = scheme
                ) {
                    val intents = listOf("Long-term", "Short-term", "Casual", "Friendship", "Marriage")
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        intents.forEach { intent ->
                            OptionChip(
                                text = intent,
                                isSelected = uiState.editIntent.equals(intent, ignoreCase = true),
                                onClick = { viewModel.onEditIntentChange(intent) },
                                scheme = scheme
                            )
                        }
                    }
                }
            }

            // ── 10. Interested In Section ────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Interested in",
                    value = uiState.editInterestedIn.ifBlank { "Women" },
                    isExpanded = expandedSection == EditProfileSection.INTERESTED_IN,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.INTERESTED_IN) null else EditProfileSection.INTERESTED_IN
                    },
                    scheme = scheme
                ) {
                    val preferences = listOf("Women", "Men", "Everyone")
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        gap = ODSVariables.spacingComponent3
                    ) {
                        preferences.forEach { pref ->
                            OptionChip(
                                text = pref,
                                isSelected = uiState.editInterestedIn.equals(pref, ignoreCase = true),
                                onClick = { viewModel.onEditInterestedInChange(pref) },
                                scheme = scheme
                            )
                        }
                    }
                }
            }

            // ── 11. Location Section ─────────────────────────────────────────
            item {
                CollapsibleProfileSectionCard(
                    label = "Location",
                    value = uiState.editLocation.ifBlank { "Bengaluru, Koramangala" },
                    isExpanded = expandedSection == EditProfileSection.LOCATION,
                    onToggleExpand = {
                        expandedSection = if (expandedSection == EditProfileSection.LOCATION) null else EditProfileSection.LOCATION
                    },
                    scheme = scheme
                ) {
                    val locations = listOf("Bengaluru, Koramangala", "Bengaluru, Indiranagar", "Mumbai, Bandra", "Delhi, NCR")
                    ODSColumn(gap = ODSVariables.spacingComponent3) {
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            gap = ODSVariables.spacingComponent3
                        ) {
                            locations.forEach { loc ->
                                OptionChip(
                                    text = loc,
                                    isSelected = uiState.editLocation.equals(loc, ignoreCase = true),
                                    onClick = { viewModel.onEditLocationChange(loc) },
                                    scheme = scheme
                                )
                            }
                        }
                        // Custom location field
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingLayout1,
                                vertical = ODSVariables.spacingComponent4
                            ),
                            cornerRadius = ODSCorners(all = 14.dp),
                            border = ODSBorder(
                                width = ODSVariables.strokes1,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                        ) {
                            BasicTextField(
                                value = uiState.editLocation,
                                onValueChange = { viewModel.onEditLocationChange(it) },
                                textStyle = TextStyle(
                                    fontFamily = funnelSansFontFamily,
                                    fontSize = 15.sp,
                                    color = scheme.basicTextDominant.getColor()
                                ),
                                cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            // Bottom scroll spacing
            item {
                Spacer(modifier = Modifier.height(ODSVariables.spacingComponent6))
            }
        }

        // ── 2. Sticky Bottom CTA ─────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = ODSVariables.spacingLayout1,
                    vertical = ODSVariables.spacingComponent3
                ),
            gap = ODSVariables.spacingComponent3,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // "Save Profile Changes" Button
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            if (!uiState.isSavingProfile) {
                                viewModel.validateAndSaveProfile(
                                    onSuccess = {
                                        context.showODSToast("Profile changes saved successfully!")
                                        onSaved()
                                    },
                                    onError = { errorMsg ->
                                        context.showODSToast(errorMsg)
                                    }
                                )
                            }
                        }
                    ),
                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                background = listOf(
                    ODSColorModel(
                        hexColor = if (uiState.isSavingProfile) scheme.basicBackgroundSubtle else scheme.basicAccent
                    )
                ),
                effect = ODSEffect(
                    elevations = listOf(
                        ODSElevation(
                            x = 0,
                            y = 6,
                            blur = 14,
                            spread = 0,
                            color = HexColor(0x38D81B60),
                            type = ODSElevationType.DROP_SHADOW
                        )
                    )
                )
            ) {
                ODSText(
                    text = if (uiState.isSavingProfile) "Saving Changes..." else "Save Profile Changes",
                    style = ODSTextStyles.bodyMBold,
                    color = if (uiState.isSavingProfile) scheme.basicTextRecessive else scheme.basicTextOnAccent
                )
            }

            // Home Indicator Bar
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = ODSVariables.spacingComponent4,
                        bottom = ODSVariables.spacingComponent2
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                ODSBox(
                    modifier = Modifier.size(width = 134.dp, height = 4.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicStroke)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull)
                )
            }
        }
    }
}

/**
 * Collapsible summary card styled identically to CompletedStepSummaryCard in onboarding.
 *
 * In collapsed state:
 * Shows a compact row: Label • Value • Edit Pencil icon.
 *
 * In expanded state:
 * Shows the accent border, a Done button to collapse, and reveals the inline editor.
 */
@Composable
private fun CollapsibleProfileSectionCard(
    label: String,
    value: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier,
    hasError: Boolean = false,
    errorMessage: String? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ODSVariables.spacingLayout1)
    ) {
        if (!isExpanded) {
            // Collapsed Compact Summary Card (Exact Onboarding CompletedStepSummaryCard style)
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggleExpand
                    ),
                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(
                        ODSColorModel(
                            hexColor = if (hasError) scheme.functionalDestructiveStandard else scheme.basicStrokeSubtle
                        )
                    )
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSRow(
                    gap = ODSVariables.spacingComponent3,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    ODSText(
                        text = label,
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                    ODSText(
                        text = value.ifBlank { "Not set" },
                        style = ODSTextStyles.bodySBold,
                        color = if (value.isNotBlank()) scheme.basicTextDominant else scheme.basicTextRecessive
                    )
                }

                ODSBox(
                    modifier = Modifier.size(ODSVariables.sizingComponent8),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_edit_3,
                            contentDescription = "Edit $label"
                        ),
                        tint = scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent7)
                    )
                }
            }
        } else {
            // Expanded Editor Card
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(
                        ODSColorModel(
                            hexColor = if (hasError) scheme.functionalDestructiveStandard else scheme.basicAccent
                        )
                    )
                ),
                gap = ODSVariables.spacingComponent4,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                effect = ODSEffect(
                    elevations = listOf(
                        ODSElevation(
                            x = 0, y = 4, blur = 12, spread = 0,
                            color = HexColor(0x14000000),
                            type = ODSElevationType.DROP_SHADOW
                        )
                    )
                )
            ) {
                // Header Row of expanded card with "Done" action
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ODSRow(
                        gap = ODSVariables.spacingComponent3,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSText(
                            text = label.uppercase(),
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicAccent
                        )
                        if (hasError && errorMessage != null) {
                            ODSText(
                                text = errorMessage,
                                style = ODSTextStyles.microcopyRegular,
                                color = scheme.functionalDestructiveStandard
                            )
                        }
                    }

                    // Collapse / Done Button
                    ODSRow(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onToggleExpand
                        ),
                        padding = ODSPadding(horizontal = 10.dp, vertical = 4.dp),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                        verticalAlignment = Alignment.CenterVertically,
                        gap = 4.dp
                    ) {
                        ODSText(
                            text = "Done",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicTextDominant
                        )
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_check,
                                contentDescription = "Done"
                            ),
                            tint = scheme.basicTextDominant.getColor(),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Editor Content
                content()
            }
        }
    }
}

/**
 * Clean selectable chip for editing profile options.
 */
@Composable
private fun OptionChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    scheme: ODSTheme
) {
    ODSBox(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingComponent5,
            vertical = ODSVariables.spacingComponent3
        ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        border = ODSBorder(
            width = if (isSelected) ODSVariables.strokes2 else ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle))
        ),
        background = listOf(
            ODSColorModel(hexColor = if (isSelected) scheme.basicAccentSecondary else scheme.basicBackgroundSubtle)
        )
    ) {
        ODSText(
            text = text,
            style = if (isSelected) ODSTextStyles.bodySBold else ODSTextStyles.bodySRegular,
            color = if (isSelected) scheme.basicTextDominant else scheme.basicTextRecessive
        )
    }
}

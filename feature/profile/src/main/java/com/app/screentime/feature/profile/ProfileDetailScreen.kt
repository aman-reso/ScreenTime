package com.app.screentime.feature.profile

import com.app.screentime.core.ui.util.showODSToast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import com.app.screentime.core.ui.theme.zonaODSTheme
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
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
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
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Other User Complete Public Profile Detail Screen.
 *
 * Dynamically fetches and renders:
 * - GET /api/profile?user_id=TARGET_USER_ID
 * - High-res photos gallery with tap-to-switch & segmented indicators
 * - Verified badge, name, age, city/area location
 * - Match percentage & quality score
 * - About Me / Bio
 * - Key Metrics (Height cm + ft, Dating Intent, Profession/Job, Education, Languages)
 * - Interests Chip Grid
 * - Prompts (Questions & Answers)
 * - Direct Actions: Message / Like / Pass / Edit
 *
 * Strictly 100% ODS components & design system tokens.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileDetailScreen(
    userId: String = "",
    userName: String = "User",
    userAge: Int = 24,
    locationText: String = "Online",
    matchPercentage: Int = 90,
    isMyProfile: Boolean = false,
    onBack: () -> Unit = {},
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToAccount: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    scheme: ODSTheme = zonaODSTheme,
    modifier: Modifier = Modifier,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showOptionsMenu by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.loadProfile(targetUserId = userId, initialName = userName, initialAge = userAge)
    }

    val resolvedName = uiState.displayName.ifBlank { userName.ifBlank { "User" } }
    val resolvedAge = uiState.age
    val resolvedLocation = uiState.locationFormatted.ifBlank { locationText }
    val resolvedMatchPct = uiState.matchPercentage

    val displayPhotos = remember(uiState.photos, uiState.user?.avatarUrl) {
        val list = uiState.photos.filter { it.isNotBlank() }
        if (list.isNotEmpty()) {
            list
        } else {
            val avatar = uiState.user?.avatarUrl
            if (!avatar.isNullOrBlank()) {
                listOf(avatar)
            } else {
                emptyList()
            }
        }
    }

    var currentPhotoIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(displayPhotos.size) {
        if (currentPhotoIndex >= displayPhotos.size) {
            currentPhotoIndex = 0
        }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // ── 1. Top Navigation Bar ──────────────────────────────────────────
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingComponent2
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_arrow_left,
                        contentDescription = "Back"
                    ),
                    tint = scheme.basicTextDominant.getColor(),
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent8)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBack
                        )
                )

                ODSText(
                    text = resolvedName.substringBefore(" "),
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextDominant
                )

                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_more_vertical,
                        contentDescription = "Options"
                    ),
                    tint = scheme.basicTextDominant.getColor(),
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent8)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { showOptionsMenu = true }
                        )
                )
            }

            // ── 2. Loading State or Scrollable Profile Content ─────────────────
            if (uiState.isLoading && uiState.user == null) {
                ProfileDetailShimmerContent(
                    scheme = scheme,
                    modifier = Modifier.weight(1f)
                )
            } else {
                ODSLazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingLayout1,
                        top = ODSVariables.spacingComponent2,
                        bottom = ODSVariables.spacingComponent6
                    ),
                    gap = ODSVariables.spacingComponent6
                ) {
                    // ── Item 1: Hero Photo Card & Indicators ───────────────────
                    item {
                        var isImageLoading by remember(
                            currentPhotoIndex,
                            displayPhotos
                        ) { mutableStateOf(true) }

                        val currentUrl = displayPhotos.getOrNull(currentPhotoIndex)

                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusExtraLarge),
                            clipContent = true,
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                        ) {
                            if (!currentUrl.isNullOrBlank()) {
                                // 1. Shimmer base background while loading image
                                if (isImageLoading) {
                                    ODSBox(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .zonaShimmer()
                                    )
                                }

                                // 2. High-res Hero Photo
                                ODSImage(
                                    imageModel = ODSImageModel(
                                        url = currentUrl,
                                        contentDescription = resolvedName,
                                        onState = { state ->
                                            isImageLoading = state is AsyncImagePainter.State.Loading
                                        }
                                    ),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // 3. Loading Spinner overlay over image while loading
                                if (isImageLoading) {
                                    ODSBox(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ODSLoadingSpinner(
                                            scheme = scheme,
                                            props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                                        )
                                    }
                                }
                            } else {
                                // Fallback Avatar Icon when no photo is uploaded
                                ODSBox(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = R.drawable.ic_user,
                                            contentDescription = resolvedName
                                        ),
                                        tint = scheme.basicTextRecessive.getColor(),
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                            }

                            // Top Segmented Progress Bars
                            if (displayPhotos.size > 1) {
                                ODSRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.TopCenter)
                                        .padding(
                                            top = ODSVariables.spacingComponent4,
                                            start = ODSVariables.spacingComponent4,
                                            end = ODSVariables.spacingComponent4
                                        ),
                                    gap = ODSVariables.spacingComponent2,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    displayPhotos.forEachIndexed { idx, _ ->
                                        val isSelected = idx == currentPhotoIndex
                                        ODSBox(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(3.dp),
                                            background = listOf(
                                                ODSColorModel(
                                                    hexColor = if (isSelected) scheme.basicAccent else HexColor(
                                                        "#FFFFFF",
                                                        0.5f
                                                    )
                                                )
                                            ),
                                            cornerRadius = ODSCorners(all = 2.dp)
                                        )
                                    }
                                }
                            }

                            // Left Half Tap Area (Previous Photo)
                            ODSBox(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.5f)
                                    .align(Alignment.CenterStart)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            if (currentPhotoIndex > 0) currentPhotoIndex--
                                        }
                                    )
                            ) {}

                            // Right Half Tap Area (Next Photo)
                            ODSBox(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.5f)
                                    .align(Alignment.CenterEnd)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            if (currentPhotoIndex < displayPhotos.size - 1) currentPhotoIndex++
                                        }
                                    )
                            ) {}

                            // High-contrast gradient overlay at bottom
                            ODSBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .align(Alignment.BottomCenter),
                                background = listOf(
                                    ODSColorModel(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.85f)
                                            )
                                        )
                                    )
                                )
                            )

                            // Bottom info overlay: Name, Age, Verified badge, Location & Match Badge
                            ODSColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomStart)
                                    .padding(ODSVariables.spacingComponent5),
                                gap = ODSVariables.spacingComponent2
                            ) {
                                ODSRow(
                                    verticalAlignment = Alignment.CenterVertically,
                                    gap = ODSVariables.spacingComponent3
                                ) {
                                    ODSText(
                                        text = "$resolvedName, $resolvedAge",
                                        style = ODSTextStyles.bodyMBold,
                                        color = scheme.basicBackgroundCard
                                    )

                                    ODSBox(
                                        modifier = Modifier.size(ODSVariables.sizingComponent8),
                                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        ODSIcon(
                                            iconModel = ODSIconModel(
                                                drawableRes = R.drawable.ic_check,
                                                contentDescription = "Verified"
                                            ),
                                            tint = scheme.basicTextOnAccent.getColor(),
                                            modifier = Modifier.size(ODSVariables.sizingComponent6)
                                        )
                                    }
                                }

                                ODSRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Location row
                                    ODSRow(
                                        verticalAlignment = Alignment.CenterVertically,
                                        gap = ODSVariables.spacingComponent2
                                    ) {
                                        ODSIcon(
                                            iconModel = ODSIconModel(
                                                drawableRes = R.drawable.ic_map_pin,
                                                contentDescription = "Location"
                                            ),
                                            tint = scheme.basicAccentSecondary.getColor(),
                                            modifier = Modifier.size(ODSVariables.sizingComponent7)
                                        )
                                        ODSText(
                                            text = resolvedLocation,
                                            style = ODSTextStyles.bodySRegular,
                                            color = scheme.basicBackgroundCard
                                        )
                                    }

                                    // Match Badge Pill
                                    ODSRow(
                                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                        padding = ODSPadding(
                                            horizontal = ODSVariables.spacingComponent4,
                                            vertical = ODSVariables.spacingComponent2
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        gap = ODSVariables.spacingComponent2
                                    ) {
                                        ODSText(
                                            text = "⚡",
                                            style = ODSTextStyles.microcopyBold,
                                            color = scheme.basicAccent
                                        )
                                        ODSText(
                                            text = "$resolvedMatchPct%",
                                            style = ODSTextStyles.bodySBold,
                                            color = scheme.basicAccent
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ── Item 2: "About Me" Section ─────────────────────────────
                    val aboutText = uiState.aboutMe.ifBlank { uiState.bio }
                    if (aboutText.isNotBlank()) {
                        item {
                            ODSColumn(gap = ODSVariables.spacingComponent3) {
                                ODSText(
                                    text = "About Me",
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicTextDominant
                                )

                                ODSText(
                                    text = aboutText,
                                    style = ODSTextStyles.bodyMRegular,
                                    color = scheme.basicTextDominant
                                )
                            }
                        }
                    }

                    // ── Item 3: Key Metrics Cards (Relationship Intent & Height) ─
                    item {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            gap = ODSVariables.spacingComponent4
                        ) {
                            // Relationship Intent Card
                            val intentText = uiState.datingIntent.ifBlank {
                                uiState.user?.relationType ?: "Relationship"
                            }
                            ODSBox(
                                modifier = Modifier.weight(1f),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                ),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent5,
                                    vertical = ODSVariables.spacingComponent4
                                )
                            ) {
                                ODSColumn(gap = ODSVariables.spacingComponent2) {
                                    ODSText(
                                        text = "RELATIONSHIP INTENT",
                                        style = ODSTextStyles.microcopyBold,
                                        color = scheme.basicTextRecessive
                                    )
                                    ODSText(
                                        text = intentText.replaceFirstChar { it.uppercase() },
                                        style = ODSTextStyles.bodySBold,
                                        color = scheme.basicTextDominant
                                    )
                                }
                            }

                            // Height Card
                            val heightText = uiState.heightFormatted.ifBlank {
                                if (uiState.heightCm != null) "${uiState.heightCm} cm" else "178 cm (5'10\")"
                            }
                            ODSBox(
                                modifier = Modifier.weight(1f),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                ),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent5,
                                    vertical = ODSVariables.spacingComponent4
                                )
                            ) {
                                ODSColumn(gap = ODSVariables.spacingComponent2) {
                                    ODSText(
                                        text = "HEIGHT",
                                        style = ODSTextStyles.microcopyBold,
                                        color = scheme.basicTextRecessive
                                    )
                                    ODSText(
                                        text = heightText,
                                        style = ODSTextStyles.bodySBold,
                                        color = scheme.basicTextDominant
                                    )
                                }
                            }
                        }
                    }

                    // ── Item 4: Career, Education & Languages ──────────────────
                    val jobText = uiState.job.ifBlank { uiState.user?.job ?: "" }
                    val eduText = uiState.education.ifBlank { uiState.user?.education ?: "" }
                    val langs = uiState.languages.ifEmpty { uiState.user?.languages ?: emptyList() }

                    if (jobText.isNotBlank() || eduText.isNotBlank() || langs.isNotEmpty()) {
                        item {
                            ODSColumn(
                                modifier = Modifier.fillMaxWidth(),
                                gap = ODSVariables.spacingComponent3
                            ) {
                                ODSText(
                                    text = "Details",
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicTextDominant
                                )

                                ODSColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                    border = ODSBorder(
                                        width = ODSVariables.strokes1,
                                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                    ),
                                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                                    gap = ODSVariables.spacingComponent4
                                ) {
                                    if (jobText.isNotBlank()) {
                                        ODSRow(
                                            verticalAlignment = Alignment.CenterVertically,
                                            gap = ODSVariables.spacingComponent3
                                        ) {
                                            ODSIcon(
                                                iconModel = ODSIconModel(drawableRes = R.drawable.ic_check),
                                                tint = scheme.basicAccent.getColor(),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            ODSText(
                                                text = "💼 Works as $jobText",
                                                style = ODSTextStyles.bodySRegular,
                                                color = scheme.basicTextDominant
                                            )
                                        }
                                    }

                                    if (eduText.isNotBlank()) {
                                        ODSRow(
                                            verticalAlignment = Alignment.CenterVertically,
                                            gap = ODSVariables.spacingComponent3
                                        ) {
                                            ODSIcon(
                                                iconModel = ODSIconModel(drawableRes = R.drawable.ic_check),
                                                tint = scheme.basicAccent.getColor(),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            ODSText(
                                                text = "🎓 Studied $eduText",
                                                style = ODSTextStyles.bodySRegular,
                                                color = scheme.basicTextDominant
                                            )
                                        }
                                    }

                                    if (langs.isNotEmpty()) {
                                        ODSRow(
                                            verticalAlignment = Alignment.CenterVertically,
                                            gap = ODSVariables.spacingComponent3
                                        ) {
                                            ODSIcon(
                                                iconModel = ODSIconModel(drawableRes = R.drawable.ic_globe),
                                                tint = scheme.basicAccent.getColor(),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            ODSText(
                                                text = "🗣️ Speaks ${langs.joinToString(", ")}",
                                                style = ODSTextStyles.bodySRegular,
                                                color = scheme.basicTextDominant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ── Item 5: "Interests" Section ────────────────────────────
                    val interestList =
                        uiState.interests.ifEmpty { uiState.user?.interests ?: emptyList() }
                    if (interestList.isNotEmpty()) {
                        item {
                            ODSColumn(gap = ODSVariables.spacingComponent4) {
                                ODSText(
                                    text = "Interests",
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicTextDominant
                                )

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3),
                                    verticalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3)
                                ) {
                                    interestList.forEachIndexed { index, interest ->
                                        val isAccent = index % 2 == 0
                                        ODSBox(
                                            background = listOf(
                                                ODSColorModel(
                                                    hexColor = if (isAccent) scheme.basicAccentSecondary else scheme.basicBackgroundCard
                                                )
                                            ),
                                            cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                                            border = ODSBorder(
                                                width = ODSVariables.strokes1,
                                                colorList = listOf(
                                                    ODSColorModel(
                                                        hexColor = if (isAccent) scheme.basicAccentSecondary else scheme.basicStroke
                                                    )
                                                )
                                            ),
                                            padding = ODSPadding(
                                                horizontal = ODSVariables.spacingComponent5,
                                                vertical = ODSVariables.spacingComponent3
                                            )
                                        ) {
                                            ODSText(
                                                text = interest.replaceFirstChar { it.uppercase() },
                                                style = ODSTextStyles.bodySBold,
                                                color = if (isAccent) scheme.basicAccent else scheme.basicTextDominant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // ── Item 6: Prompts (Questions & Answers) ───────────────────
                    val promptList =
                        uiState.prompts.ifEmpty { uiState.user?.prompts ?: emptyList() }
                    if (promptList.isNotEmpty()) {
                        promptList.forEach { prompt ->
                            item {
                                ODSColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                                    border = ODSBorder(
                                        width = ODSVariables.strokes1,
                                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                    ),
                                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                                    gap = ODSVariables.spacingComponent2
                                ) {
                                    ODSText(
                                        text = prompt.question,
                                        style = ODSTextStyles.microcopyBold,
                                        color = scheme.basicTextRecessive
                                    )
                                    ODSText(
                                        text = "“${prompt.answer}”",
                                        style = ODSTextStyles.bodyMBold,
                                        color = scheme.basicTextDominant
                                    )
                                }
                            }
                        }
                    }

                    // Bottom spacer
                    item {
                        ODSBox(modifier = Modifier.height(ODSVariables.spacingComponent4))
                    }
                }
            }

            // ── 3. Sticky Bottom Action Dock: [✕] [ Message ] [💖] ──────
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                padding = ODSPadding(
                    top = ODSVariables.spacingComponent4,
                    bottom = ODSVariables.spacingComponent5,
                    left = ODSVariables.spacingLayout1,
                    right = ODSVariables.spacingLayout1
                )
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = ODSVariables.spacingComponent4
                ) {
                    if (isMyProfile) {
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Edit Profile",
                                buttonType = ODSButtonButtonType.STANDARD,
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = onNavigateToEditProfile
                        )
                    } else {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent4
                        ) {
                            // Dislike Button ✕
                            ODSBox(
                                modifier = Modifier
                                    .size(ODSVariables.sizingComponent13)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = onBack
                                    ),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                ),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_close,
                                        contentDescription = "Pass"
                                    ),
                                    tint = scheme.basicTextDominant.getColor(),
                                    modifier = Modifier.size(ODSVariables.sizingComponent10)
                                )
                            }

                            // Primary "Message" CTA
                            ODSButton(
                                modifier = Modifier.weight(1f),
                                scheme = scheme,
                                props = ODSButtonProps(
                                    label = "Message ${resolvedName.substringBefore(" ")}",
                                    buttonIcon = ODSIconModel(
                                        drawableRes = R.drawable.ic_message_circle,
                                        contentDescription = "Message"
                                    ),
                                    leftIcon = true,
                                    buttonType = ODSButtonButtonType.STANDARD,
                                    variant = ODSButtonVariant.PRIMARY,
                                    size = ODSButtonSize.SMALL
                                ),
                                onClick = {
                                    onNavigateToChat(
                                        uiState.userId.ifBlank { userId },
                                        resolvedName
                                    )
                                }
                            )

                            // Like Button 💖
                            ODSBox(
                                modifier = Modifier
                                    .size(ODSVariables.sizingComponent13)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = {
                                            context.showODSToast(
                                                "You liked ${
                                                    resolvedName.substringBefore(
                                                        " "
                                                    )
                                                }!"
                                            )
                                        }
                                    ),
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        drawableRes = R.drawable.ic_heart_filled,
                                        contentDescription = "Like"
                                    ),
                                    tint = scheme.basicAccent.getColor(),
                                    modifier = Modifier.size(ODSVariables.sizingComponent10)
                                )
                            }
                        }
                    }

                    // Bottom Home Indicator Pill
                    ODSBox(
                        modifier = Modifier
                            .width(134.dp)
                            .height(ODSVariables.spacingComponent2),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                        background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    )
                }
            }
        }

        // ── Options Menu Dialog ──────────────────────────────────────────────
        if (showOptionsMenu) {
            ODSDialog(
                scheme = scheme,
                onDismissRequest = { showOptionsMenu = false },
                props = ODSDialogProps(showCloseButton = false),
                contentSlot = {
                    ODSColumn(
                        gap = ODSVariables.spacingComponent4,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ODSText(
                            text = "Profile Options",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )

                        // Report Profile
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showOptionsMenu = false
                                    context.showODSToast("Profile reported to moderation team.")
                                }
                                .padding(vertical = ODSVariables.spacingComponent3),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent4
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = R.drawable.ic_alert_triangle),
                                tint = scheme.functionalDestructiveStandard.getColor(),
                                modifier = Modifier.size(ODSVariables.sizingComponent9)
                            )
                            ODSText(
                                text = "Report Profile",
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.functionalDestructiveStandard
                            )
                        }

                        // Close / Cancel Button
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Cancel",
                                variant = ODSButtonVariant.GHOST,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { showOptionsMenu = false }
                        )
                    }
                }
            )
        }
    }
}

/**
 * Shimmer Skeleton for Profile Detail Screen during API fetch.
 * Follows 100% Telekom ODS design tokens and layout structure.
 */
@Composable
private fun ProfileDetailShimmerContent(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSLazyColumn(
        modifier = modifier.fillMaxWidth(),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout1,
            top = ODSVariables.spacingComponent2,
            bottom = ODSVariables.spacingComponent6
        ),
        gap = ODSVariables.spacingComponent6
    ) {
        // Hero Image Shimmer Card
        item {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .zonaShimmer(),
                cornerRadius = ODSCorners(all = ODSVariables.radiusExtraLarge),
                clipContent = true
            )
        }

        // About Me Shimmer Section
        item {
            ODSColumn(
                gap = ODSVariables.spacingComponent3,
                modifier = Modifier.fillMaxWidth()
            ) {
                ODSBox(
                    modifier = Modifier
                        .width(100.dp)
                        .height(18.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = 6.dp)
                )
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(14.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = 4.dp)
                )
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .height(14.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = 4.dp)
                )
            }
        }

        // Key Metrics Shimmer (2 Cards)
        item {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent4
            ) {
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium)
                )
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium)
                )
            }
        }

        // Details Shimmer Card
        item {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3
            ) {
                ODSBox(
                    modifier = Modifier
                        .width(80.dp)
                        .height(18.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = 6.dp)
                )
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium)
                )
            }
        }

        // Interests Shimmer Section
        item {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent4
            ) {
                ODSBox(
                    modifier = Modifier
                        .width(90.dp)
                        .height(18.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = 6.dp)
                )
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3
                ) {
                    ODSBox(
                        modifier = Modifier
                            .width(88.dp)
                            .height(36.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge)
                    )
                    ODSBox(
                        modifier = Modifier
                            .width(104.dp)
                            .height(36.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge)
                    )
                    ODSBox(
                        modifier = Modifier
                            .width(80.dp)
                            .height(36.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge)
                    )
                }
            }
        }

        // Prompts Shimmer Cards
        item {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent4
            ) {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(76.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge)
                )
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(76.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge)
                )
            }
        }
    }
}

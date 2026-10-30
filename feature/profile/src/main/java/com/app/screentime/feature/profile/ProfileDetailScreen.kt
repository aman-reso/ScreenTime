package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R as ConfigR
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.core.ui.util.showODSToast
import com.app.screentime.feature.profile.components.ProfileAboutMeSection
import com.app.screentime.feature.profile.components.ProfileDetailActionRow
import com.app.screentime.feature.profile.components.ProfileDetailHeroSection
import com.app.screentime.feature.profile.components.ProfileEssentialsSection
import com.app.screentime.feature.profile.components.ProfileInterestsSection
import com.app.screentime.feature.profile.components.ProfileLookingForSection
import com.app.screentime.feature.profile.components.ProfilePromptCard
import com.app.screentime.feature.profile.components.ProfileSafetyQuickActionsSection
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Other User Complete Public Profile Detail Screen.
 *
 * Decomposed into clean, isolated components:
 * - ProfileDetailHeroSection (540dp height, photo carousel, top 3-dots, bottom-left chat button)
 * - ProfileLookingForSection
 * - ProfileAboutMeSection
 * - ProfileEssentialsSection
 * - ProfileInterestsSection
 * - ProfilePromptCard
 * - ProfileDetailActionRow (Bottom Pass & Like buttons)
 * - ProfileMoreOptionsBottomSheet (Block & Report options via 3-dots)
 * - Confirm Dialogs (Block & Report)
 * - Shimmer Skeleton Loading State
 *
 * Strictly 100% ODS components & design system tokens.
 */
@Composable
fun ProfileDetailScreen(
    userId: String = "",
    userName: String = "User",
    userAge: Int = 24,
    isMyProfile: Boolean = false,
    onBack: () -> Unit = {},
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToEditProfile: () -> Unit = {},
    scheme: ODSTheme = zonaODSTheme,
    modifier: Modifier = Modifier,
    viewModel: ProfileDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showBlockConfirmDialog by remember { mutableStateOf(false) }
    var showReportConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.loadProfile(targetUserId = userId, initialName = userName, initialAge = userAge)
    }

    val profile = uiState.profile
    val distinctPhotos = remember(profile.photos) {
        profile.photos.filter { it.isNotBlank() }.distinct()
    }
    var currentPhotoIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(distinctPhotos.size) {
        if (currentPhotoIndex >= distinctPhotos.size) {
            currentPhotoIndex = 0
        }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {


        ODSColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingComponent3
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = ODSVariables.spacingComponent2
                ) {
                    ODSText(
                        text = profile.headerTitle,
                        style = ODSTextStyles.titleS,
                        color = scheme.basicText
                    )
                }

                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_close,
                        contentDescription = "Close"
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
            }

            // ── Content or Shimmer ─────────────────────────────────────
            if (uiState.isLoading && distinctPhotos.isEmpty()) {
                ProfileDetailShimmerContent(
                    scheme = scheme,
                    modifier = Modifier.weight(1f)
                )
            } else {
                ODSLazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3
                ) {
                    item {
                        ProfileDetailHeroSection(
                            photos = distinctPhotos,
                            currentPhotoIndex = currentPhotoIndex,
                            displayName = profile.displayName,
                            isMyProfile = isMyProfile,
                            scheme = scheme,
                            onPhotoIndexChange = { currentPhotoIndex = it },
                            onChatClick = {
                                val targetId = profile.userId.ifBlank { userId }
                                val targetName = profile.displayName.ifBlank { userName }
                                onNavigateToChat(targetId, targetName)
                            }
                        )
                    }

                    if (!profile.lookingForText.isNullOrBlank()) {
                        item {
                            ProfileLookingForSection(
                                lookingForText = profile.lookingForText,
                                scheme = scheme
                            )
                        }
                    }

                    if (!profile.aboutMeText.isNullOrBlank()) {
                        item {
                            ProfileAboutMeSection(
                                aboutMeText = profile.aboutMeText,
                                scheme = scheme
                            )
                        }
                    }

                    if (profile.essentialItems.isNotEmpty() || profile.lifestyleItems.isNotEmpty()) {
                        item {
                            ProfileEssentialsSection(
                                essentialItems = profile.essentialItems,
                                lifestyleItems = profile.lifestyleItems,
                                scheme = scheme
                            )
                        }
                    }

                    if (profile.interests.isNotEmpty()) {
                        item {
                            ProfileInterestsSection(
                                interests = profile.interests,
                                scheme = scheme
                            )
                        }
                    }

                    if (profile.prompts.isNotEmpty()) {
                        profile.prompts.forEach { prompt ->
                            item {
                                ProfilePromptCard(
                                    prompt = prompt,
                                    scheme = scheme
                                )
                            }
                        }
                    }

                    if (!isMyProfile) {
                        item {
                            ProfileSafetyQuickActionsSection(
                                userName = profile.displayName.substringBefore(" "),
                                scheme = scheme,
                                onBlockClick = { showBlockConfirmDialog = true },
                                onReportClick = { showReportConfirmDialog = true }
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(DSVariables.spacingComponent9))
                    }
                }
            }
        }

        if (!isMyProfile) {
            ProfileDetailActionRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd),
                isLiked = uiState.isLiked,
                scheme = scheme,
                onDislike = {
                    viewModel.dislikeProfile(onComplete = onBack)
                },
                onLike = {
                    viewModel.likeProfile { isMatch, _ ->
                        if (isMatch) {
                            context.showODSToast(
                                "It's a Match with ${profile.displayName.substringBefore(" ")}! 🎉"
                            )
                        } else {
                            context.showODSToast(
                                "You liked ${profile.displayName.substringBefore(" ")}!"
                            )
                        }
                    }
                }
            )
        }
        
        // ── Block Confirmation Dialog ─────────────────────────────────
        if (showBlockConfirmDialog) {
            val targetName = profile.displayName.substringBefore(" ")
            val blockSuccessToast = stringResource(ConfigR.string.profile_blocked_toast)
            ODSDialog(
                scheme = scheme,
                onDismissRequest = { showBlockConfirmDialog = false },
                props = ODSDialogProps(
                    showCloseButton = true,
                    title = stringResource(ConfigR.string.profile_block_title, targetName),
                    bodyText = stringResource(ConfigR.string.profile_block_body)
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
                                label = stringResource(ConfigR.string.profile_block_user),
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = {
                                showBlockConfirmDialog = false
                                viewModel.blockProfile {
                                    context.showODSToast(blockSuccessToast)
                                    onBack()
                                }
                            }
                        )

                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = stringResource(ConfigR.string.cancel),
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { showBlockConfirmDialog = false }
                        )
                    }
                }
            )
        }

        // ── Report Confirmation Dialog ────────────────────────────────
        if (showReportConfirmDialog) {
            val targetName = profile.displayName.substringBefore(" ")
            val reportSuccessToast = stringResource(ConfigR.string.profile_reported_toast)
            ODSDialog(
                scheme = scheme,
                onDismissRequest = { showReportConfirmDialog = false },
                props = ODSDialogProps(
                    showCloseButton = true,
                    title = stringResource(ConfigR.string.profile_report_title, targetName),
                    bodyText = stringResource(ConfigR.string.profile_report_body)
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
                                label = stringResource(ConfigR.string.profile_report_profile),
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = {
                                showReportConfirmDialog = false
                                viewModel.reportProfile {
                                    context.showODSToast(reportSuccessToast)
                                    onBack()
                                }
                            }
                        )

                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = stringResource(ConfigR.string.cancel),
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { showReportConfirmDialog = false }
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
            horizontal = 0.dp,
            top = 0.dp,
            bottom = ODSVariables.spacingComponent6
        ),
        gap = ODSVariables.spacingComponent4
    ) {
        // Hero Image Shimmer Card (0 horizontal padding, bottom rounded)
        item {
            ODSSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(540.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomEnd = 24.dp,
                            bottomStart = 24.dp
                        )
                    ),
                scheme = scheme,
                props = ODSSkeletonProps(variant = ODSSkeletonVariant.FULL)
            )
        }

        // Shimmer Card 1 ("Looking for")
        item {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ODSVariables.spacingLayout1)
            ) {
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSCardBasicProps(),
                    contentPadding = ODSPadding(all = ODSVariables.spacingLayout1),
                    contentSlot = {
                        ODSColumn(
                            gap = ODSVariables.spacingComponent3,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ODSSkeleton(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                scheme = scheme,
                                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                            )
                            ODSSkeleton(
                                modifier = Modifier
                                    .fillMaxWidth(0.92f)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                scheme = scheme,
                                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                            )
                        }
                    }
                )
            }
        }

        // Shimmer Card 2 ("About me")
        item {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ODSVariables.spacingLayout1)
            ) {
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSCardBasicProps(),
                    contentPadding = ODSPadding(all = ODSVariables.spacingLayout1),
                    contentSlot = {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = ODSVariables.spacingComponent3
                        ) {
                            ODSSkeleton(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                scheme = scheme,
                                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                            )
                            ODSSkeleton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .clip(RoundedCornerShape(ODSVariables.radiusMedium)),
                                scheme = scheme,
                                props = ODSSkeletonProps(variant = ODSSkeletonVariant.MEDIUM)
                            )
                        }
                    }
                )
            }
        }

        // Shimmer Card 3 ("Essentials")
        item {
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ODSVariables.spacingLayout1)
            ) {
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSCardBasicProps(),
                    contentPadding = ODSPadding(all = ODSVariables.spacingLayout1),
                    contentSlot = {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = ODSVariables.spacingComponent3
                        ) {
                            ODSSkeleton(
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                scheme = scheme,
                                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                            )
                            ODSSkeleton(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                scheme = scheme,
                                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                            )
                            ODSSkeleton(
                                modifier = Modifier
                                    .fillMaxWidth(0.45f)
                                    .height(16.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                scheme = scheme,
                                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL)
                            )
                        }
                    }
                )
            }
        }
    }
}

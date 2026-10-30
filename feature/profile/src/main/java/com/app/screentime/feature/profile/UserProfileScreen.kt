package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.config.R as ConfigR
import com.app.screentime.core.ui.components.GenericHeader
import com.app.screentime.core.ui.components.GenericHeaderProps
import com.app.screentime.core.ui.components.HeaderTitleAlignment
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.core.ui.util.showODSToast
import com.telekom.odsystem.R as ODSR
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
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * "My Account" Profile & Settings Screen conforming to Figma node-id 117-374 / UID 76d0fe6d.
 * 100% constructed with Telekom ODS components.
 */
@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onRateAppClick: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToLikedProfiles: () -> Unit = {},
    onNavigateToWhoLikedMe: () -> Unit = {},
    onNavigateToMatches: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToControlAccount: () -> Unit = {},
    onNavigateToPreferences: () -> Unit = {},
    onNavigateToProfileDetail: (String, String) -> Unit = { _, _ -> },
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }
    var showLanguageBottomSheet by remember { mutableStateOf(false) }

    val currentLanguageDisplay = remember(uiState.selectedLanguage) {
        DEFAULT_APP_LANGUAGES.find {
            it.languageCode.equals(uiState.selectedLanguage, ignoreCase = true)
        }?.englishName ?: uiState.selectedLanguage.ifBlank { "English" }
    }

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    val resolvedName = uiState.displayName.ifBlank { uiState.user?.name.orEmpty() }
    val resolvedEmail = uiState.email.ifBlank { uiState.user?.email.orEmpty() }
    val resolvedPhone = uiState.editPhone.ifBlank { uiState.user?.phone.orEmpty() }
    val resolvedEmailOrPhone = resolvedEmail.ifBlank { resolvedPhone }
    val resolvedPhoto = uiState.photoUrl.ifBlank { uiState.user?.avatarUrl.orEmpty() }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            GenericHeader(
                props = GenericHeaderProps(
                    title = "My Account",
                    showBackArrow = true,
                    onBackClick = onBackClick,
                    titleAlignment = HeaderTitleAlignment.CENTER,
                    showRightIcon = true,
                    rightIconRes = ODSR.drawable.ic_bell,
                    rightIconDescription = "Notifications",
                    onRightIconClick = onNavigateToNotifications
                ),
                scheme = scheme
            )

            ODSLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                padding = ODSPadding(
                    top = 16.dp,
                    horizontal = 20.dp
                ),
                gap = 24.dp
            ) {
                item {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 12.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ODSBox(
                            modifier = Modifier.size(88.dp),
                            cornerRadius = ODSCorners(all = 44.dp),
                            border = ODSBorder(
                                width = 2.dp,
                                colorList = listOf(
                                    ODSColorModel(
                                        hexColor = scheme.basicText
                                    )
                                )
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (resolvedPhoto.isNotBlank()) {
                                ODSImage(
                                    imageModel = ODSImageModel(
                                        url = resolvedPhoto,
                                        contentDescription = resolvedName
                                    ),
                                    width = 80.dp,
                                    height = 80.dp,
                                    cornerRadius = ODSCorners(all = 40.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                ODSBox(
                                    modifier = Modifier.size(80.dp),
                                    cornerRadius = ODSCorners(all = 40.dp),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = ODSR.drawable.ic_user,
                                            contentDescription = resolvedName
                                        ),
                                        tint = scheme.basicText.getColor(),
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }

                        // Name & Email
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = 4.dp,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ODSText(
                                modifier = Modifier.fillMaxWidth(),
                                text = resolvedName,
                                style = ODSTextStyle(
                                    fontFamily = ODSR.font.funnelsans_bold,
                                    fontSize = 20,
                                    lineHeight = 20
                                ),
                                color = scheme.basicText,
                                textAlign = TextAlign.Center
                            )

                            if (resolvedEmailOrPhone.isNotBlank()) {
                                ODSText(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = resolvedEmailOrPhone,
                                    style = ODSTextStyle(
                                        fontFamily = ODSR.font.funnelsans_medium,
                                        fontSize = 13,
                                        lineHeight = 13
                                    ),
                                    color = scheme.basicTextRecessive,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Edit Profile Underlined Link
                        ODSRow(
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onNavigateToEditProfile
                            ),
                            padding = ODSPadding(top = 4.dp, bottom = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSText(
                                text = "Edit Profile",
                                style = ODSTextStyle(
                                    fontFamily = ODSR.font.funnelsans_bold,
                                    fontSize = 14,
                                    lineHeight = 14
                                ),
                                color = scheme.basicAccent,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }

                // ── 2. Stats Summary Card (Matches, Likes, Dates) ────────────
                item {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        padding = ODSPadding(
                            top = 16.dp,
                            bottom = 16.dp,
                            left = 8.dp,
                            right = 8.dp
                        ),
                        cornerRadius = ODSCorners(all = 20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        // Matches
                        ODSColumn(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onNavigateToMatches
                                ),
                            gap = 4.dp,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ODSText(
                                text = (uiState.user?.matchesCount ?: 0).toString(),
                                style = ODSTextStyle(
                                    fontFamily = ODSR.font.funnelsans_bold,
                                    fontSize = 18,
                                    lineHeight = 18
                                ),
                                color = scheme.basicText
                            )
                            ODSText(
                                text = "Matches",
                                style = ODSTextStyle(
                                    fontFamily = ODSR.font.funnelsans_semibold,
                                    fontSize = 11,
                                    lineHeight = 11
                                ),
                                color = scheme.basicTextRecessive
                            )
                        }

                        // Vertical Divider Line
                        ODSBox(
                            modifier = Modifier
                                .height(24.dp)
                                .size(1.dp),
                            background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        )

                        // Likes
                        ODSColumn(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onNavigateToLikedProfiles
                                ),
                            gap = 4.dp,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ODSText(
                                text = (uiState.user?.likesCount ?: 0).toString(),
                                style = ODSTextStyle(
                                    fontFamily = ODSR.font.funnelsans_bold,
                                    fontSize = 18,
                                    lineHeight = 18
                                ),
                                color = scheme.basicText
                            )
                            ODSText(
                                text = "Likes",
                                style = ODSTextStyle(
                                    fontFamily = ODSR.font.funnelsans_semibold,
                                    fontSize = 11,
                                    lineHeight = 11
                                ),
                                color = scheme.basicTextRecessive
                            )
                        }

                        // Vertical Divider Line
                        ODSBox(
                            modifier = Modifier
                                .height(24.dp)
                                .size(1.dp),
                            background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        )

                        // Quality Score / Score
                        ODSColumn(
                            modifier = Modifier.weight(1f),
                            gap = 4.dp,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ODSText(
                                text = "${((uiState.user?.qualityScore ?: 0.0) * 100).toInt()}%",
                                style = ODSTextStyle(
                                    fontFamily = ODSR.font.funnelsans_bold,
                                    fontSize = 18,
                                    lineHeight = 18
                                ),
                                color = scheme.basicText

                            )
                            ODSText(
                                text = "Score",
                                style = ODSTextStyle(
                                    fontFamily = ODSR.font.funnelsans_semibold,
                                    fontSize = 11,
                                    lineHeight = 11
                                ),
                                color = scheme.basicTextRecessive

                            )
                        }
                    }
                }

                // ── 3. Account Menu Items List ─────────────────────────────────
                item {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Item 1: Personal Info
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_user,
                            title = "Personal Info",
                            onClick = onNavigateToEditProfile
                        )

                        // Item 2: Liked Profiles
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_heart,
                            title = "Profiles I Liked",
                            onClick = onNavigateToLikedProfiles
                        )

                        // Item 3: Who Liked Me
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_heart,
                            title = "Who Liked Me",
                            onClick = onNavigateToWhoLikedMe
                        )

                        // Item 4: Matches
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_zap,
                            title = "My Matches",
                            onClick = onNavigateToMatches
                        )

                        // Item 5: Preferences
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_sliders,
                            title = "Preferences",
                            onClick = onNavigateToPreferences
                        )

                        // Item 4: Privacy & Safety
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_shield,
                            title = "Privacy & Safety",
                            onClick = onNavigateToControlAccount
                        )

                        // Item 5: Notifications
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_bell,
                            title = "Notifications",
                            onClick = onNavigateToNotifications
                        )

                        // Item 6: Language
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_globe,
                            title = "Language",
                            badgeText = currentLanguageDisplay,
                            onClick = { showLanguageBottomSheet = true }
                        )

                        // Item 7: Help & Support
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_help_circle,
                            title = "Help & Support",
                            onClick = { context.showODSToast("Help & Support") }
                        )

                        // Item 8: Rate App
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.star_type_bold,
                            title = "Rate App",
                            onClick = onRateAppClick
                        )

                        // Item 9: Log Out
                        AccountMenuItemRow(
                            iconRes = ODSR.drawable.ic_log_out,
                            title = stringResource(ConfigR.string.account_log_out),
                            showChevron = false,
                            onClick = { showLogoutConfirmDialog = true }
                        )
                    }
                }
            }
        }

        // Language Bottom Sheet
        if (showLanguageBottomSheet) {
            AppLanguageBottomSheet(
                currentLanguage = uiState.selectedLanguage,
                onLanguageSelected = { newLanguageCode ->
                    viewModel.changeAppLanguage(newLanguageCode)
                    showLanguageBottomSheet = false
                },
                onDismiss = { showLanguageBottomSheet = false },
                scheme = scheme
            )
        }

        // Log Out Confirmation Dialog
        if (showLogoutConfirmDialog) {
            ODSDialog(
                scheme = scheme,
                onDismissRequest = { showLogoutConfirmDialog = false },
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                props = ODSDialogProps(
                    showCloseButton = true,
                    title = stringResource(ConfigR.string.account_logout_confirm_title),
                    bodyText = stringResource(ConfigR.string.account_logout_confirm_message)
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
                                label = stringResource(ConfigR.string.account_log_out),
                                buttonType = ODSButtonButtonType.STANDARD,
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = {
                                showLogoutConfirmDialog = false
                                onLogoutClick()
                            }
                        )

                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = stringResource(ConfigR.string.cancel),
                                buttonType = ODSButtonButtonType.STANDARD,
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { showLogoutConfirmDialog = false }
                        )
                    }
                }
            )
        }
    }
}

/**
 * Account Menu List Row Item (Figma node-id 117-374 / 10-1134).
 */
@Composable
private fun AccountMenuItemRow(
    iconRes: Int,
    title: String,
    badgeText: String? = null,
    showChevron: Boolean = true,
    scheme: ODSTheme = zonaODSTheme,
    onClick: () -> Unit
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start
    ) {
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
                .padding(vertical = 14.dp),
            gap = 12.dp,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSColumn(
                cornerRadius = ODSCorners(all = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                modifier = Modifier.size(36.dp)
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = iconRes),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(18.dp)
                )
            }

            // Title
            ODSRow(
                modifier = Modifier.weight(1f),
                gap = 8.dp,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = title,
                    style = ODSTextStyle(
                        fontFamily = ODSR.font.funnelsans_semibold,
                        fontSize = 15,
                        lineHeight = 15
                    ),
                    color = scheme.basicText,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            // Optional Badge Text
            if (!badgeText.isNullOrBlank()) {
                ODSBox(
                    padding = ODSPadding(horizontal = 8.dp, vertical = 4.dp),
                    cornerRadius = ODSCorners(all = 12.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                ) {
                    ODSText(
                        text = badgeText,
                        style = ODSTextStyle(
                            fontFamily = ODSR.font.funnelsans_semibold,
                            fontSize = 12,
                            lineHeight = 12
                        ),
                        color = scheme.basicTextRecessive
                    )
                }
            }

            // Trailing Chevron
            if (showChevron) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = ODSR.drawable.ic_chevron_right),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Item Bottom Divider Line
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            background = listOf(ODSColorModel(hexColor = HexColor("#E5E7EB", 1.0f)))
        )
    }
}

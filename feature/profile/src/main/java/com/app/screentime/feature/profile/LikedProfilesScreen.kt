package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.ui.components.GenericHeader
import com.app.screentime.core.ui.components.GenericHeaderProps
import com.app.screentime.core.ui.components.HeaderTitleAlignment
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
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
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Screen displaying the list of profiles liked/favorited by the current user.
 * Built using ODSCardBasic (12dp padding) with avatar on left, name & location in middle, and chat icon at right end.
 */
@Composable
fun LikedProfilesScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    showHeader: Boolean = true,
    onBack: () -> Unit = {},
    onNavigateToProfile: (String, String) -> Unit = { _, _ -> },
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToDiscover: () -> Unit = {},
    viewModel: LikedProfilesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLikedProfiles()
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // Header
            if (showHeader) {
                GenericHeader(
                    props = GenericHeaderProps(
                        title = "Liked Profiles",
                        showBackArrow = true,
                        onBackClick = onBack,
                        titleAlignment = HeaderTitleAlignment.CENTER
                    ),
                    scheme = scheme
                )
            }

            if (uiState.isLoading) {
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            size = ODSLoadingSpinnerSize.SMALL,
                            labelText = "Loading"
                        )
                    )
                }
            } else if (uiState.profiles.isEmpty()) {
                // Empty state
                ODSColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = ODSVariables.spacingLayout2),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    gap = ODSVariables.spacingComponent4
                ) {
                    ODSBox(
                        modifier = Modifier.size(80.dp),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                        cornerRadius = ODSCorners(all = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_heart,
                                contentDescription = "Liked Profiles"
                            ),
                            tint = scheme.basicAccent.getColor(),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent2,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ODSText(
                            text = "No Liked Profiles Yet",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant,
                            textAlign = TextAlign.Center
                        )

                        ODSText(
                            text = "Profiles you like will appear here. Start discovering people and like their profiles!",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive,
                            textAlign = TextAlign.Center
                        )
                    }

                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Discover People",
                            buttonType = ODSButtonButtonType.STANDARD,
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onNavigateToDiscover
                    )
                }
            } else {
                // Liked Profiles List using ODSLazyColumn
                ODSLazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingLayout1,
                        top = ODSVariables.spacingComponent3,
                        bottom = ODSVariables.spacingComponent5
                    ),
                    gap = ODSVariables.spacingComponent3
                ) {
                    items(uiState.profiles.size) { index ->
                        val profile = uiState.profiles[index]
                        LikedProfileListItem(
                            profile = profile,
                            scheme = scheme,
                            onClick = { onNavigateToProfile(profile.id, profile.name) },
                            onChatClick = { onNavigateToChat(profile.id, profile.name) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LikedProfileListItem(
    profile: ModelProfile,
    scheme: ODSTheme,
    onClick: () -> Unit,
    onChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val resolvedAvatar = profile.avatarUrl.ifBlank { profile.coverUrl }
    val resolvedLocation = profile.location.ifBlank { profile.distance.ifBlank { "Nearby" } }

    ODSCardBasic(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(isHorizontal = true),
        contentPadding = ODSPadding(all = 10.dp),
        onClick = onClick,
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = 10.dp,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSBox(
                    modifier = Modifier.size(48.dp),
                    cornerRadius = ODSCorners(all = 24.dp),
                    clipContent = true,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    contentAlignment = Alignment.Center
                ) {
                    if (resolvedAvatar.isNotBlank()) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = resolvedAvatar,
                                contentDescription = profile.name
                            ),
                            modifier = Modifier.size(48.dp),
                            cornerRadius = ODSCorners(all = 24.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_user,
                                contentDescription = profile.name
                            ),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // 2. Middle: Name on top & Location below name
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = 4.dp,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = if (profile.age > 0) "${profile.name}, ${profile.age}" else profile.name,
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    ODSRow(
                        gap = 4.dp,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_map_pin,
                                contentDescription = "Location"
                            ),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(14.dp)
                        )

                        ODSText(
                            text = resolvedLocation,
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_message_circle,
                        contentDescription = "Chat"
                    ),
                    tint = scheme.basicTextRecessive.getColor(),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onChatClick
                        )
                )
            }
        }
    )
}

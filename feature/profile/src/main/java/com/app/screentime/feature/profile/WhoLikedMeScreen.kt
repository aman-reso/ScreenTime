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
 * "Who Liked Me" Screen.
 * Renders list of profiles who liked the current user using ODSCardBasic with 12dp padding.
 */
@Composable
fun WhoLikedMeScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    showHeader: Boolean = true,
    onBack: () -> Unit = {},
    onNavigateToProfile: (String, String) -> Unit = { _, _ -> },
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToDiscover: () -> Unit = {},
    viewModel: WhoLikedMeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfiles()
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
                        title = "Who Liked Me",
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
                                contentDescription = "Who Liked Me"
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
                            text = "No Likes Yet",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant,
                            textAlign = TextAlign.Center
                        )

                        ODSText(
                            text = "People who like your profile will show up here. Boost your visibility by completing your profile and swiping!",
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
                            onChatClick = { onNavigateToChat(profile.id, profile.name) })
                    }
                }
            }
        }
    }
}
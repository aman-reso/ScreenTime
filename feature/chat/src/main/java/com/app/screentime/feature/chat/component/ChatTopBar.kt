package com.app.screentime.feature.chat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Chat Top Bar Component (Matching media_1789923373115.png & Figma 10-1432).
 *
 * Rules:
 * 1. 100% ODS components.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Compact button sizing.
 * 4. Maximum text size 16sp (bodyMBold).
 * 5. Padding and dimensions using `ODSVariables`.
 */
@Composable
fun ChatTopBar(
    modelName: String,
    scheme: ODSTheme = zonaODSTheme,
    avatarUrl: String? = null,
    statusText: String = "Active now",
    isOnline: Boolean = true,
    onBackClick: () -> Unit = {},
    onAudioCallClick: () -> Unit = {},
    onVideoCallClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = ODSVariables.spacingComponent4,
                start = ODSVariables.spacingLayout1,
                end = ODSVariables.spacingLayout1
            )
    ) {
        val (backIcon, profileGroup, audioCallBtn, videoCallBtn) = createRefs()

        // 1. Back Arrow Icon
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.ic_arrow_left,
                contentDescription = "Back"
            ),
            tint = scheme.basicText.getColor(),
            modifier = Modifier
                .size(ODSVariables.sizingComponent8)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBackClick
                )
                .constrainAs(backIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )

        // 2. User Profile Group (Avatar + Name)
        val displayName = modelName.ifBlank { "User" }

        ODSRow(
            gap = ODSVariables.spacingComponent3,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onProfileClick
                )
                .constrainAs(profileGroup) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(backIcon.end, margin = ODSVariables.spacingComponent4)
                    end.linkTo(audioCallBtn.start, margin = ODSVariables.spacingComponent2)
                    width = Dimension.fillToConstraints
                }
        ) {
            ODSRow(
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                clipContent = true,
                width = ODSVariables.sizingComponent12, // 32.dp
                height = ODSVariables.sizingComponent12,
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                if (!avatarUrl.isNullOrBlank()) {
                    ODSImage(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        imageModel = ODSImageModel(
                            url = avatarUrl,
                            contentDescription = "avatar"
                        ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ODSBox(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = displayName.take(1).uppercase(),
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicAccent
                        )
                    }
                }
            }

            ODSColumn(
                gap = ODSVariables.spacingComponent1,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                ODSText(
                    text = displayName,
                    style = ODSTextStyles.bodyMBold, // 16sp
                    color = scheme.basicText,
                )
            }
        }


        // 4. Audio Call Button
        ODSBox(
            modifier = Modifier
                .size(ODSVariables.sizingComponent8) // 24.dp
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAudioCallClick
                )
                .constrainAs(audioCallBtn) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(videoCallBtn.start, margin = ODSVariables.spacingComponent3)
                },
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_phone,
                    contentDescription = "Voice Call"
                ),
                tint = scheme.basicText.getColor(),
                modifier = Modifier.size(ODSVariables.sizingComponent10) // 24.dp
            )
        }

        // 3. Video Call Button
        ODSBox(
            modifier = Modifier
                .size(ODSVariables.sizingComponent8) // 24.dp
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onVideoCallClick
                )
                .constrainAs(videoCallBtn) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.video,
                    contentDescription = "Video Call"
                ),
                tint = scheme.basicText.getColor(),
                modifier = Modifier.size(ODSVariables.sizingComponent10) // 24.dp
            )
        }
    }
}

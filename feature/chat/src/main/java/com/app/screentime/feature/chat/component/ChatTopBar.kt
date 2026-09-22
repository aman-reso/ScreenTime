package com.app.screentime.feature.chat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
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
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = ODSVariables.spacingComponent4,
                bottom = ODSVariables.spacingComponent4,
                start = ODSVariables.spacingLayout1,
                end = ODSVariables.spacingLayout1
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left group: Back Arrow + Avatar + Name & Status
        ODSRow(
            gap = ODSVariables.spacingComponent4,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Borderless Back Arrow Button (Ghost)
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    variant = ODSButtonVariant.GHOST,
                    size = ODSButtonSize.SMALL,
                    buttonIcon = ODSIconModel(
                        drawableRes = R.drawable.ic_arrow_left,
                        contentDescription = "Back"
                    )
                ),
                onClick = onBackClick
            )

            // User Info Row (Avatar + Name)
            ODSRow(
                gap = ODSVariables.spacingComponent4,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onProfileClick
                )
            ) {
                // Circular Avatar
                ODSRow(
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                    clipContent = true,
                    width = ODSVariables.sizingComponent13, // 40.dp
                    height = ODSVariables.sizingComponent13,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ODSImage(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        imageModel = ODSImageModel(
                            url = avatarUrl
                                ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80",
                            contentDescription = "avatar"
                        ),
                        contentScale = ContentScale.Crop
                    )
                }

                // Name & Active Status Indicator
                ODSColumn(
                    gap = ODSVariables.spacingComponent1,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSText(
                        text = if (modelName.isNotBlank()) modelName else "Jessica Maple",
                        style = ODSTextStyles.bodyMBold, // 16sp
                        color = scheme.basicText
                    )

                    ODSRow(
                        gap = ODSVariables.spacingComponent2,
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        if (isOnline) {
                            // Soft Pink / Green Status Dot
                            ODSBox(
                                modifier = Modifier.size(6.dp),
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                cornerRadius = ODSCorners(all = 3.dp)
                            )
                        }

                        ODSText(
                            text = statusText,
                            style = ODSTextStyles.microcopyRegular, // 12sp
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }

        // Right group: Voice Call & Video Call Rounded Cards
        ODSRow(
            gap = ODSVariables.spacingComponent3,
            horizontalAlignment = Alignment.End,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            // Audio Call Button
            ODSBox(
                modifier = Modifier
                    .size(ODSVariables.sizingComponent13) // 40.dp
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onAudioCallClick
                    ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_phone,
                        contentDescription = "Voice Call"
                    ),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent8)
                )
            }

            // Video Call Button
            ODSBox(
                modifier = Modifier
                    .size(ODSVariables.sizingComponent13) // 40.dp
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onVideoCallClick
                    ),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_video,
                        contentDescription = "Video Call"
                    ),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(ODSVariables.sizingComponent8)
                )
            }
        }
    }
}

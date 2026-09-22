package com.app.screentime.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.discover.HomeStoryItem
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Quick View Profile Dialog (Figma node-id 81-47).
 * Shown when tapping a story in the stories row.
 * Strictly adheres to Telekom ODS components (`ODSDialog`) and project design rules (GEMINI.md).
 */
@Composable
fun QuickViewProfileDialog(
    story: HomeStoryItem,
    onDismiss: () -> Unit,
    onNavigateToProfile: (String, String) -> Unit,
    onNavigateToChat: (String, String) -> Unit,
    onLike: (String) -> Unit,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSDialog(
        modifier = modifier,
        scheme = scheme,
        onDismissRequest = onDismiss,
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        props = ODSDialogProps(
            showCloseButton = false,
            showScrollbar = false,
            title = null,
            bodyText = null
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent4,
                padding = ODSPadding(all = ODSVariables.spacingComponent5),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // ── Top Bar: Quick View Indicator + Close Button ────────────────
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ODSRow(
                        gap = ODSVariables.spacingComponent3,
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ODSBox(
                            modifier = Modifier.size(ODSVariables.spacingComponent3),
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                            cornerRadius = ODSCorners(all = ODSVariables.spacingComponent2)
                        )
                        ODSText(
                            text = "QUICK VIEW",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicTextRecessive
                        )
                    }

                    // Close Button
                    ODSBox(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onDismiss
                            ),
                        background = listOf(ODSColorModel(hexColor = HexColor("#FFF0F3", 1.00f))),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_close,
                                contentDescription = "Close"
                            ),
                            tint = scheme.basicAccent.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // ── Photo Card with Active Now Badge ────────────────────────────
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    clipContent = true
                ) {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = story.avatarUrl,
                            contentDescription = story.name
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        contentScale = ContentScale.Crop
                    )

                    // Bottom Gradient Overlay + Active Now Badge
                    ODSColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart),
                        padding = ODSPadding(all = ODSVariables.spacingComponent4),
                        verticalAlignment = Alignment.Bottom,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Bottom,
                        background = listOf(
                            ODSColorModel(
                                gradient = ODSLinearGradientModel(
                                    colorStops = arrayOf(
                                        0.00f to HexColor("#000000", 0.00f),
                                        1.00f to HexColor("#000000", 0.60f)
                                    ),
                                    opacity = 1.00f,
                                    angleInDegrees = 180f
                                )
                            )
                        ),
                        height = 80.dp
                    ) {
                        ODSRow(
                            gap = ODSVariables.spacingComponent2,
                            padding = ODSPadding(
                                top = ODSVariables.spacingComponent2,
                                bottom = ODSVariables.spacingComponent2,
                                left = ODSVariables.spacingComponent3,
                                right = ODSVariables.spacingComponent3
                            ),
                            cornerRadius = ODSCorners(all = ODSVariables.spacingComponent4),
                            horizontalAlignment = Alignment.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            background = listOf(ODSColorModel(hexColor = HexColor("#4CAF50", 1.00f)))
                        ) {
                            ODSText(
                                text = "ACTIVE NOW",
                                style = ODSTextStyles.microcopyBold,
                                color = HexColor("#FFFFFF", 1.00f)
                            )
                        }
                    }
                }

                // ── Profile Details Section ─────────────────────────────────────
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent2,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Name & Verified Badge
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3,
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSText(
                            text = "${story.name}, 26",
                            style = ODSTextStyles.bodyMBold, // Rule #4: Max 16sp
                            color = scheme.basicTextDominant
                        )
                        ODSBox(
                            modifier = Modifier.size(18.dp),
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                            cornerRadius = ODSCorners(all = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_check,
                                    contentDescription = "Verified"
                                ),
                                tint = scheme.basicBackgroundCard.getColor(),
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }

                    // Location Row
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent2,
                        horizontalAlignment = Alignment.Start,
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
                            modifier = Modifier.weight(1f),
                            text = "3 km away • Manhattan, NY",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    // Bio Description
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Coffee lover, weekend hiker, and looking for someone to explore the city's best art galleries with ✨",
                        style = ODSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                // ── Action Buttons ──────────────────────────────────────────────
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    // Like & Match Button
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    val targetId =
                                        if (story.id == "my_match") "jessica_maple" else story.id
                                    onLike(targetId)
                                    onDismiss()
                                }
                            ),
                        gap = ODSVariables.spacingComponent3,
                        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        height = 46.dp
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_heart,
                                contentDescription = "Like"
                            ),
                            tint = scheme.basicBackgroundCard.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                        ODSText(
                            text = "Like & Match",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicBackgroundCard
                        )
                    }

                    // Message & Profile Buttons Row
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3,
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        // Message Button
                        ODSRow(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        onDismiss()
                                        val targetId =
                                            if (story.id == "my_match") "jessica_maple" else story.id
                                        onNavigateToChat(targetId, story.name)
                                    }
                                ),
                            gap = ODSVariables.spacingComponent2,
                            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                            border = ODSBorder(
                                width = ODSVariables.strokes2,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            height = 42.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_message_circle,
                                    contentDescription = "Message"
                                ),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(16.dp)
                            )
                            ODSText(
                                text = "Message",
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicTextRecessive
                            )
                        }

                        // Profile Button
                        ODSRow(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        onDismiss()
                                        val targetId =
                                            if (story.id == "my_match") "jessica_maple" else story.id
                                        onNavigateToProfile(targetId, story.name)
                                    }
                                ),
                            gap = ODSVariables.spacingComponent2,
                            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                            border = ODSBorder(
                                width = ODSVariables.strokes2,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            height = 42.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_user,
                                    contentDescription = "Profile"
                                ),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(16.dp)
                            )
                            ODSText(
                                text = "Profile",
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }
                }
            }
        }
    )
}


package com.app.screentime.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.ui.theme.zonaODSTheme
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
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * "For You" Feed Card Component (Figma node-id 14-44 and 14-47).
 * Displays profile photo with match badge, name, location, bio, and pass/chat/like actions.
 */
@Composable
fun HomeFeedCard(
    profile: ModelProfile,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onCardClick: () -> Unit = {},
    onDislike: () -> Unit = {},
    onChat: () -> Unit = {},
    onLike: () -> Unit = {}
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = ODSVariables.spacingLayout1,
                end = ODSVariables.spacingLayout1,
                bottom = ODSVariables.spacingComponent4
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCardClick
            ),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        border = ODSBorder(
            width = ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        clipContent = true
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            // ── Photo with Overlaid Match Badge (node-id 14-44) ────────────
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                val photoUrl = profile.coverUrl.ifBlank { profile.avatarUrl }
                ODSImage(
                    imageModel = ODSImageModel(
                        url = photoUrl.ifBlank {
                            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80"
                        },
                        contentDescription = profile.name
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                val matchPercentage =
                    profile.matchedPreferences.filter { it.isDigit() }.ifBlank { "94" }
                ODSRow(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(
                            start = ODSVariables.spacingLayout1,
                            top = ODSVariables.spacingLayout1
                        ),
                    padding = ODSPadding(
                        top = ODSVariables.spacingComponent2,
                        bottom = ODSVariables.spacingComponent2,
                        left = ODSVariables.spacingComponent3,
                        right = ODSVariables.spacingComponent3
                    ),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    gap = ODSVariables.spacingComponent1,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_zap_filled),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(12.dp)
                    )
                    ODSText(
                        text = "$matchPercentage% Match",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicAccent
                    )
                }
            }

            // ── Card Content (node-id 14-47) ────────────────────────────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingLayout1,
                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // Header Row: Name, Age + Verified Badge
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
                        ODSText(
                            text = "${profile.name}, ${profile.age}",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                        ODSRow(
                            cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                            width = 20.dp,
                            height = 20.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_check),
                                tint = scheme.basicBackground.getColor(),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                // Location Row: Pin + Distance • City
                ODSRow(
                    gap = ODSVariables.spacingComponent2,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_map_pin),
                        tint = scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(14.dp)
                    )
                    ODSText(
                        text = "${profile.distance} • ${profile.location}",
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                // Bio
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = profile.bio.ifBlank {
                        "Creative director with a love for indie film, late-night coffee, and spontaneous weekend trips."
                    },
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicText
                )

                // Actions Row: Pass (Dislike), Chat, and Like
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Pass / Dislike (44dp)
                    ODSRow(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDislike
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        border = ODSBorder(
                            width = ODSVariables.strokes2,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
                        width = 44.dp,
                        height = 44.dp
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_close),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Center: 44dp Prominent CTA (Magenta #D81B60 with drop shadow)
                    ODSRow(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onChat
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
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
                        ),
                        width = 44.dp,
                        height = 44.dp
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_message_circle),
                            tint = scheme.basicBackground.getColor(),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Right: 44dp Like (Heart)
                    ODSRow(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onLike
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                        ),
                        width = 44.dp,
                        height = 44.dp
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_heart_filled),
                            tint = scheme.basicAccent.getColor(),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

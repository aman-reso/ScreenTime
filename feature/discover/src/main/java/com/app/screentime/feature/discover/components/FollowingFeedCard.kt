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
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * "Following" Feed Card Component (Figma node-id 49-45).
 * Features 220dp image, verified badge, name, distance, and prominent 96x44 Chat pill button.
 */
@Composable
fun FollowingFeedCard(
    profile: ModelProfile,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onCardClick: () -> Unit = {},
    onChat: () -> Unit = {}
) {
    ODSColumn(
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
        clipContent = true,
        border = ODSBorder(
            width = ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        // ── 220dp Photo Header ──────────────────────────────────────────────
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.TopStart
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
        }

        // ── Bottom Content Row: Info & Chat CTA (Figma node-id 49-45) ───────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4,
            padding = ODSPadding(all = ODSVariables.spacingLayout1),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ODSColumn(
                modifier = Modifier.weight(1f),
                gap = 6.dp,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // Name & Age + Verified Badge Row
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ODSRow(
                        cornerRadius = ODSCorners(all = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        width = 20.dp,
                        height = 20.dp
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_check),
                            tint = HexColor("#FFFFFF").getColor(),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    ODSText(
                        modifier = Modifier.weight(1f, fill = false),
                        text = "${profile.name}, ${profile.age}",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                }

                // Distance & Location Row
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
                    val locationText = buildString {
                        if (profile.distance.isNotBlank()) {
                            append(profile.distance)
                            if (profile.location.isNotBlank()) {
                                append(" • ")
                            }
                        }
                        if (profile.location.isNotBlank()) {
                            append(profile.location)
                        } else if (profile.distance.isBlank()) {
                            append("Nearby")
                        }
                    }
                    ODSText(
                        text = locationText,
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }

            // Chat Action Button (Pill 96x44, #D81B60)
            ODSRow(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onChat
                ),
                cornerRadius = ODSCorners(all = 22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                width = 96.dp,
                height = 44.dp
            ) {
                ODSText(
                    text = "Chat",
                    style = ODSTextStyles.bodySBold,
                    color = HexColor("#FFFFFF", 1.00f)
                )
            }
        }
    }
}

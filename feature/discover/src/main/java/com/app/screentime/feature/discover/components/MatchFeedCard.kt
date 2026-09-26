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
import com.app.screentime.core.model.DiscoveryMatch
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

@Composable
fun MatchFeedCard(
    match: DiscoveryMatch,
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
            val photoUrl = match.primaryPhotoUrl
            if (photoUrl.isNotBlank()) {
                ODSImage(
                    imageModel = ODSImageModel(
                        url = photoUrl,
                        contentDescription = match.displayName
                    ),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = com.telekom.odsystem.R.drawable.ic_user,
                            contentDescription = match.displayName
                        ),
                        tint = scheme.basicTextRecessive.getColor(),
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            ODSRow(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(all = ODSVariables.spacingLayout1),
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
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_heart_filled),
                    tint = HexColor("#FFFFFF").getColor(),
                    modifier = Modifier.size(12.dp)
                )
                ODSText(
                    text = "Matched",
                    style = ODSTextStyles.microcopyBold,
                    color = HexColor("#FFFFFF", 1.0f)
                )
            }
        }

        // ── Bottom Content Row: Info & Chat CTA ─────────────────────────────
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
                // Name & Age Row
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ODSText(
                        modifier = Modifier.weight(1f, fill = false),
                        text = "${match.displayName}, ${match.age}",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                }

                // Location / City Row
                val locationText = buildString {
                    if (match.area.isNotBlank()) {
                        append(match.area)
                        if (match.city.isNotBlank()) append(", ")
                    }
                    if (match.city.isNotBlank()) {
                        append(match.city)
                    } else if (match.area.isBlank()) {
                        append("Nearby")
                    }
                }
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
                        text = locationText,
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                // Bio
                if (match.bio.isNotBlank()) {
                    ODSText(
                        text = match.bio,
                        style = ODSTextStyles.microcopyRegular,
                        color = scheme.basicTextRecessive,
                        maxLines = 2
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

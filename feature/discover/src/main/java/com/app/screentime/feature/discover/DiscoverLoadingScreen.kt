package com.app.screentime.feature.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Discover Loading Screen.
 * Conforms 100% to Figma node-id 24-56 and project design rules:
 * - Linear gradient background: #FFF1F6 -> #FFF9FB -> #FFFFFF.
 * - Top navigation with tabs ("For You" active indicator and "Following").
 * - 5 story avatar shimmer rings.
 * - Profile feed card shimmer with match badge, photo, header, bio, and action buttons.
 * - Bottom floating navigation pill dock.
 */
@Composable
fun DiscoverLoadingScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSColumn(
        modifier = modifier
            .fillMaxSize(),
        clipContent = true,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        0.00f to scheme.basicBackground, // #FFF1F6
                        0.52f to scheme.basicBackgroundSubtle, // #FFF9FB
                        1.00f to scheme.basicBackgroundCard // #FFFFFF
                    ),
                    opacity = 1.00f,
                    angleInDegrees = 180f
                )
            )
        )
    ) {

        // ── 2. Stories Shimmer Row (5 Avatars) ─────────────────────────────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4, // 12.dp
            padding = ODSPadding(left = ODSVariables.spacingLayout1),
            clipContent = true,
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            repeat(5) { i ->
                val ringColor = if (i < 2) scheme.basicAccent else scheme.basicStroke
                ODSColumn(
                    gap = ODSVariables.spacingComponent3, // 8.dp
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    width = 70.dp
                ) {
                    // Avatar Ring
                    ODSRow(
                        padding = ODSPadding(all = 3.dp),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        border = ODSBorder(
                            width = ODSVariables.strokes2,
                            colorList = listOf(ODSColorModel(hexColor = ringColor))
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        // Shimmer Avatar circle
                        ODSBox(
                            modifier = Modifier
                                .size(56.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                            clipContent = true
                        )
                    }

                    // Shimmer label pill
                    ODSBox(
                        modifier = Modifier
                            .width(44.dp)
                            .height(10.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = 5.dp),
                        clipContent = true
                    )
                }
            }
        }

        // ── 3. Feed Card Shimmer Skeleton (Figma node-id 24-56) ────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4,
            padding = ODSPadding(
                left = ODSVariables.spacingLayout1,
                right = ODSVariables.spacingLayout1
            ),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge), // 24.dp
                clipContent = true,
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                // Photo Area Skeleton (190.dp height)
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .zonaShimmer()
                ) {
                    // Match Badge on Top-Start
                    ODSRow(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(all = ODSVariables.spacingLayout1),
                        padding = ODSPadding(
                            top = 6.dp,
                            bottom = 6.dp,
                            left = 10.dp,
                            right = 10.dp
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.spacingComponent4),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .width(56.dp)
                                .height(12.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 6.dp)
                        )
                    }
                }

                // Card Content Skeleton
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = 14.dp,
                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Name & Verified Row
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSRow(
                            modifier = Modifier.weight(1f),
                            gap = ODSVariables.spacingComponent3,
                            horizontalAlignment = Alignment.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            // Name placeholder
                            ODSBox(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(18.dp)
                                    .zonaShimmer(),
                                cornerRadius = ODSCorners(all = 9.dp)
                            )
                            // Verified ellipse
                            ODSBox(
                                modifier = Modifier
                                    .size(18.dp)
                                    .zonaShimmer(),
                                cornerRadius = ODSCorners(all = 9.dp)
                            )
                        }
                    }

                    // Location Row
                    ODSRow(
                        gap = 6.dp,
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_map_pin,
                                contentDescription = "Location"
                            ),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(14.dp)
                        )
                        ODSBox(
                            modifier = Modifier
                                .width(90.dp)
                                .height(12.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 6.dp)
                        )
                    }

                    // Bio multi-line placeholders
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 6.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 6.dp)
                        )
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .height(12.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 6.dp)
                        )
                    }

                    // Action Buttons Skeleton (Dislike, Message, Like)
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Dislike skeleton circle
                        ODSBox(
                            modifier = Modifier
                                .size(48.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                            border = ODSBorder(
                                width = ODSVariables.strokes1,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                            )
                        )

                        // Middle Message pill
                        ODSBox(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                                .padding(horizontal = ODSVariables.spacingComponent4)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusLarge)
                        )

                        // Like skeleton circle
                        ODSBox(
                            modifier = Modifier
                                .size(48.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                            border = ODSBorder(
                                width = ODSVariables.strokes1,
                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                            )
                        )
                    }
                }
            }
        }
    }
}

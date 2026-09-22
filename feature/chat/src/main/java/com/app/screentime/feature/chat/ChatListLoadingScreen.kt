package com.app.screentime.feature.chat

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

fun Modifier.zonaShimmer(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "zonaShimmerChat")
    val translateAnim by transition.animateFloat(
        initialValue = -800f, targetValue = 800f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer_anim_chat"
    )

    val baseColor = ZonaColors.LoadingShimmer.getColor()
    val highlightColor = ZonaColors.loadingShimmerHighlight.getColor()
    val shimmerColors = listOf(
        baseColor,
        highlightColor,
        baseColor
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 350f, translateAnim - 350f),
            end = Offset(translateAnim + 350f, translateAnim + 350f)
        )
    )
}

/**
 * Chat List Inbox Loading / Shimmer Screen (Figma node-id 69-11).
 * Conforms 100% to Telekom ODS components, ZONA design rules, and Gemini project guidelines:
 * - 100% ODS components (ODSColumn, ODSRow, ODSBox).
 * - Linear gradient background (#FFF1F6 -> #FFF9FB -> #FFFFFF).
 * - Top header ("Inbox" + compose action button shimmer).
 * - "NEW MATCHES (4)" section with 5 avatar shimmer skeleton rings.
 * - Search bar shimmer box.
 * - 4 conversation/inbox item skeleton cards (avatar circle, name, time, and message shimmers).
 */
@Composable
fun ChatListLoadingScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        clipContent = true,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        background = listOf(
            ODSColorModel(
                gradient = ODSLinearGradientModel(
                    colorStops = arrayOf(
                        0.00f to scheme.basicBackground,
                        0.52f to scheme.basicBackgroundSubtle,
                        1.00f to scheme.basicBackgroundCard
                    ),
                    opacity = 1.00f,
                    angleInDegrees = 180f
                )
            )
        )
    ) {
        ODSColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            gap = ODSVariables.spacingComponent5,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // ── 1. Top Header Row ("Inbox" + Compose Shimmer) ────────────────
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(
                    top = ODSVariables.spacingComponent4,
                    bottom = ODSVariables.spacingComponent4,
                    left = ODSVariables.spacingLayout1,
                    right = ODSVariables.spacingLayout1
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ODSBox(
                    modifier = Modifier
                        .width(72.dp)
                        .height(20.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = 10.dp),
                    clipContent = true
                )
                ODSBox(
                    modifier = Modifier
                        .size(44.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    clipContent = true
                )
            }

            // ── 2. New Matches Section Shimmer ("NEW MATCHES (4)") ───────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                padding = ODSPadding(left = ODSVariables.spacingLayout1, right = ODSVariables.spacingLayout1),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                ODSBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(14.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = 7.dp),
                    clipContent = true
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent4,
                    clipContent = true,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    repeat(5) {
                        ODSColumn(
                            gap = ODSVariables.spacingComponent2,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            width = 68.dp
                        ) {
                            ODSBox(
                                modifier = Modifier
                                    .size(60.dp)
                                    .zonaShimmer(),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                clipContent = true
                            )
                            ODSBox(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(10.dp)
                                    .zonaShimmer(),
                                cornerRadius = ODSCorners(all = 5.dp),
                                clipContent = true
                            )
                        }
                    }
                }
            }

            // ── 3. Search Bar Shimmer Box ────────────────────────────────────
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(left = ODSVariables.spacingLayout1, right = ODSVariables.spacingLayout1),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSRow(
                    modifier = Modifier.weight(1f),
                    gap = ODSVariables.spacingComponent3,
                    padding = ODSPadding(all = 14.dp),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                    ),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                ) {
                    ODSBox(
                        modifier = Modifier
                            .size(18.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = 9.dp)
                    )
                    ODSBox(
                        modifier = Modifier
                            .width(160.dp)
                            .height(14.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = 7.dp)
                    )
                }
            }

            // ── 4. Conversation / Inbox Items Skeletons (4 items) ────────────
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ODSVariables.spacingLayout1),
                gap = ODSVariables.spacingComponent3,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                repeat(4) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = 14.dp,
                        padding = ODSPadding(all = 16.dp),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .size(52.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                            clipContent = true
                        )

                        ODSColumn(
                            modifier = Modifier.weight(1f),
                            gap = ODSVariables.spacingComponent3,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            ODSRow(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                ODSBox(
                                    modifier = Modifier
                                        .width(110.dp)
                                        .height(14.dp)
                                        .zonaShimmer(),
                                    cornerRadius = ODSCorners(all = 7.dp)
                                )
                                ODSBox(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(12.dp)
                                        .zonaShimmer(),
                                    cornerRadius = ODSCorners(all = 6.dp)
                                )
                            }
                            ODSBox(
                                modifier = Modifier
                                    .fillMaxWidth(0.75f)
                                    .height(12.dp)
                                    .zonaShimmer(),
                                cornerRadius = ODSCorners(all = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

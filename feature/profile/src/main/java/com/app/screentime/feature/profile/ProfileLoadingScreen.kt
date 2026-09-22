package com.app.screentime.feature.profile

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.composed
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
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
    val transition = rememberInfiniteTransition(label = "zonaShimmerProfile")
    val translateAnim by transition.animateFloat(
        initialValue = -800f, targetValue = 800f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer_anim_profile"
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
 * Profile Loading / Shimmer Screen (Figma node-id 69-128).
 * Conforms 100% to Telekom ODS components, ZONA design rules, and Gemini project guidelines:
 * - 100% ODS components (ODSColumn, ODSRow, ODSBox).
 * - Linear gradient background (#FFF1F6 -> #FFF9FB -> #FFFFFF).
 * - Top header (title and settings button shimmer).
 * - Profile header skeleton (Avatar circle, name & badge shimmers, subtitle shimmer).
 * - Match progress bar shimmer.
 * - Bio / Details card skeleton.
 * - 4 gallery photo skeletons.
 */
@Composable
fun ProfileLoadingScreen(
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
            // ── 1. Top Header Row ("Profile" title + Settings button shimmer) ──
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
                        .width(110.dp)
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

            // ── 2. Profile Header Skeleton (Avatar + Name + Subtitle) ────────
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent5,
                padding = ODSPadding(left = ODSVariables.spacingLayout1, right = ODSVariables.spacingLayout1),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSBox(
                    modifier = Modifier
                        .size(80.dp)
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
                        gap = ODSVariables.spacingComponent3,
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .width(130.dp)
                                .height(16.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 8.dp)
                        )
                        ODSBox(
                            modifier = Modifier
                                .width(36.dp)
                                .height(16.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 8.dp)
                        )
                    }
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(12.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = 6.dp)
                    )
                }
            }

            // ── 3. Match Progress Bar Shimmer ────────────────────────────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                padding = ODSPadding(left = ODSVariables.spacingLayout1, right = ODSVariables.spacingLayout1),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ODSBox(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .zonaShimmer(),
                        cornerRadius = ODSCorners(all = 6.dp)
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
                        .fillMaxWidth()
                        .height(8.dp)
                        .zonaShimmer(),
                    cornerRadius = ODSCorners(all = 4.dp)
                )
            }

            // ── 4. Card Container Skeleton (Bio / Details) ───────────────────
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(left = ODSVariables.spacingLayout1, right = ODSVariables.spacingLayout1),
                horizontalAlignment = Alignment.Start,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = ODSVariables.spacingComponent4,
                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                ) {
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
                                modifier = Modifier
                                    .size(24.dp)
                                    .zonaShimmer(),
                                cornerRadius = ODSCorners(all = 6.dp)
                            )
                            ODSBox(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(14.dp)
                                    .zonaShimmer(),
                                cornerRadius = ODSCorners(all = 7.dp)
                            )
                        }
                        ODSBox(
                            modifier = Modifier
                                .width(50.dp)
                                .height(12.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 6.dp)
                        )
                    }
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSColumn(
                            gap = ODSVariables.spacingComponent2,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            ODSBox(
                                modifier = Modifier.width(70.dp).height(10.dp).zonaShimmer(),
                                cornerRadius = ODSCorners(all = 5.dp)
                            )
                            ODSBox(
                                modifier = Modifier.width(90.dp).height(12.dp).zonaShimmer(),
                                cornerRadius = ODSCorners(all = 6.dp)
                            )
                        }
                        ODSColumn(
                            gap = ODSVariables.spacingComponent2,
                            verticalAlignment = Alignment.Top,
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Top
                        ) {
                            ODSBox(
                                modifier = Modifier.width(70.dp).height(10.dp).zonaShimmer(),
                                cornerRadius = ODSCorners(all = 5.dp)
                            )
                            ODSBox(
                                modifier = Modifier.width(90.dp).height(12.dp).zonaShimmer(),
                                cornerRadius = ODSCorners(all = 6.dp)
                            )
                        }
                    }
                }
            }

            // ── 5. 4 Gallery Photo Skeletons ─────────────────────────────────
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                padding = ODSPadding(left = ODSVariables.spacingLayout1, right = ODSVariables.spacingLayout1),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(4) {
                    ODSColumn(
                        gap = ODSVariables.spacingComponent3,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        width = 76.dp
                    ) {
                        ODSBox(
                            modifier = Modifier
                                .size(76.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                            clipContent = true
                        )
                        ODSBox(
                            modifier = Modifier
                                .width(56.dp)
                                .height(10.dp)
                                .zonaShimmer(),
                            cornerRadius = ODSCorners(all = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

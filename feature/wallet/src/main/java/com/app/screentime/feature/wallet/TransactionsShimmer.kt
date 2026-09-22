package com.app.screentime.feature.wallet

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Shimmer Loading Brush Modifier for Transaction Shimmer Skeleton
 */
fun Modifier.transactionShimmer(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "transactionShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = -800f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "transaction_shimmer_anim",
    )

    val baseColor = ZonaColors.LoadingShimmer.getColor()
    val highlightColor = ZonaColors.loadingShimmerHighlight.getColor()
    val shimmerColors = listOf(
        baseColor,
        highlightColor,
        baseColor,
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
 * Transaction Shimmer Skeleton Screen
 *
 * 100% Telekom ODS compliant loading state for the Transactions History screen:
 * - Uses ODSLazyColumn, ODSColumn, ODSRow, ODSBox
 * - Colors sourced from ODSTheme scheme
 * - 100% spacing, padding, radii backed by ODSVariables
 */
@Composable
fun TransactionsShimmer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSBox(
        modifier = modifier.fillMaxSize(),
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
        ODSLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            gap = ODSVariables.spacingComponent5,
            padding = ODSPadding(
                left = ODSVariables.spacingLayout1,
                right = ODSVariables.spacingLayout1,
                top = ODSVariables.spacingComponent3,
                bottom = ODSVariables.spacingLayout2
            )
        ) {
            // ── 1. Top Header Row (Back Button + Centered Title Shimmer) ──────
            item(key = "header_shimmer") {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = ODSVariables.spacingComponent3),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Back Button Shimmer (Rounded Rect 44.dp x 44.dp)
                    ODSBox(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(ODSVariables.radiusMedium))
                            .transactionShimmer()
                    )

                    // Center Title Shimmer Bar
                    ODSBox(
                        modifier = Modifier
                            .width(140.dp)
                            .height(ODSVariables.spacingComponent6)
                            .clip(RoundedCornerShape(ODSVariables.radiusSmall))
                            .transactionShimmer()
                    )

                    // Right Invisible Spacer (46.dp x 46.dp) for visual centering
                    ODSBox(
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            // ── 2. Total Balance Hero Card Shimmer ────────────────────────────
            item(key = "hero_card_shimmer") {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent4,
                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                ) {
                    // Sub-label Shimmer Line
                    ODSBox(
                        modifier = Modifier
                            .width(130.dp)
                            .height(ODSVariables.spacingComponent4)
                            .clip(RoundedCornerShape(ODSVariables.radiusSmall))
                            .transactionShimmer()
                    )

                    // Price & Credits Shimmer Row
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Price Shimmer Box
                        ODSBox(
                            modifier = Modifier
                                .width(110.dp)
                                .height(28.dp)
                                .clip(RoundedCornerShape(ODSVariables.radiusSmall))
                                .transactionShimmer()
                        )

                        // Credits Pill Shimmer Box
                        ODSBox(
                            modifier = Modifier
                                .width(90.dp)
                                .height(ODSVariables.spacingComponent7)
                                .clip(RoundedCornerShape(ODSVariables.radiusFull))
                                .transactionShimmer()
                        )
                    }
                }
            }

            // ── 3. Filter Chips Shimmer Row ([All] [Purchases] [Spent]) ───────
            item(key = "filter_chips_shimmer") {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3
                ) {
                    // Active Pill Shimmer
                    ODSBox(
                        modifier = Modifier
                            .width(60.dp)
                            .height(ODSVariables.spacingComponent8)
                            .transactionShimmer(),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                        )
                    )

                    // Inactive Pill Shimmer 1
                    ODSBox(
                        modifier = Modifier
                            .width(90.dp)
                            .height(ODSVariables.spacingComponent8)
                            .clip(RoundedCornerShape(ODSVariables.radiusFull))
                            .transactionShimmer()
                    )

                    // Inactive Pill Shimmer 2
                    ODSBox(
                        modifier = Modifier
                            .width(80.dp)
                            .height(ODSVariables.spacingComponent8)
                            .clip(RoundedCornerShape(ODSVariables.radiusFull))
                            .transactionShimmer()
                    )
                }
            }

            // ── 4. Today Section Shimmer ──────────────────────────────────────
            item(key = "today_header_shimmer") {
                ODSBox(
                    modifier = Modifier
                        .width(70.dp)
                        .height(ODSVariables.spacingComponent4)
                        .clip(RoundedCornerShape(ODSVariables.radiusSmall))
                        .transactionShimmer()
                )
            }

            items(2, key = { "today_tx_shimmer_$it" }) {
                TransactionItemShimmer(scheme = scheme)
            }

            // ── 5. Past Date Section Shimmer ──────────────────────────────────
            item(key = "past_header_shimmer") {
                Spacer(modifier = Modifier.height(ODSVariables.spacingComponent3))
                ODSBox(
                    modifier = Modifier
                        .width(130.dp)
                        .height(ODSVariables.spacingComponent4)
                        .clip(RoundedCornerShape(ODSVariables.radiusSmall))
                        .transactionShimmer()
                )
            }

            items(2, key = { "past_tx_shimmer_$it" }) {
                TransactionItemShimmer(scheme = scheme)
            }

            // ── 6. Bottom Drag Indicator Bar Shimmer ──────────────────────────
            item(key = "indicator_bar_shimmer") {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = ODSVariables.spacingComponent3),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ODSBox(
                        modifier = Modifier
                            .width(134.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(ODSVariables.radiusFull))
                            .transactionShimmer()
                    )
                }
            }
        }
    }
}

/**
 * Individual Transaction Item Card Shimmer Skeleton
 */
@Composable
private fun TransactionItemShimmer(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        border = ODSBorder(
            width = ODSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        padding = ODSPadding(all = ODSVariables.spacingComponent4)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                gap = ODSVariables.spacingComponent4
            ) {
                // Direction / Category Icon Shimmer Square
                ODSBox(
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent13)
                        .clip(RoundedCornerShape(ODSVariables.radiusMedium))
                        .transactionShimmer()
                )

                // Title & Subtitle / Date Lines
                ODSColumn(gap = ODSVariables.spacingComponent2) {
                    ODSBox(
                        modifier = Modifier
                            .width(140.dp)
                            .height(ODSVariables.spacingComponent4)
                            .clip(RoundedCornerShape(ODSVariables.radiusSmall))
                            .transactionShimmer()
                    )
                    ODSBox(
                        modifier = Modifier
                            .width(100.dp)
                            .height(ODSVariables.spacingComponent3)
                            .clip(RoundedCornerShape(ODSVariables.radiusSmall))
                            .transactionShimmer()
                    )
                }
            }

            // Value / Amount Shimmer Box
            ODSBox(
                modifier = Modifier
                    .width(64.dp)
                    .height(ODSVariables.spacingComponent5)
                    .clip(RoundedCornerShape(ODSVariables.radiusSmall))
                    .transactionShimmer()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionsShimmerPreview() {
    TransactionsShimmer()
}

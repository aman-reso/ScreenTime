package com.app.screentime.feature.discover.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun DiscoverMapShimmer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
) {
    ODSBox(
        modifier = modifier
            .fillMaxSize()

    ) {
        // Map Area Background with Route Lines & Map Pin Markers
        MapBackgroundWithPins(
            modifier = Modifier
                .fillMaxSize(),
            scheme = scheme,
        )

        // Top Filter / Control Shimmer Pills Header
        DiscoverTopHeaderPillShimmer(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            scheme = scheme,
        )

        // Floating Bottom Store / Service Card Shimmer
        FloatingStoreCardShimmer(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            scheme = scheme,
        )
    }
}

@Composable
fun DiscoverTopHeaderPillShimmer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
) {
    ODSRow(
        modifier = modifier,
        gap = 8.dp,
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // Main Search / Primary Pill Shimmer
        ODSSkeleton(
            modifier = Modifier
                .width(140.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(18.dp)),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
        )

        // Secondary Filter Pill Shimmer 1
        ODSSkeleton(
            modifier = Modifier
                .width(80.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(18.dp)),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
        )

        // Secondary Filter Pill Shimmer 2
        ODSSkeleton(
            modifier = Modifier
                .width(80.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(18.dp)),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
        )
    }
}

@Composable
fun MapBackgroundWithPins(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
) {
    ODSBox(
        modifier = modifier,
        background = listOf(ODSColorModel(hexColor = HexColor("#F5F5F7", 1.00f))),
    ) {
        // Subtle route lines across the map canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val strokeColor = Color(0xFFFCE4EC)
            val strokeWidth = 2.dp.toPx()

            // Line 1: Top-Left to Bottom-Right angle
            drawLine(
                color = strokeColor,
                start = Offset(0f, size.height * 0.12f),
                end = Offset(size.width, size.height * 0.28f),
                strokeWidth = strokeWidth,
            )

            // Line 2: Top-Center down to Center-Right angle
            drawLine(
                color = strokeColor,
                start = Offset(size.width * 0.55f, 0f),
                end = Offset(size.width * 0.15f, size.height * 0.42f),
                strokeWidth = strokeWidth,
            )

            // Line 3: Cross line
            drawLine(
                color = strokeColor,
                start = Offset(0f, size.height * 0.36f),
                end = Offset(size.width, size.height * 0.31f),
                strokeWidth = strokeWidth,
            )

            // Line 4: Vertical route line
            drawLine(
                color = strokeColor,
                start = Offset(size.width * 0.38f, 0f),
                end = Offset(size.width * 0.52f, size.height * 0.45f),
                strokeWidth = strokeWidth,
            )
        }

        // Map Pin 1 (Top Right area)
        MapPinMarkerShimmer(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 110.dp, end = 80.dp),
            scheme = scheme,
        )

        // Map Pin 2 (Middle Left area)
        MapPinMarkerShimmer(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 180.dp, start = 65.dp),
            scheme = scheme,
        )

        // Map Pin 3 (Center Bottom area)
        MapPinMarkerShimmer(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 60.dp),
            scheme = scheme,
        )
    }
}

@Composable
fun MapPinMarkerShimmer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
) {
    ODSColumn(
        modifier = modifier,
        gap = 2.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        // Circular Avatar Glow Marker
        ODSSkeleton(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape),
            scheme = scheme,
            props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
        )

        // Small Downward Pointer Polygon / Triangle
        Canvas(
            modifier = Modifier
                .size(width = 10.dp, height = 6.dp),
        ) {
            val pointerPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2f, size.height)
                close()
            }
            drawPath(
                path = pointerPath,
                color = Color(0xFFFCE4EC),
            )
        }
    }
}

@Composable
fun FloatingStoreCardShimmer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
) {
    ODSRow(
        modifier = modifier,
        padding = ODSPadding(bottom = 16.dp, left = 16.dp, right = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
    ) {
        ODSRow(
            modifier = Modifier
                .fillMaxWidth(),
            gap = 16.dp,
            padding = ODSPadding(all = 12.dp),
            cornerRadius = ODSCorners(all = 24.dp),
            border = ODSBorder(
                width = 2.dp,
                colorList = listOf(ODSColorModel(hexColor = HexColor("#FCE4EC", 1.00f))),
            ),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            background = listOf(ODSColorModel(hexColor = HexColor("#FFFFFF", 1.00f))),
        ) {
            // Left Avatar / Image Placeholder Shimmer
            ODSSkeleton(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp)),
                scheme = scheme,
                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
            )

            // Middle Info Content Column
            ODSColumn(
                modifier = Modifier
                    .weight(1f),
                gap = 8.dp,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                // Badge Shimmer Line
                ODSSkeleton(
                    modifier = Modifier
                        .width(80.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    scheme = scheme,
                    props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
                )

                // Title Shimmer Line
                ODSSkeleton(
                    modifier = Modifier
                        .width(140.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    scheme = scheme,
                    props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
                )

                // Info / Subtitle Shimmer Line
                ODSSkeleton(
                    modifier = Modifier
                        .width(160.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    scheme = scheme,
                    props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
                )
            }

            // Right Action / Chat Button Shimmer
            ODSSkeleton(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp)),
                scheme = scheme,
                props = ODSSkeletonProps(variant = ODSSkeletonVariant.SMALL),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun DiscoverMapShimmerPreview() {
    DiscoverMapShimmer()
}

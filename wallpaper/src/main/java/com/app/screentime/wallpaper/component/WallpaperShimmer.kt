package com.app.screentime.wallpaper.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Shimmer loading for category tabs
 */
@Composable
fun CategoryTabsShimmer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(
                horizontal = DSVariables.spacingComponent4,
                vertical = DSVariables.spacingComponent2
            )
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = DSVariables.spacingComponent2
        ) {
            repeat(5) {
                ODSSkeleton(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    scheme = scheme,
                    props = ODSSkeletonProps(
                        variant = ODSSkeletonVariant.SMALL
                    )
                )
            }
        }
    }
}

/**
 * Shimmer loading for wallpaper grid
 */
@Composable
fun WallpaperGridShimmer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme
) {
    val shimmerItems = (1..10).toList() // Show 10 shimmer items

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(DSVariables.spacingComponent4),
        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
        verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
    ) {
        items(shimmerItems) {
            ODSSkeleton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                scheme = scheme,
                props = ODSSkeletonProps(
                    variant = ODSSkeletonVariant.LARGE
                )
            )
        }
    }
}

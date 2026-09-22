package com.telekom.odsystem.organisms.cardfeature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-09 (v1.33.1) - uid: 29d108b9
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=137-12140
 */

data class ODSCardFeatureTokens(
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalArrangement: Arrangement.Horizontal,
    val imageZStackWidth: Dp,
    val imageZStackClipContent: Boolean,
    val imageZStackContentAlignment: Alignment,
    val imageCornerRadius: ODSCorners,
    val imageWidth: Dp,
    val imageClipContent: Boolean,
    val imageVerticalAlignment: Alignment.Vertical,
    val imageHorizontalAlignment: Alignment.Horizontal,
    val imageVerticalArrangement: Arrangement.Vertical,
    val imageContentAlignment: Alignment,
    val imageBgCornerRadius: ODSCorners,
    val imageBgVerticalAlignment: Alignment.Vertical,
    val imageBgHorizontalAlignment: Alignment.Horizontal,
    val imageBgHorizontalArrangement: Arrangement.Horizontal,
    val image2Width: Dp,
    val image2Height: Dp,
    val image2ContentScale: ContentScale,
    val contentZStackMinHeight: Dp,
    val contentZStackContentAlignment: Alignment,
    val contentGap: Dp,
    val contentPadding: ODSPadding,
    val contentMinHeight: Dp,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val contentContentAlignment: Alignment,
    val cardBgCornerRadius: ODSCorners,
    val cardBgVerticalAlignment: Alignment.Vertical,
    val cardBgHorizontalAlignment: Alignment.Horizontal,
    val cardBgVerticalArrangement: Arrangement.Vertical,
    val slotContainerGap: Dp,
    val slotContainerVerticalAlignment: Alignment.Vertical,
    val slotContainerHorizontalAlignment: Alignment.Horizontal,
    val slotContainerVerticalArrangement: Arrangement.Vertical,
)

val defaultODSCardFeatureTokens = ODSCardFeatureTokens(
    minWidth = DSVariables.columns3Columns,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    imageZStackWidth = 96.dp,
    imageZStackClipContent = true,
    imageZStackContentAlignment = Alignment.Center,
    imageCornerRadius = ODSCorners(
        topLeft = DSVariables.radiusMedium,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = 0.dp
    ),
    imageWidth = 96.dp,
    imageClipContent = true,
    imageVerticalAlignment = Alignment.CenterVertically,
    imageHorizontalAlignment = Alignment.CenterHorizontally,
    imageVerticalArrangement = Arrangement.Center,
    imageContentAlignment = Alignment.Center,
    imageBgCornerRadius = ODSCorners(
        topLeft = DSVariables.radiusMedium,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = 0.dp
    ),
    imageBgVerticalAlignment = Alignment.Top,
    imageBgHorizontalAlignment = Alignment.Start,
    imageBgHorizontalArrangement = Arrangement.Start,
    image2Width = 96.dp,
    image2Height = 96.dp,
    image2ContentScale = ContentScale.Fit,
    contentZStackMinHeight = 96.dp,
    contentZStackContentAlignment = Alignment.CenterStart,
    contentGap = DSVariables.spacingComponent5,
    contentPadding = ODSPadding(
        top = DSVariables.spacingComponent7,
        bottom = DSVariables.spacingComponent7,
        left = DSVariables.spacingComponent5,
        right = DSVariables.spacingComponent7
    ),
    contentMinHeight = 96.dp,
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    contentContentAlignment = Alignment.CenterStart,
    cardBgCornerRadius = ODSCorners(
        topLeft = 0.dp,
        topRight = DSVariables.radiusMedium,
        bottomLeft = 0.dp,
        bottomRight = DSVariables.radiusMedium
    ),
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    slotContainerGap = 10.dp,
    slotContainerVerticalAlignment = Alignment.Top,
    slotContainerHorizontalAlignment = Alignment.Start,
    slotContainerVerticalArrangement = Arrangement.Top
)

var DSCardFeatureTokens: ODSCardFeatureTokens = defaultODSCardFeatureTokens

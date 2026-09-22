package com.telekom.odsystem.atoms.thumbnail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-01 (v1.32.3) - uid: 1dc2b9ea
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=7904-10948
 */

data class ODSThumbnailTokens(
    val cornerRadiusTypeImage: ODSCorners,
    val width: Dp,
    val height: Dp,
    val clipContent: Boolean,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val imageContentScaleTypeImage: ContentScale,
    val iconWidthTypeIcon: Dp,
    val iconHeightTypeIcon: Dp
)

val defaultODSThumbnailTokens = ODSThumbnailTokens(
    cornerRadiusTypeImage = ODSCorners(all = DSVariables.radiusSmall),
    width = DSVariables.sizingComponent13,
    height = DSVariables.sizingComponent13,
    clipContent = true,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center,
    imageContentScaleTypeImage = ContentScale.Crop,
    iconWidthTypeIcon = DSVariables.sizingComponent10,
    iconHeightTypeIcon = DSVariables.sizingComponent10
)

var DSThumbnailTokens: ODSThumbnailTokens = defaultODSThumbnailTokens

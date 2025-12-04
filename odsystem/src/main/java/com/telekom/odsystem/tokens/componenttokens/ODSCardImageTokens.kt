package com.telekom.odsystem.tokens.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding

data class ODSCardImageTokens(
    val width: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val imageAspectRatioVerticalAlignment: Alignment.Vertical,
    val imageAspectRatioHorizontalAlignment: Alignment.Horizontal,
    val imageAspectRatioVerticalArrangement: Arrangement.Vertical,
    val imageAspectRatioContentAlignment: Alignment,
    val imageContainerBorderRadiusImagePositionTop: ODSCorners,
    val imageContainerBorderRadiusImagePositionBottom: ODSCorners,
    val imageContainerClipContent: Boolean,
    val imageContainerVerticalAlignment: Alignment.Vertical,
    val imageContainerHorizontalAlignment: Alignment.Horizontal,
    val imageContainerHorizontalArrangement: Arrangement.Horizontal,
    val imageObjectFit: ContentScale,
    val logoImageContentAlignment: Alignment,
    val logoImageHeight: Dp,
    val logoImageWidth: Dp,
    val logoImageOffset: ODSOffset,
    val logoImageObjectFit: ContentScale,
    val contentGap: Dp,
    val contentPadding: ODSPadding,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val contentVerticalArrangementCustomHeight: Arrangement.Vertical,
    val contentContentAlignment: Alignment,
    val cardBgBorderRadiusImagePositionTop: ODSCorners,
    val cardBgBorderRadiusImagePositionBottom: ODSCorners,
    val cardBgBorderRadiusImagePositionLeft: ODSCorners,
    val cardBgClipContent: Boolean,
    val cardBgVerticalAlignment: Alignment.Vertical,
    val cardBgHorizontalAlignment: Alignment.Horizontal,
    val cardBgVerticalArrangement: Arrangement.Vertical,
    val slotContainerVerticalAlignment: Alignment.Vertical,
    val slotContainerHorizontalAlignment: Alignment.Horizontal,
    val slotContainerVerticalArrangement: Arrangement.Vertical,
    val actionContainerVerticalAlignment: Alignment.Vertical,
    val actionContainerVerticalAlignmentCustomHeight: Alignment.Vertical,
    val actionContainerHorizontalAlignment: Alignment.Horizontal,
    val actionContainerHorizontalAlignmentCustomHeight: Alignment.Horizontal,
    val actionContainerVerticalArrangement: Arrangement.Vertical,
    val actionContainerVerticalArrangementCustomHeight: Arrangement.Vertical,
    var logoRadius: ODSCorners? = null, // Not exported from plugin
    var scaleFactor: Float // Not exported from plugin
)

val defaultODSCardImageTokens = ODSCardImageTokens(
    width = DSVariables.columns3Columns,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    imageAspectRatioVerticalAlignment = Alignment.Bottom,
    imageAspectRatioHorizontalAlignment = Alignment.CenterHorizontally,
    imageAspectRatioVerticalArrangement = Arrangement.Bottom,
    imageAspectRatioContentAlignment = Alignment.BottomCenter,
    imageContainerBorderRadiusImagePositionTop = ODSCorners(
        topLeft = DSVariables.radiusMedium,
        topRight = DSVariables.radiusMedium,
        bottomLeft = DSVariables.radiusZero,
        bottomRight = DSVariables.radiusZero
    ),
    imageContainerBorderRadiusImagePositionBottom = ODSCorners(
        topLeft = DSVariables.radiusZero,
        topRight = DSVariables.radiusZero,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = DSVariables.radiusMedium
    ),
    imageContainerClipContent = true,
    imageContainerVerticalAlignment = Alignment.Top,
    imageContainerHorizontalAlignment = Alignment.Start,
    imageContainerHorizontalArrangement = Arrangement.Start,
    imageObjectFit = ContentScale.Crop,
    logoImageContentAlignment = Alignment.TopStart,
    logoImageHeight = DSVariables.sizingComponent14,
    logoImageWidth = DSVariables.sizingComponent14,
    logoImageOffset = ODSOffset(x = 24.dp, y = 24.dp),
    logoImageObjectFit = ContentScale.Crop,
    contentGap = DSVariables.spacingLayout5,
    contentPadding = ODSPadding(all = DSVariables.spacingComponent4),
    contentVerticalAlignment = Alignment.Top,
    contentHorizontalAlignment = Alignment.CenterHorizontally,
    contentVerticalArrangement = Arrangement.Top,
    contentVerticalArrangementCustomHeight = Arrangement.SpaceBetween,
    contentContentAlignment = Alignment.TopCenter,
    cardBgBorderRadiusImagePositionTop = ODSCorners(
        topLeft = DSVariables.radiusZero,
        topRight = DSVariables.radiusZero,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = DSVariables.radiusMedium
    ),
    cardBgBorderRadiusImagePositionBottom = ODSCorners(
        topLeft = DSVariables.radiusMedium,
        topRight = DSVariables.radiusMedium,
        bottomLeft = DSVariables.radiusZero,
        bottomRight = DSVariables.radiusZero
    ),
    cardBgClipContent = true,
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    slotContainerVerticalAlignment = Alignment.Top,
    slotContainerHorizontalAlignment = Alignment.Start,
    slotContainerVerticalArrangement = Arrangement.Top,
    actionContainerVerticalAlignment = Alignment.Top,
    actionContainerVerticalAlignmentCustomHeight = Alignment.Bottom,
    actionContainerHorizontalAlignment = Alignment.Start,
    actionContainerHorizontalAlignmentCustomHeight = Alignment.CenterHorizontally,
    actionContainerVerticalArrangement = Arrangement.Bottom, // Custom value is used
    actionContainerVerticalArrangementCustomHeight = Arrangement.Bottom,
    logoRadius = ODSCorners(all = DSVariables.radiusFull),
    cardBgBorderRadiusImagePositionLeft = ODSCorners(
        topLeft = DSVariables.radiusMedium,
        topRight = DSVariables.radiusZero,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = DSVariables.radiusZero
    ),
    scaleFactor = 8f // Custom addition only for card image
)

var DSCardImageTokens: ODSCardImageTokens = defaultODSCardImageTokens

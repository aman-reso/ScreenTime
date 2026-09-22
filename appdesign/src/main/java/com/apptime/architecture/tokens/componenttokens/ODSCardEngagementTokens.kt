package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardEngagementTokens(
    var borderRadius: ODSCorners,
    var height: Dp, // Not used in mobile
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var backgroundBorderRadius: ODSCorners,
    var backgroundWidth: Dp, // Not used in mobile
//    var backgroundWidthStatePressed: Dp,
//    var backgroundWidthStateHovered: Dp,
    var backgroundHeight: Dp, // Not used in mobile
//    var backgroundHeightStatePressed: Dp,
//    var backgroundHeightStateHovered: Dp,
    var backgroundClipContent: Boolean,
    var backgroundVerticalAlignment: Alignment.Vertical,
    var backgroundHorizontalAlignment: Alignment.Horizontal,
    var backgroundVerticalArrangement: Arrangement.Vertical,
    var labelContainerPadding: ODSPadding,
    var labelContainerVerticalAlignment: Alignment.Vertical,
    var labelContainerHorizontalAlignment: Alignment.Horizontal,
    var labelContainerHorizontalArrangement: Arrangement.Horizontal,
    var imageContainerBorderRadius: ODSCorners,
    var imageContainerWidth: Dp, // Not used in mobile
//    var imageContainerWidthStatePressed: Dp,
//    var imageContainerWidthStateHovered: Dp,
    var imageContainerHeight: Dp,
//    var imageContainerHeightStatePressed: Dp,
//    var imageContainerHeightStateHovered: Dp,
    var imageContainerClipContent: Boolean, // Not used in mobile
    var imageWidth: Dp,
    var imageHeight: Dp,
    var imageObjectFit: ContentScale,
    var labelTextStyle: ODSTextStyle,
    var labelTextAlign: TextAlign,
    var labelTextOverflow: TextOverflow,
    var scaleFactor: Float?, // Not exported by plugin
    var labelTextMaxLines: Int?, // Not exported by plugin
    var imageHorizontalOffset: Float?, // Not exported by plugin
    val textContainerAlignment: Alignment?, // Not exported by plugin
)

var defaultODSCardEngagementTokens = ODSCardEngagementTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusMedium),
    height = 120.dp,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    backgroundBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    backgroundWidth = 328.dp,
//    backgroundWidthStatePressed = 328.dp,
//    backgroundWidthStateHovered = 336.dp,
    backgroundHeight = 120.dp,
//    backgroundHeightStatePressed = 120.dp,
//    backgroundHeightStateHovered = 128.dp,
    backgroundClipContent = true,
    backgroundVerticalAlignment = Alignment.Top,
    backgroundHorizontalAlignment = Alignment.CenterHorizontally,
    backgroundVerticalArrangement = Arrangement.Top,
    labelContainerPadding = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4,
        left = DSVariables.spacingComponent7,
        right = 144.dp
    ),
    labelContainerVerticalAlignment = Alignment.CenterVertically,
    labelContainerHorizontalAlignment = Alignment.Start,
    labelContainerHorizontalArrangement = Arrangement.Start,
    imageContainerBorderRadius = ODSCorners(
        topLeft = 0.dp,
        topRight = DSVariables.radiusMedium,
        bottomLeft = 0.dp,
        bottomRight = DSVariables.radiusMedium
    ),
    imageContainerWidth = 120.dp,
//    imageContainerWidthStatePressed = 120.dp,
//    imageContainerWidthStateHovered = 124.dp,
    imageContainerHeight = 120.dp,
//    imageContainerHeightStatePressed = 120.dp,
//    imageContainerHeightStateHovered = 128.dp,
    imageContainerClipContent = true,
    imageWidth = 144.dp,
    imageHeight = 144.dp,
    imageObjectFit = ContentScale.Crop,
    labelTextStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    labelTextOverflow = TextOverflow.Ellipsis,
    scaleFactor = SCALE_FACTOR,
    labelTextMaxLines = 4,
    imageHorizontalOffset = 24f,
    textContainerAlignment = Alignment.CenterStart
)

var DSCardEngagementTokens: ODSCardEngagementTokens = defaultODSCardEngagementTokens

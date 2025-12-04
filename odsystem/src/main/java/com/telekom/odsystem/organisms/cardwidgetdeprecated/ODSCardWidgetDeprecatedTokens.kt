package com.telekom.odsystem.organisms.cardwidgetdeprecated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardWidgetDeprecatedTokens(
    var borderRadius: ODSCorners,
    var minWidth: Dp,
    var width: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var aspectContainerBorderRadius: ODSCorners,
    var aspectContainerVerticalAlignment: Alignment.Vertical,
    var aspectContainerHorizontalAlignment: Alignment.Horizontal,
    var aspectContainerVerticalArrangement: Arrangement.Vertical,
    var contentContainerGap: Dp,
    var contentContainerPadding: ODSPadding,
    var contentContainerWidth: Dp,
    var contentContainerHeight: Dp,
    var contentContainerVerticalAlignment: Alignment.Vertical,
    var contentContainerHorizontalAlignment: Alignment.Horizontal,
    var contentContainerVerticalArrangement: Arrangement.Vertical,
    var imageWidth: Dp,
    var imageWidthTypeSlot: Dp,
    var imageWidthTypeSlotStateHovered: Dp,
    var imageWidthTypeImage: Dp,
    var imageVerticalAlignment: Alignment.Vertical,
    var imageHorizontalAlignment: Alignment.Horizontal,
    var imageVerticalArrangement: Arrangement.Vertical,
    var imageHeightTypeSlot: Dp,
    var imageHeightTypeSlotStateHovered: Dp,
    var imageHeightTypeImage: Dp,
    var imageObjectFitTypeSlot: ContentScale,
    var imageObjectFitTypeImage: ContentScale,
    var backgroundBorderRadius: ODSCorners,
    var backgroundWidth: Dp,
    var backgroundWidthStateHovered: Dp,
    var backgroundClipContent: Boolean,
    var backgroundVerticalAlignment: Alignment.Vertical,
    var backgroundHorizontalAlignment: Alignment.Horizontal,
    var backgroundVerticalArrangement: Arrangement.Vertical,
    var imageContainerBorderRadiusTypeSlot: ODSCorners,
    var imageContainerBorderRadiusTypeImage: ODSCorners,
    var imageContainerWidth: Dp,
    var imageContainerWidthStateHovered: Dp,
    var imageContainerClipContent: Boolean,
    var imageContainerVerticalAlignment: Alignment.Vertical,
    var imageContainerHorizontalAlignment: Alignment.Horizontal,
    var imageContainerVerticalArrangement: Arrangement.Vertical,
    var titleAndSubtitleVerticalAlignment: Alignment.Vertical,
    var titleAndSubtitleHorizontalAlignment: Alignment.Horizontal,
    var titleAndSubtitleVerticalArrangement: Arrangement.Vertical,
    var odsSlotHeightTypeImage: Dp,
    var titleTextStyle: ODSTextStyle,
    var titleTextAlign: TextAlign,
    var subtitleTextStyle: ODSTextStyle,
    var subtitleTextAlign: TextAlign,

    var scaleFactor: Float? = null, // Not exported from the plugin
    var verticalImageOffset: Dp? = null, // Not exported from the plugin
    var logoPadding: ODSPadding? = null, // Not exported from the plugin
    var logoSize: Dp? = null, // Not exported from the plugin
    var imageContainerAlignment: Alignment? = null, // Not exported from the plugin
)

var defaultODSCardWidgetTokens = ODSCardWidgetDeprecatedTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusMedium),
    minWidth = 154.dp,
    width = 184.dp,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    aspectContainerBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    aspectContainerVerticalAlignment = Alignment.Top,
    aspectContainerHorizontalAlignment = Alignment.Start,
    aspectContainerVerticalArrangement = Arrangement.Top,
    contentContainerGap = DSVariables.spacingComponent3,
    contentContainerPadding = ODSPadding(all = DSVariables.spacingComponent7),
    contentContainerWidth = 184.dp,
    contentContainerHeight = 184.dp,
    contentContainerVerticalAlignment = Alignment.Top,
    contentContainerHorizontalAlignment = Alignment.Start,
    contentContainerVerticalArrangement = Arrangement.Top,
    imageWidth = 184.dp,
    imageWidthTypeSlot = 192.dp,
    imageWidthTypeSlotStateHovered = 193.dp,
    imageWidthTypeImage = 200.dp,
    imageVerticalAlignment = Alignment.Bottom,
    imageHorizontalAlignment = Alignment.CenterHorizontally,
    imageVerticalArrangement = Arrangement.Bottom,
    imageHeightTypeSlot = 188.dp,
    imageHeightTypeSlotStateHovered = 196.dp,
    imageHeightTypeImage = 146.dp,
    imageObjectFitTypeSlot = ContentScale.Crop,
    imageObjectFitTypeImage = ContentScale.Fit,
    backgroundBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    backgroundWidth = 184.dp,
    backgroundWidthStateHovered = 192.dp,
    backgroundClipContent = true,
    backgroundVerticalAlignment = Alignment.Top,
    backgroundHorizontalAlignment = Alignment.Start,
    backgroundVerticalArrangement = Arrangement.Top,
    imageContainerBorderRadiusTypeSlot = ODSCorners(all = DSVariables.radiusMedium),
    imageContainerBorderRadiusTypeImage = ODSCorners(
        topLeft = 0.dp,
        topRight = 0.dp,
        bottomLeft = DSVariables.radiusMedium,
        bottomRight = DSVariables.radiusMedium
    ),
    imageContainerWidth = 184.dp,
    imageContainerWidthStateHovered = 192.dp,
    imageContainerClipContent = true,
    imageContainerVerticalAlignment = Alignment.Top,
    imageContainerHorizontalAlignment = Alignment.Start,
    imageContainerVerticalArrangement = Arrangement.Top,
    titleAndSubtitleVerticalAlignment = Alignment.Top,
    titleAndSubtitleHorizontalAlignment = Alignment.Start,
    titleAndSubtitleVerticalArrangement = Arrangement.Top,
    odsSlotHeightTypeImage = 89.dp,
    titleTextStyle = DSTextStyles.bodyMBold,
    titleTextAlign = TextAlign.Left,
    subtitleTextStyle = DSTextStyles.bodyMBold,
    subtitleTextAlign = TextAlign.Left,
    scaleFactor = SCALE_FACTOR,
    verticalImageOffset = 8.dp,
    logoPadding = ODSPadding(
        bottom = DSVariables.spacingComponent7,
        left = DSVariables.spacingComponent7
    ),
    logoSize = DSVariables.sizingComponent14,
    imageContainerAlignment = Alignment.BottomStart
)

var DSCardWidgetTokens: ODSCardWidgetDeprecatedTokens = defaultODSCardWidgetTokens

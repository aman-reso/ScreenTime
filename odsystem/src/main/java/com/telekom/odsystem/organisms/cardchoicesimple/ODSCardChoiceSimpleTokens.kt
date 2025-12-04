package com.telekom.odsystem.organisms.cardchoicesimple

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.foundations.SCALE_FACTOR

data class ODSCardChoiceSimpleTokens(
    var width: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var containerGap: Dp,
    var containerPadding: ODSPadding,
    var containerMinHeight: Dp,
    var containerVerticalAlignment: Alignment.Vertical,
    var containerHorizontalAlignment: Alignment.Horizontal,
    var containerVerticalArrangement: Arrangement.Vertical,
    var cardBgBorderRadius: ODSCorners,
    var cardBgWidth: Dp,
    var cardBgWidthStatePressed: Dp,
    var cardBgWidthStateHovered: Dp,
    var cardBgHeight: Dp,
    var cardBgHeightStatePressed: Dp,
    var cardBgHeightStateHovered: Dp,
    var cardBgClipContent: Boolean,
    var cardBgVerticalAlignment: Alignment.Vertical,
    var cardBgHorizontalAlignment: Alignment.Horizontal,
    var cardBgVerticalArrangement: Arrangement.Vertical,
    var cardBgBorderSelected: Dp,
    var cardBgBorderVariantOutline: Dp,
    var contentGap: Dp,
    var contentVerticalAlignment: Alignment.Vertical,
    var contentHorizontalAlignment: Alignment.Horizontal,
    var contentHorizontalArrangement: Arrangement.Horizontal,
    var leftContentGap: Dp,
    var leftContentVerticalAlignment: Alignment.Vertical,
    var leftContentHorizontalAlignment: Alignment.Horizontal,
    var leftContentVerticalArrangement: Arrangement.Vertical,
    var rightContentContainerVerticalAlignment: Alignment.Vertical,
    var rightContentContainerHorizontalAlignment: Alignment.Horizontal,
    var rightContentContainerVerticalArrangement: Arrangement.Vertical,
    var bottomSlotContainerVerticalAlignment: Alignment.Vertical,
    var bottomSlotContainerHorizontalAlignment: Alignment.Horizontal,
    var bottomSlotContainerHorizontalArrangement: Arrangement.Horizontal,
    var labelTopTextStyle: ODSTextStyle,
    var labelTopTextAlign: TextAlign,
    var headingTextStyle: ODSTextStyle,
    var headingTextAlign: TextAlign,
    var labelBottomTextStyle: ODSTextStyle,
    var labelBottomTextAlign: TextAlign,
    var scaleFactor: Float, // Not exported from the plugin
)

var defaultODSCardChoiceSimpleTokens = ODSCardChoiceSimpleTokens(
    width = DSVariables.columns5Columns,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    containerGap = DSVariables.spacingLayout1,
    containerPadding = ODSPadding(
        top = DSVariables.spacingLayout1,
        bottom = DSVariables.spacingLayout1,
        left = DSVariables.spacingLayout2,
        right = DSVariables.spacingLayout2
    ),
    containerMinHeight = 80.dp,
    containerVerticalAlignment = Alignment.CenterVertically,
    containerHorizontalAlignment = Alignment.CenterHorizontally,
    containerVerticalArrangement = Arrangement.Center,
    cardBgBorderRadius = ODSCorners(all = DSVariables.radiusMedium),
    cardBgWidth = 575.dp,
    cardBgWidthStatePressed = 575.dp,
    cardBgWidthStateHovered = 583.dp,
    cardBgHeight = 80.dp,
    cardBgHeightStatePressed = 80.dp,
    cardBgHeightStateHovered = 88.dp,
    cardBgClipContent = true,
    cardBgVerticalAlignment = Alignment.Top,
    cardBgHorizontalAlignment = Alignment.CenterHorizontally,
    cardBgVerticalArrangement = Arrangement.Top,
    cardBgBorderSelected = DSVariables.strokes2,
    cardBgBorderVariantOutline = DSVariables.strokes1,
    contentGap = DSVariables.spacingComponent5,
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    leftContentGap = DSVariables.spacingComponent2,
    leftContentVerticalAlignment = Alignment.CenterVertically,
    leftContentHorizontalAlignment = Alignment.Start,
    leftContentVerticalArrangement = Arrangement.Center,
    rightContentContainerVerticalAlignment = Alignment.CenterVertically,
    rightContentContainerHorizontalAlignment = Alignment.End,
    rightContentContainerVerticalArrangement = Arrangement.Center,
    bottomSlotContainerVerticalAlignment = Alignment.Top,
    bottomSlotContainerHorizontalAlignment = Alignment.Start,
    bottomSlotContainerHorizontalArrangement = Arrangement.Start,
    labelTopTextStyle = DSTextStyles.bodyMBold,
    labelTopTextAlign = TextAlign.Left,
    headingTextStyle = DSTextStyles.titleS,
    headingTextAlign = TextAlign.Left,
    labelBottomTextStyle = DSTextStyles.bodyMBold,
    labelBottomTextAlign = TextAlign.Left,
    scaleFactor = SCALE_FACTOR
)

var DSCardChoiceSimpleTokens: ODSCardChoiceSimpleTokens = defaultODSCardChoiceSimpleTokens

package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSFilterChipDropdownItemTokens(
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val minHeight: Dp,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalArrangement: Arrangement.Horizontal,
    val contentFrameGap: Dp,
    val contentFrameVerticalAlignment: Alignment.Vertical,
    val contentFrameHorizontalAlignment: Alignment.Horizontal,
    val contentFrameHorizontalArrangement: Arrangement.Horizontal,
    val leftIconWidth: Dp,
    val leftIconHeight: Dp,
    val labelStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelOverflow: TextOverflow,
    val iconContainerPadding: ODSPadding,
    val iconContainerWidth: Dp,
    val iconContainerVerticalAlignment: Alignment.Vertical,
    val iconContainerHorizontalAlignment: Alignment.Horizontal,
    val iconContainerHorizontalArrangement: Arrangement.Horizontal,
    val checkmarkWidth: Dp,
    val checkmarkHeight: Dp
)

val defaultODSFilterChipDropdownItemTokens = ODSFilterChipDropdownItemTokens(
    padding = ODSPadding(
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent3
    ),
    cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
    minHeight = DSVariables.sizingMinimumTappableArea,
    minWidth = DSVariables.sizingComponent18,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    contentFrameGap = DSVariables.spacingComponent3,
    contentFrameVerticalAlignment = Alignment.CenterVertically,
    contentFrameHorizontalAlignment = Alignment.Start,
    contentFrameHorizontalArrangement = Arrangement.Start,
    leftIconWidth = DSVariables.sizingComponent8,
    leftIconHeight = DSVariables.sizingComponent8,
    labelStyle = DSTextStyles.bodyMBold,
    labelTextAlign = TextAlign.Left,
    labelOverflow = TextOverflow.Ellipsis,
    iconContainerPadding = ODSPadding(left = DSVariables.spacingComponent5),
    iconContainerWidth = 36.dp,
    iconContainerVerticalAlignment = Alignment.CenterVertically,
    iconContainerHorizontalAlignment = Alignment.Start,
    iconContainerHorizontalArrangement = Arrangement.Start,
    checkmarkWidth = DSVariables.sizingComponent8,
    checkmarkHeight = DSVariables.sizingComponent8
)

var DSFilterChipDropdownItemTokens: ODSFilterChipDropdownItemTokens =
    defaultODSFilterChipDropdownItemTokens

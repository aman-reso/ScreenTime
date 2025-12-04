package com.telekom.odsystem.componenttokens

/**
 * Created by dmarinopoulos on 26/2/24
 */

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSLinkTokens(
    var minHeight: Dp,
    var minWidth: Dp,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignmentAlignmentLeft: Alignment.Horizontal,
    var horizontalAlignmentAlignmentRight: Alignment.Horizontal,
    var horizontalAlignmentAlignmentCentered: Alignment.Horizontal,
    var horizontalArrangementAlignmentLeft: Arrangement.Horizontal,
    var horizontalArrangementAlignmentRight: Arrangement.Horizontal,
    var horizontalArrangementAlignmentCentered: Arrangement.Horizontal,
    var linkContainerGap: Dp,
    var linkContainerVerticalAlignment: Alignment.Vertical,
    var linkContainerHorizontalAlignment: Alignment.Horizontal,
    var linkContainerHorizontalArrangement: Arrangement.Horizontal,
    var linkContentVerticalAlignment: Alignment.Vertical,
    var linkContentHorizontalAlignment: Alignment.Horizontal,
    var linkContentVerticalArrangement: Arrangement.Vertical,
    var leftIconWidth: Dp,
    var leftIconHeight: Dp,
    var rightIconWidth: Dp,
    var rightIconHeight: Dp,
    var linkTextStyle: ODSTextStyle,
    var linkTextAlign: TextAlign,
    var underlineThickness: Dp, // Not exported from the plugin
    var underlineThicknessStatePressedDisabledFalse: Dp, // Not exported from the plugin
    var underlineThicknessStateHoveredDisabledFalse: Dp // Not exported from the plugin
)

var defaultODSLinkTokens = ODSLinkTokens(
    minHeight = DSVariables.sizingMinimumTappableArea,
    minWidth = DSVariables.sizingMinimumTappableArea,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignmentAlignmentLeft = Alignment.Start,
    horizontalAlignmentAlignmentRight = Alignment.End,
    horizontalAlignmentAlignmentCentered = Alignment.CenterHorizontally,
    horizontalArrangementAlignmentLeft = Arrangement.Start,
    horizontalArrangementAlignmentRight = Arrangement.End,
    horizontalArrangementAlignmentCentered = Arrangement.Center,
    linkContainerGap = DSVariables.spacingComponent2,
    linkContainerVerticalAlignment = Alignment.CenterVertically,
    linkContainerHorizontalAlignment = Alignment.Start,
    linkContainerHorizontalArrangement = Arrangement.Start,
    linkContentVerticalAlignment = Alignment.CenterVertically,
    linkContentHorizontalAlignment = Alignment.Start,
    linkContentVerticalArrangement = Arrangement.Center,
    leftIconWidth = DSVariables.sizingComponent7,
    leftIconHeight = DSVariables.sizingComponent7,
    rightIconWidth = DSVariables.sizingComponent7,
    rightIconHeight = DSVariables.sizingComponent7,
    linkTextStyle = DSTextStyles.bodyMBold,
    linkTextAlign = TextAlign.Left,
    underlineThickness = DSVariables.strokes1,
    underlineThicknessStatePressedDisabledFalse = DSVariables.strokes2,
    underlineThicknessStateHoveredDisabledFalse = DSVariables.strokes2
)

var DSLinkTokens: ODSLinkTokens = defaultODSLinkTokens

package com.telekom.odsystem.tokens.componenttokens

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

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-23 (v1.31.6) - uid: 427e2ad2
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=4627-4153
 */

data class ODSAccordionTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val headerIconGap: Dp,
    val headerIconPaddingSizeLarge: ODSPadding,
    val headerIconPaddingSizeSmall: ODSPadding,
    val headerIconCornerRadius: ODSCorners,
    val headerIconMinHeightSizeLarge: Dp,
    val headerIconMinHeightSizeSmall: Dp,
    val headerIconVerticalAlignment: Alignment.Vertical,
    val headerIconHorizontalAlignment: Alignment.Horizontal,
    val headerIconHorizontalArrangement: Arrangement.Horizontal,
    val headerStyleSizeLarge: ODSTextStyle,
    val headerStyleSizeSmall: ODSTextStyle,
    val headerTextAlign: TextAlign,
    val expandAndCollapseIconWidth: Dp,
    val expandAndCollapseIconHeight: Dp,
    val expandAndCollapseIconClipContent: Boolean,
    val expandAndCollapseIconVerticalAlignment: Alignment.Vertical,
    val expandAndCollapseIconHorizontalAlignment: Alignment.Horizontal,
    val expandAndCollapseIconHorizontalArrangement: Arrangement.Horizontal,
    val collapseDownWidthSizeLarge: Dp,
    val collapseDownWidthSizeSmall: Dp,
    val collapseDownHeightSizeLarge: Dp,
    val collapseDownHeightSizeSmall: Dp,
    val collapseUpWidthSizeLargeExpanded: Dp,
    val collapseUpWidthSizeSmallExpanded: Dp,
    val collapseUpHeightSizeLargeExpanded: Dp,
    val collapseUpHeightSizeSmallExpanded: Dp,
    val contentFrameVerticalAlignment: Alignment.Vertical,
    val contentFrameHorizontalAlignment: Alignment.Horizontal,
    val contentFrameVerticalArrangement: Arrangement.Vertical
)

val defaultODSAccordionTokens = ODSAccordionTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    headerIconGap = DSVariables.spacingComponent5,
    headerIconPaddingSizeLarge = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent5
    ),
    headerIconPaddingSizeSmall = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent5
    ),
    headerIconCornerRadius = ODSCorners(all = DSVariables.radiusSmall),
    headerIconMinHeightSizeLarge = DSVariables.sizingComponent16,
    headerIconMinHeightSizeSmall = 58.dp,
    headerIconVerticalAlignment = Alignment.CenterVertically,
    headerIconHorizontalAlignment = Alignment.Start,
    headerIconHorizontalArrangement = Arrangement.Start,
    headerStyleSizeLarge = DSTextStyles.subtitle,
    headerStyleSizeSmall = DSTextStyles.bodySRegular,
    headerTextAlign = TextAlign.Left,
    expandAndCollapseIconWidth = DSVariables.sizingMinimumTappableArea,
    expandAndCollapseIconHeight = DSVariables.sizingMinimumTappableArea,
    expandAndCollapseIconClipContent = true,
    expandAndCollapseIconVerticalAlignment = Alignment.CenterVertically,
    expandAndCollapseIconHorizontalAlignment = Alignment.CenterHorizontally,
    expandAndCollapseIconHorizontalArrangement = Arrangement.Center,
    collapseDownWidthSizeLarge = DSVariables.sizingComponent10,
    collapseDownWidthSizeSmall = DSVariables.sizingComponent8,
    collapseDownHeightSizeLarge = DSVariables.sizingComponent10,
    collapseDownHeightSizeSmall = DSVariables.sizingComponent8,
    collapseUpWidthSizeLargeExpanded = DSVariables.sizingComponent10,
    collapseUpWidthSizeSmallExpanded = DSVariables.sizingComponent8,
    collapseUpHeightSizeLargeExpanded = DSVariables.sizingComponent10,
    collapseUpHeightSizeSmallExpanded = DSVariables.sizingComponent8,
    contentFrameVerticalAlignment = Alignment.Top,
    contentFrameHorizontalAlignment = Alignment.Start,
    contentFrameVerticalArrangement = Arrangement.Top
)

var DSAccordionTokens: ODSAccordionTokens = defaultODSAccordionTokens

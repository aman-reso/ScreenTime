package com.telekom.odsystem.componenttokens

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

data class ODSTooltipTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val borderRadius: ODSCorners,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val labelTextStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelMaxWidth: Dp,
    val containerVerticalAlignmentTop: Alignment.Vertical, // Not exported from the plugin
    val containerVerticalAlignmentBottom: Alignment.Vertical, // Not exported from the plugin
    val containerHorizontalAlignmentStart: Alignment.Horizontal, // Not exported from the plugin
    val containerHorizontalAlignmentCenter: Alignment.Horizontal, // Not exported from the plugin
    val containerHorizontalAlignmentEnd: Alignment.Horizontal, // Not exported from the plugin
    val containerVerticalArrangementTop: Arrangement.Vertical, // Not exported from the plugin
    val containerVerticalArrangementBottom: Arrangement.Vertical, // Not exported from the plugin
    val containerHorizontalArrangementStart: Arrangement.Horizontal, // Not exported from the plugin
    val containerHorizontalArrangementCenter: Arrangement.Horizontal, // Not exported from the plugin
    val containerHorizontalArrangementEnd: Arrangement.Horizontal, // Not exported from the plugin
    val caretVerticalHeight: Dp, // Not exported from the plugin
    val caretVerticalWidth: Dp, // Not exported from the plugin
    val caretHorizontalHeight: Dp, // Not exported from the plugin
    val caretHorizontalWidth: Dp, // Not exported from the plugin
    val caretPaddingLeftAlignment: ODSPadding, // Not exported from the plugin
    val caretPaddingRightAlignment: ODSPadding, // Not exported from the plugin
    val caretPaddingTopAlignment: ODSPadding, // Not exported from the plugin
    val caretPaddingBottomAlignment: ODSPadding, // Not exported from the plugin
)

val defaultODSTooltipTokens = ODSTooltipTokens(
    gap = DSVariables.spacingComponent5,
    padding = ODSPadding(
        top = DSVariables.spacingComponent2,
        bottom = DSVariables.spacingComponent2,
        left = DSVariables.spacingComponent4,
        right = DSVariables.spacingComponent4
    ),
    borderRadius = ODSCorners(all = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    contentAlignment = Alignment.Center,
    labelTextStyle = DSTextStyles.bodySBold,
    labelTextAlign = TextAlign.Left,
    labelMaxWidth = 230.dp,
    containerVerticalAlignmentTop = Alignment.Top,
    containerVerticalAlignmentBottom = Alignment.Bottom,
    containerHorizontalAlignmentStart = Alignment.Start,
    containerHorizontalAlignmentCenter = Alignment.CenterHorizontally,
    containerHorizontalAlignmentEnd = Alignment.End,
    containerVerticalArrangementTop = Arrangement.Top,
    containerVerticalArrangementBottom = Arrangement.Bottom,
    containerHorizontalArrangementStart = Arrangement.Start,
    containerHorizontalArrangementCenter = Arrangement.Center,
    containerHorizontalArrangementEnd = Arrangement.End,
    caretVerticalHeight = 6.dp,
    caretVerticalWidth = 12.dp,
    caretHorizontalHeight = 12.dp,
    caretHorizontalWidth = 6.dp,
    caretPaddingLeftAlignment = ODSPadding(
        top = 0.dp,
        bottom = 0.dp,
        left = 12.dp,
        right = 0.dp
    ),
    caretPaddingRightAlignment = ODSPadding(
        top = 0.dp,
        bottom = 0.dp,
        left = 0.dp,
        right = 12.dp
    ),
    caretPaddingTopAlignment = ODSPadding(
        top = 7.dp,
        bottom = 0.dp,
        left = 0.dp,
        right = 0.dp
    ),
    caretPaddingBottomAlignment = ODSPadding(
        top = 0.dp,
        bottom = 7.dp,
        left = 0.dp,
        right = 0.dp
    ),
)

var DSTooltipTokens: ODSTooltipTokens = defaultODSTooltipTokens

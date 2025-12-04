package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-01 (v1.32.3) - uid: aa83a39
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-57227
 */

data class ODSDropdownSelectTokens(
    val zStackContentAlignment: Alignment,
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val contentAlignment: Alignment,
    val inputFieldGapSizeLarge: Dp,
    val inputFieldGapSizeSmall: Dp,
    val inputFieldPadding: ODSPadding,
    val inputFieldCornerRadius: ODSCorners,
    val inputFieldBorderModeStandard: Dp,
    val inputFieldBorderModeInformative: Dp,
    val inputFieldBorderModeError: Dp,
    val inputFieldMinHeightSizeLarge: Dp,
    val inputFieldMinHeightSizeSmall: Dp,
    val inputFieldVerticalAlignment: Alignment.Vertical,
    val inputFieldHorizontalAlignment: Alignment.Horizontal,
    val inputFieldHorizontalArrangement: Arrangement.Horizontal,
    val contentGapSizeLarge: Dp,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val eyebrowGap: Dp,
    val eyebrowVerticalAlignment: Alignment.Vertical,
    val eyebrowVerticalAlignmentSelected: Alignment.Vertical,
    val eyebrowHorizontalAlignment: Alignment.Horizontal,
    val eyebrowHorizontalArrangement: Arrangement.Horizontal,
    val labelStyleSizeLarge: ODSTextStyle,
    val labelStyleSizeSmall: ODSTextStyle,
    val labelStyleSizeLargeSelected: ODSTextStyle,
    val labelStyleSizeSmallSelected: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelOverflow: TextOverflow,
    val labelMaxLines: Int,
    val requiredStyleSizeLarge: ODSTextStyle,
    val requiredStyleSizeSmall: ODSTextStyle,
    val requiredStyleSizeLargeSelected: ODSTextStyle,
    val requiredStyleSizeSmallSelected: ODSTextStyle,
    val requiredTextAlign: TextAlign,
    val inputGap: Dp,
    val inputMinHeight: Dp,
    val inputClipContent: Boolean,
    val inputVerticalAlignment: Alignment.Vertical,
    val inputHorizontalAlignment: Alignment.Horizontal,
    val inputHorizontalArrangement: Arrangement.Horizontal,
    val iconWidth: Dp,
    val iconHeight: Dp,
    val valueStyleSizeLarge: ODSTextStyle,
    val valueStyleSizeSmall: ODSTextStyle,
    val valueTextAlign: TextAlign,
    val valueOverflow: TextOverflow,
    val valueMaxLines: Int,
    val expandAndCollapseIconPadding: ODSPadding,
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
    val collapseUpWidthSizeLarge: Dp,
    val collapseUpWidthSizeSmall: Dp,
    val collapseUpHeightSizeLarge: Dp,
    val collapseUpHeightSizeSmall: Dp,
    val supportTextPadding: ODSPadding,
    val supportTextVerticalAlignment: Alignment.Vertical,
    val supportTextHorizontalAlignment: Alignment.Horizontal,
    val supportTextHorizontalArrangement: Arrangement.Horizontal,
    val flyoutContainerAbsoluteOffsetSizeLarge: ODSOffset,
    val flyoutContainerAbsoluteOffsetSizeSmall: ODSOffset,
    val flyoutContainerAbsoluteContentAlignment: Alignment,
    val flyoutContainerVerticalAlignment: Alignment.Vertical,
    val flyoutContainerHorizontalAlignment: Alignment.Horizontal,
    val flyoutContainerHorizontalArrangement: Arrangement.Horizontal,
    val flyoutContainerVerticalArrangementSelectedModeErrorExpanded: Arrangement.Vertical,
    val flyoutContainerVerticalArrangementSelectedModeInformativeExpanded: Arrangement.Vertical,
    val flyoutContainerVerticalArrangementSelectedModeStandardExpanded: Arrangement.Vertical,
    val flyoutContainerVerticalArrangementModeErrorExpanded: Arrangement.Vertical,
    val flyoutContainerVerticalArrangementModeInformativeExpanded: Arrangement.Vertical,
    val flyoutContainerVerticalArrangementModeStandardExpanded: Arrangement.Vertical,
    val dropdownPaddingSizeLarge: ODSPadding, // Not exported from the plugin
    val dropdownPaddingSizeSmall: ODSPadding, // Not exported from the plugin
    val dropdownOffset: DpOffset, // Not exported from the plugin
    val dropdownBorderWidth: Dp, // Not exported from the plugin
)

val defaultODSDropdownSelectTokens = ODSDropdownSelectTokens(
    zStackContentAlignment = Alignment.TopStart,
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    contentAlignment = Alignment.TopStart,
    inputFieldGapSizeLarge = DSVariables.spacingComponent5,
    inputFieldGapSizeSmall = DSVariables.spacingComponent3,
    inputFieldPadding = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent5
    ),
    inputFieldCornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    inputFieldBorderModeStandard = DSVariables.strokes1,
    inputFieldBorderModeInformative = DSVariables.strokes1,
    inputFieldBorderModeError = DSVariables.strokes3,
    inputFieldMinHeightSizeLarge = DSVariables.sizingInputHeight,
    inputFieldMinHeightSizeSmall = 58.dp,
    inputFieldVerticalAlignment = Alignment.CenterVertically,
    inputFieldHorizontalAlignment = Alignment.Start,
    inputFieldHorizontalArrangement = Arrangement.Start,
    contentGapSizeLarge = DSVariables.spacingComponent2,
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentVerticalArrangement = Arrangement.Center,
    eyebrowGap = DSVariables.spacingComponent1,
    eyebrowVerticalAlignment = Alignment.CenterVertically,
    eyebrowVerticalAlignmentSelected = Alignment.Top,
    eyebrowHorizontalAlignment = Alignment.Start,
    eyebrowHorizontalArrangement = Arrangement.Start,
    labelStyleSizeLarge = DSTextStyles.subtitle,
    labelStyleSizeSmall = DSTextStyles.bodyMBold,
    labelStyleSizeLargeSelected = DSTextStyles.bodyMBold,
    labelStyleSizeSmallSelected = DSTextStyles.bodySBold,
    labelTextAlign = TextAlign.Left,
    labelOverflow = TextOverflow.Ellipsis,
    labelMaxLines = 1,
    requiredStyleSizeLarge = DSTextStyles.subtitle,
    requiredStyleSizeSmall = DSTextStyles.bodyMBold,
    requiredStyleSizeLargeSelected = DSTextStyles.bodyMBold,
    requiredStyleSizeSmallSelected = DSTextStyles.bodySBold,
    requiredTextAlign = TextAlign.Left,
    inputGap = DSVariables.spacingComponent3,
    inputMinHeight = DSVariables.sizingComponent10,
    inputClipContent = true,
    inputVerticalAlignment = Alignment.CenterVertically,
    inputHorizontalAlignment = Alignment.Start,
    inputHorizontalArrangement = Arrangement.Start,
    iconWidth = DSVariables.sizingComponent10,
    iconHeight = DSVariables.sizingComponent10,
    valueStyleSizeLarge = DSTextStyles.subtitle,
    valueStyleSizeSmall = DSTextStyles.bodyMBold,
    valueTextAlign = TextAlign.Left,
    valueOverflow = TextOverflow.Ellipsis,
    valueMaxLines = 1,
    expandAndCollapseIconPadding = ODSPadding(all = DSVariables.spacingComponent3),
    expandAndCollapseIconWidth = DSVariables.sizingComponent14,
    expandAndCollapseIconHeight = DSVariables.sizingComponent14,
    expandAndCollapseIconClipContent = true,
    expandAndCollapseIconVerticalAlignment = Alignment.CenterVertically,
    expandAndCollapseIconHorizontalAlignment = Alignment.CenterHorizontally,
    expandAndCollapseIconHorizontalArrangement = Arrangement.Center,
    collapseDownWidthSizeLarge = DSVariables.sizingComponent12,
    collapseDownWidthSizeSmall = DSVariables.sizingComponent10,
    collapseDownHeightSizeLarge = DSVariables.sizingComponent12,
    collapseDownHeightSizeSmall = DSVariables.sizingComponent10,
    collapseUpWidthSizeLarge = DSVariables.sizingComponent12,
    collapseUpWidthSizeSmall = DSVariables.sizingComponent10,
    collapseUpHeightSizeLarge = DSVariables.sizingComponent12,
    collapseUpHeightSizeSmall = DSVariables.sizingComponent10,
    supportTextPadding = ODSPadding(left = DSVariables.spacingComponent7),
    supportTextVerticalAlignment = Alignment.CenterVertically,
    supportTextHorizontalAlignment = Alignment.Start,
    supportTextHorizontalArrangement = Arrangement.Start,
    flyoutContainerAbsoluteOffsetSizeLarge = ODSOffset(y = 80.dp),
    flyoutContainerAbsoluteOffsetSizeSmall = ODSOffset(y = 66.dp),
    flyoutContainerAbsoluteContentAlignment = Alignment.TopStart,
    flyoutContainerVerticalAlignment = Alignment.Top,
    flyoutContainerHorizontalAlignment = Alignment.Start,
    flyoutContainerHorizontalArrangement = Arrangement.Start,
    flyoutContainerVerticalArrangementSelectedModeErrorExpanded = Arrangement.Top,
    flyoutContainerVerticalArrangementSelectedModeInformativeExpanded = Arrangement.Top,
    flyoutContainerVerticalArrangementSelectedModeStandardExpanded = Arrangement.Top,
    flyoutContainerVerticalArrangementModeErrorExpanded = Arrangement.Top,
    flyoutContainerVerticalArrangementModeInformativeExpanded = Arrangement.Top,
    flyoutContainerVerticalArrangementModeStandardExpanded = Arrangement.Top,
    dropdownPaddingSizeLarge = ODSPadding(
        top = 8.dp,
        bottom = 8.dp,
        left = 16.dp,
        right = 16.dp
    ), // Not exported from the plugin
    dropdownPaddingSizeSmall = ODSPadding(
        top = 4.dp,
        bottom = 4.dp,
        left = 12.dp,
        right = 12.dp
    ), // Not exported from the plugin
    dropdownOffset = DpOffset(
        DSVariables.spacingComponent0,
        DSVariables.spacingComponent3
    ), // Not exported from the plugin
    dropdownBorderWidth = 1.dp, // Not exported from the plugin
)

var DSDropdownSelectTokens: ODSDropdownSelectTokens = defaultODSDropdownSelectTokens

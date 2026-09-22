package com.telekom.odsystem.atoms.datepickerinputfield

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

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: 1bd061e7
 * Figma link: https://figma.com/design/ZSwasQrEi7Qi0JRbX3dMuB/Untitled?node-id=33-6401
 */

data class ODSDatePickerInputFieldTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val inputFieldPaddingSizeLarge: ODSPadding,
    val inputFieldPaddingSizeSmall: ODSPadding,
    val inputFieldCornerRadius: ODSCorners,
    val inputFieldBorderModeStandard: Dp,
    val inputFieldBorderModeInformative: Dp,
    val inputFieldBorderModeError: Dp,
    val inputFieldMinHeightSizeLarge: Dp,
    val inputFieldMinHeightSizeSmall: Dp,
    val inputFieldClipContent: Boolean,
    val inputFieldVerticalAlignment: Alignment.Vertical,
    val inputFieldHorizontalAlignment: Alignment.Horizontal,
    val inputFieldHorizontalArrangement: Arrangement.Horizontal,
    val contentGapSizeLarge: Dp,
    val contentPadding: ODSPadding,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val eyebrowGap: Dp,
    val eyebrowVerticalAlignment: Alignment.Vertical,
    val eyebrowHorizontalAlignment: Alignment.Horizontal,
    val eyebrowHorizontalArrangement: Arrangement.Horizontal,
    val labelStyleSizeLargeStatusUnfilled: ODSTextStyle,
    val labelStyleSizeSmallStatusUnfilled: ODSTextStyle,
    val labelStyleSizeLargeStatusEditing: ODSTextStyle,
    val labelStyleSizeLargeStatusFilled: ODSTextStyle,
    val labelStyleSizeSmallStatusFilled: ODSTextStyle,
    val labelStyleSizeSmallStatusEditing: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelOverflow: TextOverflow,
    val labelMaxLines: Int,
    val requiredStyleSizeLargeStatusUnfilled: ODSTextStyle,
    val requiredStyleSizeSmallStatusUnfilled: ODSTextStyle,
    val requiredStyleSizeLargeStatusEditing: ODSTextStyle,
    val requiredStyleSizeLargeStatusFilled: ODSTextStyle,
    val requiredStyleSizeSmallStatusFilled: ODSTextStyle,
    val requiredStyleSizeSmallStatusEditing: ODSTextStyle,
    val requiredTextAlign: TextAlign,
    val inputValueVerticalAlignment: Alignment.Vertical,
    val inputValueHorizontalAlignment: Alignment.Horizontal,
    val inputValueHorizontalArrangement: Arrangement.Horizontal,
    val placeholderStyleSizeLargeStatusEditing: ODSTextStyle,
    val placeholderStyleSizeLargeStatusUnfilled: ODSTextStyle,
    val placeholderStyleSizeSmallStatusEditing: ODSTextStyle,
    val placeholderStyleSizeSmallStatusUnfilled: ODSTextStyle,
    val placeholderTextAlignStatusEditing: TextAlign,
    val placeholderTextAlignStatusUnfilled: TextAlign,
    val placeholderOverflowStatusEditing: TextOverflow,
    val placeholderOverflowStatusUnfilled: TextOverflow,
    val placeholderMaxLinesStatusEditing: Int,
    val placeholderMaxLinesStatusUnfilled: Int,
    val dateInputStyleSizeLargeStatusFilled: ODSTextStyle,
    val dateInputStyleSizeSmallStatusFilled: ODSTextStyle,
    val dateInputTextAlignStatusFilled: TextAlign,
    val dateInputOverflowStatusFilled: TextOverflow,
    val dateInputMaxLinesStatusFilled: Int,
    val supportTextPadding: ODSPadding,
    val supportTextVerticalAlignment: Alignment.Vertical,
    val supportTextHorizontalAlignment: Alignment.Horizontal,
    val supportTextHorizontalArrangement: Arrangement.Horizontal,
    val contentContainerAlignment: Alignment // Not exported from plugin
)

val defaultODSDatePickerInputFieldTokens = ODSDatePickerInputFieldTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    inputFieldPaddingSizeLarge = ODSPadding(right = DSVariables.spacingComponent3),
    inputFieldPaddingSizeSmall = ODSPadding(right = DSVariables.spacingComponent5),
    inputFieldCornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    inputFieldBorderModeStandard = DSVariables.strokes1,
    inputFieldBorderModeInformative = DSVariables.strokes1,
    inputFieldBorderModeError = DSVariables.strokes3,
    inputFieldMinHeightSizeLarge = DSVariables.sizingInputHeight,
    inputFieldMinHeightSizeSmall = 58.dp,
    inputFieldClipContent = true,
    inputFieldVerticalAlignment = Alignment.CenterVertically,
    inputFieldHorizontalAlignment = Alignment.Start,
    inputFieldHorizontalArrangement = Arrangement.Start,
    contentGapSizeLarge = DSVariables.spacingComponent1,
    contentPadding = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent4
    ),
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentVerticalArrangement = Arrangement.Center,
    eyebrowGap = DSVariables.spacingComponent1,
    eyebrowVerticalAlignment = Alignment.CenterVertically,
    eyebrowHorizontalAlignment = Alignment.Start,
    eyebrowHorizontalArrangement = Arrangement.Start,
    labelStyleSizeLargeStatusUnfilled = DSTextStyles.subtitle,
    labelStyleSizeSmallStatusUnfilled = DSTextStyles.bodyMBold,
    labelStyleSizeLargeStatusEditing = DSTextStyles.bodyMBold,
    labelStyleSizeLargeStatusFilled = DSTextStyles.bodyMBold,
    labelStyleSizeSmallStatusFilled = DSTextStyles.bodySBold,
    labelStyleSizeSmallStatusEditing = DSTextStyles.bodySBold,
    labelTextAlign = TextAlign.Left,
    labelOverflow = TextOverflow.Ellipsis,
    labelMaxLines = 1,
    requiredStyleSizeLargeStatusUnfilled = DSTextStyles.subtitle,
    requiredStyleSizeSmallStatusUnfilled = DSTextStyles.bodyMBold,
    requiredStyleSizeLargeStatusEditing = DSTextStyles.bodyMBold,
    requiredStyleSizeLargeStatusFilled = DSTextStyles.bodyMBold,
    requiredStyleSizeSmallStatusFilled = DSTextStyles.bodySBold,
    requiredStyleSizeSmallStatusEditing = DSTextStyles.bodySBold,
    requiredTextAlign = TextAlign.Left,
    inputValueVerticalAlignment = Alignment.CenterVertically,
    inputValueHorizontalAlignment = Alignment.Start,
    inputValueHorizontalArrangement = Arrangement.Start,
    placeholderStyleSizeLargeStatusEditing = DSTextStyles.subtitle,
    placeholderStyleSizeLargeStatusUnfilled = DSTextStyles.subtitle,
    placeholderStyleSizeSmallStatusEditing = DSTextStyles.bodyMBold,
    placeholderStyleSizeSmallStatusUnfilled = DSTextStyles.bodyMBold,
    placeholderTextAlignStatusEditing = TextAlign.Left,
    placeholderTextAlignStatusUnfilled = TextAlign.Left,
    placeholderOverflowStatusEditing = TextOverflow.Ellipsis,
    placeholderOverflowStatusUnfilled = TextOverflow.Ellipsis,
    placeholderMaxLinesStatusEditing = 1,
    placeholderMaxLinesStatusUnfilled = 1,
    dateInputStyleSizeLargeStatusFilled = DSTextStyles.subtitle,
    dateInputStyleSizeSmallStatusFilled = DSTextStyles.bodyMBold,
    dateInputTextAlignStatusFilled = TextAlign.Left,
    dateInputOverflowStatusFilled = TextOverflow.Ellipsis,
    dateInputMaxLinesStatusFilled = 1,
    supportTextPadding = ODSPadding(left = DSVariables.spacingComponent7),
    supportTextVerticalAlignment = Alignment.Top,
    supportTextHorizontalAlignment = Alignment.Start,
    supportTextHorizontalArrangement = Arrangement.Start,
    contentContainerAlignment = Alignment.CenterStart
)

var DSDatePickerInputFieldTokens: ODSDatePickerInputFieldTokens =
    defaultODSDatePickerInputFieldTokens

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

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a7bb15c
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-22305
 */

data class ODSTextAreaTokens(
    val gap: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val inputFieldZStackMinHeight: Dp,
    val inputFieldGap: Dp,
    val inputFieldPadding: ODSPadding,
    val inputFieldCornerRadius: ODSCorners,
    val inputFieldBorderModeStandard: Dp,
    val inputFieldBorderModeInformative: Dp,
    val inputFieldBorderModeError: Dp,
    val inputFieldMinHeight: Dp,
    val inputFieldVerticalAlignment: Alignment.Vertical,
    val inputFieldHorizontalAlignment: Alignment.Horizontal,
    val inputFieldHorizontalArrangement: Arrangement.Horizontal,
    val contentGapSizeLarge: Dp,
    val contentGapSizeSmall: Dp,
    val contentClipContent: Boolean,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val eyebrowGap: Dp,
    val eyebrowVerticalAlignment: Alignment.Vertical,
    val eyebrowHorizontalAlignment: Alignment.Horizontal,
    val eyebrowHorizontalArrangement: Arrangement.Horizontal,
    val labelStyleSizeLarge: ODSTextStyle,
    val labelStyleSizeLargeFilled: ODSTextStyle,
    val labelStyleSizeSmall: ODSTextStyle,
    val labelStyleSizeSmallFilled: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val requiredStyleSizeLarge: ODSTextStyle,
    val requiredStyleSizeLargeFilled: ODSTextStyle,
    val requiredStyleSizeSmall: ODSTextStyle,
    val requiredStyleSizeSmallFilled: ODSTextStyle,
    val requiredTextAlign: TextAlign,
    val inputGap: Dp,
    val inputClipContent: Boolean,
    val inputVerticalAlignment: Alignment.Vertical,
    val inputHorizontalAlignment: Alignment.Horizontal,
    val inputHorizontalArrangement: Arrangement.Horizontal,
    val inputValueStyleSizeLarge: ODSTextStyle,
    val inputValueStyleSizeSmall: ODSTextStyle,
    val inputValueTextAlign: TextAlign,
    val inputValueHeightSizeLarge: Dp,
    val inputValueHeightSizeSmall: Dp,
    val supportTextPadding: ODSPadding,
    val supportTextVerticalAlignment: Alignment.Vertical,
    val supportTextHorizontalAlignment: Alignment.Horizontal,
    val supportTextHorizontalArrangement: Arrangement.Horizontal,
    val counterStyle: ODSTextStyle,
    val counterTextAlign: TextAlign,
    val counterWidth: Dp, // Not used in mobile
    var contentContainerAlignment: Alignment // Not exported from the plugin
)

val defaultODSTextAreaTokens = ODSTextAreaTokens(
    gap = DSVariables.spacingComponent3,
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    inputFieldZStackMinHeight = DSVariables.sizingComponent20,
    inputFieldGap = DSVariables.spacingComponent5,
    inputFieldPadding = ODSPadding(
        top = DSVariables.spacingComponent6,
        bottom = DSVariables.spacingComponent6,
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent7
    ),
    inputFieldCornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    inputFieldBorderModeStandard = DSVariables.strokes1,
    inputFieldBorderModeInformative = DSVariables.strokes1,
    inputFieldBorderModeError = DSVariables.strokes3,
    inputFieldMinHeight = DSVariables.sizingComponent20,
    inputFieldVerticalAlignment = Alignment.Top,
    inputFieldHorizontalAlignment = Alignment.Start,
    inputFieldHorizontalArrangement = Arrangement.Start,
    contentGapSizeLarge = DSVariables.spacingComponent1,
    contentGapSizeSmall = DSVariables.spacingComponent0,
    contentClipContent = true,
    contentVerticalAlignment = Alignment.Top,
    contentHorizontalAlignment = Alignment.Start,
    contentVerticalArrangement = Arrangement.Top,
    eyebrowGap = DSVariables.spacingComponent1,
    eyebrowVerticalAlignment = Alignment.CenterVertically,
    eyebrowHorizontalAlignment = Alignment.Start,
    eyebrowHorizontalArrangement = Arrangement.Start,
    labelStyleSizeLarge = DSTextStyles.subtitle,
    labelStyleSizeLargeFilled = DSTextStyles.bodyMBold,
    labelStyleSizeSmall = DSTextStyles.bodyMBold,
    labelStyleSizeSmallFilled = DSTextStyles.bodySBold,
    labelTextAlign = TextAlign.Left,
    requiredStyleSizeLarge = DSTextStyles.subtitle,
    requiredStyleSizeLargeFilled = DSTextStyles.bodyMBold,
    requiredStyleSizeSmall = DSTextStyles.bodyMBold,
    requiredStyleSizeSmallFilled = DSTextStyles.bodySBold,
    requiredTextAlign = TextAlign.Left,
    inputGap = DSVariables.spacingComponent3,
    inputClipContent = true,
    inputVerticalAlignment = Alignment.Top,
    inputHorizontalAlignment = Alignment.Start,
    inputHorizontalArrangement = Arrangement.Start,
    inputValueStyleSizeLarge = DSTextStyles.subtitle,
    inputValueStyleSizeSmall = DSTextStyles.bodyMBold,
    inputValueTextAlign = TextAlign.Left,
    inputValueHeightSizeLarge = 44.dp,
    inputValueHeightSizeSmall = 40.dp,
    supportTextPadding = ODSPadding(left = DSVariables.spacingComponent7),
    supportTextVerticalAlignment = Alignment.Top,
    supportTextHorizontalAlignment = Alignment.End,
    supportTextHorizontalArrangement = Arrangement.End,
    counterStyle = DSTextStyles.bodySRegular,
    counterTextAlign = TextAlign.Right,
    counterWidth = 80.dp,
    contentContainerAlignment = Alignment.CenterStart
)

var DSTextAreaTokens: ODSTextAreaTokens = defaultODSTextAreaTokens

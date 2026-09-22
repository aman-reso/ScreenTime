package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSFlyoutListItemLargeTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val borderRadius: ODSCorners,
    val minHeight: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val iconBeforeWidth: Dp,
    val iconBeforeHeight: Dp,
    val textGap: Dp,
    val textVerticalAlignment: Alignment.Vertical,
    val textHorizontalAlignment: Alignment.Horizontal,
    val textVerticalArrangement: Arrangement.Vertical,
    val iconAfterWidthVariantStandard: Dp,
    val iconAfterHeightVariantStandard: Dp,
    val checkmarkWidthVariantChecked: Dp,
    val checkmarkHeightVariantChecked: Dp,
    val labelTextStyle: ODSTextStyle,
    val labelTextAlign: TextAlign,
    val labelTextOverflow: TextOverflow,
    val helperTextTextStyle: ODSTextStyle,
    val helperTextTextAlign: TextAlign,
    val helperTextTextOverflow: TextOverflow
)

val defaultODSFlyoutListItemLargeTokens = ODSFlyoutListItemLargeTokens(
    gap = DSVariables.spacingComponent3,
    padding = ODSPadding(
        top = DSVariables.spacingComponent3,
        bottom = DSVariables.spacingComponent3,
        left = DSVariables.spacingComponent3,
        right = DSVariables.spacingComponent5
    ),
    borderRadius = ODSCorners(all = DSVariables.radiusSmall),
    minHeight = DSVariables.sizingInputHeight,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    iconBeforeWidth = DSVariables.sizingComponent10,
    iconBeforeHeight = DSVariables.sizingComponent10,
    textGap = DSVariables.spacingComponent1,
    textVerticalAlignment = Alignment.CenterVertically,
    textHorizontalAlignment = Alignment.Start,
    textVerticalArrangement = Arrangement.Center,
    iconAfterWidthVariantStandard = DSVariables.sizingComponent10,
    iconAfterHeightVariantStandard = DSVariables.sizingComponent10,
    checkmarkWidthVariantChecked = DSVariables.sizingComponent10,
    checkmarkHeightVariantChecked = DSVariables.sizingComponent10,
    labelTextStyle = DSTextStyles.subtitle,
    labelTextAlign = TextAlign.Left,
    labelTextOverflow = TextOverflow.Ellipsis,
    helperTextTextStyle = DSTextStyles.bodyMBold,
    helperTextTextAlign = TextAlign.Left,
    helperTextTextOverflow = TextOverflow.Ellipsis
)

var DSFlyoutListItemLargeTokens: ODSFlyoutListItemLargeTokens = defaultODSFlyoutListItemLargeTokens

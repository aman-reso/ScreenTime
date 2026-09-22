package com.telekom.odsystem.molecules.searchbar

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

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a705149
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-18282
 */

data class ODSSearchBarTokens(
    val gap: Dp,
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val border: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val horizontalArrangement: Arrangement.Horizontal,
    val contentClipContent: Boolean,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentHorizontalArrangement: Arrangement.Horizontal,
    val placeholderStyle: ODSTextStyle,
    val placeholderTextAlign: TextAlign,
    val placeholderOverflow: TextOverflow,
    val placeholderMaxLines: Int,
    val inputValueStyle: ODSTextStyle,
    val inputValueTextAlign: TextAlign,
    val inputValueOverflow: TextOverflow,
    val inputValueMaxLines: Int
)

val defaultODSSearchBarTokens = ODSSearchBarTokens(
    gap = DSVariables.spacingComponent4,
    padding = ODSPadding(
        left = DSVariables.spacingComponent7,
        right = DSVariables.spacingComponent5
    ),
    cornerRadius = ODSCorners(all = DSVariables.radiusFull),
    border = DSVariables.strokes1,
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    contentClipContent = true,
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentHorizontalArrangement = Arrangement.Start,
    placeholderStyle = DSTextStyles.bodyMBold,
    placeholderTextAlign = TextAlign.Left,
    placeholderOverflow = TextOverflow.Ellipsis,
    placeholderMaxLines = 1,
    inputValueStyle = DSTextStyles.bodyMBold,
    inputValueTextAlign = TextAlign.Left,
    inputValueOverflow = TextOverflow.Ellipsis,
    inputValueMaxLines = 1
)

var DSSearchBarTokens: ODSSearchBarTokens = defaultODSSearchBarTokens

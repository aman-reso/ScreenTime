package com.telekom.odsystem.molecules.fileupload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-29 (v1.32.3) - uid: 1dc2ba06
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=7904-10955
 */

data class ODSFileUploadTokens(
    val padding: ODSPadding,
    val cornerRadius: ODSCorners,
    val verticalAlignment: Alignment.Vertical,
    val horizontalAlignment: Alignment.Horizontal,
    val verticalArrangement: Arrangement.Vertical,
    val uploadContentGap: Dp,
    val uploadContentVerticalAlignment: Alignment.Vertical,
    val uploadContentHorizontalAlignment: Alignment.Horizontal,
    val uploadContentHorizontalArrangement: Arrangement.Horizontal,
    val iconProgressGap: Dp,
    val iconProgressVerticalAlignment: Alignment.Vertical,
    val iconProgressHorizontalAlignment: Alignment.Horizontal,
    val iconProgressHorizontalArrangement: Arrangement.Horizontal,
    val textLoadVerticalAlignmentTypeSimple: Alignment.Vertical,
    val textLoadHorizontalArrangementTypeSimple: Arrangement.Horizontal,
    val filenameStyleTypeSimple: ODSTextStyle,
    val filenameTextAlignTypeSimple: TextAlign
)

val defaultODSFileUploadTokens = ODSFileUploadTokens(
    padding = ODSPadding(
        top = DSVariables.spacingComponent5,
        bottom = DSVariables.spacingComponent5,
        left = DSVariables.spacingComponent6,
        right = DSVariables.spacingComponent5
    ),
    cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    uploadContentGap = DSVariables.spacingComponent5,
    uploadContentVerticalAlignment = Alignment.CenterVertically,
    uploadContentHorizontalAlignment = Alignment.Start,
    uploadContentHorizontalArrangement = Arrangement.Start,
    iconProgressGap = DSVariables.spacingComponent5,
    iconProgressVerticalAlignment = Alignment.CenterVertically,
    iconProgressHorizontalAlignment = Alignment.Start,
    iconProgressHorizontalArrangement = Arrangement.Start,
    textLoadVerticalAlignmentTypeSimple = Alignment.CenterVertically,
    textLoadHorizontalArrangementTypeSimple = Arrangement.SpaceBetween,
    filenameStyleTypeSimple = DSTextStyles.bodyMBold,
    filenameTextAlignTypeSimple = TextAlign.Left
)

var DSFileUploadTokens: ODSFileUploadTokens = defaultODSFileUploadTokens

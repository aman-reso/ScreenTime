package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSBottomSheetTitleLabelTokens(
    var padding: ODSPadding,
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var titleLabelTextStyle: ODSTextStyle,
    var titleLabelTextAlign: TextAlign
)

var defaultODSBottomSheetTitleLabelTokens = ODSBottomSheetTitleLabelTokens(
    padding = ODSPadding(
        top = DSVariables.spacingComponent4,
        bottom = DSVariables.spacingComponent4
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.Start,
    horizontalArrangement = Arrangement.Start,
    titleLabelTextStyle = DSTextStyles.titleL,
    titleLabelTextAlign = TextAlign.Left
)

var DSBottomSheetTitleLabelTokens: ODSBottomSheetTitleLabelTokens =
    defaultODSBottomSheetTitleLabelTokens

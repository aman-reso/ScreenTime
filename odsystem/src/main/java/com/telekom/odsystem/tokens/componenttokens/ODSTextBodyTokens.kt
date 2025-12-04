package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSTextBodyTokens(
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var verticalArrangement: Arrangement.Vertical,
    var paragraphTextStyle: ODSTextStyle,
    var paragraphTextAlign: TextAlign
)

var defaultODSTextBodyTokens = ODSTextBodyTokens(
    verticalAlignment = Alignment.Top,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top,
    paragraphTextStyle = DSTextStyles.bodyMRegular,
    paragraphTextAlign = TextAlign.Left
)

var DSTextBodyTokens: ODSTextBodyTokens = defaultODSTextBodyTokens

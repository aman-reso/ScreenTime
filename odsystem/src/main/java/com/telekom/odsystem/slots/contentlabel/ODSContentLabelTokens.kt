package com.telekom.odsystem.slots.contentlabel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSContentLabelTokens(
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal,
    var rightTextTextStyle: ODSTextStyle,
    var rightTextTextAlign: TextAlign
)

var defaultODSContentLabelTokens = ODSContentLabelTokens(
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.End,
    horizontalArrangement = Arrangement.End,
    rightTextTextStyle = DSTextStyles.titleS,
    rightTextTextAlign = TextAlign.Right
)

var DSContentLabelTokens: ODSContentLabelTokens = defaultODSContentLabelTokens

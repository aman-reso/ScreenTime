package com.telekom.odsystem.atoms.dim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import com.telekom.odsystem.componenttokens.DSDimTokens
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSDimStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSDimStyle {
        var style = ODSDimStyle()
        style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicModalOverlay))
        style.verticalAlignment = DSDimTokens.verticalAlignment
        style.horizontalAlignment = DSDimTokens.horizontalAlignment
        style.horizontalArrangement = DSDimTokens.horizontalArrangement
        return style
    }
}

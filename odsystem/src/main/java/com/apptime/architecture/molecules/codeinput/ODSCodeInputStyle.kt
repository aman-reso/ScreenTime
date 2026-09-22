package com.telekom.odsystem.molecules.codeinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSCodeInputTokens

class ODSCodeInputStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var inputContainerGap: Dp? = null
    var inputContainerVerticalAlignment: Alignment.Vertical? = null
    var inputContainerHorizontalAlignment: Alignment.Horizontal? = null
    var inputContainerHorizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(): ODSCodeInputStyle {
        val style = ODSCodeInputStyle()
        style.gap = DSCodeInputTokens.gap
        style.verticalAlignment = DSCodeInputTokens.verticalAlignment
        style.horizontalAlignment = DSCodeInputTokens.horizontalAlignment
        style.verticalArrangement = DSCodeInputTokens.verticalArrangement
        style.inputContainerGap = DSCodeInputTokens.inputContainerGap
        style.inputContainerVerticalAlignment = DSCodeInputTokens.inputContainerVerticalAlignment
        style.inputContainerHorizontalAlignment =
            DSCodeInputTokens.inputContainerHorizontalAlignment
        style.inputContainerHorizontalArrangement =
            DSCodeInputTokens.inputContainerHorizontalArrangement
        return style
    }
}

package com.telekom.odsystem.molecules.checkboxlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp

class ODSCheckboxListStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var listContainerGap: Dp? = null
    var listContainerVerticalAlignment: Alignment.Vertical? = null
    var listContainerHorizontalAlignment: Alignment.Horizontal? = null
    var listContainerVerticalArrangement: Arrangement.Vertical? = null
    fun getStyle(
        props: ODSCheckboxListProps
    ): ODSCheckboxListStyle {
        var style = ODSCheckboxListStyle()
        style.verticalAlignment = DSCheckboxListTokens.verticalAlignment
        style.horizontalAlignment = DSCheckboxListTokens.horizontalAlignment
        style.verticalArrangement = DSCheckboxListTokens.verticalArrangement
        style.listContainerVerticalAlignment = DSCheckboxListTokens.listContainerVerticalAlignment
        style.listContainerHorizontalAlignment =
            DSCheckboxListTokens.listContainerHorizontalAlignment
        style.listContainerVerticalArrangement =
            DSCheckboxListTokens.listContainerVerticalArrangement
        if (props.size == ODSCheckboxListSize.LARGE) {
            style.listContainerGap = DSCheckboxListTokens.listContainerGapSizeLarge
        }
        if (props.size == ODSCheckboxListSize.SMALL) {
            style.listContainerGap = DSCheckboxListTokens.listContainerGapSizeSmall
        }
        return style
    }
}

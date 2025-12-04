package com.telekom.odsystem.slots.productcardfeature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSProductCardFeatureStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var listContainerHorizontalGap: Dp? = null
    var listContainerVerticalGap: Dp? = null
    var listContainerVerticalAlignment: Alignment.Vertical? = null
    var listContainerHorizontalAlignment: Alignment.Horizontal? = null
    var listContainerHorizontalArrangement: Arrangement.Horizontal? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSProductCardFeatureStyle {
        val style = ODSProductCardFeatureStyle()
        style.verticalAlignment = DSProductCardFeatureTokens.verticalAlignment
        style.horizontalAlignment = DSProductCardFeatureTokens.horizontalAlignment
        style.horizontalArrangement = DSProductCardFeatureTokens.horizontalArrangement
        style.listContainerHorizontalGap = DSProductCardFeatureTokens.listContainerHorizontalGap
        style.listContainerVerticalGap = DSProductCardFeatureTokens.listContainerVerticalGap
        style.listContainerVerticalAlignment =
            DSProductCardFeatureTokens.listContainerVerticalAlignment
        style.listContainerHorizontalAlignment =
            DSProductCardFeatureTokens.listContainerHorizontalAlignment
        style.listContainerHorizontalArrangement =
            DSProductCardFeatureTokens.listContainerHorizontalArrangement
        return style
    }
}

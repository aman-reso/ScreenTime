package com.telekom.odsystem.molecules.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSListTokens
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-31 (v1.32.3) - uid: a70490f
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=5982-18024
 */
class ODSListStyle {
    var gap: Dp? = null
    var padding: ODSPadding? = null
    var cornerRadius: ODSCorners? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var listContainerGap: Dp? = null
    var listContainerVerticalAlignment: Alignment.Vertical? = null
    var listContainerHorizontalAlignment: Alignment.Horizontal? = null
    var listContainerVerticalArrangement: Arrangement.Vertical? = null
    var secondLevelPadding: ODSPadding? = null
    var secondLevelVerticalAlignment: Alignment.Vertical? = null
    var secondLevelHorizontalAlignment: Alignment.Horizontal? = null
    var secondLevelVerticalArrangement: Arrangement.Vertical? = null
    var listContainer2Gap: Dp? = null // Not used in mobile
    var listContainer2VerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var listContainer2HorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var listContainer2VerticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var thirdLevelPadding: ODSPadding? = null
    var thirdLevelVerticalAlignment: Alignment.Vertical? = null
    var thirdLevelHorizontalAlignment: Alignment.Horizontal? = null
    var thirdLevelVerticalArrangement: Arrangement.Vertical? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSListStyle {
        val style = ODSListStyle()
        style.gap = DSListTokens.gap
        style.padding = DSListTokens.padding
        style.cornerRadius = DSListTokens.cornerRadius
        style.verticalAlignment = DSListTokens.verticalAlignment
        style.horizontalAlignment = DSListTokens.horizontalAlignment
        style.verticalArrangement = DSListTokens.verticalArrangement
        style.listContainerGap = DSListTokens.listContainerGap
        style.listContainerVerticalAlignment = DSListTokens.listContainerVerticalAlignment
        style.listContainerHorizontalAlignment = DSListTokens.listContainerHorizontalAlignment
        style.listContainerVerticalArrangement = DSListTokens.listContainerVerticalArrangement
        style.secondLevelPadding = DSListTokens.secondLevelPadding
        style.secondLevelVerticalAlignment = DSListTokens.secondLevelVerticalAlignment
        style.secondLevelHorizontalAlignment = DSListTokens.secondLevelHorizontalAlignment
        style.secondLevelVerticalArrangement = DSListTokens.secondLevelVerticalArrangement
        style.listContainer2Gap = DSListTokens.listContainer2Gap
        style.listContainer2VerticalAlignment = DSListTokens.listContainer2VerticalAlignment
        style.listContainer2HorizontalAlignment = DSListTokens.listContainer2HorizontalAlignment
        style.listContainer2VerticalArrangement = DSListTokens.listContainer2VerticalArrangement
        style.thirdLevelPadding = DSListTokens.thirdLevelPadding
        style.thirdLevelVerticalAlignment = DSListTokens.thirdLevelVerticalAlignment
        style.thirdLevelHorizontalAlignment = DSListTokens.thirdLevelHorizontalAlignment
        style.thirdLevelVerticalArrangement = DSListTokens.thirdLevelVerticalArrangement
        return style
    }
}

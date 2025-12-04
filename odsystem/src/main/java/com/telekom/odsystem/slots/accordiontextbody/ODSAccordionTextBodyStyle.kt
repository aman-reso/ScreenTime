package com.telekom.odsystem.slots.accordiontextbody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.telekom.odsystem.tokens.componenttokens.DSAccordionTextBodyTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-23 (v1.31.6) - uid: 427e26ec
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=4627-4269
 */

class ODSAccordionTextBodyStyle {
    var padding: ODSPadding? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var paragraphStyle: ODSTextStyle? = null
    var paragraphColor: HexColor? = null
    var paragraphTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSAccordionTextBodyStyle {
        val style = ODSAccordionTextBodyStyle()
        style.padding = DSAccordionTextBodyTokens.padding
        style.verticalAlignment = DSAccordionTextBodyTokens.verticalAlignment
        style.horizontalAlignment = DSAccordionTextBodyTokens.horizontalAlignment
        style.verticalArrangement = DSAccordionTextBodyTokens.verticalArrangement
        style.paragraphStyle = DSAccordionTextBodyTokens.paragraphStyle
        style.paragraphColor = scheme.basicText
        style.paragraphTextAlign = DSAccordionTextBodyTokens.paragraphTextAlign
        return style
    }
}

package com.telekom.odsystem.molecules.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSDialogTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSDialogStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var borderRadius: ODSCorners? = null
    var boxShadow: ODSEffect? = null
    var minHeight: Dp? = null
    var width: Dp? = null // Not used in mobile
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var headerContainerBackgroundColor: List<ODSColorModel>? = null
    var headerContainerVerticalAlignment: Alignment.Vertical? = null
    var headerContainerHorizontalAlignment: Alignment.Horizontal? = null
    var headerContainerVerticalArrangement: Arrangement.Vertical? = null
    var titleContainerVerticalAlignment: Alignment.Vertical? = null
    var titleContainerHorizontalAlignment: Alignment.Horizontal? = null
    var titleContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var textContainerPadding: ODSPadding? = null
    var textContainerVerticalAlignment: Alignment.Vertical? = null
    var textContainerHorizontalAlignment: Alignment.Horizontal? = null
    var textContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var headerTextStyle: ODSTextStyle? = null
    var headerColor: HexColor? = null
    var headerTextAlign: TextAlign? = null
    var buttonContainerPadding: ODSPadding? = null
    var buttonContainerVerticalAlignment: Alignment.Vertical? = null
    var buttonContainerHorizontalAlignment: Alignment.Horizontal? = null
    var buttonContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var dividerContainerVerticalAlignment: Alignment.Vertical? = null
    var dividerContainerHorizontalAlignment: Alignment.Horizontal? = null
    var dividerContainerVerticalArrangement: Arrangement.Vertical? = null
    var scrollContainerGap: Dp? = null
    var scrollContainerClipContent: Boolean? = null
    var scrollContainerVerticalAlignment: Alignment.Vertical? = null
    var scrollContainerHorizontalAlignment: Alignment.Horizontal? = null
    var scrollContainerVerticalArrangement: Arrangement.Vertical? = null
    var textSectionPadding: ODSPadding? = null
    var textSectionClipContent: Boolean? = null
    var textSectionVerticalAlignment: Alignment.Vertical? = null
    var textSectionHorizontalAlignment: Alignment.Horizontal? = null
    var textSectionVerticalArrangement: Arrangement.Vertical? = null
    var textTextStyle: ODSTextStyle? = null
    var textColor: HexColor? = null
    var textTextAlign: TextAlign? = null
    var textTextOverflow: TextOverflow? = null
    var slotContainerPadding: ODSPadding? = null
    var slotContainerVerticalAlignment: Alignment.Vertical? = null
    var slotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var slotContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var actionSlotContainerPadding: ODSPadding? = null
    var actionSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var actionSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    // Custom addition
    var padding: ODSPadding? = null
    fun getStyle(
        scheme: ODSTheme
    ): ODSDialogStyle {
        val style = ODSDialogStyle()
        style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.borderRadius = DSDialogTokens.borderRadius
        style.boxShadow = scheme.elevationLevel4
        style.minHeight = DSDialogTokens.minHeight
        style.width = DSDialogTokens.width
        style.clipContent = DSDialogTokens.clipContent
        style.verticalAlignment = DSDialogTokens.verticalAlignment
        style.horizontalAlignment = DSDialogTokens.horizontalAlignment
        style.verticalArrangement = DSDialogTokens.verticalArrangement
        style.headerContainerBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.headerContainerVerticalAlignment = DSDialogTokens.headerContainerVerticalAlignment
        style.headerContainerHorizontalAlignment = DSDialogTokens.headerContainerHorizontalAlignment
        style.headerContainerVerticalArrangement = DSDialogTokens.headerContainerVerticalArrangement
        style.titleContainerVerticalAlignment = DSDialogTokens.titleContainerVerticalAlignment
        style.titleContainerHorizontalAlignment = DSDialogTokens.titleContainerHorizontalAlignment
        style.titleContainerHorizontalArrangement =
            DSDialogTokens.titleContainerHorizontalArrangement
        style.textContainerPadding = DSDialogTokens.textContainerPadding
        style.textContainerVerticalAlignment = DSDialogTokens.textContainerVerticalAlignment
        style.textContainerHorizontalAlignment = DSDialogTokens.textContainerHorizontalAlignment
        style.textContainerHorizontalArrangement = DSDialogTokens.textContainerHorizontalArrangement
        style.headerTextStyle = DSDialogTokens.headerTextStyle
        style.headerColor = scheme.basicText
        style.headerTextAlign = DSDialogTokens.headerTextAlign
        style.buttonContainerPadding = DSDialogTokens.buttonContainerPadding
        style.buttonContainerVerticalAlignment = DSDialogTokens.buttonContainerVerticalAlignment
        style.buttonContainerHorizontalAlignment = DSDialogTokens.buttonContainerHorizontalAlignment
        style.buttonContainerHorizontalArrangement =
            DSDialogTokens.buttonContainerHorizontalArrangement
        style.dividerContainerVerticalAlignment = DSDialogTokens.dividerContainerVerticalAlignment
        style.dividerContainerHorizontalAlignment =
            DSDialogTokens.dividerContainerHorizontalAlignment
        style.dividerContainerVerticalArrangement =
            DSDialogTokens.dividerContainerVerticalArrangement
        style.scrollContainerGap = DSDialogTokens.scrollContainerGap
        style.scrollContainerClipContent = DSDialogTokens.scrollContainerClipContent
        style.scrollContainerVerticalAlignment = DSDialogTokens.scrollContainerVerticalAlignment
        style.scrollContainerHorizontalAlignment = DSDialogTokens.scrollContainerHorizontalAlignment
        style.scrollContainerVerticalArrangement = DSDialogTokens.scrollContainerVerticalArrangement
        style.textSectionPadding = DSDialogTokens.textSectionPadding
        style.textSectionClipContent = DSDialogTokens.textSectionClipContent
        style.textSectionVerticalAlignment = DSDialogTokens.textSectionVerticalAlignment
        style.textSectionHorizontalAlignment = DSDialogTokens.textSectionHorizontalAlignment
        style.textSectionVerticalArrangement = DSDialogTokens.textSectionVerticalArrangement
        style.textTextStyle = DSDialogTokens.textTextStyle
        style.textColor = scheme.basicText
        style.textTextAlign = DSDialogTokens.textTextAlign
        style.textTextOverflow = DSDialogTokens.textTextOverflow
        style.slotContainerPadding = DSDialogTokens.slotContainerPadding
        style.slotContainerVerticalAlignment = DSDialogTokens.slotContainerVerticalAlignment
        style.slotContainerHorizontalAlignment = DSDialogTokens.slotContainerHorizontalAlignment
        style.slotContainerHorizontalArrangement = DSDialogTokens.slotContainerHorizontalArrangement
        style.actionSlotContainerPadding = DSDialogTokens.actionSlotContainerPadding
        style.actionSlotContainerVerticalAlignment =
            DSDialogTokens.actionSlotContainerVerticalAlignment
        style.actionSlotContainerHorizontalAlignment =
            DSDialogTokens.actionSlotContainerHorizontalAlignment
        style.actionSlotContainerVerticalArrangement =
            DSDialogTokens.actionSlotContainerVerticalArrangement
        style.padding = DSDialogTokens.padding
        return style
    }
}

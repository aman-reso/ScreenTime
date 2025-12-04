package com.telekom.odsystem.molecules.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSTabsTokens
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSTabsStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var dividerFrameBackgroundColor: List<ODSColorModel>? = null
    var dividerFrameBorderRadius: ODSCorners? = null
    var dividerFrameHeight: Dp? = null
    var dividerFrameOffset: ODSOffset? = null
    var dividerFrameVerticalAlignment: Alignment.Vertical? = null
    var dividerFrameHorizontalAlignment: Alignment.Horizontal? = null
    var dividerFrameHorizontalArrangement: Arrangement.Horizontal? = null
    var dividerFrameContentAlignment: Alignment? = null
    var listContainerGap: Dp? = null
    var listContainerVerticalAlignment: Alignment.Vertical? = null
    var listContainerHorizontalAlignment: Alignment.Horizontal? = null
    var listContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var lineContainerBorderRadius: ODSCorners? = null // Not exported from plugin
    var lineContainerHeight: Dp? = null // Not exported from plugin
    var lineContainerVerticalAlignment: Alignment.Vertical? = null // Not exported from plugin
    var lineContainerHorizontalAlignment: Alignment.Horizontal? = null // Not exported from plugin
    var lineContainerHorizontalArrangement: Arrangement.Horizontal? =
        null // Not exported from plugin
    var lineContainerBackgroundColor: List<ODSColorModel>? = null // Not exported from plugin

    fun getStyle(
        scheme: ODSTheme,
        props: ODSTabsProps,
        tabSelected: Boolean,
        state: ODSActions,
    ): ODSTabsStyle {
        val style = ODSTabsStyle()
        style.verticalAlignment = DSTabsTokens.verticalAlignment
        style.horizontalAlignment = DSTabsTokens.horizontalAlignment
        style.verticalArrangement = DSTabsTokens.verticalArrangement
        style.contentAlignment = DSTabsTokens.contentAlignment
        style.dividerFrameBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.interactionStatesDisabledBackgroundDisabled))
        style.dividerFrameBorderRadius = DSTabsTokens.dividerFrameBorderRadius
        style.dividerFrameHeight = DSTabsTokens.dividerFrameHeight
        style.dividerFrameOffset = DSTabsTokens.dividerFrameOffset
        style.dividerFrameVerticalAlignment = DSTabsTokens.dividerFrameVerticalAlignment
        style.dividerFrameHorizontalAlignment = DSTabsTokens.dividerFrameHorizontalAlignment
        style.dividerFrameHorizontalArrangement = DSTabsTokens.dividerFrameHorizontalArrangement
        style.dividerFrameContentAlignment = DSTabsTokens.dividerFrameContentAlignment
        style.listContainerVerticalAlignment = DSTabsTokens.listContainerVerticalAlignment
        style.listContainerHorizontalAlignment = DSTabsTokens.listContainerHorizontalAlignment
        style.listContainerHorizontalArrangement = DSTabsTokens.listContainerHorizontalArrangement
        if (props.size == ODSTabsSize.LARGE) {
            style.listContainerGap = DSTabsTokens.listContainerGapSizeLarge
        }
        if (props.size == ODSTabsSize.SMALL) {
            style.listContainerGap = DSTabsTokens.listContainerGapSizeSmall
        }

        // Custom additions
        style.lineContainerBorderRadius = DSTabsTokens.lineContainerBorderRadius
        style.lineContainerHeight = DSTabsTokens.lineContainerHeight
        style.lineContainerVerticalAlignment = DSTabsTokens.lineContainerVerticalAlignment
        style.lineContainerHorizontalAlignment = DSTabsTokens.lineContainerHorizontalAlignment
        style.lineContainerHorizontalArrangement =
            DSTabsTokens.lineContainerHorizontalArrangement
        style.lineContainerBackgroundColor =
            listOf(ODSColorModel(hexColor = scheme.basicAccent))
        if (state == ODSActions.HOVERED && tabSelected) {
            style.lineContainerBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesHoverAccentHover))
        }
        if (state == ODSActions.PRESSED && tabSelected) {
            style.lineContainerBackgroundColor =
                listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedAccentPressed))
        }

        return style
    }
}

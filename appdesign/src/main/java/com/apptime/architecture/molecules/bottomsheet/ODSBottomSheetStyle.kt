package com.telekom.odsystem.molecules.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-04 (v1.32.3) - uid: 2df8b99b
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=17924-192
 */
@Suppress("LongMethod")
class ODSBottomSheetStyle {
    var zStackWidth: Dp? = null // Not used in mobile
    var zStackClipContent: Boolean? = null
    var zStackContentAlignment: Alignment? = null
    var background: List<ODSColorModel>? = null
    var cornerRadius: ODSCorners? = null
    var width: Dp? = null // Not used in mobile
    var clipContent: Boolean? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var scrollContainerClipContent: Boolean? = null
    var scrollContainerVerticalAlignment: Alignment.Vertical? = null
    var scrollContainerHorizontalAlignment: Alignment.Horizontal? = null
    var scrollContainerVerticalArrangement: Arrangement.Vertical? = null
    var imageContainerZStackContentAlignment: Alignment? = null // Not used in mobile
    var imageContainerVerticalAlignment: Alignment.Vertical? = null // Not used in mobile
    var imageContainerHorizontalAlignment: Alignment.Horizontal? = null // Not used in mobile
    var imageContainerVerticalArrangement: Arrangement.Vertical? = null // Not used in mobile
    var imageContainerContentAlignment: Alignment? = null
    var imageContentScale: ContentScale? = null
    var titleContainerGap: Dp? = null
    var titleContainerBackground: List<ODSColorModel>? = null
    var titleContainerPadding: ODSPadding? = null
    var titleContainerVerticalAlignment: Alignment.Vertical? = null
    var titleContainerHorizontalAlignment: Alignment.Horizontal? = null
    var titleContainerVerticalArrangement: Arrangement.Vertical? = null
    var slotContainerPadding: ODSPadding? = null
    var slotContainerVerticalAlignment: Alignment.Vertical? = null
    var slotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var slotContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var dividerContainerHeight: Dp? = null
    var dividerContainerVerticalAlignment: Alignment.Vertical? = null
    var dividerContainerHorizontalAlignment: Alignment.Horizontal? = null
    var dividerContainerVerticalArrangement: Arrangement.Vertical? = null
    var slotComponentContainerPadding: ODSPadding? = null
    var slotComponentContainerVerticalAlignment: Alignment.Vertical? = null
    var slotComponentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var slotComponentContainerVerticalArrangement: Arrangement.Vertical? = null
    var actionSlotContainerPadding: ODSPadding? = null
    var actionSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var actionSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var odsCloseButtonAbsoluteContentAlignment: Alignment? = null
    var odsCloseButtonAbsoluteOffset: ODSOffset? = null
    var handleAbsoluteOffset: ODSOffset? = null
    var handleAbsoluteContentAlignment: Alignment? = null
    var handleBackground: List<ODSColorModel>? = null
    var handleCornerRadius: ODSCorners? = null
    var handleHeight: Dp? = null
    var handleWidth: Dp? = null
    var handleVerticalAlignment: Alignment.Vertical? = null
    var handleHorizontalAlignment: Alignment.Horizontal? = null
    var handleHorizontalArrangement: Arrangement.Horizontal? = null
    var dimColor: HexColor? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme
    ): ODSBottomSheetStyle {
        val style = ODSBottomSheetStyle()
        style.zStackWidth = DSBottomSheetTokens.zStackWidth
        style.zStackClipContent = DSBottomSheetTokens.zStackClipContent
        style.zStackContentAlignment = DSBottomSheetTokens.zStackContentAlignment
        style.background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.cornerRadius = DSBottomSheetTokens.cornerRadius
        style.width = DSBottomSheetTokens.width
        style.clipContent = DSBottomSheetTokens.clipContent
        style.verticalAlignment = DSBottomSheetTokens.verticalAlignment
        style.horizontalAlignment = DSBottomSheetTokens.horizontalAlignment
        style.verticalArrangement = DSBottomSheetTokens.verticalArrangement
        style.contentAlignment = DSBottomSheetTokens.contentAlignment
        style.scrollContainerClipContent = DSBottomSheetTokens.scrollContainerClipContent
        style.scrollContainerVerticalAlignment =
            DSBottomSheetTokens.scrollContainerVerticalAlignment
        style.scrollContainerHorizontalAlignment =
            DSBottomSheetTokens.scrollContainerHorizontalAlignment
        style.scrollContainerVerticalArrangement =
            DSBottomSheetTokens.scrollContainerVerticalArrangement
        style.imageContainerZStackContentAlignment =
            DSBottomSheetTokens.imageContainerZStackContentAlignment
        style.imageContainerVerticalAlignment = DSBottomSheetTokens.imageContainerVerticalAlignment
        style.imageContainerHorizontalAlignment =
            DSBottomSheetTokens.imageContainerHorizontalAlignment
        style.imageContainerVerticalArrangement =
            DSBottomSheetTokens.imageContainerVerticalArrangement
        style.imageContainerContentAlignment = DSBottomSheetTokens.imageContainerContentAlignment
        style.imageContentScale = DSBottomSheetTokens.imageContentScale
        style.titleContainerGap = DSBottomSheetTokens.titleContainerGap
        style.titleContainerBackground = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.titleContainerPadding = DSBottomSheetTokens.titleContainerPadding
        style.titleContainerVerticalAlignment = DSBottomSheetTokens.titleContainerVerticalAlignment
        style.titleContainerHorizontalAlignment =
            DSBottomSheetTokens.titleContainerHorizontalAlignment
        style.titleContainerVerticalArrangement =
            DSBottomSheetTokens.titleContainerVerticalArrangement
        style.slotContainerPadding = DSBottomSheetTokens.slotContainerPadding
        style.slotContainerVerticalAlignment = DSBottomSheetTokens.slotContainerVerticalAlignment
        style.slotContainerHorizontalAlignment =
            DSBottomSheetTokens.slotContainerHorizontalAlignment
        style.slotContainerHorizontalArrangement =
            DSBottomSheetTokens.slotContainerHorizontalArrangement
        style.dividerContainerHeight = DSBottomSheetTokens.dividerContainerHeight
        style.dividerContainerVerticalAlignment =
            DSBottomSheetTokens.dividerContainerVerticalAlignment
        style.dividerContainerHorizontalAlignment =
            DSBottomSheetTokens.dividerContainerHorizontalAlignment
        style.dividerContainerVerticalArrangement =
            DSBottomSheetTokens.dividerContainerVerticalArrangement
        style.slotComponentContainerPadding = DSBottomSheetTokens.slotComponentContainerPadding
        style.slotComponentContainerVerticalAlignment =
            DSBottomSheetTokens.slotComponentContainerVerticalAlignment
        style.slotComponentContainerHorizontalAlignment =
            DSBottomSheetTokens.slotComponentContainerHorizontalAlignment
        style.slotComponentContainerVerticalArrangement =
            DSBottomSheetTokens.slotComponentContainerVerticalArrangement
        style.actionSlotContainerPadding = DSBottomSheetTokens.actionSlotContainerPadding
        style.actionSlotContainerVerticalAlignment =
            DSBottomSheetTokens.actionSlotContainerVerticalAlignment
        style.actionSlotContainerHorizontalAlignment =
            DSBottomSheetTokens.actionSlotContainerHorizontalAlignment
        style.actionSlotContainerVerticalArrangement =
            DSBottomSheetTokens.actionSlotContainerVerticalArrangement
        style.odsCloseButtonAbsoluteContentAlignment =
            DSBottomSheetTokens.odsCloseButtonAbsoluteContentAlignment
        style.odsCloseButtonAbsoluteOffset = DSBottomSheetTokens.odsCloseButtonAbsoluteOffset
        style.handleAbsoluteOffset = DSBottomSheetTokens.handleAbsoluteOffset
        style.handleAbsoluteContentAlignment = DSBottomSheetTokens.handleAbsoluteContentAlignment
        style.handleBackground =
            listOf(ODSColorModel(hexColor = scheme.interactionStatesPressedBackgroundSubtlePressed))
        style.handleCornerRadius = DSBottomSheetTokens.handleCornerRadius
        style.handleHeight = DSBottomSheetTokens.handleHeight
        style.handleWidth = DSBottomSheetTokens.handleWidth
        style.handleVerticalAlignment = DSBottomSheetTokens.handleVerticalAlignment
        style.handleHorizontalAlignment = DSBottomSheetTokens.handleHorizontalAlignment
        style.handleHorizontalArrangement = DSBottomSheetTokens.handleHorizontalArrangement

        // Not exported from the plugin
        style.dimColor = scheme.basicModalOverlay
        return style
    }
}

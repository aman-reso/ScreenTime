package com.telekom.odsystem.organisms.popover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSPopoverTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.invertedScheme

@Suppress("All")
class ODSPopoverStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var maxWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentAlignment: Alignment? = null
    var titleCloseGap: Dp? = null
    var titleCloseVerticalAlignment: Alignment.Vertical? = null
    var titleCloseHorizontalAlignment: Alignment.Horizontal? = null
    var titleCloseHorizontalArrangement: Arrangement.Horizontal? = null
    var titleVerticalAlignment: Alignment.Vertical? = null
    var titleHorizontalAlignment: Alignment.Horizontal? = null
    var titleHorizontalArrangement: Arrangement.Horizontal? = null
    var labelTextStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var labelMaxWidth: Dp? = null
    var contentActionsGap: Dp? = null
    var contentActionsVerticalAlignment: Alignment.Vertical? = null
    var contentActionsHorizontalAlignment: Alignment.Horizontal? = null
    var contentActionsVerticalArrangement: Arrangement.Vertical? = null
    var contentGap: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var textTextStyle: ODSTextStyle? = null
    var textColor: HexColor? = null
    var textTextAlign: TextAlign? = null
    var textMaxWidth: Dp? = null
    var contentSlotContainerMaxHeight: Dp? = null
    var contentSlotContainerMaxWidth: Dp? = null
    var contentSlotContainerClipContent: Boolean? = null
    var contentSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var contentSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var actionSlotContainerGap: Dp? = null
    var actionSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var actionSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionSlotContainerHorizontalArrangement: Arrangement.Horizontal? = null
    var containerVerticalAlignment: Alignment.Vertical? = null // Not exported from the plugin
    var containerHorizontalAlignment: Alignment.Horizontal? = null // Not exported from the plugin
    var containerVerticalArrangement: Arrangement.Vertical? = null // Not exported from the plugin
    var containerHorizontalArrangement: Arrangement.Horizontal? =
        null // Not exported from the plugin
    var caretHeight: Dp? = null // Not exported from the plugin
    var caretWidth: Dp? = null // Not exported from the plugin
    var caretPadding: ODSPadding? = null // Not exported from the plugin
    fun getStyle(
        props: ODSPopoverProps
    ): ODSPopoverStyle {
        val style = ODSPopoverStyle()
        style.backgroundColor = listOf(ODSColorModel(hexColor = invertedScheme.basicBackground))
        style.padding = DSPopoverTokens.padding
        style.borderRadius = DSPopoverTokens.borderRadius
        style.maxWidth = DSPopoverTokens.maxWidth
        style.verticalAlignment = DSPopoverTokens.verticalAlignment
        style.horizontalAlignment = DSPopoverTokens.horizontalAlignment
        style.verticalArrangement = DSPopoverTokens.verticalArrangement
        style.contentAlignment = DSPopoverTokens.contentAlignment
        style.titleCloseGap = DSPopoverTokens.titleCloseGap
        style.titleCloseVerticalAlignment = DSPopoverTokens.titleCloseVerticalAlignment
        style.titleCloseHorizontalAlignment = DSPopoverTokens.titleCloseHorizontalAlignment
        style.titleCloseHorizontalArrangement = DSPopoverTokens.titleCloseHorizontalArrangement
        style.titleVerticalAlignment = DSPopoverTokens.titleVerticalAlignment
        style.titleHorizontalAlignment = DSPopoverTokens.titleHorizontalAlignment
        style.titleHorizontalArrangement = DSPopoverTokens.titleHorizontalArrangement
        style.labelTextStyle = DSPopoverTokens.labelTextStyle
        style.labelColor = invertedScheme.basicText
        style.labelTextAlign = DSPopoverTokens.labelTextAlign
        style.labelMaxWidth = DSPopoverTokens.labelMaxWidth
        style.contentActionsGap = DSPopoverTokens.contentActionsGap
        style.contentActionsVerticalAlignment = DSPopoverTokens.contentActionsVerticalAlignment
        style.contentActionsHorizontalAlignment = DSPopoverTokens.contentActionsHorizontalAlignment
        style.contentActionsVerticalArrangement = DSPopoverTokens.contentActionsVerticalArrangement
        style.contentGap = DSPopoverTokens.contentGap
        style.contentVerticalAlignment = DSPopoverTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSPopoverTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSPopoverTokens.contentVerticalArrangement
        style.textTextStyle = DSPopoverTokens.textTextStyle
        style.textColor = invertedScheme.basicText
        style.textTextAlign = DSPopoverTokens.textTextAlign
        style.textMaxWidth = DSPopoverTokens.textMaxWidth
        style.contentSlotContainerMaxHeight = DSPopoverTokens.contentSlotContainerMaxHeight
        style.contentSlotContainerMaxWidth = DSPopoverTokens.contentSlotContainerMaxWidth
        style.contentSlotContainerClipContent = DSPopoverTokens.contentSlotContainerClipContent
        style.contentSlotContainerVerticalAlignment =
            DSPopoverTokens.contentSlotContainerVerticalAlignment
        style.contentSlotContainerHorizontalAlignment =
            DSPopoverTokens.contentSlotContainerHorizontalAlignment
        style.contentSlotContainerVerticalArrangement =
            DSPopoverTokens.contentSlotContainerVerticalArrangement
        style.actionSlotContainerGap = DSPopoverTokens.actionSlotContainerGap
        style.actionSlotContainerVerticalAlignment =
            DSPopoverTokens.actionSlotContainerVerticalAlignment
        style.actionSlotContainerHorizontalAlignment =
            DSPopoverTokens.actionSlotContainerHorizontalAlignment
        style.actionSlotContainerHorizontalArrangement =
            DSPopoverTokens.actionSlotContainerHorizontalArrangement
        // Not exported from the plugin
        if (props.placement == ODSPopoverPlacement.TOP) {
            style.containerVerticalAlignment = DSPopoverTokens.containerVerticalAlignmentTop
            style.containerVerticalArrangement = DSPopoverTokens.containerVerticalArrangementTop
            style.caretWidth = DSPopoverTokens.caretVerticalWidth
            style.caretHeight = DSPopoverTokens.caretVerticalHeight
        }
        if (props.placement == ODSPopoverPlacement.BOTTOM) {
            style.containerVerticalAlignment = DSPopoverTokens.containerVerticalAlignmentBottom
            style.containerVerticalArrangement = DSPopoverTokens.containerVerticalArrangementBottom
            style.caretWidth = DSPopoverTokens.caretVerticalWidth
            style.caretHeight = DSPopoverTokens.caretVerticalHeight
        }
        if (props.placement == ODSPopoverPlacement.LEFT) {
            style.containerHorizontalAlignment = DSPopoverTokens.containerHorizontalAlignmentStart
            style.containerHorizontalArrangement =
                DSPopoverTokens.containerHorizontalArrangementStart
            style.containerVerticalAlignment = Alignment.CenterVertically
            style.caretWidth = DSPopoverTokens.caretHorizontalWidth
            style.caretHeight = DSPopoverTokens.caretHorizontalHeight
        }
        if (props.placement == ODSPopoverPlacement.RIGHT) {
            style.containerHorizontalAlignment = DSPopoverTokens.containerHorizontalAlignmentEnd
            style.containerHorizontalArrangement = DSPopoverTokens.containerHorizontalArrangementEnd
            style.containerVerticalAlignment = Alignment.CenterVertically
            style.caretWidth = DSPopoverTokens.caretHorizontalWidth
            style.caretHeight = DSPopoverTokens.caretHorizontalHeight
        }
        if (props.placement == ODSPopoverPlacement.TOP || props.placement == ODSPopoverPlacement.BOTTOM) {
            if (props.alignment == ODSPopoverAlignment.CENTER) {
                style.containerHorizontalAlignment =
                    DSPopoverTokens.containerHorizontalAlignmentCenter
                style.containerHorizontalArrangement =
                    DSPopoverTokens.containerHorizontalArrangementCenter
            }
            if (props.alignment == ODSPopoverAlignment.START) {
                style.containerHorizontalAlignment =
                    DSPopoverTokens.containerHorizontalAlignmentStart
                style.containerHorizontalArrangement =
                    DSPopoverTokens.containerHorizontalArrangementStart
                style.caretPadding = DSPopoverTokens.caretPaddingLeftAlignment
            }
            if (props.alignment == ODSPopoverAlignment.END) {
                style.containerHorizontalAlignment = DSPopoverTokens.containerHorizontalAlignmentEnd
                style.containerHorizontalArrangement =
                    DSPopoverTokens.containerHorizontalArrangementEnd
                style.caretPadding = DSPopoverTokens.caretPaddingRightAlignment
            }
        }
        if (props.placement == ODSPopoverPlacement.LEFT || props.placement == ODSPopoverPlacement.RIGHT) {
            if (props.alignment == ODSPopoverAlignment.CENTER) {
                style.containerVerticalAlignment = Alignment.CenterVertically
            }
            if (props.alignment == ODSPopoverAlignment.START) {
                style.containerVerticalAlignment = Alignment.Top
                style.caretPadding = DSPopoverTokens.caretPaddingTopAlignment
            }
            if (props.alignment == ODSPopoverAlignment.END) {
                style.containerVerticalAlignment = Alignment.Bottom
                style.caretPadding = DSPopoverTokens.caretPaddingBottomAlignment
            }
        }
        return style
    }
}

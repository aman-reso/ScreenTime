package com.telekom.odsystem.organisms.cardbasic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSCardBasicTokens
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSCardBasicStyle {
    var width: Dp? = null // Not used in mobile
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var contentGap: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var contentContentAlignment: Alignment? = null
    var cardBgBackgroundColor: List<ODSColorModel>? = null
    var cardBgBorderRadius: ODSCorners? = null
    var cardBgBoxShadow: ODSEffect? = null
    var cardBgClipContent: Boolean? = null
    var cardBgVerticalAlignment: Alignment.Vertical? = null
    var cardBgHorizontalAlignment: Alignment.Horizontal? = null
    var cardBgVerticalArrangement: Arrangement.Vertical? = null
    var copyGap: Dp? = null
    var copyVerticalAlignment: Alignment.Vertical? = null
    var copyHorizontalAlignment: Alignment.Horizontal? = null
    var copyVerticalArrangement: Arrangement.Vertical? = null
    var actionContainerVerticalAlignment: Alignment.Vertical? = null
    var actionContainerHorizontalAlignment: Alignment.Horizontal? = null
    var actionContainerVerticalArrangement: Arrangement.Vertical? = null
    var scaleFactor: Float? = null // Not exported from plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardBasicProps,
        state: ODSActions
    ): ODSCardBasicStyle {
        val style = ODSCardBasicStyle()
        style.width = DSCardBasicTokens.width
        style.verticalAlignment = DSCardBasicTokens.verticalAlignment
        style.horizontalAlignment = DSCardBasicTokens.horizontalAlignment
        style.verticalArrangement = DSCardBasicTokens.verticalArrangement
        style.contentPadding = DSCardBasicTokens.contentPadding
        style.contentHorizontalAlignment = DSCardBasicTokens.contentHorizontalAlignment
        style.contentContentAlignment = DSCardBasicTokens.contentContentAlignment
//        if (props.customHeight) {
//            style.contentVerticalArrangement =
//                DSCardBasicTokens.contentVerticalArrangementCustomHeight
//        }
//        if (!props.customHeight) {
        style.contentGap = DSCardBasicTokens.contentGap
        style.contentVerticalAlignment = DSCardBasicTokens.contentVerticalAlignment
        style.contentVerticalArrangement = DSCardBasicTokens.contentVerticalArrangement
//        }
        style.cardBgBackgroundColor = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        style.cardBgBorderRadius = DSCardBasicTokens.cardBgBorderRadius
        style.cardBgBoxShadow = scheme.elevationLevel4
        style.cardBgClipContent = DSCardBasicTokens.cardBgClipContent
        style.cardBgVerticalAlignment = DSCardBasicTokens.cardBgVerticalAlignment
        style.cardBgHorizontalAlignment = DSCardBasicTokens.cardBgHorizontalAlignment
        style.cardBgVerticalArrangement = DSCardBasicTokens.cardBgVerticalArrangement
        if (state == ODSActions.HOVERED) {
            style.cardBgBoxShadow = scheme.elevationLevel6
        }
        if (state == ODSActions.PRESSED) {
            style.cardBgBoxShadow = scheme.elevationLevel2
        }
        style.copyGap = DSCardBasicTokens.copyGap
        style.copyVerticalAlignment = DSCardBasicTokens.copyVerticalAlignment
        style.copyHorizontalAlignment = DSCardBasicTokens.copyHorizontalAlignment
        style.copyVerticalArrangement = DSCardBasicTokens.copyVerticalArrangement
//        if (props.customHeight) {
//            style.actionContainerVerticalAlignment =
//                DSCardBasicTokens.actionContainerVerticalAlignmentCustomHeight
//            style.actionContainerHorizontalAlignment =
//                DSCardBasicTokens.actionContainerHorizontalAlignmentCustomHeight
//            style.actionContainerVerticalArrangement =
//                DSCardBasicTokens.actionContainerVerticalArrangementCustomHeight
//        }
//        if (!props.customHeight) {
        style.actionContainerVerticalAlignment =
            DSCardBasicTokens.actionContainerVerticalAlignment
        style.actionContainerHorizontalAlignment =
            DSCardBasicTokens.actionContainerHorizontalAlignment
        style.actionContainerVerticalArrangement =
            DSCardBasicTokens.actionContainerVerticalArrangement
//        }
        // Custom addition
        style.scaleFactor = DSCardBasicTokens.scaleFactor
        return style
    }
}

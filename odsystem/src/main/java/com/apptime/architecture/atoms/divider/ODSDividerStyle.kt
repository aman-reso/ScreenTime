package com.telekom.odsystem.atoms.divider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSDividerTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

class ODSDividerStyle {
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var padding: ODSPadding? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var backgroundColor: HexColor? = null // Not exported from the plugin
    var thickness: Dp? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSDividerProps
    ): ODSDividerStyle {
        var style = ODSDividerStyle()
        if (props.variant == ODSDividerVariant.VERTICAL) {
            style.verticalAlignment = DSDividerTokens.verticalAlignmentTypeVertical
            style.horizontalAlignment = DSDividerTokens.horizontalAlignmentTypeVertical
            style.horizontalArrangement = DSDividerTokens.horizontalArrangementTypeVertical
        }
        if (props.variant == ODSDividerVariant.HORIZONTAL) {
            style.verticalAlignment = DSDividerTokens.verticalAlignmentTypeHorizontal
            style.horizontalAlignment = DSDividerTokens.horizontalAlignmentTypeHorizontal
            style.verticalArrangement = DSDividerTokens.verticalArrangementTypeHorizontal
        }
        if (props.variant == ODSDividerVariant.VERTICAL && props.inset && !props.spacing) {
            style.padding = DSDividerTokens.paddingTypeVerticalInset
        }
        if (props.variant == ODSDividerVariant.VERTICAL && !props.inset && props.spacing) {
            style.padding = DSDividerTokens.paddingTypeVerticalSpacing
        }
        if (props.variant == ODSDividerVariant.VERTICAL && props.inset && props.spacing) {
            style.padding = DSDividerTokens.paddingTypeVerticalInsetSpacing
        }
        if (props.variant == ODSDividerVariant.HORIZONTAL && props.inset && !props.spacing) {
            style.padding = DSDividerTokens.paddingTypeHorizontalInset
        }
        if (props.variant == ODSDividerVariant.HORIZONTAL && !props.inset && props.spacing) {
            style.padding = DSDividerTokens.paddingTypeHorizontalSpacing
        }
        if (props.variant == ODSDividerVariant.HORIZONTAL && props.inset && props.spacing) {
            style.padding = DSDividerTokens.paddingTypeHorizontalInsetSpacing
        }
        // Not exported from the plugin
        style.thickness = DSDividerTokens.thickness
        style.backgroundColor = scheme.basicStrokeSubtle
        return style
    }
}

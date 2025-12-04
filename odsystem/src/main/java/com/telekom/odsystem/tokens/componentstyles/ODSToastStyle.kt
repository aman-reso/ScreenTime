package com.telekom.odsystem.componentstyles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.componenttokens.DSToastTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.invertedScheme
import com.telekom.odsystem.organisms.toast.ODSToastProps
import com.telekom.odsystem.organisms.toast.ODSToastMode
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSToastStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
    var boxShadow: ODSEffect? = null
    var maxWidth: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var notificationGap: Dp? = null
    var notificationPadding: ODSPadding? = null
    var notificationVerticalAlignment: Alignment.Vertical? = null
    var notificationHorizontalAlignment: Alignment.Horizontal? = null
    var notificationHorizontalArrangement: Arrangement.Horizontal? = null
    var successColor: HexColor? = null
    var successWidth: Dp? = null
    var successHeight: Dp? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var titleTextGap: Dp? = null
    var titleTextPadding: ODSPadding? = null
    var titleTextVerticalAlignment: Alignment.Vertical? = null
    var titleTextHorizontalAlignment: Alignment.Horizontal? = null
    var titleTextVerticalArrangement: Arrangement.Vertical? = null
    var titleTextStyle: ODSTextStyle? = null
    var titleColor: HexColor? = null
    var titleTextAlign: TextAlign? = null
    var textTextStyle: ODSTextStyle? = null
    var textColor: HexColor? = null
    var textTextAlign: TextAlign? = null
    var linksHorizontalGap: Dp? = null
    var linksVerticalAlignment: Alignment.Vertical? = null
    var linksHorizontalAlignment: Alignment.Horizontal? = null
    var linksHorizontalArrangement: Arrangement.Horizontal? = null
    var informationColor: HexColor? = null
    var informationWidth: Dp? = null
    var informationHeight: Dp? = null
    var spacingPadding: ODSPadding? = null
    var spacingVerticalAlignment: Alignment.Vertical? = null
    var spacingHorizontalAlignment: Alignment.Horizontal? = null
    var spacingHorizontalArrangement: Arrangement.Horizontal? = null
    var scheme: ODSTheme? = null // Not exported from the plugin
    var closeButtonIconModel: ODSIconModel? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSToastProps
    ): ODSToastStyle {
        val style = ODSToastStyle()
        style.scheme = invertedScheme // Custom change
        style.backgroundColor = listOf(ODSColorModel(hexColor = invertedScheme.basicBackground)) // Custom change
        style.padding = DSToastTokens.padding
        style.borderRadius = DSToastTokens.borderRadius
        style.boxShadow = invertedScheme.elevationLevel0 // Custom change
        style.maxWidth = DSToastTokens.maxWidth
        style.minWidth = DSToastTokens.minWidth
        style.verticalAlignment = DSToastTokens.verticalAlignment
        style.horizontalArrangement = DSToastTokens.horizontalArrangement
        style.notificationGap = DSToastTokens.notificationGap
        style.notificationPadding = DSToastTokens.notificationPadding
        style.notificationVerticalAlignment = DSToastTokens.notificationVerticalAlignment
        style.notificationHorizontalAlignment = DSToastTokens.notificationHorizontalAlignment
        style.notificationHorizontalArrangement = DSToastTokens.notificationHorizontalArrangement
        if (props.mode == ODSToastMode.SUCCESS) {
            style.successColor = invertedScheme.functionalSuccessStandard // Custom change
            style.successWidth = DSToastTokens.successWidthTypeSuccess
            style.successHeight = DSToastTokens.successHeightTypeSuccess
        }
        style.contentVerticalAlignment = DSToastTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSToastTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSToastTokens.contentVerticalArrangement
        style.titleTextGap = DSToastTokens.titleTextGap
        style.titleTextPadding = DSToastTokens.titleTextPadding
        style.titleTextVerticalAlignment = DSToastTokens.titleTextVerticalAlignment
        style.titleTextHorizontalAlignment = DSToastTokens.titleTextHorizontalAlignment
        style.titleTextVerticalArrangement = DSToastTokens.titleTextVerticalArrangement
        style.titleTextStyle = DSToastTokens.titleTextStyle
        style.titleColor = invertedScheme.basicText
        style.titleTextAlign = DSToastTokens.titleTextAlign
        style.textTextStyle = DSToastTokens.textTextStyle
        style.textColor = invertedScheme.basicText
        style.textTextAlign = DSToastTokens.textTextAlign
        style.linksHorizontalGap = DSToastTokens.linksHorizontalGap
        style.linksVerticalAlignment = DSToastTokens.linksVerticalAlignment
        style.linksHorizontalAlignment = DSToastTokens.linksHorizontalAlignment
        style.linksHorizontalArrangement = DSToastTokens.linksHorizontalArrangement
        if (props.mode == ODSToastMode.INFORMATIVE) {
            style.informationColor = invertedScheme.basicTextRecessive // Custom change
            style.informationWidth = DSToastTokens.informationWidthTypeInformation
            style.informationHeight = DSToastTokens.informationHeightTypeInformation
        }
        style.spacingPadding = DSToastTokens.spacingPadding
        style.spacingVerticalAlignment = DSToastTokens.spacingVerticalAlignment
        style.spacingHorizontalAlignment = DSToastTokens.spacingHorizontalAlignment
        style.spacingHorizontalArrangement = DSToastTokens.spacingHorizontalArrangement

        // Custom addition
        style.closeButtonIconModel = DSToastTokens.closeButtonIconModel
        return style
    }
}

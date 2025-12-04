package com.telekom.odsystem.componentstyles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.componenttokens.DSBannerTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.invertedScheme
import com.telekom.odsystem.organisms.banner.ODSBannerMode
import com.telekom.odsystem.organisms.banner.ODSBannerProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSBannerStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var borderRadius: ODSCorners? = null
    var boxShadow: ODSEffect? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var maxWidthWrapperPadding: ODSPadding? = null
    var maxWidthWrapperMaxWidth: Dp? = null
    var maxWidthWrapperVerticalAlignment: Alignment.Vertical? = null
    var maxWidthWrapperHorizontalArrangement: Arrangement.Horizontal? = null
    var notificationGap: Dp? = null
    var notificationPadding: ODSPadding? = null
    var notificationVerticalAlignment: Alignment.Vertical? = null
    var notificationHorizontalAlignment: Alignment.Horizontal? = null
    var notificationHorizontalArrangement: Arrangement.Horizontal? = null
    var successColor: HexColor? = null
    var successWidth: Dp? = null
    var successHeight: Dp? = null
    var contentPadding: ODSPadding? = null
    var contentVerticalAlignment: Alignment.Vertical? = null
    var contentHorizontalAlignment: Alignment.Horizontal? = null
    var contentVerticalArrangement: Arrangement.Vertical? = null
    var titleTextGap: Dp? = null
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
    var warningColor: HexColor? = null
    var warningWidth: Dp? = null
    var warningHeight: Dp? = null
    var errorColor: HexColor? = null
    var errorWidth: Dp? = null
    var errorHeight: Dp? = null
    var spacingPadding: ODSPadding? = null
    var spacingVerticalAlignment: Alignment.Vertical? = null
    var spacingHorizontalAlignment: Alignment.Horizontal? = null
    var spacingHorizontalArrangement: Arrangement.Horizontal? = null
    var scheme: ODSTheme? = null // Not exported from the plugin
    var closeButtonIconModel: ODSIconModel? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSBannerProps
    ): ODSBannerStyle {
        val style = ODSBannerStyle()
        style.scheme = invertedScheme
        style.backgroundColor = listOf(ODSColorModel(hexColor = invertedScheme.basicBackground))
        style.borderRadius = DSBannerTokens.borderRadius
        style.boxShadow = invertedScheme.elevationLevel0
        style.minWidth = DSBannerTokens.minWidth
        style.verticalAlignment = DSBannerTokens.verticalAlignment
        style.horizontalArrangement = DSBannerTokens.horizontalArrangement
        style.maxWidthWrapperPadding = DSBannerTokens.maxWidthWrapperPadding
        style.maxWidthWrapperMaxWidth = DSBannerTokens.maxWidthWrapperMaxWidth
        style.maxWidthWrapperVerticalAlignment = DSBannerTokens.maxWidthWrapperVerticalAlignment
        style.maxWidthWrapperHorizontalArrangement =
            DSBannerTokens.maxWidthWrapperHorizontalArrangement
        style.notificationGap = DSBannerTokens.notificationGap
        style.notificationPadding = DSBannerTokens.notificationPadding
        style.notificationVerticalAlignment = DSBannerTokens.notificationVerticalAlignment
        style.notificationHorizontalAlignment = DSBannerTokens.notificationHorizontalAlignment
        style.notificationHorizontalArrangement = DSBannerTokens.notificationHorizontalArrangement
        if (props.mode == ODSBannerMode.SUCCESS) {
            style.successColor = invertedScheme.functionalSuccessStandard
            style.successWidth = DSBannerTokens.successWidthTypeSuccess
            style.successHeight = DSBannerTokens.successHeightTypeSuccess
        }
        style.contentPadding = DSBannerTokens.contentPadding
        style.contentVerticalAlignment = DSBannerTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSBannerTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSBannerTokens.contentVerticalArrangement
        style.titleTextGap = DSBannerTokens.titleTextGap
        style.titleTextVerticalAlignment = DSBannerTokens.titleTextVerticalAlignment
        style.titleTextHorizontalAlignment = DSBannerTokens.titleTextHorizontalAlignment
        style.titleTextVerticalArrangement = DSBannerTokens.titleTextVerticalArrangement
        style.titleTextStyle = DSBannerTokens.titleTextStyle
        style.titleColor = invertedScheme.basicText
        style.titleTextAlign = DSBannerTokens.titleTextAlign
        style.textTextStyle = DSBannerTokens.textTextStyle
        style.textColor = invertedScheme.basicText
        style.textTextAlign = DSBannerTokens.textTextAlign
        style.linksHorizontalGap = DSBannerTokens.linksHorizontalGap
        style.linksVerticalAlignment = DSBannerTokens.linksVerticalAlignment
        style.linksHorizontalAlignment = DSBannerTokens.linksHorizontalAlignment
        style.linksHorizontalArrangement = DSBannerTokens.linksHorizontalArrangement
        if (props.mode == ODSBannerMode.INFORMATIVE) {
            style.informationColor = invertedScheme.basicTextRecessive
            style.informationWidth = DSBannerTokens.informationWidthTypeInformation
            style.informationHeight = DSBannerTokens.informationHeightTypeInformation
        }
        if (props.mode == ODSBannerMode.WARNING) {
            style.warningColor = invertedScheme.functionalWarningStandard
            style.warningWidth = DSBannerTokens.warningWidthTypeWarning
            style.warningHeight = DSBannerTokens.warningHeightTypeWarning
        }
        if (props.mode == ODSBannerMode.ERROR) {
            style.errorColor = invertedScheme.functionalDestructiveStandard
            style.errorWidth = DSBannerTokens.errorWidthTypeError
            style.errorHeight = DSBannerTokens.errorHeightTypeError
        }
        style.spacingPadding = DSBannerTokens.spacingPadding
        style.spacingVerticalAlignment = DSBannerTokens.spacingVerticalAlignment
        style.spacingHorizontalAlignment = DSBannerTokens.spacingHorizontalAlignment
        style.spacingHorizontalArrangement = DSBannerTokens.spacingHorizontalArrangement

        // Custom addition
        style.closeButtonIconModel = DSBannerTokens.closeButtonIconModel
        return style
    }
}

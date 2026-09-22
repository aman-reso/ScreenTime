package com.telekom.odsystem.componentstyles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.componenttokens.DSInlineNotificationTokens
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSInlineNotificationStyle {
    var backgroundColor: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var borderRadius: ODSCorners? = null
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
    var closeButtonIconModel: ODSIconModel? = null // Not exported from the plugin
    var scheme: ODSTheme? = null // Not exported from the plugin
    fun getStyle(
        scheme: ODSTheme,
        props: ODSInlineNotificationProps
    ): ODSInlineNotificationStyle {
        val style = ODSInlineNotificationStyle()
        style.scheme = scheme
        style.padding = DSInlineNotificationTokens.padding
        style.borderRadius = DSInlineNotificationTokens.borderRadius
        style.minWidth = DSInlineNotificationTokens.minWidth
        style.verticalAlignment = DSInlineNotificationTokens.verticalAlignment
        style.horizontalArrangement = DSInlineNotificationTokens.horizontalArrangement
        if (props.mode == ODSInlineNotificationMode.ERROR) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle))
        }
        if (props.mode == ODSInlineNotificationMode.SUCCESS) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalSuccessSubtle))
        }
        if (props.mode == ODSInlineNotificationMode.WARNING) {
            style.backgroundColor = listOf(ODSColorModel(hexColor = scheme.functionalWarningSubtle))
        }
        if (props.mode == ODSInlineNotificationMode.INFORMATIVE) {
            style.backgroundColor =
                listOf(ODSColorModel(hexColor = scheme.functionalInformationalSubtle))
        }
        style.notificationGap = DSInlineNotificationTokens.notificationGap
        style.notificationPadding = DSInlineNotificationTokens.notificationPadding
        style.notificationVerticalAlignment =
            DSInlineNotificationTokens.notificationVerticalAlignment
        style.notificationHorizontalAlignment =
            DSInlineNotificationTokens.notificationHorizontalAlignment
        style.notificationHorizontalArrangement =
            DSInlineNotificationTokens.notificationHorizontalArrangement
        if (props.mode == ODSInlineNotificationMode.SUCCESS) {
            style.successColor = scheme.functionalSuccessStandard
            style.successWidth = DSInlineNotificationTokens.successWidthTypeSuccess
            style.successHeight = DSInlineNotificationTokens.successHeightTypeSuccess
        }
        style.contentVerticalAlignment = DSInlineNotificationTokens.contentVerticalAlignment
        style.contentHorizontalAlignment = DSInlineNotificationTokens.contentHorizontalAlignment
        style.contentVerticalArrangement = DSInlineNotificationTokens.contentVerticalArrangement
        style.titleTextGap = DSInlineNotificationTokens.titleTextGap
        style.titleTextPadding = DSInlineNotificationTokens.titleTextPadding
        style.titleTextVerticalAlignment = DSInlineNotificationTokens.titleTextVerticalAlignment
        style.titleTextHorizontalAlignment = DSInlineNotificationTokens.titleTextHorizontalAlignment
        style.titleTextVerticalArrangement = DSInlineNotificationTokens.titleTextVerticalArrangement
        style.titleTextStyle = DSInlineNotificationTokens.titleTextStyle
        style.titleColor = scheme.basicText
        style.titleTextAlign = DSInlineNotificationTokens.titleTextAlign
        style.textTextStyle = DSInlineNotificationTokens.textTextStyle
        style.textColor = scheme.basicText
        style.textTextAlign = DSInlineNotificationTokens.textTextAlign
        style.linksHorizontalGap = DSInlineNotificationTokens.linksHorizontalGap
        style.linksVerticalAlignment = DSInlineNotificationTokens.linksVerticalAlignment
        style.linksHorizontalAlignment = DSInlineNotificationTokens.linksHorizontalAlignment
        style.linksHorizontalArrangement = DSInlineNotificationTokens.linksHorizontalArrangement
        if (props.mode == ODSInlineNotificationMode.INFORMATIVE) {
            style.informationColor = scheme.functionalInformationalStandard
            style.informationWidth = DSInlineNotificationTokens.informationWidthTypeInformation
            style.informationHeight = DSInlineNotificationTokens.informationHeightTypeInformation
        }
        if (props.mode == ODSInlineNotificationMode.WARNING) {
            style.warningColor = scheme.functionalWarningStandard
            style.warningWidth = DSInlineNotificationTokens.warningWidthTypeWarning
            style.warningHeight = DSInlineNotificationTokens.warningHeightTypeWarning
        }
        if (props.mode == ODSInlineNotificationMode.ERROR) {
            style.errorColor = scheme.functionalDestructiveStandard
            style.errorWidth = DSInlineNotificationTokens.errorWidthTypeError
            style.errorHeight = DSInlineNotificationTokens.errorHeightTypeError
        }
        style.spacingPadding = DSInlineNotificationTokens.spacingPadding
        style.spacingVerticalAlignment = DSInlineNotificationTokens.spacingVerticalAlignment
        style.spacingHorizontalAlignment = DSInlineNotificationTokens.spacingHorizontalAlignment
        style.spacingHorizontalArrangement = DSInlineNotificationTokens.spacingHorizontalArrangement

        // Custom addition
        style.closeButtonIconModel = DSInlineNotificationTokens.closeButtonIconModel
        return style
    }
}

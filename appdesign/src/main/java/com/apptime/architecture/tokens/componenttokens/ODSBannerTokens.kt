package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle

data class ODSBannerTokens(
    val borderRadius: ODSCorners,
    val minWidth: Dp,
    val verticalAlignment: Alignment.Vertical,
    val horizontalArrangement: Arrangement.Horizontal,
    val maxWidthWrapperPadding: ODSPadding,
    val maxWidthWrapperMaxWidth: Dp,
    val maxWidthWrapperVerticalAlignment: Alignment.Vertical,
    val maxWidthWrapperHorizontalArrangement: Arrangement.Horizontal,
    val notificationGap: Dp,
    val notificationPadding: ODSPadding,
    val notificationVerticalAlignment: Alignment.Vertical,
    val notificationHorizontalAlignment: Alignment.Horizontal,
    val notificationHorizontalArrangement: Arrangement.Horizontal,
    val successWidthTypeSuccess: Dp,
    val successHeightTypeSuccess: Dp,
    val contentPadding: ODSPadding,
    val contentVerticalAlignment: Alignment.Vertical,
    val contentHorizontalAlignment: Alignment.Horizontal,
    val contentVerticalArrangement: Arrangement.Vertical,
    val titleTextGap: Dp,
    val titleTextVerticalAlignment: Alignment.Vertical,
    val titleTextHorizontalAlignment: Alignment.Horizontal,
    val titleTextVerticalArrangement: Arrangement.Vertical,
    val titleTextStyle: ODSTextStyle,
    val titleTextAlign: TextAlign,
    val textTextStyle: ODSTextStyle,
    val textTextAlign: TextAlign,
    val linksHorizontalGap: Dp,
    val linksVerticalAlignment: Alignment.Vertical,
    val linksHorizontalAlignment: Alignment.Horizontal,
    val linksHorizontalArrangement: Arrangement.Horizontal,
    val informationWidthTypeInformation: Dp,
    val informationHeightTypeInformation: Dp,
    val warningWidthTypeWarning: Dp,
    val warningHeightTypeWarning: Dp,
    val errorWidthTypeError: Dp,
    val errorHeightTypeError: Dp,
    val spacingPadding: ODSPadding,
    val spacingVerticalAlignment: Alignment.Vertical,
    val spacingHorizontalAlignment: Alignment.Horizontal,
    val spacingHorizontalArrangement: Arrangement.Horizontal,
    var closeButtonIconModel: ODSIconModel // Not exported from the plugin
)

val defaultODSBannerTokens = ODSBannerTokens(
    borderRadius = ODSCorners(all = DSVariables.radiusZero),
    minWidth = 320.dp,
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.Center, // exported as space between
    maxWidthWrapperPadding = ODSPadding(left = DSVariables.spacingComponent7),
    maxWidthWrapperMaxWidth = 1576.dp,
    maxWidthWrapperVerticalAlignment = Alignment.Top,
    maxWidthWrapperHorizontalArrangement = Arrangement.SpaceBetween,
    notificationGap = DSVariables.spacingComponent6,
    notificationPadding = ODSPadding(
        top = DSVariables.spacingComponent6,
        bottom = DSVariables.spacingComponent6,
        right = DSVariables.spacingComponent7
    ),
    notificationVerticalAlignment = Alignment.Top,
    notificationHorizontalAlignment = Alignment.Start,
    notificationHorizontalArrangement = Arrangement.Start,
    successWidthTypeSuccess = DSVariables.sizingComponent10,
    successHeightTypeSuccess = DSVariables.sizingComponent10,
    contentPadding = ODSPadding(top = DSVariables.spacingComponent1),
    contentVerticalAlignment = Alignment.CenterVertically,
    contentHorizontalAlignment = Alignment.Start,
    contentVerticalArrangement = Arrangement.Center,
    titleTextGap = DSVariables.spacingComponent2,
    titleTextVerticalAlignment = Alignment.CenterVertically,
    titleTextHorizontalAlignment = Alignment.Start,
    titleTextVerticalArrangement = Arrangement.Center,
    titleTextStyle = DSTextStyles.bodyMBold,
    titleTextAlign = TextAlign.Left,
    textTextStyle = DSTextStyles.bodyMRegular,
    textTextAlign = TextAlign.Left,
    linksHorizontalGap = DSVariables.spacingComponent7,
    linksVerticalAlignment = Alignment.CenterVertically,
    linksHorizontalAlignment = Alignment.Start,
    linksHorizontalArrangement = Arrangement.Start,
    informationWidthTypeInformation = DSVariables.sizingComponent10,
    informationHeightTypeInformation = DSVariables.sizingComponent10,
    warningWidthTypeWarning = DSVariables.sizingComponent10,
    warningHeightTypeWarning = DSVariables.sizingComponent10,
    errorWidthTypeError = DSVariables.sizingComponent10,
    errorHeightTypeError = DSVariables.sizingComponent10,
    spacingPadding = ODSPadding(all = DSVariables.spacingComponent3),
    spacingVerticalAlignment = Alignment.Top,
    spacingHorizontalAlignment = Alignment.Start,
    spacingHorizontalArrangement = Arrangement.Start,
    closeButtonIconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.close_type_bold)
)

var DSBannerTokens: ODSBannerTokens = defaultODSBannerTokens

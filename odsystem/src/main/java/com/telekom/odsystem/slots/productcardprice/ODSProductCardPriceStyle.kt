package com.telekom.odsystem.slots.productcardprice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSProductCardPriceStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var priceContainerGap: Dp? = null
    var priceContainerVerticalAlignment: Alignment.Vertical? = null
    var priceContainerHorizontalAlignment: Alignment.Horizontal? = null
    var priceContainerVerticalArrangement: Arrangement.Vertical? = null
    var labelStyle: ODSTextStyle? = null
    var labelColor: HexColor? = null
    var labelTextAlign: TextAlign? = null
    var priceStyle: ODSTextStyle? = null
    var priceColor: HexColor? = null
    var priceTextAlign: TextAlign? = null
    var priceSavingsGap: Dp? = null
    var priceSavingsVerticalAlignment: Alignment.Vertical? = null
    var priceSavingsHorizontalAlignment: Alignment.Horizontal? = null
    var priceSavingsHorizontalArrangement: Arrangement.Horizontal? = null
    var beforePriceStrikeThroughZStackContentAlignment: Alignment? = null
    var beforePriceStrikeThroughVerticalAlignment: Alignment.Vertical? = null
    var beforePriceStrikeThroughHorizontalAlignment: Alignment.Horizontal? = null
    var beforePriceStrikeThroughVerticalArrangement: Arrangement.Vertical? = null
    var beforePriceStrikeThroughContentAlignment: Alignment? = null
    var priceBeforeStyle: ODSTextStyle? = null
    var priceBeforeColor: HexColor? = null
    var priceBeforeTextAlign: TextAlign? = null
    var strikeThroughAbsoluteContentAlignment: Alignment? = null
    var strikeThroughBackground: List<ODSColorModel>? = null
    var strikeThroughHeight: Dp? = null
    var installmentsContainerGap: Dp? = null
    var installmentsContainerVerticalAlignment: Alignment.Vertical? = null
    var installmentsContainerHorizontalAlignment: Alignment.Horizontal? = null
    var installmentsContainerVerticalArrangement: Arrangement.Vertical? = null
    var installmentsSavingsGap: Dp? = null
    var installmentsSavingsVerticalAlignment: Alignment.Vertical? = null
    var installmentsSavingsHorizontalAlignment: Alignment.Horizontal? = null
    var installmentsSavingsHorizontalArrangement: Arrangement.Horizontal? = null
    var beforePriceStrikeThrough2ZStackContentAlignment: Alignment? = null
    var beforePriceStrikeThrough2VerticalAlignment: Alignment.Vertical? = null
    var beforePriceStrikeThrough2HorizontalAlignment: Alignment.Horizontal? = null
    var beforePriceStrikeThrough2VerticalArrangement: Arrangement.Vertical? = null
    var beforePriceStrikeThrough2ContentAlignment: Alignment? = null
    var priceBefore2Style: ODSTextStyle? = null
    var priceBefore2Color: HexColor? = null
    var priceBefore2TextAlign: TextAlign? = null
    var strikeThrough2AbsoluteContentAlignment: Alignment? = null
    var strikeThrough2Background: List<ODSColorModel>? = null
    var strikeThrough2Height: Dp? = null
    var installmentsTextStyle: ODSTextStyle? = null
    var installmentsTextColor: HexColor? = null
    var installmentsTextTextAlign: TextAlign? = null
    var installmentStyle: ODSTextStyle? = null
    var installmentColor: HexColor? = null
    var installmentTextAlign: TextAlign? = null
    var supportTextStyle: ODSTextStyle? = null
    var supportTextColor: HexColor? = null
    var supportTextTextAlign: TextAlign? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSProductCardPriceProps
    ): ODSProductCardPriceStyle {
        val style = ODSProductCardPriceStyle()
        style.verticalAlignment = DSProductCardPriceTokens.verticalAlignment
        style.horizontalAlignment = DSProductCardPriceTokens.horizontalAlignment
        style.verticalArrangement = DSProductCardPriceTokens.verticalArrangement
        if (props.financialOptions == ODSProductCardPriceFinancialOptions.ONE_TIME_ONLY) {
            style.gap = DSProductCardPriceTokens.gapFinancialOptionsOneTimeOnly
        }
        if (props.financialOptions == ODSProductCardPriceFinancialOptions.INSTALLMENTS_ONLY) {
            style.gap = DSProductCardPriceTokens.gapFinancialOptionsInstallmentsOnly
        }
        if (props.financialOptions == ODSProductCardPriceFinancialOptions.ONE_TIME_OR_INSTALLMENTS) {
            style.gap = DSProductCardPriceTokens.gapFinancialOptionsOneTimeOrInstallments
        }
        style.priceContainerGap = DSProductCardPriceTokens.priceContainerGap
        style.priceContainerVerticalAlignment =
            DSProductCardPriceTokens.priceContainerVerticalAlignment
        style.priceContainerHorizontalAlignment =
            DSProductCardPriceTokens.priceContainerHorizontalAlignment
        style.priceContainerVerticalArrangement =
            DSProductCardPriceTokens.priceContainerVerticalArrangement
        style.labelStyle = DSProductCardPriceTokens.labelStyle
        style.labelColor = scheme.basicTextRecessive
        style.labelTextAlign = DSProductCardPriceTokens.labelTextAlign
        style.priceStyle = DSProductCardPriceTokens.priceStyle
        style.priceTextAlign = DSProductCardPriceTokens.priceTextAlign
        if (props.variant == ODSProductCardPriceVariant.SAVINGS) {
            style.priceColor = scheme.basicAccent
        }
        if (props.variant == ODSProductCardPriceVariant.STANDARD && props.financialOptions == ODSProductCardPriceFinancialOptions.ONE_TIME_ONLY) {
            style.priceColor = scheme.basicText
        }
        if (props.variant == ODSProductCardPriceVariant.STANDARD && props.financialOptions == ODSProductCardPriceFinancialOptions.ONE_TIME_OR_INSTALLMENTS) {
            style.priceColor = scheme.basicText
        }
        if (props.variant == ODSProductCardPriceVariant.STANDARD && props.financialOptions == ODSProductCardPriceFinancialOptions.INSTALLMENTS_ONLY) {
            style.priceColor = scheme.basicAccent
        }
        style.priceSavingsGap = DSProductCardPriceTokens.priceSavingsGap
        style.priceSavingsVerticalAlignment = DSProductCardPriceTokens.priceSavingsVerticalAlignment
        style.priceSavingsHorizontalAlignment =
            DSProductCardPriceTokens.priceSavingsHorizontalAlignment
        style.priceSavingsHorizontalArrangement =
            DSProductCardPriceTokens.priceSavingsHorizontalArrangement
        style.beforePriceStrikeThroughZStackContentAlignment =
            DSProductCardPriceTokens.beforePriceStrikeThroughZStackContentAlignment
        style.beforePriceStrikeThroughVerticalAlignment =
            DSProductCardPriceTokens.beforePriceStrikeThroughVerticalAlignment
        style.beforePriceStrikeThroughHorizontalAlignment =
            DSProductCardPriceTokens.beforePriceStrikeThroughHorizontalAlignment
        style.beforePriceStrikeThroughVerticalArrangement =
            DSProductCardPriceTokens.beforePriceStrikeThroughVerticalArrangement
        style.beforePriceStrikeThroughContentAlignment =
            DSProductCardPriceTokens.beforePriceStrikeThroughContentAlignment
        style.priceBeforeStyle = DSProductCardPriceTokens.priceBeforeStyle
        style.priceBeforeColor = scheme.basicText
        style.priceBeforeTextAlign = DSProductCardPriceTokens.priceBeforeTextAlign
        style.strikeThroughAbsoluteContentAlignment =
            DSProductCardPriceTokens.strikeThroughAbsoluteContentAlignment
        style.strikeThroughBackground = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        style.strikeThroughHeight = DSProductCardPriceTokens.strikeThroughHeight
        style.installmentsContainerGap = DSProductCardPriceTokens.installmentsContainerGap
        style.installmentsContainerVerticalAlignment =
            DSProductCardPriceTokens.installmentsContainerVerticalAlignment
        style.installmentsContainerHorizontalAlignment =
            DSProductCardPriceTokens.installmentsContainerHorizontalAlignment
        style.installmentsContainerVerticalArrangement =
            DSProductCardPriceTokens.installmentsContainerVerticalArrangement
        style.installmentsSavingsGap = DSProductCardPriceTokens.installmentsSavingsGap
        style.installmentsSavingsVerticalAlignment =
            DSProductCardPriceTokens.installmentsSavingsVerticalAlignment
        style.installmentsSavingsHorizontalAlignment =
            DSProductCardPriceTokens.installmentsSavingsHorizontalAlignment
        style.installmentsSavingsHorizontalArrangement =
            DSProductCardPriceTokens.installmentsSavingsHorizontalArrangement
        style.beforePriceStrikeThrough2ZStackContentAlignment =
            DSProductCardPriceTokens.beforePriceStrikeThrough2ZStackContentAlignment
        style.beforePriceStrikeThrough2VerticalAlignment =
            DSProductCardPriceTokens.beforePriceStrikeThrough2VerticalAlignment
        style.beforePriceStrikeThrough2HorizontalAlignment =
            DSProductCardPriceTokens.beforePriceStrikeThrough2HorizontalAlignment
        style.beforePriceStrikeThrough2VerticalArrangement =
            DSProductCardPriceTokens.beforePriceStrikeThrough2VerticalArrangement
        style.beforePriceStrikeThrough2ContentAlignment =
            DSProductCardPriceTokens.beforePriceStrikeThrough2ContentAlignment
        style.priceBefore2Style = DSProductCardPriceTokens.priceBefore2Style
        style.priceBefore2Color = scheme.basicText
        style.priceBefore2TextAlign = DSProductCardPriceTokens.priceBefore2TextAlign
        style.strikeThrough2AbsoluteContentAlignment =
            DSProductCardPriceTokens.strikeThrough2AbsoluteContentAlignment
        style.strikeThrough2Background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
        style.strikeThrough2Height = DSProductCardPriceTokens.strikeThrough2Height
        style.installmentsTextStyle = DSProductCardPriceTokens.installmentsTextStyle
        style.installmentsTextColor = scheme.basicTextRecessive
        style.installmentsTextTextAlign = DSProductCardPriceTokens.installmentsTextTextAlign
        style.installmentStyle = DSProductCardPriceTokens.installmentStyle
        style.installmentTextAlign = DSProductCardPriceTokens.installmentTextAlign
        if (props.variant == ODSProductCardPriceVariant.STANDARD) {
            style.installmentColor = scheme.basicText
        }
        if (props.variant == ODSProductCardPriceVariant.SAVINGS && props.financialOptions == ODSProductCardPriceFinancialOptions.ONE_TIME_ONLY) {
            style.installmentColor = scheme.basicText
        }
        if (props.variant == ODSProductCardPriceVariant.SAVINGS && props.financialOptions == ODSProductCardPriceFinancialOptions.ONE_TIME_OR_INSTALLMENTS) {
            style.installmentColor = scheme.basicAccent
        }
        if (props.variant == ODSProductCardPriceVariant.SAVINGS && props.financialOptions == ODSProductCardPriceFinancialOptions.INSTALLMENTS_ONLY) {
            style.installmentColor = scheme.basicAccent
        }
        style.supportTextStyle = DSProductCardPriceTokens.supportTextStyle
        style.supportTextColor = scheme.basicTextRecessive
        style.supportTextTextAlign = DSProductCardPriceTokens.supportTextTextAlign
        return style
    }
}

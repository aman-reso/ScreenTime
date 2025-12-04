package com.telekom.odsystem.slots.productcardprice

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSProductCardPrice(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSProductCardPriceProps = ODSProductCardPriceProps()
) {

    val style = ODSProductCardPriceStyle().getStyle(scheme = scheme, props = props)

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        if (props.financialOptions in listOf(
                ODSProductCardPriceFinancialOptions.ONE_TIME_ONLY,
                ODSProductCardPriceFinancialOptions.ONE_TIME_OR_INSTALLMENTS
            )
        ) {
            ODSPriceContainer(scheme = scheme, style = style, props = props)
        }
        if (props.financialOptions in listOf(
                ODSProductCardPriceFinancialOptions.INSTALLMENTS_ONLY,
                ODSProductCardPriceFinancialOptions.ONE_TIME_OR_INSTALLMENTS
            )
        ) {
            ODSInstallmentsContainer(style, props, scheme)
        }
        ODSSupportText(style = style, props = props)
    }
}

@Composable
private fun ODSPriceContainer(
    scheme: ODSTheme,
    style: ODSProductCardPriceStyle,
    props: ODSProductCardPriceProps
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = style.priceContainerGap,
        verticalAlignment = style.priceContainerVerticalAlignment,
        horizontalAlignment = style.priceContainerHorizontalAlignment,
        verticalArrangement = style.priceContainerVerticalArrangement
    ) {
        if (!props.priceText.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.priceText,
                style = style.labelStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign
            )
        }
        if (!props.price.isNullOrEmpty()) {
            ODSText(
                text = props.price,
                style = style.priceStyle,
                color = style.priceColor,
                textAlign = style.priceTextAlign
            )
        }
        if (props.variant == ODSProductCardPriceVariant.SAVINGS) {
            ODSPriceSavingsContainer(scheme = scheme, style = style, props = props)
        }
    }
}

@Composable
private fun ODSPriceSavingsContainer(
    scheme: ODSTheme,
    style: ODSProductCardPriceStyle,
    props: ODSProductCardPriceProps
) {
    ODSRow(
        gap = style.priceSavingsGap,
        horizontalAlignment = style.priceSavingsHorizontalAlignment,
        verticalAlignment = style.priceSavingsVerticalAlignment,
        horizontalArrangement = style.priceSavingsHorizontalArrangement
    ) {
        val beforePrice = props.beforePrice
        if (!beforePrice.isNullOrEmpty()) {
            ODSPriceStrikethrough(style = style, price = beforePrice)
        }
        props.priceSavingsTagProps?.let { tagProps ->
            ODSTagStatic(scheme = scheme, props = tagProps)
        }
    }
}

@Composable
private fun ODSPriceStrikethrough(style: ODSProductCardPriceStyle, price: String) {
    ODSBox(
        contentAlignment = style.beforePriceStrikeThroughZStackContentAlignment,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        ODSColumn(
            verticalAlignment = style.beforePriceStrikeThroughVerticalAlignment,
            horizontalAlignment = style.beforePriceStrikeThroughHorizontalAlignment,
            verticalArrangement = style.beforePriceStrikeThroughVerticalArrangement
        ) {
            ODSText(
                text = price,
                style = style.priceBeforeStyle,
                color = style.priceBeforeColor,
                textAlign = style.priceBeforeTextAlign,
            )
        }
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    alignment = style.strikeThroughAbsoluteContentAlignment
                        ?: Alignment.CenterStart,
                ),
            background = style.strikeThroughBackground,
            height = style.strikeThroughHeight
        ) { }
    }
}

@Composable
private fun ODSInstallmentsContainer(
    style: ODSProductCardPriceStyle,
    props: ODSProductCardPriceProps,
    scheme: ODSTheme
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = style.installmentsContainerGap,
        verticalAlignment = style.installmentsContainerVerticalAlignment,
        horizontalAlignment = style.installmentsContainerHorizontalAlignment,
        verticalArrangement = style.installmentsContainerVerticalArrangement
    ) {
        if (props.financialOptions == ODSProductCardPriceFinancialOptions.INSTALLMENTS_ONLY && props.variant == ODSProductCardPriceVariant.SAVINGS) {
            ODSInstallmentsSavingsContainer(style, props, scheme)
        }
        if (!props.installmentText.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.installmentText,
                style = style.installmentsTextStyle,
                color = style.installmentsTextColor,
                textAlign = style.installmentsTextTextAlign
            )
        }
        if (!props.installmentPrice.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.installmentPrice,
                style = style.installmentStyle,
                color = style.installmentColor,
                textAlign = style.installmentTextAlign
            )
        }
    }
}

@Composable
private fun ODSInstallmentsSavingsContainer(
    style: ODSProductCardPriceStyle,
    props: ODSProductCardPriceProps,
    scheme: ODSTheme
) {
    ODSRow(
        gap = style.installmentsSavingsGap,
        horizontalAlignment = style.installmentsSavingsHorizontalAlignment,
        verticalAlignment = style.installmentsSavingsVerticalAlignment,
        horizontalArrangement = style.installmentsSavingsHorizontalArrangement
    ) {
        ODSInstallmentsStrikethrough(style, props)
        props.installmentsSavingsTagProps?.let { tagProps ->
            ODSTagStatic(scheme = scheme, props = tagProps)
        }
    }
}

@Composable
private fun ODSInstallmentsStrikethrough(
    style: ODSProductCardPriceStyle,
    props: ODSProductCardPriceProps
) {
    ODSBox(
        contentAlignment = style.beforePriceStrikeThrough2ZStackContentAlignment,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        ODSColumn(
            verticalAlignment = style.beforePriceStrikeThrough2VerticalAlignment,
            horizontalAlignment = style.beforePriceStrikeThrough2HorizontalAlignment,
            verticalArrangement = style.beforePriceStrikeThrough2VerticalArrangement
        ) {
            ODSText(
                text = props.beforePrice,
                style = style.priceBefore2Style,
                color = style.priceBefore2Color,
                textAlign = style.priceBefore2TextAlign
            )
        }
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    alignment = style.strikeThrough2AbsoluteContentAlignment
                        ?: Alignment.CenterStart,
                ),
            background = style.strikeThrough2Background,
            height = style.strikeThrough2Height
        ) { }
    }
}

@Composable
private fun ODSSupportText(style: ODSProductCardPriceStyle, props: ODSProductCardPriceProps) {
    ODSText(
        modifier = Modifier.fillMaxWidth(),
        text = props.supportText,
        style = style.supportTextStyle,
        color = style.supportTextColor,
        textAlign = style.supportTextTextAlign
    )
}

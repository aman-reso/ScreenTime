package com.telekom.odsystem.slots.productcardprice

import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps

enum class ODSProductCardPriceFinancialOptions {
    ONE_TIME_ONLY,
    ONE_TIME_OR_INSTALLMENTS,
    INSTALLMENTS_ONLY,
}

enum class ODSProductCardPriceVariant {
    STANDARD,
    SAVINGS,
}

data class ODSProductCardPriceProps(
    var beforePrice: String? = null,
    var financialOptions: ODSProductCardPriceFinancialOptions = ODSProductCardPriceFinancialOptions.ONE_TIME_ONLY,
    var installmentPrice: String? = null,
    var installmentText: String? = null,
    var priceText: String? = null,
    var price: String? = null,
    var supportText: String? = null,
    var variant: ODSProductCardPriceVariant = ODSProductCardPriceVariant.STANDARD,
    var priceSavingsTagProps: ODSTagStaticProps? = null,
    var installmentsSavingsTagProps: ODSTagStaticProps? = null
)

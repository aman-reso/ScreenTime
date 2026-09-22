package com.telekom.odsystem.slots.productcarddescriptivetext

import com.telekom.odsystem.slots.featurelistitem.ODSFeatureListItemProps

data class ODSProductCardDescriptiveTextProps(
    var content: String? = null,
    var heading: String? = null,
    var featureListItemProps: List<ODSFeatureListItemProps>? = null
)

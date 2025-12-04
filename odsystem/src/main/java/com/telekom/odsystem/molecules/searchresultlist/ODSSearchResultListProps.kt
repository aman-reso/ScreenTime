package com.telekom.odsystem.molecules.searchresultlist

import com.telekom.odsystem.atoms.resultitem.ODSResultItemProps

data class ODSSearchResultListProps(
    var label: String? = null,
    var resultList: List<ODSResultItemProps>? = null,
)

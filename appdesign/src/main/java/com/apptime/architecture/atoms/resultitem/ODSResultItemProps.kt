package com.telekom.odsystem.atoms.resultitem

import com.telekom.odsystem.atoms.icon.ODSIconModel

data class ODSResultItemProps(
    var fragMagenta: Boolean = false,
    var fragMagentaPrompt: String? = null,
    var icon: ODSIconModel? = null,
    var labelText: String? = null,
    var recessiveLabelText: String? = null,
)

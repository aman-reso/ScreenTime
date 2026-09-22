package com.telekom.odsystem.slots.actionslot

import com.telekom.odsystem.atoms.button.ODSButtonProps

data class ODSActionSlotProps(
    var actionOneProps: ODSButtonProps? = null,
    var actionTwoProps: ODSButtonProps? = null,
    var actionThreeProps: ODSButtonProps? = null
)

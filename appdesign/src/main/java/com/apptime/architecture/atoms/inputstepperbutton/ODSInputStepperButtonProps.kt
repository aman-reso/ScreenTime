package com.telekom.odsystem.atoms.inputstepperbutton

import com.telekom.odsystem.atoms.icon.ODSIconModel

enum class ODSInputStepperButtonSize {
    SMALL,
    LARGE,
}

data class ODSInputStepperButtonProps(
    var buttonIcon: ODSIconModel? = null,
    var disabled: Boolean = false,
    var size: ODSInputStepperButtonSize = ODSInputStepperButtonSize.SMALL,
)

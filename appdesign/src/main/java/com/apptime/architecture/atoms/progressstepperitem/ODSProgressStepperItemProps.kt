package com.telekom.odsystem.atoms.progressstepperitem

enum class ODSProgressStepperItemSize {
    STANDARD,
    SMALL,
}

enum class ODSProgressStepperItemType {
    CURRENT,
    NEXT,
    SUCCESS,
    ERROR,
}

data class ODSProgressStepperItemProps(
    var number: String? = null,
    var size: ODSProgressStepperItemSize = ODSProgressStepperItemSize.STANDARD,
    var type: ODSProgressStepperItemType = ODSProgressStepperItemType.CURRENT,
)

package com.telekom.odsystem.molecules.progressstepper

import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.progressstepperitem.ODSProgressStepperItemProps
import com.telekom.odsystem.atoms.progressstepperitem.ODSProgressStepperItemSize
import com.telekom.odsystem.atoms.progressstepperitem.ODSProgressStepperItemType

enum class ODSProgressStepperSize {
    /** Standard sized stepper. */
    STANDARD,

    /** Compact stepper for dense UI. */
    SMALL,
}

enum class ODSProgressStepperVariant {
    /** Layout steps vertically. */
    VERTICAL,

    /** Layout steps horizontally. */
    HORIZONTAL,
}

/**
 * Properties describing a single progress step item.
 *
 * @property number Optional number displayed inside the badge.
 * @property type Visual state of the step item.
 */
data class ODSProgressStepperProgressStepperItemProps(
    var number: String? = null,
    var type: ODSProgressStepperItemType = ODSProgressStepperItemType.CURRENT,
)

internal fun ODSProgressStepperProgressStepperItemProps.toODSProgressStepperItemProps(size: ODSProgressStepperSize): ODSProgressStepperItemProps {
    return ODSProgressStepperItemProps(
        number = this.number,
        type = this.type,
        size = when (size) {
            ODSProgressStepperSize.STANDARD -> ODSProgressStepperItemSize.STANDARD
            ODSProgressStepperSize.SMALL -> ODSProgressStepperItemSize.SMALL
        }
    )
}

/**
 * Properties for the divider displayed between steps.
 *
 * @property inset Applies inset spacing when true.
 * @property spacing Adds extra spacing around the divider.
 */
data class ODSProgressStepperDividerProps(
    var inset: Boolean = false,
    var spacing: Boolean = false,
)

internal fun ODSProgressStepperDividerProps.toODSDividerProps(variant: ODSProgressStepperVariant): ODSDividerProps {
    return ODSDividerProps(
        inset = this.inset,
        spacing = this.spacing,
        variant = when (variant) {
            ODSProgressStepperVariant.VERTICAL -> ODSDividerVariant.VERTICAL
            ODSProgressStepperVariant.HORIZONTAL -> ODSDividerVariant.HORIZONTAL
        }
    )
}

/**
 * Configuration of an ODS progress stepper.
 *
 * @property label Optional label displayed above the stepper.
 * @property showContent Whether descriptive texts are shown.
 * @property size Overall size of the stepper.
 * @property text Additional text displayed below the label.
 * @property variant Direction of the stepper layout.
 * @property progressStepperItemProps Properties for the step badge.
 * @property dividerProps Divider styling between steps.
 */
data class ODSProgressStepperProps(
    var label: String? = null,
    var showContent: Boolean = true,
    var size: ODSProgressStepperSize = ODSProgressStepperSize.STANDARD,
    var text: String? = null,
    var variant: ODSProgressStepperVariant = ODSProgressStepperVariant.VERTICAL,
    var progressStepperItemProps: ODSProgressStepperProgressStepperItemProps? = null,
    var dividerProps: ODSProgressStepperDividerProps? = null
)

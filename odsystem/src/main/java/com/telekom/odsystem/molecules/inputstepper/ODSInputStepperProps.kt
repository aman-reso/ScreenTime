package com.telekom.odsystem.molecules.inputstepper

/**
 * Defines the size of the ODS input stepper.
 *
 * This enum specifies the available sizes for the input stepper component, which can affect its appearance and usability.
 */
enum class ODSInputStepperSize {
    LARGE,
    SMALL,
}

/**
 * Defines the type of the ODS input stepper.
 *
 * This enum specifies the available types for the input stepper component, which can affect its appearance and behavior.
 */
enum class ODSInputStepperType {
    OUTLINE,
    GHOST,
}

/**
 * Properties used to configure the appearance and behavior of an ODS input stepper.
 *
 * @property disabled Indicates whether the input stepper is disabled and non-interactive. Default is `false`.
 * @property readOnly If `true`, the input stepper is in a read-only state, meaning it cannot be modified but its value is visible. Default is `false`.
 * @property showRemoveIcon If `true`, a remove icon is displayed, allowing users to clear the value. Default is `false`.
 * @property size The size of the input stepper (e.g., large, small). Default is `SMALL`.
 * @property type The type of the input stepper (e.g., outline, ghost). Default is `OUTLINE`.
 * @property value The current value of the input stepper. Default is `null`.
 * @property minValue The minimum value allowed for the input stepper. Not exported from the plugin. Default is `null`.
 * @property maxValue The maximum value allowed for the input stepper. Not exported from the plugin. Default is `null`.
 */
data class ODSInputStepperProps(
    var disabled: Boolean = false,
    var readOnly: Boolean = false,
    var showRemoveIcon: Boolean = false,
    var size: ODSInputStepperSize = ODSInputStepperSize.SMALL,
    var type: ODSInputStepperType = ODSInputStepperType.OUTLINE,
    var value: String? = null,
    var minValue: String? = null, // Not exported from the plugin
    var maxValue: String? = null, // Not exported from the plugin
)

package com.telekom.odsystem.atoms.loadingspinner

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-06-16 (v1.31.6) - uid: 1932e655
 * Figma link: https://figma.com/design/MpQgyLR8JN6QeprILJwaD4/ODS_Feedback-Components_Exploration?node-id=866-16068
 */

/**
 * Defines the alignment of the label relative to the ODS loading spinner.
 *
 * This enum specifies how the label text should be positioned in relation to the spinner.
 */
enum class ODSLoadingSpinnerLabelAlignment {
    HORIZONTAL,
    VERTICAL,
    NONE,
}

/**
 * Defines the size of the ODS loading spinner.
 *
 * This enum specifies the available sizes for the loading spinner component.
 */
enum class ODSLoadingSpinnerSize {
    X_SMALL,
    SMALL,
    LARGE,
}

/**
 * Defines the color variant of the ODS loading spinner.
 *
 * This enum specifies the available color styles for the loading spinner,
 * allowing it to be adapted for different background contrasts.
 */
enum class ODSLoadingSpinnerVariant {
    STANDARD,
    WHITE,
    BLACK,
}

/**
 * Properties for configuring the ODS loading spinner component.
 *
 * @property labelAlignment The alignment of the label relative to the spinner. Default is [ODSLoadingSpinnerLabelAlignment.NONE].
 * @property labelText The text to display alongside the spinner. Default is `null`, meaning no label is displayed.
 * @property size The size of the loading spinner. Default is [ODSLoadingSpinnerSize.LARGE].
 * @property variant The color variant of the loading spinner. Default is [ODSLoadingSpinnerVariant.STANDARD].
 */
data class ODSLoadingSpinnerProps(
    var labelAlignment: ODSLoadingSpinnerLabelAlignment = ODSLoadingSpinnerLabelAlignment.NONE,
    var labelText: String? = null,
    var size: ODSLoadingSpinnerSize = ODSLoadingSpinnerSize.LARGE,
    var variant: ODSLoadingSpinnerVariant = ODSLoadingSpinnerVariant.STANDARD,
)

package com.telekom.odsystem.atoms.link

import com.telekom.odsystem.atoms.icon.ODSIconModel

/**
 * Defines the alignment of the ODS link.
 */
enum class ODSLinkAlignment {
    /**
     * Aligns the link to the right.
     */
    RIGHT,

    /**
     * Aligns the link to the left.
     */
    LEFT,

    /**
     * Centers the link.
     */
    CENTERED,
}

/**
 * Defines the visual variant of the ODS link.
 */
enum class ODSLinkType {
    PRIMARY,
    SECONDARY,
}

/**
 * Properties used to configure the appearance and behavior of an ODS link.
 *
 * @property alignment The alignment of the link (e.g., right, left, centered).
 * @property disabled Indicates whether the link is disabled and non-interactive.
 * @property leftIcon The icon displayed to the left of the link label (if applicable).
 * @property label The text label of the link.
 * @property rightIcon The icon displayed to the right of the link label (if applicable).
 * @property type The visual type of the link (e.g., primary, secondary).
 */
data class ODSLinkProps(
    var alignment: ODSLinkAlignment = ODSLinkAlignment.RIGHT,
    var disabled: Boolean = false,
    var leftIcon: ODSIconModel? = null,
    var label: String? = null,
    var rightIcon: ODSIconModel? = null,
    var type: ODSLinkType = ODSLinkType.PRIMARY,
)

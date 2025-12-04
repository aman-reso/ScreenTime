package com.telekom.odsystem.atoms.tagdismissible

import com.telekom.odsystem.atoms.icon.ODSIconModel

enum class ODSTagDismissibleType {
    /** Basic style without emphasis. */
    BASIC,

    /** Subtle style with soft colors. */
    SUBTLE,

    /** Strong style for critical actions. */
    STRONG,
}

/**
 * Properties describing a dismissible tag component.
 *
 * @property disabled Disables the dismiss action when true.
 * @property icon Optional icon displayed before the label.
 * @property label Text shown in the tag.
 * @property type Visual style of the tag.
 */
data class ODSTagDismissibleProps(
    var disabled: Boolean = false,
    var icon: ODSIconModel? = null,
    var label: String? = null,
    var type: ODSTagDismissibleType = ODSTagDismissibleType.BASIC,
)

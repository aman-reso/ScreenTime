package com.telekom.odsystem.molecules.checkboxlist

import com.telekom.odsystem.atoms.checkbox.ODSCheckboxMode
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxProps
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSelected
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSize

/**
 * Size options for a checkbox list.
 */
enum class ODSCheckboxListSize {
    /** Regular checkbox list. */
    LARGE,

    /** Compact checkbox list for dense UI. */
    SMALL,
}

/**
 * Configuration for an ODS checkbox list.
 *
 * @property size Overall component size.
 * @property checkboxProps List of checkbox items to display.
 * @property nested Marks this list as a nested list when true.
 */
data class ODSCheckboxListProps(
    var size: ODSCheckboxListSize = ODSCheckboxListSize.LARGE,
    var checkboxProps: List<ODSCheckboxListCheckboxProps>? = null,
    var nested: Boolean = false // Internal: Indicates if this list is a sublist of another.
)

/**
 * Properties describing a single checkbox in a list.
 *
 * @property label Text shown next to the checkbox.
 * @property mode Visual mode of the checkbox.
 * @property selected Current selection state.
 * @property disabled Disables user interaction when true.
 * @property readOnly Prevents changes when true.
 */
data class ODSCheckboxListCheckboxProps(
    var label: String? = null,
    var mode: ODSCheckboxMode = ODSCheckboxMode.STANDARD,
    var selected: ODSCheckboxSelected = ODSCheckboxSelected.UNSELECTED,
    var disabled: Boolean = false,
    var readOnly: Boolean = false,
)

internal fun ODSCheckboxListCheckboxProps.toODSCheckboxProps(size: ODSCheckboxListSize): ODSCheckboxProps {
    return ODSCheckboxProps(
        label = this.label,
        mode = this.mode,
        selected = this.selected,
        disabled = this.disabled,
        readOnly = this.readOnly,
        size = when (size) {
            ODSCheckboxListSize.SMALL -> ODSCheckboxSize.SMALL
            ODSCheckboxListSize.LARGE -> ODSCheckboxSize.LARGE
        }
    )
}

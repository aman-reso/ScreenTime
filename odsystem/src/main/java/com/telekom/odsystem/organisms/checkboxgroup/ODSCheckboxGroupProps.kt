package com.telekom.odsystem.organisms.checkboxgroup

import com.telekom.odsystem.atoms.checkbox.ODSCheckboxMode
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxProps
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSelected
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSize
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import com.telekom.odsystem.molecules.checkboxlist.ODSCheckboxListCheckboxProps
import com.telekom.odsystem.molecules.checkboxlist.ODSCheckboxListProps
import com.telekom.odsystem.molecules.checkboxlist.ODSCheckboxListSize

enum class ODSCheckboxGroupSize {
    SMALL,
    LARGE,
}

enum class ODSCheckboxGroupType {
    STANDARD,
    NESTED,
}

/**
 * Properties describing a single checkbox within the group.
 *
 * @property label Text label of the checkbox.
 * @property mode Visual mode of the checkbox (standard or error).
 * @property selected Current selection state.
 * @property disabled Disables the checkbox when true.
 * @property readOnly Prevents changes when true.
 */
data class ODSCheckboxGroupCheckboxProps(
    var label: String? = null,
    var mode: ODSCheckboxMode = ODSCheckboxMode.STANDARD,
    var selected: ODSCheckboxSelected = ODSCheckboxSelected.UNSELECTED,
    var disabled: Boolean = false,
    var readOnly: Boolean = false,
)

internal fun ODSCheckboxGroupCheckboxProps.toODSCheckboxProps(size: ODSCheckboxGroupSize): ODSCheckboxProps {
    return ODSCheckboxProps(
        label = this.label,
        mode = this.mode,
        selected = this.selected,
        disabled = this.disabled,
        readOnly = this.readOnly,
        size = when (size) {
            ODSCheckboxGroupSize.SMALL -> ODSCheckboxSize.SMALL
            ODSCheckboxGroupSize.LARGE -> ODSCheckboxSize.LARGE
        }
    )
}

/**
 * Container for multiple checkbox definitions used inside a group.
 *
 * @property checkboxProps The list of checkbox properties.
 */
data class ODSCheckboxGroupCheckboxListProps(
    var checkboxProps: List<ODSCheckboxListCheckboxProps>? = null,
)

internal fun ODSCheckboxGroupCheckboxListProps.toODSCheckboxListProps(
    size: ODSCheckboxGroupSize,
    nested: Boolean = false
): ODSCheckboxListProps {
    return ODSCheckboxListProps(
        nested = nested,
        checkboxProps = this.checkboxProps,
        size = when (size) {
            ODSCheckboxGroupSize.SMALL -> ODSCheckboxListSize.SMALL
            ODSCheckboxGroupSize.LARGE -> ODSCheckboxListSize.LARGE
        }
    )
}

/**
 * Properties configuring a group of ODS checkboxes.
 *
 * @property size Size of each checkbox.
 * @property titleText Optional title displayed above the group.
 * @property type Defines the layout of the group.
 * @property supportMessageProps Support message shown below the group.
 * @property checkboxProps Configuration for a single checkbox.
 * @property checkboxListProps Configuration for a list of checkboxes.
 */
data class ODSCheckboxGroupProps(
    var size: ODSCheckboxGroupSize = ODSCheckboxGroupSize.LARGE,
    var titleText: String? = null,
    var type: ODSCheckboxGroupType = ODSCheckboxGroupType.STANDARD,
    var supportMessageProps: ODSSupportMessageProps? = null,
    var checkboxProps: ODSCheckboxGroupCheckboxProps? = null,
    var checkboxListProps: ODSCheckboxGroupCheckboxListProps? = null,
)

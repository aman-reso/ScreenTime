package com.telekom.odsystem.molecules.dropdownselect

import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageMode
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import java.util.UUID

enum class ODSDropdownSelectMode {
    /** Default drop-down style. */
    STANDARD,

    /** Shows an informative message below the field. */
    INFORMATIVE,

    /** Shows an error message below the field. */
    ERROR,
}

enum class ODSDropdownSelectSize {
    /** Large drop-down field. */
    LARGE,

    /** Small drop-down field. */
    SMALL,
}

/**
 * Properties for an optional support message below the field.
 *
 * @property message Text shown beneath the drop-down.
 */
data class ODSDropdownSelectSupportMessageProps(
    var message: String? = null,
)

internal fun ODSDropdownSelectSupportMessageProps.toODSSupportMessageProps(
    mode: ODSDropdownSelectMode,
    disabled: Boolean
): ODSSupportMessageProps {
    return ODSSupportMessageProps(
        helperText = this.message,
        mode = when (mode) {
            ODSDropdownSelectMode.INFORMATIVE -> ODSSupportMessageMode.INFORMATIVE
            ODSDropdownSelectMode.ERROR -> ODSSupportMessageMode.ERROR
            else -> ODSSupportMessageMode.INFORMATIVE
        },
        disabled = disabled
    )
}

/**
 * Properties configuring an ODS drop-down select component.
 *
 * @property disabled Disables user input when true.
 * @property expanded Whether the options list is currently visible.
 * @property icon Optional icon displayed before the text.
 * @property label Field label describing the selection.
 * @property mode Visual mode affecting colors and messages.
 * @property readOnly Prevents selection changes when true.
 * @property required Marks the field as required.
 * @property size Visual size of the field.
 * @property supportMessageProps Optional support message configuration.
 * @property selectedValue Currently selected option model.
 * @property options List of selectable options.
 */
data class ODSDropdownSelectProps(
    var disabled: Boolean = false,
    var expanded: Boolean = false,
    var icon: ODSIconModel? = null,
    var label: String? = null,
    var mode: ODSDropdownSelectMode = ODSDropdownSelectMode.STANDARD,
    var readOnly: Boolean = false,
    var required: Boolean = false,
    var size: ODSDropdownSelectSize = ODSDropdownSelectSize.LARGE,
    var supportMessageProps: ODSDropdownSelectSupportMessageProps? = null,
    var selectedValue: ODSDropdownSelectOptions? = null, // Not exported by plugin
    var options: List<ODSDropdownSelectOptions>? = null, // Not exported by plugin
)

/**
 * Model describing a selectable option.
 *
 * @property iconBefore Optional icon placed before the label.
 * @property label Text displayed for the option.
 * @property helperText Additional text beneath the label.
 * @property disabled Marks the option as disabled.
 * @property id Unique identifier for stable keys.
 */
data class ODSDropdownSelectOptions(
    var iconBefore: ODSIconModel? = null,
    var label: String? = null,
    var helperText: String? = null,
    var disabled: Boolean = false,
    var id: String = UUID.randomUUID().toString(), // Not exported by plugin
)

internal val ODSDropdownSelectProps.selected: Boolean
    get() = selectedValue != null

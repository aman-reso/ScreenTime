package com.telekom.odsystem.atoms.filterchip

import com.telekom.odsystem.atoms.icon.ODSIconModel
import java.util.UUID

/**
 * Properties used to configure the appearance and behavior of an ODS filter chip.
 *
 * @property disabled Indicates whether the filter chip is disabled and non-interactive.
 * @property expanded Indicates whether the filter chip is in an expanded state.
 * @property label The label text displayed on the filter chip.
 * @property selectedValue The currently selected option for the filter chip.
 * @property options The list of selectable options for the filter chip.
 */
data class ODSFilterChipProps(
    var disabled: Boolean = false,
    var expanded: Boolean = false,
    var label: String? = null,
    var selectedValue: ODSFilterChipOptions? = null,
    var options: List<ODSFilterChipOptions>? = null,
)

/**
 * Represents an individual option for an ODS filter chip.
 *
 * @property id A unique identifier for the filter chip option. (Not exported by plugin)
 * @property disabled Indicates whether the option is disabled and non-interactive.
 * @property iconBefore The icon displayed before the label text (if applicable).
 * @property labelText The label text for the filter chip option.
 * @property selected Indicates whether the option is currently selected.
 */
data class ODSFilterChipOptions(
    var id: String = UUID.randomUUID().toString(), // Not exported by plugin
    var disabled: Boolean = false,
    var iconBefore: ODSIconModel? = null,
    var labelText: String? = null,
    var selected: Boolean = false,
)

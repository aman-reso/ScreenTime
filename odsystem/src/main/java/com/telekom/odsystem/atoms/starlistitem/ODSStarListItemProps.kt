package com.telekom.odsystem.atoms.starlistitem

import com.telekom.odsystem.foundations.ODSActions

/**
 * Properties describing a single item within a star rating list.
 *
 * @property disabled Disables interaction when true.
 * @property readOnly Prevents changes when true.
 * @property selected Indicates whether the star is selected.
 * @property state Interaction state for styling purposes.
 */
data class ODSStarListItemProps(
    var disabled: Boolean = false,
    var readOnly: Boolean = false,
    var selected: Boolean = false,
    var state: ODSActions = ODSActions.DEFAULT,
)

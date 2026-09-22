package com.telekom.odsystem.slots.popoverpreferredactions

import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.link.ODSLinkProps

enum class ODSPopoverPreferredActionsType {
    TWO_LINKS,
    TWO_BUTTONS,
}

/**
 * Properties configuring preferred actions inside a popover.
 *
 * @property type Layout type of the actions area.
 * @property button1Props Properties for the first button.
 * @property button2Props Properties for the second button.
 * @property link1Props Properties for the first link.
 * @property link2Props Properties for the second link.
 */
data class ODSPopoverPreferredActionsProps(
    var type: ODSPopoverPreferredActionsType = ODSPopoverPreferredActionsType.TWO_BUTTONS,
    var button1Props: ODSButtonProps? = null,
    var button2Props: ODSButtonProps? = null,
    var link1Props: ODSLinkProps? = null,
    var link2Props: ODSLinkProps? = null
)

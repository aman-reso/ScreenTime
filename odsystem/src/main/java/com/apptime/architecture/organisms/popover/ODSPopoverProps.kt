package com.telekom.odsystem.organisms.popover

enum class ODSPopoverAlignment {
    /** Align popover to the start edge. */
    START,

    /** Center the popover. */
    CENTER,

    /** Align popover to the end edge. */
    END,
}

enum class ODSPopoverPlacement {
    /** Popover appears below the anchor. */
    BOTTOM,

    /** Popover appears to the right of the anchor. */
    RIGHT,

    /** Popover appears above the anchor. */
    TOP,

    /** Popover appears to the left of the anchor. */
    LEFT,
}

/**
 * Properties controlling an ODS popover component.
 *
 * @property alignment Horizontal alignment of the popover.
 * @property label Optional label displayed within the popover.
 * @property placement Side of the anchor where the popover appears.
 * @property text Main text content inside the popover.
 */
data class ODSPopoverProps(
    var alignment: ODSPopoverAlignment = ODSPopoverAlignment.START,
    var label: String? = null,
    var placement: ODSPopoverPlacement = ODSPopoverPlacement.BOTTOM,
    var text: String? = null,
)

package com.telekom.odsystem.atoms.tooltip

enum class ODSTooltipAlignment {
    /** Tooltip aligns to the start edge. */
    START,

    /** Tooltip is centered. */
    CENTER,

    /** Tooltip aligns to the end edge. */
    END,
}

enum class ODSTooltipPlacement {
    /** Tooltip appears below the anchor. */
    BOTTOM,

    /** Tooltip appears to the right of the anchor. */
    RIGHT,

    /** Tooltip appears to the left of the anchor. */
    LEFT,

    /** Tooltip appears above the anchor. */
    TOP,
}

/**
 * Properties controlling the ODS tooltip behavior.
 *
 * @property alignment Horizontal alignment relative to the anchor.
 * @property label Text shown inside the tooltip.
 * @property placement Side of the anchor where the tooltip is displayed.
 */
data class ODSTooltipProps(
    var alignment: ODSTooltipAlignment = ODSTooltipAlignment.CENTER,
    var label: String? = null,
    var placement: ODSTooltipPlacement = ODSTooltipPlacement.BOTTOM,
)

package com.telekom.odsystem.atoms.segments

import com.telekom.odsystem.atoms.icon.ODSIconModel

enum class ODSSegmentsVariant {
    /** Segment fills available width. */
    FILL,

    /** Segment hugs its content width. */
    HUG,
}

enum class ODSSegmentsSize {
    /** Large sized segment. */
    LARGE,

    /** Small sized segment. */
    SMALL,
}

/**
 * Properties describing a single segment element.
 *
 * @property disabled Disables the segment.
 * @property icon Optional icon displayed in the segment.
 * @property label Text label.
 * @property selected Indicates if the segment is selected.
 * @property variant How segments lay out horizontally.
 * @property size Visual size of the segment.
 */
data class ODSSegmentsProps(
    var disabled: Boolean = false,
    var icon: ODSIconModel? = null,
    var label: String? = null,
    var selected: Boolean = false,
    var variant: ODSSegmentsVariant = ODSSegmentsVariant.HUG,
    var size: ODSSegmentsSize = ODSSegmentsSize.LARGE,
)

package com.telekom.odsystem.molecules.segmentedbutton

import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.segments.ODSSegmentsProps
import com.telekom.odsystem.atoms.segments.ODSSegmentsSize
import com.telekom.odsystem.atoms.segments.ODSSegmentsVariant
import java.util.UUID

enum class ODSSegmentedButtonSize {
    /** Standard large button size. */
    LARGE,

    /** Compact small button size. */
    SMALL,
}

enum class ODSSegmentedButtonVariant {
    /** Segments hug their content width. */
    HUG,

    /** Segments stretch to fill available width. */
    FILL,
}

/**
 * Properties to configure a segmented button group.
 *
 * @property size Size of the segments.
 * @property variant Layout behaviour of the segments.
 * @property segments Individual segment definitions.
 */
data class ODSSegmentedButtonProps(
    var size: ODSSegmentedButtonSize = ODSSegmentedButtonSize.LARGE,
    var variant: ODSSegmentedButtonVariant = ODSSegmentedButtonVariant.HUG,
    var segments: List<ODSSegmentedButtonSegmentsProps> = listOf(ODSSegmentedButtonSegmentsProps())
)

/**
 * Properties describing a single segment in the group.
 *
 * @property id Unique identifier for state management.
 * @property disabled Disables the segment when true.
 * @property icon Optional icon shown inside the segment.
 * @property label Text label for the segment.
 */
data class ODSSegmentedButtonSegmentsProps(
    var id: String = UUID.randomUUID().toString(), // Not exported by plugin
    var disabled: Boolean = false,
    var icon: ODSIconModel? = null,
    var label: String? = null,
)

internal fun ODSSegmentedButtonSegmentsProps.toODSSegmentsProps(
    size: ODSSegmentedButtonSize,
    variant: ODSSegmentedButtonVariant,
    selected: Boolean
): ODSSegmentsProps {
    return ODSSegmentsProps(
        disabled = this.disabled,
        icon = this.icon,
        label = this.label,
        selected = selected,
        size = when (size) {
            ODSSegmentedButtonSize.LARGE -> ODSSegmentsSize.LARGE
            ODSSegmentedButtonSize.SMALL -> ODSSegmentsSize.SMALL
        },
        variant = when (variant) {
            ODSSegmentedButtonVariant.HUG -> ODSSegmentsVariant.HUG
            ODSSegmentedButtonVariant.FILL -> ODSSegmentsVariant.FILL
        }
    )
}

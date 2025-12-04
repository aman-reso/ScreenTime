package com.telekom.odsystem.atoms.carouseltimer

/**
 * Model containing the duration for a carousel timer segment.
 *
 * @property duration Duration of the segment in milliseconds.
 */
data class ODSSegmentDurationModel(
    var duration: Int = 0,
)

/**
 * Properties configuring a carousel timer indicator.
 *
 * @property segmentsDuration List of durations for each segment.
 */
data class ODSCarouselTimerProps(
    var segmentsDuration: List<ODSSegmentDurationModel> = emptyList(),
)

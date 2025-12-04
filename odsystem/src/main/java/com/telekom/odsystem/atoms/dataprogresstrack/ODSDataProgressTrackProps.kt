package com.telekom.odsystem.atoms.dataprogresstrack

/**
 * Defines the size of the ODS data progress track.
 */
enum class ODSDataProgressTrackSize {
    /**
     * A large-sized data progress track.
     */
    LARGE,

    /**
     * A medium-sized data progress track.
     */
    MEDIUM,

    /**
     * A small-sized data progress track.
     */
    SMALL,
}

/**
 * Defines the type of the ODS data progress track.
 */
enum class ODSDataProgressTrackMode {
    STANDARD,
    ERROR,
    SUCCESS,
    DISABLED,
}

/**
 * Properties used to configure the appearance and behavior of an ODS data progress track.
 *
 * @property progress The current progress value, represented as a float between 0.0 and 1.0.
 * @property size The size of the data progress track (e.g., large, medium, small).
 * @property mode The mode of the data progress track, which can be either standard, error, success, or disabled.
 */
data class ODSDataProgressTrackProps(
    var progress: Float? = 0.0f,
    var size: ODSDataProgressTrackSize = ODSDataProgressTrackSize.LARGE,
    var mode: ODSDataProgressTrackMode = ODSDataProgressTrackMode.STANDARD,
)

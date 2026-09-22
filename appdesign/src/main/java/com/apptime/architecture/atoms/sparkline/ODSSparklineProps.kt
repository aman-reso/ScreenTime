package com.telekom.odsystem.atoms.sparkline

enum class ODSSparklineSize {
    /** Small sized sparkline. */
    SMALL,

    /** Large sized sparkline. */
    LARGE,
}

enum class ODSSparklineType {
    /** Display as a continuous progress bar. */
    PROGRESS_BAR,

    /** Display as discrete bars. */
    BARS,
}

/**
 * Properties configuring an ODS sparkline chart.
 *
 * @property percentage Percentage value to display.
 * @property size Size of the sparkline.
 * @property type Visual representation type.
 */
data class ODSSparklineProps(
    var percentage: Float = 0.0f,
    var size: ODSSparklineSize = ODSSparklineSize.SMALL,
    var type: ODSSparklineType = ODSSparklineType.BARS,
)

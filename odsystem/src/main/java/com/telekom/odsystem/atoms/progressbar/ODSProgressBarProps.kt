package com.telekom.odsystem.atoms.progressbar

/**
 * Available sizes for an [ODSProgressBar].
 */
enum class ODSProgressBarSize {
    /** A small progress bar. */
    SMALL,

    /** A medium-sized progress bar. */
    MEDIUM,

    /** A large progress bar. */
    LARGE,
}

/**
 * Possible states of an [ODSProgressBar].
 */
enum class ODSProgressBarMode {
    ERROR,
    STANDARD,
    SUCCESS
}

/**
 * Properties used to configure an [ODSProgressBar].
 *
 * @property counterText Text displayed inside the counter area.
 * @property disabled Disables interaction when true.
 * @property extraDataText Additional text displayed beside the main progress.
 * @property helperText Optional helper text shown below the bar.
 * @property size Defines the size of the bar.
 * @property mainDataProgress Current progress of the main data track.
 * @property extraDataProgress Current progress of the secondary data track.
 * @property mode Defines the mode of the progress bar, which can be error, standard, or success.
 * @property label Descriptive label displayed above the bar. If `labelText` is set, it will be used as the label.
 */
data class ODSProgressBarProps(
    var counterText: String? = null,
    var disabled: Boolean = false,
    var extraDataText: String? = null,
    var helperText: String? = null,
    var label: String? = null,
    var size: ODSProgressBarSize = ODSProgressBarSize.SMALL,
    var mode: ODSProgressBarMode = ODSProgressBarMode.STANDARD,
    var mainDataProgress: Float? = null,
    var extraDataProgress: Float? = null,
)

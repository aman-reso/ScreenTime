package com.telekom.odsystem.slots.cardanchoredimagepreferredcontent

import com.telekom.odsystem.atoms.sparkline.ODSSparklineProps

enum class ODSCardAnchoredImagePreferredContentContent {
    OVERVIEW,
    PROGRESS_BAR,
    BARS,
}

/**
 * Properties for content displayed over an anchored image.
 *
 * @property barsLabel Label for the bars representation.
 * @property content Type of content to show.
 * @property progressLabel Label for the progress bar.
 * @property sparklineProps Sparkline data for the overview.
 * @property sparklineUsageProps Usage sparkline data.
 * @property sparklineDataProps Data sparkline values.
 */
data class ODSCardAnchoredImagePreferredContentProps(
    var barsLabel: String? = null,
    var content: ODSCardAnchoredImagePreferredContentContent = ODSCardAnchoredImagePreferredContentContent.PROGRESS_BAR,
    var progressLabel: String? = null,
    var sparklineProps: ODSSparklineProps? = null,
    var sparklineUsageProps: ODSSparklineProps? = null,
    var sparklineDataProps: ODSSparklineProps? = null
)

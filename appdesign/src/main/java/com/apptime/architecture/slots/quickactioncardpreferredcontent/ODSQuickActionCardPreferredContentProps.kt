package com.telekom.odsystem.slots.quickactioncardpreferredcontent

import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.sparkline.ODSSparklineProps
import com.telekom.odsystem.atoms.sparkline.ODSSparklineType
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps

/**
 * Specifies the type of the quick action card preferred content within an `ODSCardQuickAction`.
 * The size affects the overall dimensions of the button, including its icon and text.
 */
enum class ODSQuickActionCardPreferredContentTitleType {
    TEXT,
    LOGO,
}

/**
 * @property type The visual style of the sparkline. Defaults to [ODSSparklineType.BARS].
 * @property percentage A value from 0.0 to 1.0 representing the sparkline's fill level. Defaults to 0.0f.
 */
data class ODSQuickActionCardPreferredContentSparklineProps(
    var type: ODSSparklineType = ODSSparklineType.BARS,
    var percentage: Float = 0.0f,
)

internal fun ODSQuickActionCardPreferredContentSparklineProps.toODSSparklineProps(): ODSSparklineProps {
    return ODSSparklineProps(
        type = this.type,
        percentage = this.percentage
    )
}

/**
 * Defines the configuration for the main content area of an [ODSQuickActionCard].
 *
 * @property logo The logo to display, used when `titleType` is [ODSQuickActionCardPreferredContentTitleType.LOGO].
 * @property showTags Toggles the visibility of the tags (`tag1Props`, `tag2Props`). Defaults to `true`.
 * @property subtitle An optional subtitle string displayed below the title.
 * @property titleType Determines if the title is text or a logo. See [ODSQuickActionCardPreferredContentTitleType].
 * @property title The title string, used when `titleType` is [ODSQuickActionCardPreferredContentTitleType.TEXT].
 * @property sparklineProps Configuration for the optional sparkline graph.
 * @property tag1Props Configuration for the first static tag.
 * @property tag2Props Configuration for the second static tag.
 */
data class ODSQuickActionCardPreferredContentProps(
    var logo: ODSImageModel? = null, // Not exported by plugin
    var showTags: Boolean = true,
    var subtitle: String? = null,
    var titleType: ODSQuickActionCardPreferredContentTitleType = ODSQuickActionCardPreferredContentTitleType.TEXT,
    var title: String? = null,
    var sparklineProps: ODSQuickActionCardPreferredContentSparklineProps? = null,
    var tag1Props: ODSTagStaticProps? = null,
    var tag2Props: ODSTagStaticProps? = null
)

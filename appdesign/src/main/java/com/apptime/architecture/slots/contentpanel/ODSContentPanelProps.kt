import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.carouseltimer.ODSCarouselTimerProps

/**
 * Properties for the ODSContentPanel component.
 *
 * @param segmentText Optional text for the panel's segments.
 * @param isInProgressElementIndex Index of the active carousel element (defaults to 0).
 * @param isRunning Whether the carousel is animating (defaults to false).
 * @param carouselTimerProps Optional properties for the carousel timer.
 * @param buttonProps Optional properties for the main button.
 */

data class ODSContentPanelProps(
    var segmentText: String? = null,
    var isInProgressElementIndex: Int = 0, // Not exported from plugin
    var isRunning: Boolean = false, // Not exported from plugin
    var carouselTimerProps: ODSCarouselTimerProps? = null,
    var buttonProps: ODSContentPanelButtonProps? = null,
)

/**
 * Properties for the button within the ODSContentPanel.
 *
 * @param label Optional text label for the button.
 */
data class ODSContentPanelButtonProps(
    var label: String? = null,
)

internal fun ODSContentPanelButtonProps.toODSButtonProps(): ODSButtonProps {
    return ODSButtonProps(
        label = this.label,
        size = ODSButtonSize.SMALL
    )
}

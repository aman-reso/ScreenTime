package com.telekom.odsystem.slots.cardwidgetpreferredcontent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * A composable function that displays the preferred content for a card widget,
 * typically consisting of a title and an optional subtitle. It's designed to be
 * a standard content slot within larger card components.
 *
 * The layout arranges the title and subtitle vertically. The subtitle is only
 * displayed if `props.showSubtitle` is true and `props.subtitle` is not empty.
 *
 * @param modifier The [Modifier] to be applied to the content container.
 * @param scheme The [ODSTheme] to apply for styling, determining colors and typography.
 * @param props The [ODSCardWidgetPreferredContentProps] containing the data to display,
 * such as title and subtitle text.
 */
@Composable
fun ODSCardWidgetPreferredContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardWidgetPreferredContentProps = ODSCardWidgetPreferredContentProps(),
) {

    val style = ODSCardWidgetPreferredContentStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement,
    ) {
        if (!props.title.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.title,
                style = style.titleStyle,
                color = style.titleColor,
                textAlign = style.titleTextAlign
            )
        }
        if (props.showSubtitle && !props.subtitle.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.subtitle,
                style = style.subtitleStyle,
                color = style.subtitleColor,
                textAlign = style.subtitleTextAlign
            )
        }
    }
}

package com.telekom.odsystem.slots.cardcontentbasic

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardContentBasic composable.
 *
 * Renders the basic content of a card, including a label, heading, subtitle, and content text.
 *
 * @param modifier Modifier for the root `ODSColumn`.
 * @param scheme The [ODSTheme] for styling. Defaults to `neutralScheme`.
 * @param props [ODSCardContentBasicProps] containing the text content. Defaults to empty.
 */
@Composable
fun ODSCardContentBasic(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardContentBasicProps = ODSCardContentBasicProps()
) {

    val style = ODSCardContentBasicStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier,
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
    ) {

        if (!props.label.isNullOrEmpty() || !props.heading.isNullOrEmpty()) {
            ODSLabelAndHeading(props = props, style = style)
        }

        if (!props.subtitle.isNullOrEmpty() || !props.content.isNullOrEmpty()) {
            ODSContent(props = props, style = style)
        }
    }
}

@Composable
private fun ODSLabelAndHeading(
    props: ODSCardContentBasicProps,
    style: ODSCardContentBasicStyle
) {
    ODSColumn(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                this.role = Role.Button
                onClick(
                    action = { false },
                )
            },
        gap = style.labelHeadingGap,
        verticalArrangement = style.labelHeadingVerticalArrangement,
        verticalAlignment = style.labelHeadingVerticalAlignment,
        horizontalAlignment = style.labelHeadingHorizontalAlignment
    ) {
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign
            )
        }
        if (!props.heading.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.heading,
                style = style.headingTextStyle,
                color = style.headingColor,
                textAlign = style.headingTextAlign
            )
        }
    }
}

@Composable
private fun ODSContent(
    props: ODSCardContentBasicProps,
    style: ODSCardContentBasicStyle
) {
    ODSColumn(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { },
        gap = style.contentGap,
        verticalArrangement = style.contentVerticalArrangement,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalAlignment = style.contentHorizontalAlignment
    ) {
        if (!props.subtitle.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.subtitle,
                style = style.subtitleTextStyle,
                color = style.subtitleColor,
                textAlign = style.subtitleTextAlign
            )
        }
        if (!props.content.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = AnnotatedString.fromHtml(htmlString = props.content!!),
                style = style.bodyTextTextStyle,
                color = style.bodyTextColor,
                textAlign = style.bodyTextTextAlign
            )
        }
    }
}

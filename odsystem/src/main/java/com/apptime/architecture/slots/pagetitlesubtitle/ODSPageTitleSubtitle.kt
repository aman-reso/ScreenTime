package com.telekom.odsystem.slots.pagetitlesubtitle

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ODSPageTitleSubtitle(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSPageTitleSubtitleProps = ODSPageTitleSubtitleProps()
) {

    val style = ODSPageTitleSubtitleStyle().getStyle(scheme = scheme, props = props)

    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                isTraversalGroup = true
            },
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = style.odsPageTitleHorizontalAlignment,
            verticalAlignment = style.odsPageTitleVerticalAlignment,
            horizontalArrangement = style.odsPageTitleHorizontalArrangement
        ) {
            if (!props.titleText.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier
                        .weight(1f)
                        .semantics { heading() },
                    text = props.titleText,
                    style = style.pageTitleStyle,
                    color = style.pageTitleColor,
                    textAlign = style.pageTitleTextAlign,
                    maxLines = style.pageTitleMaxLines,
                    overflow = style.pageTitleOverflow
                )
            }
        }
        if (props.showPageSubtitle) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = style.odsPageSubtitleHorizontalAlignment,
                verticalAlignment = style.odsPageSubtitleVerticalAlignment,
                horizontalArrangement = style.odsPageSubtitleHorizontalArrangement
            ) {
                if (!props.subtitleText.isNullOrEmpty()) {
                    ODSText(
                        modifier = Modifier.weight(1f),
                        text = props.subtitleText,
                        style = style.pageSubtitleStyle,
                        color = style.pageSubtitleColor,
                        textAlign = style.pageSubtitleTextAlign,
                        maxLines = style.pageSubtitleMaxLines,
                        overflow = style.pageSubtitleOverflow
                    )
                }
            }
        }
    }
}

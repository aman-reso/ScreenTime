package com.telekom.odsystem.slots.cardanchoredimagepreferredcontent

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.sparkline.ODSSparkline
import com.telekom.odsystem.atoms.sparkline.ODSSparklineProps
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod", "MaximumLineLength")
@Composable
/**
 * ODSCardAnchoredImagePreferredContent composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
fun ODSCardAnchoredImagePreferredContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardAnchoredImagePreferredContentProps = ODSCardAnchoredImagePreferredContentProps()
) {
    val style = ODSCardAnchoredImagePreferredContentStyle().getStyle(scheme = scheme, props = props)
    when (props.content) {
        ODSCardAnchoredImagePreferredContentContent.OVERVIEW -> {
            ODSOverview(
                modifier = modifier,
                scheme = scheme,
                style = style,
                sparklineDataProps = props.sparklineDataProps,
                progressLabel = props.progressLabel,
                sparklineUsageProps = props.sparklineUsageProps,
                barsLabel = props.barsLabel
            )
        }

        ODSCardAnchoredImagePreferredContentContent.PROGRESS_BAR -> {
            props.sparklineProps?.let {
                ODSProgressBar(
                    modifier = modifier,
                    scheme = scheme,
                    style = style,
                    sparklineProps = it
                )
            }
        }

        ODSCardAnchoredImagePreferredContentContent.BARS -> {
            ODSBars(
                modifier = modifier,
                scheme = scheme,
                style = style,
                sparklineProps = props.sparklineProps,
                barsLabel = props.barsLabel
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Suppress("LongMethod")
@Composable
private fun ODSOverview(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardAnchoredImagePreferredContentStyle,
    sparklineDataProps: ODSSparklineProps?,
    progressLabel: String?,
    sparklineUsageProps: ODSSparklineProps?,
    barsLabel: String?
) {
    ODSWrap(
        modifier = modifier.semantics(mergeDescendants = true) { },
        horizontalGap = style.gap,
        verticalGap = style.gap,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment
    ) {
        ODSColumn(
            gap = style.dataGap,
            padding = style.dataPadding,
            verticalArrangement = style.dataVerticalArrangement,
            verticalAlignment = style.dataVerticalAlignment,
            horizontalAlignment = style.dataHorizontalAlignment
        ) {
            if (sparklineDataProps != null) {
                ODSSparkline(
                    scheme = scheme,
                    props = sparklineDataProps
                )
            }
            if (!progressLabel.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.sizeWithinBounds(
                        maxWidth = style.progressLabelMaxWidth ?: 0.dp,
                        minWidth = style.progressLabelMinWidth ?: 0.dp
                    ),
                    text = progressLabel,
                    style = style.progressLabelTextStyle,
                    color = style.progressLabelColor,
                    textAlign = style.progressLabelTextAlign,
                )
            }
        }
        ODSColumn(
            gap = style.usageGap,
            verticalArrangement = style.usageVerticalArrangement,
            verticalAlignment = style.usageVerticalAlignment,
            horizontalAlignment = style.usageHorizontalAlignment
        ) {
            if (sparklineUsageProps != null) {
                ODSSparkline(
                    scheme = scheme,
                    props = sparklineUsageProps
                )
            }
            if (!barsLabel.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.sizeWithinBounds(
                        minWidth = style.barsLabelMinWidth ?: 0.dp,
                        maxWidth = style.barsLabelMaxWidth ?: 0.dp
                    ),
                    text = barsLabel,
                    style = style.barsLabelTextStyle,
                    color = style.barsLabelColor,
                    textAlign = style.barsLabelTextAlign
                )
            }
        }
    }
}

@Composable
private fun ODSProgressBar(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardAnchoredImagePreferredContentStyle,
    sparklineProps: ODSSparklineProps
) {
    ODSRow(
        modifier = modifier.semantics(mergeDescendants = true) { },
        gap = style.gap,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment
    ) {
        ODSSparkline(
            scheme = scheme,
            props = sparklineProps
        )
    }
}

@Composable
private fun ODSBars(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardAnchoredImagePreferredContentStyle,
    sparklineProps: ODSSparklineProps?,
    barsLabel: String?
) {
    ODSRow(
        modifier = modifier.semantics(mergeDescendants = true) { },
        gap = style.gap,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment
    ) {
        ODSColumn(
            gap = style.usageGap,
            verticalArrangement = style.usageVerticalArrangement,
            verticalAlignment = style.usageVerticalAlignment,
            horizontalAlignment = style.usageHorizontalAlignment
        ) {
            sparklineProps?.let {
                ODSSparkline(
                    scheme = scheme,
                    props = it
                )
            }
            if (!barsLabel.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.sizeWithinBounds(
                        minWidth = style.barsLabelMinWidth ?: 0.dp,
                    ),
                    text = barsLabel,
                    style = style.barsLabelTextStyle,
                    color = style.barsLabelColor,
                    textAlign = style.barsLabelTextAlign
                )
            }
        }
    }
}

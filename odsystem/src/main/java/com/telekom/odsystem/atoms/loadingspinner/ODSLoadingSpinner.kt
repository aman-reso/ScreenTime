package com.telekom.odsystem.atoms.loadingspinner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSLoadingSpinner composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSLoadingSpinner(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSLoadingSpinnerProps = ODSLoadingSpinnerProps()
) {
    val style = ODSLoadingSpinnerStyle().getStyle(scheme = scheme, props = props)

    if (props.labelAlignment == ODSLoadingSpinnerLabelAlignment.HORIZONTAL) {
        ODSHorizontalLoadingSpinner(
            modifier = modifier,
            props = props,
            style = style
        )
    } else {
        ODSVerticalLoadingSpinner(
            modifier = modifier,
            props = props,
            style = style
        )
    }
}

@Composable
private fun ODSVerticalLoadingSpinner(
    modifier: Modifier = Modifier,
    props: ODSLoadingSpinnerProps = ODSLoadingSpinnerProps(),
    style: ODSLoadingSpinnerStyle
) {

    ODSColumn(
        modifier = modifier.semantics(mergeDescendants = true) {
            progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        },
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        ODSRow(
            modifier = Modifier,
            width = style.loadingSpinnerContainerWidth,
            height = style.loadingSpinnerContainerHeight,
            verticalAlignment = style.loadingSpinnerContainerVerticalAlignment,
            horizontalAlignment = style.loadingSpinnerContainerHorizontalAlignment,
            horizontalArrangement = style.loadingSpinnerContainerHorizontalArrangement
        ) {
            // The CircularProgressIndicator is placed inside the ODSRow to ensure it takes the full size of the container.
            CircularProgressIndicator(
                modifier = Modifier
                    .clearAndSetSemantics { /*Handle semantics in the container*/ }
                    .fillMaxSize(),
                color = style.progressIndicatorColor?.getColor() ?: Color.Transparent,
                trackColor = Color.Transparent,
                strokeWidth = style.progressIndicatorStrokeWidth ?: 0.dp
            )
        }

        if (props.labelAlignment != ODSLoadingSpinnerLabelAlignment.NONE) {
            LabelText(props = props, style = style)
        }
    }
}

@Composable
private fun ODSHorizontalLoadingSpinner(
    modifier: Modifier = Modifier,
    props: ODSLoadingSpinnerProps = ODSLoadingSpinnerProps(),
    style: ODSLoadingSpinnerStyle
) {
    ODSRow(
        modifier = modifier.semantics(mergeDescendants = true) {
            progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        },
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSRow(
            modifier = Modifier,
            width = style.loadingSpinnerContainerWidth,
            height = style.loadingSpinnerContainerHeight,
            verticalAlignment = style.loadingSpinnerContainerVerticalAlignment,
            horizontalAlignment = style.loadingSpinnerContainerHorizontalAlignment,
            horizontalArrangement = style.loadingSpinnerContainerHorizontalArrangement
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .clearAndSetSemantics { /*Handle semantics in the container*/ }
                    .fillMaxSize(),
                color = style.progressIndicatorColor?.getColor() ?: Color.Transparent,
                trackColor = Color.Transparent,
                strokeWidth = style.progressIndicatorStrokeWidth ?: 0.dp
            )
        }
        if (props.labelAlignment != ODSLoadingSpinnerLabelAlignment.NONE) {
            LabelText(props = props, style = style)
        }
    }
}

@Composable
private fun LabelText(
    props: ODSLoadingSpinnerProps = ODSLoadingSpinnerProps(),
    style: ODSLoadingSpinnerStyle
) {
    if (props.labelAlignment != ODSLoadingSpinnerLabelAlignment.NONE) {
        ODSText(
            text = props.labelText,
            style = style.labelStyle,
            color = style.labelColor,
            textAlign = style.labelTextAlign
        )
    }
}

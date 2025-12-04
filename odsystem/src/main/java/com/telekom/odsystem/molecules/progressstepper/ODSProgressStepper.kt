package com.telekom.odsystem.molecules.progressstepper

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.progressstepperitem.ODSProgressStepperItem
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSProgressStepper composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSProgressStepper(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSProgressStepperProps = ODSProgressStepperProps(),
) {
    val style = ODSProgressStepperStyle().getStyle(scheme = scheme, props = props)

    if (props.variant == ODSProgressStepperVariant.VERTICAL) {
        ODSVerticalStepper(
            modifier = modifier,
            scheme = scheme,
            style = style,
            props = props
        )
    } else {
        ODSHorizontalStepper(
            modifier = modifier,
            scheme = scheme,
            style = style,
            props = props
        )
    }
}

@Composable
private fun ODSVerticalStepper(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    style: ODSProgressStepperStyle,
    props: ODSProgressStepperProps = ODSProgressStepperProps(),
) {
    ODSRow(
        modifier = modifier
            .semantics(mergeDescendants = true) { /* Merges semantics */ }
            .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified)
            .height(IntrinsicSize.Min),
        gap = style.gap,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        ODSBadgeDividerVerticalFrameContainer(
            style = style,
            props = props,
            scheme = scheme
        )

        if (props.showContent) {
            ODSContentFrameContainer(
                style = style,
                props = props
            )
        }
    }
}

@Composable
private fun ODSHorizontalStepper(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    style: ODSProgressStepperStyle,
    props: ODSProgressStepperProps = ODSProgressStepperProps(),
) {
    ODSColumn(
        modifier = modifier
            .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified)
            .semantics(mergeDescendants = true) { /* Merges semantics */ },
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        ODSBadgeDividerHorizontalFrameContainer(
            style = style,
            props = props,
            scheme = scheme
        )

        if (props.showContent) {
            ODSContentFrameContainer(
                style = style,
                props = props
            )
        }
    }
}

@Composable
private fun ODSBadgeDividerHorizontalFrameContainer(
    style: ODSProgressStepperStyle,
    props: ODSProgressStepperProps,
    scheme: ODSTheme,
) {
    ODSRow(
        gap = style.badgeDividerFrameGap,
        padding = style.badgeDividerFramePadding,
        horizontalArrangement = style.badgeDividerFrameHorizontalArrangement,
        verticalAlignment = style.badgeDividerFrameVerticalAlignment,
        horizontalAlignment = style.badgeDividerFrameHorizontalAlignment
    ) {
        props.progressStepperItemProps?.let {
            ODSProgressStepperItem(
                scheme = scheme,
                props = it.toODSProgressStepperItemProps(size = props.size)
            )
        }
        props.dividerProps?.let {

            ODSDivider(
                scheme = scheme,
                props = it.toODSDividerProps(variant = props.variant)
            )
        }
    }
}

@Composable
private fun ODSBadgeDividerVerticalFrameContainer(
    style: ODSProgressStepperStyle,
    props: ODSProgressStepperProps,
    scheme: ODSTheme,
) {
    ODSColumn(
        modifier = Modifier.fillMaxHeight(),
        gap = style.badgeDividerFrameGap,
        padding = style.badgeDividerFramePadding,
        verticalArrangement = style.badgeDividerFrameVerticalArrangement,
        verticalAlignment = style.badgeDividerFrameVerticalAlignment,
        horizontalAlignment = style.badgeDividerFrameHorizontalAlignment
    ) {
        props.progressStepperItemProps?.let {
            ODSProgressStepperItem(
                scheme = scheme,
                props = it.toODSProgressStepperItemProps(size = props.size)
            )
        }
        props.dividerProps?.let {
            ODSDivider(
                scheme = scheme,
                props = it.toODSDividerProps(variant = props.variant)
            )
        }
    }
}

@Composable
private fun ODSContentFrameContainer(
    style: ODSProgressStepperStyle,
    props: ODSProgressStepperProps,
) {
    ODSColumn(
        gap = style.contentFrameGap,
        padding = style.contentFramePadding,
        verticalArrangement = style.contentFrameVerticalArrangement,
        verticalAlignment = style.contentFrameVerticalAlignment,
        horizontalAlignment = style.contentFrameHorizontalAlignment
    ) {
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = props.label,
                style = style.labelStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign
            )
        }
        if (!props.text.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.text,
                style = style.textStyle,
                color = style.textColor,
                textAlign = style.textTextAlign
            )
        }
    }
}

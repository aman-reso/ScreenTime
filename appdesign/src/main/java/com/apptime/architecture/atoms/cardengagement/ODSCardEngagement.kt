package com.telekom.odsystem.atoms.cardengagement

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardEngagement composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSCardEngagement(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardEngagementProps = ODSCardEngagementProps(),
    onClick: () -> Unit = {}
) {

    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val style = ODSCardEngagementStyle().getStyle(
        scheme = scheme,
        state = if (pressed) ODSActions.PRESSED else if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    val scale by animateFloatAsState(
        targetValue = style.scaleFactor ?: DEFAULT_FACTOR,
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )

    ODSBox(
        modifier = modifier
            .customClickable(
                interactionSource = interactionSource,
                isPressed = { pressed = it },
                onClick = onClick,
                role = Role.Button
            )
    ) {
        ODSColumn(
            cornerRadius = style.backgroundBorderRadius,
            clipContent = style.backgroundClipContent ?: true,
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale),
            verticalAlignment = style.backgroundVerticalAlignment,
            horizontalAlignment = style.backgroundHorizontalAlignment,
            verticalArrangement = style.backgroundVerticalArrangement,
            background = style.backgroundBackgroundColor,
        ) {
            ODSImageContainer(
                style = style,
                props = props,
                modifier = Modifier
                    .scale(1 / scale) // Added so that image is not scaled on hover
                    .fillMaxWidth(),
            )
        }
        props.label?.takeIf { it.isNotEmpty() }?.let { label ->
            ODSTextContainer(
                style = style,
                label = label,
                modifier = Modifier.align(style.textContainerAlignment ?: Alignment.CenterStart)
            )
        }
    }
}

@Composable
private fun ODSImageContainer(
    modifier: Modifier,
    style: ODSCardEngagementStyle,
    props: ODSCardEngagementProps,
) {
    ODSRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        cornerRadius = style.imageContainerBorderRadius,
        height = style.imageContainerHeight,
    ) {
        ODSImage(
            modifier = Modifier
                .offset(x = style.imageHorizontalOffset?.dp ?: 0.dp)
                .wrapContentSize(unbounded = true),
            height = style.imageHeight,
            width = style.imageWidth,
            imageModel = props.image,
            contentScale = style.imageObjectFit ?: ContentScale.Crop
        )
    }
}

@Composable
private fun ODSTextContainer(
    modifier: Modifier,
    style: ODSCardEngagementStyle,
    label: String
) {
    ODSColumn(
        modifier = modifier,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        cornerRadius = style.borderRadius,
        verticalArrangement = style.verticalArrangement,
    ) {
        ODSRow(
            padding = style.labelContainerPadding,
            horizontalAlignment = style.labelContainerHorizontalAlignment,
            verticalAlignment = style.labelContainerVerticalAlignment,
            horizontalArrangement = style.labelContainerHorizontalArrangement
        ) {
            ODSText(
                maxLines = style.labelTextMaxLines,
                overflow = style.labelTextOverflow,
                text = label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign
            )
        }
    }
}

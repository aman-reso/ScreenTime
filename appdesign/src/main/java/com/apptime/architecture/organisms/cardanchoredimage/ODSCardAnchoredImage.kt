package com.telekom.odsystem.organisms.cardanchoredimage

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * ODSCardAnchoredImage composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param contentSlot Parameter for customization.
 * @param actionSlot Parameter for customization.
 * @param imageSlot Parameter for customization.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSCardAnchoredImage(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardAnchoredImageProps = ODSCardAnchoredImageProps(),
    contentSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable (() -> Unit)? = null,
    imageSlot: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val style = ODSCardAnchoredImageStyle().getStyle(
        scheme = scheme, props = props, state = when {
            pressed -> ODSActions.PRESSED
            isHovered -> ODSActions.HOVERED
            else -> ODSActions.DEFAULT
        }
    )
    val scale by animateFloatAsState(
        targetValue = if (isHovered && !pressed) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )
    ODSBox(
        contentAlignment = Alignment.TopStart,
        modifier = modifier.customClickable(
            interactionSource = interactionSource,
            isPressed = { pressed = it },
            onClick = onClick,
            role = Role.Button
        )
    ) {
        ODSColumn(
            modifier = Modifier
                .matchParentSize()
                .sizeWithinBounds(
                    minWidth = style.cardBackgroundWidth ?: Dp.Unspecified,
                    minHeight = style.cardBackgroundHeight ?: Dp.Unspecified
                )
                .scale(scale),
            clipContent = style.cardBackgroundClipContent != false,
            cornerRadius = style.cardBackgroundBorderRadius,
            verticalArrangement = style.cardBackgroundVerticalArrangement,
            verticalAlignment = style.cardBackgroundVerticalAlignment,
            horizontalAlignment = style.cardBackgroundHorizontalAlignment,
            background = style.cardBackgroundBackgroundColor,
        ) {
            imageSlot?.let {
                ODSBox(
                    modifier = Modifier
                        .scale(1 / scale)
                        .fillMaxSize(),
                    content = {
                        it()
                    }
                )
            }
        }
        ODSColumn(
            modifier = Modifier.sizeWithinBounds(minHeight = style.minHeight ?: MIN_HEIGHT.dp),
            padding = style.padding,
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
            effect = style.boxShadow,
            clipContent = style.clipContent != false
        ) {
            ODSCardContentContainer(
                style = style,
                props = props,
                contentSlot = contentSlot,
                onClick = onClick,
            )
            if (actionSlot != null && props.size == ODSCardAnchoredImageSize.MEDIUM) {
                ODSActionSlotContainer(style = style, props = props, actionSlot = actionSlot)
            }
        }
    }
}

@Composable
private fun ODSCardContentContainer(
    modifier: Modifier = Modifier,
    style: ODSCardAnchoredImageStyle,
    props: ODSCardAnchoredImageProps,
    contentSlot: @Composable (() -> Unit)?,
    onClick: () -> Unit,
) {
    ODSColumn(
        gap = style.contentContainerGap,
        verticalArrangement = style.contentContainerVerticalArrangement,
        verticalAlignment = style.contentContainerVerticalAlignment,
        horizontalAlignment = style.contentContainerHorizontalAlignment,
        modifier = modifier.sizeWithinBounds(
            minHeight = style.contentContainerMinHeight ?: Dp.Unspecified
        )
    ) {
        ODSCardHeadingLabelContainer(
            style = style,
            props = props,
            modifier = Modifier.semantics(mergeDescendants = true) {
                role = Role.Button
                onClick { onClick(); true }
            }
        )
        if (contentSlot != null && props.size == ODSCardAnchoredImageSize.MEDIUM) {
            ODSContentSlotContainer(style = style, contentSlot = contentSlot)
        }
    }
}

@Composable
private fun ODSCardHeadingLabelContainer(
    modifier: Modifier = Modifier,
    style: ODSCardAnchoredImageStyle,
    props: ODSCardAnchoredImageProps,
) {
    ODSColumn(
        modifier = modifier,
        gap = style.headingLabelContainerGap,
        verticalArrangement = style.headingLabelContainerVerticalArrangement,
        verticalAlignment = style.headingLabelContainerVerticalAlignment,
        horizontalAlignment = style.headingLabelContainerHorizontalAlignment
    ) {
        if (!props.heading.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.heading,
                style = style.headingTextStyle,
                color = style.headingColor,
                textAlign = style.headingTextAlign
            )
        }
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign
            )
        }
    }
}

@Composable
private fun ODSContentSlotContainer(
    modifier: Modifier = Modifier,
    style: ODSCardAnchoredImageStyle,
    contentSlot: @Composable () -> Unit,
) {
    ODSRow(
        modifier = modifier,
        horizontalArrangement = style.contentSlotContainerHorizontalArrangement,
        horizontalAlignment = style.contentSlotContainerHorizontalAlignment,
        verticalAlignment = style.contentSlotContainerVerticalAlignment
    ) {
        contentSlot()
    }
}

@Composable
private fun ODSActionSlotContainer(
    style: ODSCardAnchoredImageStyle,
    props: ODSCardAnchoredImageProps,
    actionSlot: @Composable () -> Unit,
) {
    ODSColumn(
        padding = style.actionSlotContainerPadding,
        verticalArrangement = style.actionSlotContainerVerticalArrangement,
        verticalAlignment = style.actionSlotContainerVerticalAlignment,
        horizontalAlignment = style.actionSlotContainerHorizontalAlignment
    ) {
        if (props.alignActionSlotToBottom) {
            Spacer(modifier = Modifier.weight(1f))
        }
        actionSlot()
    }
}

package com.telekom.odsystem.organisms.cardimage

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.offset
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * ODSCardImage composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param contentSlot Parameter for customization.
 * @param actionSlot Parameter for customization.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSCardImage(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardImageProps = ODSCardImageProps(),
    contentSlot: @Composable () -> Unit = { },
    actionSlot: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }
    val style = ODSCardImageStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (pressed) ODSActions.PRESSED else if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    )
    val scaleX by animateFloatAsState(
        if (isHovered && !pressed) {
            (width + (style.scaleFactor ?: 0f)) / width
        } else {
            1f
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = "",
    )
    val scaleY by animateFloatAsState(
        if (isHovered && !pressed) {
            (height + (style.scaleFactor ?: 0f)) / height
        } else {
            1f
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = "",
    )
    ODSBox(
        contentAlignment = style.contentContentAlignment,
        modifier = modifier.customClickable(
            interactionSource = interactionSource,
            isPressed = { pressed = it },
            onClick = onClick,
            role = Role.Button
        )
    ) {
        ODSColumn(
            cornerRadius = style.cardBgBorderRadius,
            clipContent = style.cardBgClipContent != false,
            verticalArrangement = style.cardBgVerticalArrangement,
            verticalAlignment = style.cardBgVerticalAlignment,
            horizontalAlignment = style.cardBgHorizontalAlignment,
            background = style.cardBgBackgroundColor,
            effect = style.boxShadow,
            modifier = Modifier
                .matchParentSize()
                .scale(scaleX = scaleX, scaleY = scaleY)
        ) { }
        ODSColumn(
            modifier = Modifier
                .onGloballyPositioned {
                    width = it.size.width
                    height = it.size.height
                }
                .semantics {
                    isTraversalGroup = true
                },
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
        ) {
            if (props.imagePosition == ODSCardImageImagePosition.TOP) {
                ODSCardImageAndLogoContainer(
                    modifier = Modifier.semantics(mergeDescendants = true) { traversalIndex = 1f },
                    props = props,
                    style = style,
                    scale = scaleX to scaleY
                )
                ODSCardContent(
                    modifier = (if (props.isHorizontal) Modifier.weight(1f) else Modifier)
                        .semantics(mergeDescendants = true) {
                            traversalIndex = 0f
                        },
                    isHorizontal = props.isHorizontal,
                    style = style,
                    contentSlot = contentSlot,
                    actionSlot = actionSlot
                )
            } else if (props.imagePosition == ODSCardImageImagePosition.BOTTOM) {
                ODSCardContent(
                    modifier = (if (props.isHorizontal) Modifier.weight(1f) else Modifier)
                        .semantics(mergeDescendants = true) {
                            traversalIndex = 1f
                        },
                    isHorizontal = props.isHorizontal,
                    style = style,
                    contentSlot = contentSlot,
                    actionSlot = actionSlot
                )
                ODSCardImageAndLogoContainer(
                    modifier = Modifier.semantics(mergeDescendants = true) { traversalIndex = 0f },
                    props = props,
                    style = style,
                    scale = scaleX to scaleY
                )
            } else if (props.imagePosition == ODSCardImageImagePosition.LEFT) {
                ODSRow(modifier = Modifier.fillMaxWidth()) {
                    ODSCardImageAndLogoContainer(
                        modifier = Modifier.semantics(mergeDescendants = true) {
                            traversalIndex = 0f
                        }, props = props, style = style, scale = scaleX to scaleY
                    )
                    ODSCardContent(
                        modifier = (if (props.isHorizontal) Modifier.weight(1f) else Modifier).semantics(
                                mergeDescendants = true
                            ) {
                                traversalIndex = 1f
                            },
                        isHorizontal = props.isHorizontal,
                        style = style,
                        contentSlot = contentSlot,
                        actionSlot = actionSlot
                    )
                }
            }
        }
    }
}

@Composable
private fun ODSCardImageAndLogoContainer(
    modifier: Modifier = Modifier,
    props: ODSCardImageProps,
    style: ODSCardImageStyle,
    scale: Pair<Float, Float>
) {
    ODSBox(
        contentAlignment = style.imageAspectRatioContentAlignment,
        modifier = modifier.fillMaxWidth()
    ) {
        ODSBox(
            modifier = Modifier.scale(scaleX = scale.first, scaleY = scale.second),
            clipContent = style.imageContainerClipContent != false,
        ) {
            ODSRow(
                cornerRadius = style.imageContainerBorderRadius,
                clipContent = style.imageContainerClipContent != false,
                horizontalArrangement = style.imageContainerHorizontalArrangement,
                horizontalAlignment = style.imageContainerHorizontalAlignment,
                verticalAlignment = style.imageContainerVerticalAlignment,
                modifier = Modifier.matchParentSize()
            ) { }
            ODSImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(props.imageAspectRatio.value),
                imageModel = props.image,
                cornerRadius = style.imageContainerBorderRadius,
                contentScale = style.imageObjectFit ?: ContentScale.Crop,
            )
        }
        props.logo?.let {
            ODSImage(
                cornerRadius = style.logoRadius,
                imageModel = props.logo,
                width = style.logoImageWidth,
                height = style.logoImageHeight,
                modifier = Modifier
                    .offset(offset = style.logoImageOffset)
                    .align(alignment = style.logoImageContentAlignment ?: Alignment.TopStart),
                contentScale = style.logoImageObjectFit ?: ContentScale.Fit
            )
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun ODSCardContent(
    modifier: Modifier,
    isHorizontal: Boolean,
    style: ODSCardImageStyle,
    contentSlot: @Composable () -> Unit,
    actionSlot: @Composable (() -> Unit)?
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = style.contentGap,
        padding = style.contentPadding,
        verticalArrangement = style.contentVerticalArrangement,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalAlignment = style.contentHorizontalAlignment,
    ) {
        ODSColumn(
            verticalArrangement = style.slotContainerVerticalArrangement,
            verticalAlignment = style.slotContainerVerticalAlignment,
            horizontalAlignment = style.slotContainerHorizontalAlignment,
            modifier = Modifier.fillMaxWidth()
        ) {
            contentSlot()
        }
        actionSlot?.let {
            ODSColumn(
                modifier = (if (isHorizontal) Modifier.weight(1f) else Modifier).fillMaxWidth(),
                verticalArrangement = style.actionContainerVerticalArrangement,
                verticalAlignment = style.actionContainerVerticalAlignment,
                horizontalAlignment = style.actionContainerHorizontalAlignment,
                content = {
                    it()
                }
            )
        }
    }
}

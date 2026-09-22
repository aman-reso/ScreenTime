package com.telekom.odsystem.organisms.cardwidget

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.extensions.ifNotNull
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.offset
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardWidget composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 * @param contentSlot Parameter for customization.
 */
@Suppress("MagicNumber", "LongMethod")
@Composable
fun ODSCardWidget(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardWidgetProps = ODSCardWidgetProps(),
    contentSlot: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    val localDensity = LocalDensity.current
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val state =
        when {
            isPressed -> ODSActions.PRESSED
            isHovered -> ODSActions.HOVERED
            else -> ODSActions.DEFAULT
        }

    val style = ODSCardWidgetStyle().getStyle(scheme = scheme, props = props, state = state)
    var width by remember { mutableIntStateOf(0) }
    val cardWidth = with(localDensity) { width.toDp() }
    val scale by animateFloatAsState(
        targetValue = if (isHovered && !isPressed) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )
    ODSColumn(
        modifier = modifier
            .customClickable(
                isPressed = { isPressed = it },
                interactionSource = interactionSource,
                onClick = onClick,
                role = Role.Button
            ),
        cornerRadius = style.cornerRadius,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement,
        minWidth = style.minWidth,
        minHeight = style.minHeight,
    ) {
        if (props.type == ODSCardWidgetType.TOP_IMAGE) {
            ODSTopImageContainer(
                style = style,
                props = props,
                cardWidth = cardWidth,
                scale = scale
            )
        }
        ODSContentContainer(
            style = style,
            props = props,
            contentSlot = contentSlot,
            scale = scale,
            getCardWidth = {
                width = it
            }
        )
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE) {
            ODSBottomImageContainer(
                style = style,
                props = props,
                cardWidth = cardWidth,
                scale = scale
            )
        }
    }
}

@Suppress("MagicNumber", "LongMethod")
@Composable
private fun ODSTopImageContainer(
    style: ODSCardWidgetStyle,
    props: ODSCardWidgetProps,
    cardWidth: Dp,
    scale: Float,
) {
    ODSBox(contentAlignment = style.topImageContainerZStackContentAlignment) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = style.topImageContainerVerticalAlignment,
            horizontalAlignment = style.topImageContainerHorizontalAlignment,
            verticalArrangement = style.topImageContainerVerticalArrangement
        ) {}
        ODSBox {
            ODSColumn(
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0.5f, 1f)
                },
                cornerRadius = style.imageTopCornerRadius,
                verticalAlignment = style.imageTopVerticalAlignment,
                horizontalAlignment = style.imageTopHorizontalAlignment,
                verticalArrangement = style.imageTopVerticalArrangement,
                clipContent = true,
                width = cardWidth,
                height = style.imageTopHeight
            ) {
                val imageHorizontalOverflow = style.imageVerticalOffset?.times(2)
                val imageWidth = cardWidth.plus(imageHorizontalOverflow ?: 0.dp)
                val height = style.imageTopHeight?.plus(
                    style.imageVerticalOffset ?: 0.dp
                )
                ODSImage(
                    modifier = Modifier
                        .wrapContentSize(unbounded = true)
                        .graphicsLayer {
                            scaleX = 1 / scale
                            scaleY = 1 / scale
                            transformOrigin = TransformOrigin(0.5f, 1f)
                        },
                    width = imageWidth,
                    height = height,
                    imageModel = props.image,
                    contentScale = style.imageContentScale ?: ContentScale.Fit,
                )
            }
            if (props.showLogo) {
                ODSColumn(
                    modifier = Modifier
                        .ifNotNull(style.logoContainerAbsoluteContentAlignment) { param ->
                            align(param)
                        }
                        .offset(offset = style.logoContainerAbsoluteOffset),
                    cornerRadius = style.logoContainerCornerRadius,
                    verticalAlignment = style.logoContainerVerticalAlignment,
                    horizontalAlignment = style.logoContainerHorizontalAlignment,
                    verticalArrangement = style.logoContainerVerticalArrangement,
                    width = style.logoContainerWidth,
                    height = style.logoContainerHeight,
                    clipContent = true
                ) {
                    ODSImage(
                        imageModel = props.logo,
                        width = style.logoContainerWidth,
                        height = style.logoContainerHeight
                    )
                }
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun ODSContentContainer(
    scale: Float,
    style: ODSCardWidgetStyle,
    props: ODSCardWidgetProps,
    contentSlot: (@Composable () -> Unit)?,
    getCardWidth: (width: Int) -> Unit,
) {
    ODSBox(
        contentAlignment = style.contentContainerZStackContentAlignment,
    ) {
        ODSBox(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = when (props.type) {
                        ODSCardWidgetType.BOTTOM_IMAGE -> {
                            TransformOrigin(0.5f, 1f)
                        }

                        ODSCardWidgetType.TOP_IMAGE -> {
                            TransformOrigin(0.5f, 0f)
                        }

                        ODSCardWidgetType.NO_IMAGE -> {
                            TransformOrigin(0.5f, 0.5f)
                        }
                    }
                }
                .onGloballyPositioned {
                    getCardWidth(it.size.width)
                },
            cornerRadius = style.backgroundCornerRadius,
            background = style.backgroundBackground
        ) {}
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = style.contentContainerGap,
            padding = style.contentContainerPadding,
            verticalAlignment = style.contentContainerVerticalAlignment,
            horizontalAlignment = style.contentContainerHorizontalAlignment,
            verticalArrangement = style.contentContainerVerticalArrangement,
            minHeight = style.contentContainerMinHeight
        ) {
            contentSlot?.invoke()
        }
    }
}

@Suppress("MagicNumber", "LongMethod")
@Composable
private fun ODSBottomImageContainer(
    style: ODSCardWidgetStyle,
    props: ODSCardWidgetProps,
    cardWidth: Dp,
    scale: Float,
) {
    ODSBox(contentAlignment = style.bottomImageContainerZStackContentAlignment) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = style.bottomImageContainerVerticalAlignment,
            horizontalAlignment = style.bottomImageContainerHorizontalAlignment,
            verticalArrangement = style.bottomImageContainerVerticalArrangement
        ) {}
        ODSBox {
            ODSColumn(
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0.5f, 0f)
                },
                cornerRadius = style.imageBottomCornerRadius,
                verticalAlignment = style.imageBottomVerticalAlignment,
                horizontalAlignment = style.imageBottomHorizontalAlignment,
                verticalArrangement = style.imageBottomVerticalArrangement,
                width = cardWidth,
                height = style.imageBottomHeight,
                clipContent = true
            ) {
                val imageHorizontalOverflow = style.imageVerticalOffset?.times(2)
                val imageWidth = cardWidth.plus(imageHorizontalOverflow ?: 0.dp)
                val height = style.imageBottomHeight?.plus(
                    style.imageVerticalOffset ?: 0.dp
                )
                ODSImage(
                    modifier = Modifier
                        .wrapContentSize(unbounded = true)
                        .graphicsLayer {
                            scaleX = 1 / scale
                            scaleY = 1 / scale
                            transformOrigin = TransformOrigin(0.5f, 0f)
                        },
                    width = imageWidth,
                    height = height,
                    imageModel = props.image,
                    contentScale = style.image2ContentScale ?: ContentScale.Fit,
                )
            }
            if (props.showLogo) {
                ODSColumn(
                    modifier = Modifier
                        .ifNotNull(style.logoContainer2AbsoluteContentAlignment) { param ->
                            align(param)
                        }
                        .offset(style.logoContainer2AbsoluteOffset),
                    cornerRadius = style.logoContainer2CornerRadius,
                    verticalAlignment = style.logoContainer2VerticalAlignment,
                    horizontalAlignment = style.logoContainer2HorizontalAlignment,
                    verticalArrangement = style.logoContainer2VerticalArrangement,
                    width = style.logoContainer2Width,
                    height = style.logoContainer2Height,
                    clipContent = true
                ) {
                    ODSImage(
                        imageModel = props.logo,
                        width = style.logoContainer2Width,
                        height = style.logoContainer2Height
                    )
                }
            }
        }
    }
}

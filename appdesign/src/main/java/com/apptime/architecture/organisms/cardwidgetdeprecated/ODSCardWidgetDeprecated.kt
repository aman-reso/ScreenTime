package com.telekom.odsystem.organisms.cardwidgetdeprecated

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardWidgetDeprecated composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 * @param slot Parameter for customization.
 */
@Composable
fun ODSCardWidgetDeprecated(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardWidgetDeprecatedProps = ODSCardWidgetDeprecatedProps(),
    onClick: () -> Unit = {},
    slot: @Composable (() -> Unit)? = null,
) {
    val style =
        ODSCardWidgetDeprecatedStyle().getStyle(
            scheme = scheme,
            props = props,
            state = ODSActions.DEFAULT
        )

    ODSCardWidgetContainer(
        modifier = modifier,
        style = style,
        props = props,
        slot = slot,
        onClick = onClick
    )
}

@Suppress("LongMethod")
@Composable
private fun ODSCardWidgetContainer(
    modifier: Modifier,
    style: ODSCardWidgetDeprecatedStyle,
    props: ODSCardWidgetDeprecatedProps,
    slot: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var pressed by remember { mutableStateOf(false) }
    var width by remember { mutableIntStateOf(0) }

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
        modifier = modifier
            .semantics { isTraversalGroup = true }
            .aspectRatio(1f)
            .sizeWithinBounds(minWidth = style.minWidth ?: Dp.Unspecified)
            .customClickable(
                isPressed = { pressed = it },
                interactionSource = interactionSource,
                onClick = onClick,
                role = Role.Button
            ),
    ) {
        ODSColumn(
            modifier = Modifier
                .semantics(mergeDescendants = true) {
                    traversalIndex = 1f
                }
                .matchParentSize()
                .scale(scale)
                .onGloballyPositioned {
                    width = it.size.width
                },
            clipContent = style.backgroundClipContent ?: true,
            cornerRadius = style.backgroundBorderRadius,
            verticalArrangement = style.backgroundVerticalArrangement,
            verticalAlignment = style.backgroundVerticalAlignment,
            horizontalAlignment = style.backgroundHorizontalAlignment,
            background = style.backgroundBackgroundColor,
        ) {
            if (props.type == ODSCardWidgetDeprecatedType.IMAGE) {
                val density = LocalDensity.current
                val cardWidth = with(density) { width.toDp() }
                ODSCardWidgetImageContainer(
                    modifier = Modifier
                        .scale(1 / scale) // Added so that image is not scaled on hover
                        .fillMaxSize(),
                    style = style,
                    cardWidth = cardWidth,
                    props = props
                )
            }
        }
        ODSRow(
            modifier = Modifier.semantics(mergeDescendants = true) {
                traversalIndex = 0f
            },
            cornerRadius = style.borderRadius,
            horizontalArrangement = style.horizontalArrangement,
            horizontalAlignment = style.horizontalAlignment,
            verticalAlignment = style.verticalAlignment,
        ) {
            ODSCardWidgetContentContainer(
                style = style,
                props = props,
                slot = slot,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun ODSCardWidgetContentContainer(
    style: ODSCardWidgetDeprecatedStyle,
    props: ODSCardWidgetDeprecatedProps,
    onClick: () -> Unit,
    slot: @Composable (() -> Unit)? = null,
) {
    ODSColumn(
        gap = style.contentContainerGap,
        padding = style.contentContainerPadding,
        verticalArrangement = style.contentContainerVerticalArrangement,
        verticalAlignment = style.contentContainerVerticalAlignment,
        horizontalAlignment = style.contentContainerHorizontalAlignment,
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {
                    role = Role.Button
                    onClick { onClick(); true }
                },
            verticalArrangement = style.titleAndSubtitleVerticalArrangement,
            verticalAlignment = style.titleAndSubtitleVerticalAlignment,
            horizontalAlignment = style.titleAndSubtitleHorizontalAlignment,
        ) {
            if (!props.title.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = props.title,
                    style = style.titleTextStyle,
                    color = style.titleColor,
                    textAlign = style.titleTextAlign
                )
            }
            if (!props.subtitle.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = props.subtitle,
                    style = style.subtitleTextStyle,
                    color = style.subtitleColor,
                    textAlign = style.subtitleTextAlign
                )
            }
        }
        if (props.type == ODSCardWidgetDeprecatedType.SLOT) {
            slot?.invoke()
        }
    }
}

@Composable
private fun ODSCardWidgetImageContainer(
    modifier: Modifier,
    style: ODSCardWidgetDeprecatedStyle,
    props: ODSCardWidgetDeprecatedProps,
    cardWidth: Dp,
) {
    val imageHorizontalOverflow = style.imageVerticalOffset?.times(2)
    val imageWidth = cardWidth.plus(imageHorizontalOverflow ?: 0.dp)
    ODSBox(
        modifier = modifier,
        contentAlignment = style.imageContainerAlignment
    ) {
        ODSColumn(
            modifier = Modifier
                .width(cardWidth)
                .wrapContentSize(unbounded = true)
                .aspectRatio(props.imageAspectRatio.value),
            cornerRadius = style.imageContainerBorderRadius,
            horizontalAlignment = style.imageContainerHorizontalAlignment,
            verticalAlignment = style.imageContainerVerticalAlignment,
            verticalArrangement = style.imageContainerVerticalArrangement,
        ) {
            val height = (cardWidth.div(props.imageAspectRatio.value)).plus(
                style.imageVerticalOffset ?: 0.dp
            )
            ODSImage(
                modifier = Modifier
                    .width(imageWidth)
                    .height(height)
                    .offset(y = style.imageVerticalOffset ?: 8.dp),
                imageModel = props.image,
                contentScale = ContentScale.Crop,
            )
        }
        props.logo?.let {
            ODSCardWidgetLogo(it, style)
        }
    }
}

@Composable
private fun ODSCardWidgetLogo(
    logo: ODSImageModel,
    style: ODSCardWidgetDeprecatedStyle,
) {
    ODSBox(
        padding = style.logoPadding,
    ) {
        ODSBox(
            cornerRadius = ODSCorners(DSVariables.radiusFull),
            clipContent = true
        ) {
            ODSImage(
                modifier = Modifier
                    .size(style.logoSize ?: DSVariables.sizingComponent14),
                imageModel = logo,
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}

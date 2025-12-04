package com.telekom.odsystem.organisms.cardcheckmarkimage

import android.content.Context
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.invokeWith
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-11 (v1.33.1) - uid: 5ac57ab5
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8756-24496
 */

/**
 * ODS Card Checkmark component.
 *
 * @param modifier Modifier to be applied to the component.
 * @param scheme The ODSTheme to be applied, defaulting to neutralScheme.
 * @param props The ODSCardCheckmarkProps to configure the component, defaulting to ODSCardCheckmarkProps().
 * @param contentSlot An optional composable lambda for custom content within the card.
 * @param onCheckedChange An optional lambda to be executed when the card is clicked.
 */
@Composable
fun ODSCardCheckmarkImage(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardCheckmarkImageProps = ODSCardCheckmarkImageProps(),
    contentSlot: (@Composable () -> Unit)? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val style = ODSCardCheckmarkImageStyle().getStyle(scheme = scheme, props = props)

    ODSCardCheckmarkImageContainer(
        modifier = modifier,
        style = style,
        props = props,
        contentSlot = contentSlot,
        interactionSource = interactionSource,
        isPressed = { isPressed = it },
        pressed = isPressed,
        isHovered = isHovered,
        onCheckedChange = onCheckedChange
    )
}

@Suppress("LongMethod")
@Composable
private fun ODSCardCheckmarkImageContainer(
    modifier: Modifier = Modifier,
    props: ODSCardCheckmarkImageProps,
    style: ODSCardCheckmarkImageStyle,
    interactionSource: MutableInteractionSource,
    isPressed: (Boolean) -> Unit,
    isHovered: Boolean,
    pressed: Boolean,
    contentSlot: (@Composable () -> Unit)?,
    onCheckedChange: ((Boolean) -> Unit)?
) {
    val context = LocalContext.current
    var width by remember { mutableIntStateOf(0) }
    var height by remember { mutableIntStateOf(0) }

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
        modifier = modifier
            .applySemantics(
                props = props,
                context = context
            )
            .height(IntrinsicSize.Min)
            .customClickable(
                disabled = props.disabled,
                readOnly = props.readOnly,
                interactionSource = interactionSource,
                isPressed = isPressed,
                onClick = onCheckedChange?.invokeWith { !props.selected }
            ),
        contentAlignment = style.zStackContentAlignment
    ) {
        ODSCardCheckmarkImageContent(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    width = it.size.width
                    height = it.size.height
                },
            props = props,
            style = style,
            contentSlot = contentSlot,
            scale = scaleX to scaleY
        )
        ODSColumn(
            modifier = Modifier
                .matchParentSize()
                .scale(scaleX = scaleX, scaleY = scaleY),
            cornerRadius = style.cardStrokeCornerRadius,
            border = ODSBorder(
                width = style.cardStrokeBorder,
                colorList = style.cardStrokeBorderColor
            ),
            verticalAlignment = style.cardStrokeVerticalAlignment,
            horizontalAlignment = style.cardStrokeHorizontalAlignment,
            verticalArrangement = style.cardStrokeVerticalArrangement
        ) {
        }
    }
}

@Composable
private fun ODSCardCheckmarkImageContent(
    modifier: Modifier,
    props: ODSCardCheckmarkImageProps,
    style: ODSCardCheckmarkImageStyle,
    contentSlot: (@Composable () -> Unit)?,
    scale: Pair<Float, Float>
) {
    ODSColumn(
        modifier = modifier,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement,
    ) {
        ODSImageContainer(
            props = props,
            style = style,
            scale = scale
        )
        ODSContainer(
            props = props,
            style = style,
            contentSlot = contentSlot,
            scale = scale
        )
    }
}

@Composable
private fun ODSImageContainer(
    props: ODSCardCheckmarkImageProps,
    style: ODSCardCheckmarkImageStyle,
    scale: Pair<Float, Float>
) {
    ODSBox(
        contentAlignment = style.imageAspectRatioZStackContentAlignment
    ) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scaleX = scale.first, scaleY = scale.second),
            clipContent = style.imageContainerZStackClipContent != false
        ) {
            ODSColumn(
                modifier = Modifier.matchParentSize(),
                cornerRadius = style.imageContainerCornerRadius,
                verticalAlignment = style.imageContainerVerticalAlignment,
                horizontalAlignment = style.imageContainerHorizontalAlignment,
                verticalArrangement = style.imageContainerVerticalArrangement
            ) {}
            ODSImage(
                modifier = Modifier.fillMaxWidth(),
                imageModel = props.image,
                aspectRatio = props.imageAspectRatio,
                cornerRadius = style.imageContainerCornerRadius,
                contentScale = style.imageContentScale ?: ContentScale.Fit
            )
        }
    }
}

@Composable
private fun ODSContainer(
    props: ODSCardCheckmarkImageProps,
    style: ODSCardCheckmarkImageStyle,
    contentSlot: (@Composable () -> Unit)?,
    scale: Pair<Float, Float>
) {
    ODSBox(
        modifier = Modifier.height(IntrinsicSize.Min),
        contentAlignment = style.containerZStackContentAlignment
    ) {
        ODSColumn(
            modifier = Modifier
                .matchParentSize()
                .scale(scaleX = scale.first, scaleY = scale.second),
            cornerRadius = style.cardBgCornerRadius,
            verticalAlignment = style.cardBgVerticalAlignment,
            horizontalAlignment = style.cardBgHorizontalAlignment,
            verticalArrangement = style.cardBgVerticalArrangement,
            background = style.cardBgBackground
        ) {
        }
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = style.containerGap,
            padding = style.containerPadding,
            verticalAlignment = style.containerVerticalAlignment,
            horizontalAlignment = style.containerHorizontalAlignment,
            verticalArrangement = style.containerVerticalArrangement
        ) {
            ODSContentContainer(
                props = props,
                style = style,
                contentSlot = contentSlot
            )
        }
    }
}

@Composable
private fun ODSContentContainer(
    props: ODSCardCheckmarkImageProps,
    style: ODSCardCheckmarkImageStyle,
    contentSlot: (@Composable () -> Unit)?
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        gap = style.contentGap,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalArrangement = style.contentHorizontalArrangement
    ) {
        ODSColumn(
            modifier = Modifier.weight(1f),
            verticalAlignment = style.contentContainerVerticalAlignment,
            horizontalAlignment = style.contentContainerHorizontalAlignment,
            verticalArrangement = style.contentContainerVerticalArrangement
        ) {
            contentSlot?.invoke()
        }
        ODSRow(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = style.selectorContainerRightHorizontalAlignment,
            verticalAlignment = style.selectorContainerRightVerticalAlignment,
            horizontalArrangement = style.selectorContainerRightHorizontalArrangement,
            width = style.selectorContainerRightWidth
        ) {
            if (!props.disabled && props.selected) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.checkmark_type_standard_size_standard),
                    tint = style.checkmarkRightColor?.getColor(),
                    width = style.checkmarkRightWidth,
                    height = style.checkmarkRightHeight
                )
            }
        }
    }
}

private fun Modifier.applySemantics(
    props: ODSCardCheckmarkImageProps,
    context: Context
): Modifier {
    val isReadOnly = props.readOnly
    var contentDescription = ""
    if (isReadOnly) {
        contentDescription += "${context.getString(R.string.semantic_read_only)}\n"
    }
    return this.semantics {
        this.toggleableState = ToggleableState(props.selected)
        this.contentDescription = contentDescription
    }
}

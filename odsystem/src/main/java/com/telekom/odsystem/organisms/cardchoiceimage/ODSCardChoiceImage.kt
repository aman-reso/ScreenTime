package com.telekom.odsystem.organisms.cardchoiceimage

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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.checkboxicon.ODSCheckboxIcon
import com.telekom.odsystem.atoms.radioicon.ODSRadioIcon
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIcon
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-11 (v1.33.1) - uid: 4794709a
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=86-10038
 */

/**
 * ODS Card Choice Image component.
 *
 * @param modifier Optional [Modifier] for this component.
 * @param scheme The [ODSTheme] to be used for styling the component. Defaults to [neutralScheme].
 * @param props The [ODSCardChoiceImageProps] to configure the component. Defaults to [ODSCardChoiceImageProps].
 * @param contentSlot An optional composable slot for adding custom content within the card, typically text or other descriptive elements.
 * @param onClick An optional lambda function to be executed when the card is clicked.
 */
@Composable
fun ODSCardChoiceImage(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardChoiceImageProps = ODSCardChoiceImageProps(),
    contentSlot: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val state = when {
        isPressed && !props.disabled && !props.readOnly -> ODSActions.PRESSED
        isHovered && !props.disabled && !props.readOnly -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSCardChoiceImageStyle().getStyle(scheme = scheme, props = props)

    ODSCardChoiceImageContainer(
        modifier = modifier,
        scheme = scheme,
        props = props,
        style = style,
        contentSlot = contentSlot,
        state = state,
        isPressed = { isPressed = it },
        pressed = isPressed,
        isHovered = isHovered,
        interactionSource = interactionSource,
        onClick = onClick
    )
}

@Suppress("LongMethod")
@Composable
private fun ODSCardChoiceImageContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardChoiceImageProps,
    style: ODSCardChoiceImageStyle,
    contentSlot: (@Composable () -> Unit)?,
    state: ODSActions,
    isPressed: (Boolean) -> Unit,
    isHovered: Boolean,
    pressed: Boolean,
    interactionSource: MutableInteractionSource,
    onClick: (() -> Unit)? = null
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
                onClick = onClick,
                role = getRole(props = props)
            ),
        contentAlignment = style.zStackContentAlignment
    ) {
        ODSCardChoiceImageContent(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    width = it.size.width
                    height = it.size.height
                },
            scheme = scheme,
            props = props,
            style = style,
            contentSlot = contentSlot,
            state = state,
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
private fun ODSCardChoiceImageContent(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSCardChoiceImageProps,
    style: ODSCardChoiceImageStyle,
    contentSlot: (@Composable () -> Unit)?,
    state: ODSActions,
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
            scheme = scheme,
            style = style,
            props = props,
            contentSlot = contentSlot,
            state = state,
            scale = scale
        )
    }
}

@Composable
private fun ODSImageContainer(
    props: ODSCardChoiceImageProps,
    style: ODSCardChoiceImageStyle,
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
    scheme: ODSTheme,
    style: ODSCardChoiceImageStyle,
    props: ODSCardChoiceImageProps,
    contentSlot: (@Composable () -> Unit)?,
    state: ODSActions,
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
                scheme = scheme,
                style = style,
                props = props,
                contentSlot = contentSlot,
                state = state
            )
        }
    }
}

@Composable
private fun ODSContentContainer(
    scheme: ODSTheme,
    style: ODSCardChoiceImageStyle,
    props: ODSCardChoiceImageProps,
    contentSlot: (@Composable () -> Unit)?,
    state: ODSActions
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
        ODSSelectorContainer(
            scheme = scheme,
            style = style,
            props = props,
            state = state
        )
    }
}

@Composable
private fun ODSSelectorContainer(
    scheme: ODSTheme,
    style: ODSCardChoiceImageStyle,
    props: ODSCardChoiceImageProps,
    state: ODSActions
) {
    ODSRow(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = style.selectorContainerRightHorizontalAlignment,
        verticalAlignment = style.selectorContainerRightVerticalAlignment,
        horizontalArrangement = style.selectorContainerRightHorizontalArrangement
    ) {
        when (props.type) {
            ODSCardChoiceImageType.RADIO_CHOICE -> {
                ODSRadioIcon(
                    scheme = scheme,
                    props = props.radioIconProps.toODSRadioIconProps(
                        disabled = props.disabled,
                        selected = props.selected,
                        state = state
                    )
                )
            }

            ODSCardChoiceImageType.CHECKBOX_CHOICE -> {
                ODSCheckboxIcon(
                    scheme = scheme,
                    props = props.checkboxIconProps.toODSCheckboxIconProps(
                        disabled = props.disabled,
                        selected = props.selected,
                        state = state
                    )
                )
            }

            ODSCardChoiceImageType.SWITCH_CHOICE -> {
                ODSSwitchIcon(
                    scheme = scheme,
                    props = props.switchIconProps.toODSSwitchIconProps(
                        disabled = props.disabled,
                        selected = props.selected,
                        state = state
                    )
                )
            }
        }
    }
}

private fun getRole(props: ODSCardChoiceImageProps): Role {
    return when (props.type) {
        ODSCardChoiceImageType.CHECKBOX_CHOICE -> Role.Checkbox
        ODSCardChoiceImageType.RADIO_CHOICE -> Role.RadioButton
        ODSCardChoiceImageType.SWITCH_CHOICE -> Role.Switch
    }
}

private fun Modifier.applySemantics(
    props: ODSCardChoiceImageProps,
    context: Context
): Modifier {
    val isReadOnly = props.readOnly
    var contentDescription = ""
    if (isReadOnly) {
        contentDescription += "${context.getString(R.string.semantic_read_only)}\n"
    }
    return this.semantics {
        when (props.type) {
            ODSCardChoiceImageType.CHECKBOX_CHOICE -> this.toggleableState =
                ToggleableState(props.selected)

            ODSCardChoiceImageType.RADIO_CHOICE -> this.selected = props.selected
            ODSCardChoiceImageType.SWITCH_CHOICE -> this.toggleableState =
                ToggleableState(props.selected)
        }
        this.contentDescription = contentDescription
    }
}

package com.telekom.odsystem.organisms.cardcheckmark

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.invokeWith
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.scaleAnimationSpec
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-09 (v1.33.1) - uid: 5ac49cde
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8756-22767
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
fun ODSCardCheckmark(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardCheckmarkProps = ODSCardCheckmarkProps(),
    contentSlot: (@Composable () -> Unit)? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSCardCheckmarkStyle().getStyle(scheme = scheme, props = props)

    ODSCardCheckmarkContainer(
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

@Composable
private fun ODSCardCheckmarkContainer(
    modifier: Modifier,
    style: ODSCardCheckmarkStyle,
    props: ODSCardCheckmarkProps,
    contentSlot: (@Composable () -> Unit)?,
    interactionSource: MutableInteractionSource,
    isPressed: (Boolean) -> Unit,
    isHovered: Boolean,
    pressed: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    val context = LocalContext.current
    val scale by animateFloatAsState(
        targetValue = if (isHovered && !pressed) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = scaleAnimationSpec,
        label = ""
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
                role = Role.Button,
                onClick = onCheckedChange?.invokeWith { !props.selected }
            ),
        contentAlignment = style.zStackContentAlignment
    ) {
        ODSColumn(
            modifier = Modifier
                .matchParentSize()
                .scale(scale),
            cornerRadius = style.cardBgCornerRadius,
            border = ODSBorder(width = style.cardBgBorder, colorList = style.cardBgBorderColor),
            verticalAlignment = style.cardBgVerticalAlignment,
            horizontalAlignment = style.cardBgHorizontalAlignment,
            verticalArrangement = style.cardBgVerticalArrangement,
            background = style.cardBgBackground
        ) {
        }
        ODSColumn(
            modifier = Modifier.fillMaxHeight(),
            gap = style.gap,
            padding = style.padding,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
            verticalArrangement = style.verticalArrangement,
        ) {
            ODSContentContainer(
                style = style,
                props = props,
                contentSlot = contentSlot
            )
        }
    }
}

@Composable
private fun ODSContentContainer(
    style: ODSCardCheckmarkStyle,
    props: ODSCardCheckmarkProps,
    contentSlot: (@Composable () -> Unit)?
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        gap = style.contentGap,
        padding = style.contentPadding,
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
    props: ODSCardCheckmarkProps,
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

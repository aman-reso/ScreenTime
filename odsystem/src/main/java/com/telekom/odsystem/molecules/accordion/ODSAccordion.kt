package com.telekom.odsystem.molecules.accordion

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.invokeWith
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.MIN_WIDTH
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSAccordion composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback for click event. Parameter is expanded state.
 * @param contentSlot Parameter for customization.
 */
@Composable
fun ODSAccordion(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSAccordionProps = ODSAccordionProps(),
    contentSlot: @Composable () -> Unit,
    onClick: ((Boolean) -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val state = when {
        isPressed && !props.disabled -> ODSActions.PRESSED
        isHovered && !props.disabled -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSAccordionStyle().getStyle(
        scheme = scheme,
        props = props,
        state = state
    )
    val rotationState by animateFloatAsState(
        targetValue = if (props.expanded) 180f else 0f, label = ""
    )

    ODSAccordionContainer(
        modifier = modifier,
        style = style,
        props = props,
        rotationState = rotationState,
        onClick = onClick?.invokeWith { !props.expanded },
        isPressed = { isPressed = it },
        interactionSource = interactionSource,
        contentSlot = contentSlot
    )
}

@Composable
private fun ODSAccordionContainer(
    modifier: Modifier,
    style: ODSAccordionStyle,
    props: ODSAccordionProps,
    rotationState: Float,
    onClick: (() -> Unit)? = null,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    contentSlot: @Composable () -> Unit
) {
    ODSColumn(
        gap = style.gap,
        modifier = modifier
            .sizeWithinBounds(
                minHeight = MIN_HEIGHT.dp,
                minWidth = MIN_WIDTH.dp
            )
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing
                )
            ),
        verticalArrangement = style.verticalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment
    ) {
        ODSHeaderAndIconContainer(
            interactionSource = interactionSource,
            style = style,
            props = props,
            rotationState = rotationState,
            isPressed = isPressed,
            onClick = onClick
        )
        if (props.expanded) {
            ODSColumn(
                verticalArrangement = style.contentFrameVerticalArrangement,
                verticalAlignment = style.contentFrameVerticalAlignment,
                horizontalAlignment = style.contentFrameHorizontalAlignment
            ) {
                contentSlot()
            }
        }
    }
}

@Composable
private fun ODSHeaderAndIconContainer(
    style: ODSAccordionStyle,
    props: ODSAccordionProps,
    rotationState: Float,
    isPressed: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource,
    onClick: (() -> Unit)? = null
) {
    ODSCardBasic(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = ODSPadding(DSVariables.spacingLayout0),
        contentSlot = {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeWithinBounds(
                        minHeight = style.headerIconMinHeight ?: MIN_HEIGHT.dp,
                        minWidth = MIN_WIDTH.dp
                    )
                    .applySemantics(props = props, context = LocalContext.current)
                    .customClickable(
                        interactionSource = interactionSource,
                        isPressed = { isPressed(it) },
                        onClick = onClick,
                        role = Role.Button,
                        disabled = props.disabled
                    ),
                gap = style.headerIconGap,
                cornerRadius = style.headerIconCornerRadius,
                padding = style.headerIconPadding,
                horizontalArrangement = style.headerIconHorizontalArrangement,
                horizontalAlignment = style.headerIconHorizontalAlignment,
                verticalAlignment = style.headerIconVerticalAlignment,
                background = style.headerIconBackground
            ) {
                ODSText(
                    modifier = Modifier.weight(1f),
                    text = props.headerText,
                    style = style.headerStyle,
                    color = style.headerColor,
                    textAlign = style.headerTextAlign
                )
                ODSExpandAndCollapseIconContainer(
                    style = style,
                    rotationState = rotationState,
                    props = props
                )
            }
        })
}

@Composable
private fun ODSExpandAndCollapseIconContainer(
    style: ODSAccordionStyle,
    rotationState: Float,
    props: ODSAccordionProps
) {
    ODSRow(
        horizontalAlignment = style.expandAndCollapseIconHorizontalAlignment,
        verticalAlignment = style.expandAndCollapseIconVerticalAlignment,
        horizontalArrangement = style.expandAndCollapseIconHorizontalArrangement,
        clipContent = style.expandAndCollapseIconClipContent ?: true,
        width = style.expandAndCollapseIconWidth,
        height = style.expandAndCollapseIconHeight
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = if (props.size == ODSAccordionSize.LARGE) {
                    R.drawable.collapse_down_type_standard_size_standard
                } else {
                    R.drawable.collapse_down_type_standard_size_small
                },
            ),
            width = if (!props.expanded) style.collapseDownWidth else style.collapseUpWidth,
            height = if (!props.expanded) style.collapseDownHeight else style.collapseUpHeight,
            tint = if (!props.expanded) style.collapseDownColor?.getColor() else style.collapseUpColor?.getColor(),
            modifier = Modifier
                .rotate(rotationState)
        )
    }
}

private fun Modifier.applySemantics(
    props: ODSAccordionProps,
    context: Context
): Modifier {
    return this.semantics(mergeDescendants = true) {
        stateDescription = if (props.expanded) {
            context.getString(R.string.semantic_expanded_state)
        } else {
            context.getString(R.string.semantic_collapsed_state)
        }
    }
}

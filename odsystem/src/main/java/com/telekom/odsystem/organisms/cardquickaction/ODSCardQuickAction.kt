package com.telekom.odsystem.organisms.cardquickaction

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.semantics.Role
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardQuickAction is a clickable card component that displays content and an arrow icon,
 * indicating a navigational action. It is commonly used for quick access to other sections
 * or functionalities within an application.
 *
 * @param modifier Modifier to be applied to the component.
 * @param scheme Color scheme used for theming. Defaults to `neutralScheme`.
 * @param props Visual configuration for the component, such as disabled state.
 * @param onClick Callback triggered when the card is clicked.
 * @param contentSlot A composable slot for the main content to be displayed within the card.
 */
@Suppress("LongMethod")
@Composable
fun ODSCardQuickAction(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardQuickActionProps = ODSCardQuickActionProps(),
    onClick: () -> Unit,
    contentSlot: (@Composable () -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val style = ODSCardQuickActionStyle().getStyle(scheme = scheme, props = props)

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
            .height(IntrinsicSize.Min)
            .customClickable(
                disabled = props.disabled,
                interactionSource = interactionSource,
                isPressed = {
                    isPressed = it
                },
                onClick = onClick,
                role = Role.Button
            ),
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement,
    ) {
        ODSBox(
            contentAlignment = style.containerZStackContentAlignment
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
                modifier = Modifier.fillMaxWidth(),
                gap = style.containerGap,
                padding = style.containerPadding,
                verticalAlignment = style.containerVerticalAlignment,
                horizontalAlignment = style.containerHorizontalAlignment,
                verticalArrangement = style.containerVerticalArrangement
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = style.contentGap,
                    horizontalAlignment = style.contentHorizontalAlignment,
                    verticalAlignment = style.contentVerticalAlignment,
                    horizontalArrangement = style.contentHorizontalArrangement
                ) {
                    ODSColumn(
                        modifier = Modifier
                            .weight(1f),
                        verticalAlignment = style.contentContainerVerticalAlignment,
                        horizontalAlignment = style.contentContainerHorizontalAlignment,
                        verticalArrangement = style.contentContainerVerticalArrangement
                    ) {
                        contentSlot?.invoke()
                    }
                    ODSRow(
                        modifier = Modifier.fillMaxHeight(),
                        padding = style.selectorContainerRightPadding,
                        horizontalAlignment = style.selectorContainerRightHorizontalAlignment,
                        verticalAlignment = style.selectorContainerRightVerticalAlignment,
                        horizontalArrangement = style.selectorContainerRightHorizontalArrangement
                    ) {
                        if (props.iconModel != null) {
                            ODSIcon(
                                iconModel = props.iconModel,
                                tint = style.arrowRightColor?.getColor(),
                                width = style.arrowRightWidth,
                                height = style.arrowRightHeight
                            )
                        } else {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = R.drawable.arrow_right_type_bold_size_standard),
                                tint = style.arrowRightColor?.getColor(),
                                width = style.arrowRightWidth,
                                height = style.arrowRightHeight
                            )
                        }
                    }
                }
            }
        }
    }
}

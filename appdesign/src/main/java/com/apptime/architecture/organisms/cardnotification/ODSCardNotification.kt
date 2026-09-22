package com.telekom.odsystem.organisms.cardnotification

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardNotification composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 * @param onDismiss Callback triggered when action occurs.
 * @param actionSlot Parameter for customization.
 */
@Composable
fun ODSCardNotification(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardNotificationProps = ODSCardNotificationProps(),
    onClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
    imageSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable (() -> Unit)? = null,
) {

    val style = ODSCardNotificationStyle().getStyle(
        scheme = scheme,
        showImage = imageSlot != null
    )

    ODSCardNotificationContainer(
        modifier = modifier,
        props = props,
        style = style,
        scheme = scheme,
        onClick = onClick,
        onDismiss = onDismiss,
        actionSlot = actionSlot,
        imageSlot = imageSlot
    )
}

@Suppress("LongMethod")
@Composable
private fun ODSCardNotificationContainer(
    modifier: Modifier,
    props: ODSCardNotificationProps,
    style: ODSCardNotificationStyle,
    scheme: ODSTheme,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    imageSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable (() -> Unit)? = null,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isHovered && !pressed) {
            style.scaleFactor ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )

    ODSBox(
        modifier = modifier
            .customClickable(
                isPressed = { pressed = it },
                interactionSource = interactionSource,
                onClick = onClick,
                role = Role.Button
            )
            .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified)
            .fillMaxWidth(),
        contentAlignment = style.contentAlignment,
    ) {
        ODSBox(
            clipContent = style.cardBgClipContent != false,
            modifier = Modifier
                .matchParentSize()
                .scale(scale),
        ) {
            ODSColumn(
                cornerRadius = style.cardBgBorderRadius,
                clipContent = style.cardBgClipContent != false,
                verticalArrangement = style.cardBgVerticalArrangement,
                verticalAlignment = style.cardBgVerticalAlignment,
                horizontalAlignment = style.cardBgHorizontalAlignment,
                background = style.cardBgBackgroundColor,
                modifier = Modifier.matchParentSize()
            ) {}
            imageSlot?.let {
                ODSBox(
                    modifier = Modifier
                        .scale(1 / scale) // Added so that image is not scaled on hover
                        .fillMaxSize(), content = {
                        it()
                    })
            }
        }
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified),
            padding = style.padding,
            verticalArrangement = style.verticalArrangement,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment
        ) {
            ODSHeaderContainer(
                props = props,
                style = style,
                scheme = scheme,
                onClick = onClick,
                onDismiss = onDismiss
            )
            actionSlot?.let {
                ODSActionContainer(
                    style = style,
                    actionSlot = actionSlot
                )
            }
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun ODSHeaderContainer(
    props: ODSCardNotificationProps,
    style: ODSCardNotificationStyle,
    scheme: ODSTheme,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    ODSRow(
        gap = style.headerContainerGap,
        padding = style.headerContainerPadding,
        horizontalArrangement = style.headerContainerHorizontalArrangement,
        horizontalAlignment = style.headerContainerHorizontalAlignment,
        verticalAlignment = style.headerContainerVerticalAlignment,
        modifier = Modifier.sizeWithinBounds(
            minHeight = style.headerContainerMinHeight ?: Dp.Unspecified
        )
    ) {
        ODSColumn(
            modifier = Modifier
                .weight(1f)
                .semantics(mergeDescendants = true) {
                    this.role = Role.Button
                    onClick {
                        onClick()
                        true
                    }
                },
            gap = style.headerContentGap,
            padding = style.headerContentPadding,
            verticalArrangement = style.headerContentVerticalArrangement,
            verticalAlignment = style.headerContentVerticalAlignment,
            horizontalAlignment = style.headerContentHorizontalAlignment
        ) {
            if (!props.title.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = props.title,
                    style = style.headerTextStyle,
                    color = style.headerColor,
                    textAlign = style.headerTextAlign
                )
            }
            if (!props.text.isNullOrEmpty()) {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = props.text,
                    style = style.textTextStyle,
                    color = style.textColor,
                    textAlign = style.textTextAlign
                )
            }
        }
        if (props.showCloseButton) {
            ODSRow(
                padding = style.closeButtonContainerPadding,
                horizontalArrangement = style.closeButtonContainerHorizontalArrangement,
                horizontalAlignment = style.closeButtonContainerHorizontalAlignment,
                verticalAlignment = style.closeButtonContainerVerticalAlignment,
            ) {
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(
                            drawableRes = R.drawable.close_type_bold,
                            contentDescription = context.getString(R.string.semantics_close)
                        ),
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        size = ODSButtonSize.SMALL,
                        variant = ODSButtonVariant.GHOST
                    ),
                    onClick = onDismiss
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ODSActionContainer(
    style: ODSCardNotificationStyle,
    actionSlot: @Composable () -> Unit
) {
    ODSRow(
        gap = style.actionContainerGap,
        padding = style.actionContainerPadding,
        horizontalArrangement = style.actionContainerHorizontalArrangement,
        horizontalAlignment = style.actionContainerHorizontalAlignment,
        verticalAlignment = style.actionContainerVerticalAlignment
    ) {
        actionSlot()
    }
}

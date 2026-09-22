package com.telekom.odsystem.organisms.cardnotificationstack

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardnotification.ODSCardNotification
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardNotificationStack composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onViewAllClick Callback triggered when action occurs.
 * @param onClick Callback triggered when the card is clicked.
 * @param onDismiss Callback triggered when the card is dismissed.
 * @param imageSlot Optional slot for an image in the card.
 * @param actionSlot Optional slot for an action in the card.
 */
@Composable
fun ODSCardNotificationStack(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardNotificationStackProps = ODSCardNotificationStackProps(),
    onViewAllClick: () -> Unit = {},
    onClick: () -> Unit = { },
    onDismiss: () -> Unit = { },
    imageSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable (() -> Unit)? = null,
) {

    val style = ODSCardNotificationStackStyle().getStyle(scheme = scheme, props = props)

    ODSCardNotificationStackContainer(
        modifier = modifier,
        props = props,
        style = style,
        scheme = scheme,
        imageSlot = imageSlot,
        onViewAllClick = onViewAllClick,
        actionSlot = actionSlot,
        onClick = onClick,
        onDismiss = onDismiss
    )
}

@Composable
private fun ODSCardNotificationStackContainer(
    modifier: Modifier,
    props: ODSCardNotificationStackProps,
    style: ODSCardNotificationStackStyle,
    scheme: ODSTheme,
    imageSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onViewAllClick: () -> Unit,
) {
    ODSColumn(
        modifier = modifier.sizeWithinBounds(minWidth = style.minWidth ?: 0.dp),
        verticalArrangement = style.verticalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        ODSCardNotificationContainer(
            scheme = scheme,
            props = props,
            imageSlot = imageSlot,
            actionSlot = actionSlot,
            onClick = onClick,
            onDismiss = onDismiss
        )
        ODSCardContainers(style = style, props = props)
        if (props.show2ndCard) {
            ODSViewAll(
                scheme = neutralScheme,
                style = style,
                props = props,
                onViewAllClick = onViewAllClick
            )
        }
    }
}

@Composable
private fun ODSCardNotificationContainer(
    scheme: ODSTheme,
    props: ODSCardNotificationStackProps,
    imageSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    props.notificationCardProps?.let {
        ODSBox {
            ODSCardNotification(
                modifier = Modifier
                    .focusProperties {
                        canFocus = !props.show2ndCard
                    },
                scheme = scheme,
                props = it.copy(showCloseButton = it.showCloseButton && !props.show2ndCard),
                imageSlot = imageSlot,
                onClick = onClick,
                actionSlot = actionSlot,
                onDismiss = onDismiss
            )
            if (props.show2ndCard) {
                val context = LocalContext.current
                ODSBox(
                    modifier = Modifier
                        .clearAndSetSemantics {
                            contentDescription =
                                context.getString(R.string.semantic_notifications)
                        }
                        .matchParentSize()
                        .clickable(false) {}
                ) {
                }
            }
        }
    }
}

@Composable
private fun ODSCardContainers(
    style: ODSCardNotificationStackStyle,
    props: ODSCardNotificationStackProps
) {
    if (props.show2ndCard) {
        ODSColumn(
            verticalArrangement = style.cardContainersVerticalArrangement,
            verticalAlignment = style.cardContainersVerticalAlignment,
            horizontalAlignment = style.cardContainersHorizontalAlignment
        ) {
            ODSColumn(
                padding = style.cardHolder1Padding,
                verticalArrangement = style.cardHolder1VerticalArrangement,
                verticalAlignment = style.cardHolder1VerticalAlignment,
                horizontalAlignment = style.cardHolder1HorizontalAlignment
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    clipContent = style.cardBgClipContent != false,
                    cornerRadius = style.cardBgBorderRadius,
                    verticalArrangement = style.cardBgVerticalArrangement,
                    verticalAlignment = style.cardBgVerticalAlignment,
                    horizontalAlignment = style.cardBgHorizontalAlignment,
                    background = style.cardBgBackgroundColor,
                    height = style.cardBgHeight
                ) {
                }
            }
            if (props.show3rdCard) {
                ODSColumn(
                    padding = style.cardHolder2Padding,
                    verticalArrangement = style.cardHolder2VerticalArrangement,
                    verticalAlignment = style.cardHolder2VerticalAlignment,
                    horizontalAlignment = style.cardHolder2HorizontalAlignment
                ) {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        clipContent = style.cardBg2ClipContent != false,
                        cornerRadius = style.cardBg2BorderRadius,
                        verticalArrangement = style.cardBg2VerticalArrangement,
                        verticalAlignment = style.cardBg2VerticalAlignment,
                        horizontalAlignment = style.cardBg2HorizontalAlignment,
                        background = style.cardBg2BackgroundColor,
                        height = style.cardBg2Height
                    ) {
                    }
                }
            }
        }
    }
}

@Composable
private fun ODSViewAll(
    scheme: ODSTheme,
    style: ODSCardNotificationStackStyle,
    props: ODSCardNotificationStackProps,
    onViewAllClick: () -> Unit
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = style.viewAllVerticalArrangement,
        horizontalAlignment = style.viewAllHorizontalAlignment,
        verticalAlignment = style.viewAllVerticalAlignment,
    ) {
        ODSButton(
            scheme = scheme,
            props = props.viewAllButtonProps?.toODSButtonProps() ?: ODSButtonProps(
                rightIcon = true,
                buttonIcon = ODSIconModel(drawableRes = R.drawable.navigation_right_type_standard),
                size = ODSButtonSize.SMALL,
                buttonType = ODSButtonButtonType.STANDARD,
                variant = ODSButtonVariant.GHOST,
                label = stringResource(R.string.semantic_view_all),
            ),
            onClick = onViewAllClick
        )
    }
}

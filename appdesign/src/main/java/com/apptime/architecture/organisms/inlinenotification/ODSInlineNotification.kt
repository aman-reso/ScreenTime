package com.telekom.odsystem.organisms.inlinenotification

import android.content.Context
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.componentstyles.ODSInlineNotificationStyle
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSInlineNotification composable.
 *
 * @param modifier Modifier applied to this component.
 * @param props Visual configuration for the component.
 * @param scheme Color scheme used for theming.
 * @param onFirstLinkClicked Callback triggered when action occurs.
 * @param onSecondLinkClicked Callback triggered when action occurs.
 * @param onDismiss Callback triggered when action occurs.
 */
@Composable
fun ODSInlineNotification(
    modifier: Modifier = Modifier,
    props: ODSInlineNotificationProps = ODSInlineNotificationProps(),
    scheme: ODSTheme = neutralScheme,
    onFirstLinkClicked: (() -> Unit)? = null,
    onSecondLinkClicked: (() -> Unit)? = null,
    onDismiss: () -> Unit = { },
) {
    val style = ODSInlineNotificationStyle().getStyle(scheme = scheme, props = props)
    ODSRow(
        modifier = modifier
            .fillMaxWidth(),
        padding = style.padding,
        cornerRadius = style.borderRadius,
        verticalAlignment = style.verticalAlignment,
        background = style.backgroundColor,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSRow(
            modifier = Modifier.weight(1f),
            gap = style.notificationGap,
            padding = style.notificationPadding,
            horizontalAlignment = style.notificationHorizontalAlignment,
            verticalAlignment = style.notificationVerticalAlignment,
            horizontalArrangement = style.notificationHorizontalArrangement
        ) {
            ODSInlineTypeIcon(style = style, props = props)
            ODSInlineNotificationContentContainer(
                style = style,
                props = props,
                scheme = scheme,
                onFirstLinkClicked = onFirstLinkClicked,
                onSecondLinkClicked = onSecondLinkClicked
            )
        }
        if (props.showCloseButton) {
            ODSInlineCloseIcon(
                style = style,
                scheme = scheme,
                onDismiss = onDismiss,
                context = LocalContext.current
            )
        }
    }
}

@Composable
private fun ODSInlineCloseIcon(
    style: ODSInlineNotificationStyle,
    scheme: ODSTheme,
    onDismiss: () -> Unit,
    context: Context
) {
    ODSRow(
        padding = style.spacingPadding,
        horizontalAlignment = style.spacingHorizontalAlignment,
        verticalAlignment = style.spacingVerticalAlignment,
        horizontalArrangement = style.spacingHorizontalArrangement
    ) {
        ODSButton(
            modifier = Modifier.clearAndSetSemantics {
                contentDescription =
                    context.getString(R.string.semantic_close_button) + "," + context.getString(R.string.semantic_double_tap_to_dismiss)
            },
            scheme = style.scheme ?: scheme,
            props = ODSButtonProps(
                buttonIcon = style.closeButtonIconModel,
                buttonType = ODSButtonButtonType.ICON_ONLY,
                size = ODSButtonSize.SMALL,
                variant = ODSButtonVariant.GHOST
            ),
            onClick = onDismiss
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ODSInlineNotificationContentContainer(
    style: ODSInlineNotificationStyle,
    props: ODSInlineNotificationProps,
    scheme: ODSTheme,
    onFirstLinkClicked: (() -> Unit)?,
    onSecondLinkClicked: (() -> Unit)?
) {
    ODSColumn(
        verticalAlignment = style.contentVerticalAlignment,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalArrangement = style.contentVerticalArrangement
    ) {

        ODSColumn(
            gap = style.titleTextGap,
            padding = style.titleTextPadding,
            verticalAlignment = style.titleTextVerticalAlignment,
            horizontalAlignment = style.titleTextHorizontalAlignment,
            verticalArrangement = style.titleTextVerticalArrangement
        ) {
            if (!props.title.isNullOrEmpty()) {
                ODSText(
                    text = props.title,
                    style = style.titleTextStyle,
                    color = style.titleColor,
                    textAlign = style.titleTextAlign,
                )
            }

            if (!props.text.isNullOrEmpty()) {
                ODSText(
                    text = props.text,
                    style = style.textTextStyle,
                    color = style.textColor,
                    textAlign = style.textTextAlign
                )
            }
        }
        if (props.link1Props != null) {
            ODSWrap(
                horizontalGap = style.linksHorizontalGap,
                horizontalAlignment = style.linksHorizontalAlignment,
                verticalAlignment = style.linksVerticalAlignment,
                horizontalArrangement = style.linksHorizontalArrangement
            ) {
                ODSLink(
                    scheme = style.scheme ?: scheme,
                    props = props.link1Props ?: ODSLinkProps(),
                    onClick = onFirstLinkClicked
                )
                if (props.link2Props != null) {
                    ODSLink(
                        scheme = style.scheme ?: scheme,
                        props = props.link2Props ?: ODSLinkProps(),
                        onClick = onSecondLinkClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun ODSInlineTypeIcon(
    style: ODSInlineNotificationStyle,
    props: ODSInlineNotificationProps
) {
    val context = LocalContext.current
    when (props.mode) {
        ODSInlineNotificationMode.SUCCESS -> {
            ODSIcon(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = context.getString(R.string.semantic_success_alert)
                },
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.success_type_bold,
                    tint = style.successColor
                ),
                height = style.successHeight,
                width = style.successWidth
            )
        }

        ODSInlineNotificationMode.INFORMATIVE -> {
            ODSIcon(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = context.getString(R.string.semantic_informative_alert)
                },
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.information_type_bold,
                    tint = style.informationColor
                ),
                height = style.informationHeight,
                width = style.informationWidth
            )
        }

        ODSInlineNotificationMode.ERROR -> {
            ODSIcon(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = context.getString(R.string.semantic_error)
                },
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.error_type_bold,
                    tint = style.errorColor
                ),
                height = style.errorHeight,
                width = style.errorWidth
            )
        }

        ODSInlineNotificationMode.WARNING -> {
            ODSIcon(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = context.getString(R.string.semantic_warning)
                },
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.warning_type_bold,
                    tint = style.warningColor
                ),
                height = style.warningHeight,
                width = style.warningWidth
            )
        }
    }
}

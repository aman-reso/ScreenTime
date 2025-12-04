package com.telekom.odsystem.organisms.toast

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
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
import com.telekom.odsystem.componentstyles.ODSToastStyle
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSToast composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onFirstLinkClicked Callback triggered when action occurs.
 * @param onSecondLinkClicked Callback triggered when action occurs.
 * @param onDismiss Callback triggered when action occurs.
 */
@Composable
fun ODSToast(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSToastProps = ODSToastProps(),
    onFirstLinkClicked: (() -> Unit)? = null,
    onSecondLinkClicked: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
) {

    val style = ODSToastStyle().getStyle(scheme = scheme, props = props)
    val context = LocalContext.current
    ODSRow(
        modifier = modifier
            .sizeWithinBounds(minWidth = style.minWidth ?: 0.dp, maxWidth = style.maxWidth ?: 0.dp)
            .semantics(mergeDescendants = true) {
                this.isTraversalGroup = true
                this.contentDescription = context.getString(R.string.semantic_dialog)
            },
        padding = style.padding,
        cornerRadius = style.borderRadius,
        verticalAlignment = style.verticalAlignment,
        background = style.backgroundColor,
        effect = style.boxShadow,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSRow(
            modifier = Modifier
                .weight(1f)
                .semantics { traversalIndex = 1f },
            gap = style.notificationGap,
            padding = style.notificationPadding,
            verticalAlignment = style.notificationVerticalAlignment,
            horizontalAlignment = style.notificationHorizontalAlignment,
            horizontalArrangement = style.notificationHorizontalArrangement
        ) {
            ODSToastTypeIcon(style = style, props = props)
            ODSToastNotificationContainer(
                style = style,
                props = props,
                scheme = scheme,
                onFirstLinkClicked = onFirstLinkClicked,
                onSecondLinkClicked = onSecondLinkClicked
            )
        }

        if (props.showCloseButton) {
            ODSInlineCloseIcon(
                modifier = Modifier.semantics { traversalIndex = 0f },
                style = style,
                scheme = scheme,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun ODSInlineCloseIcon(
    modifier: Modifier,
    style: ODSToastStyle,
    scheme: ODSTheme,
    onDismiss: () -> Unit,
) {
    ODSRow(
        modifier = modifier,
        padding = style.spacingPadding,
        horizontalAlignment = style.spacingHorizontalAlignment,
        verticalAlignment = style.spacingVerticalAlignment,
        horizontalArrangement = style.spacingHorizontalArrangement
    ) {
        ODSButton(
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
private fun ODSToastNotificationContainer(
    style: ODSToastStyle,
    scheme: ODSTheme,
    props: ODSToastProps,
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
                    textAlign = style.textTextAlign,
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
private fun ODSToastTypeIcon(style: ODSToastStyle, props: ODSToastProps) {
    val context = LocalContext.current
    when (props.mode) {
        ODSToastMode.SUCCESS -> {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.success_type_bold,
                    tint = style.successColor,
                    contentDescription = context.getString(R.string.semantic_success_alert)
                ),
                height = style.successHeight,
                width = style.successWidth
            )
        }

        ODSToastMode.INFORMATIVE -> {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.information_type_bold,
                    tint = style.informationColor,
                    contentDescription = context.getString(R.string.semantic_informative_alert)
                ),
                height = style.informationHeight,
                width = style.informationWidth
            )
        }
    }
}

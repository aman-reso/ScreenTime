package com.telekom.odsystem.organisms.banner

import android.content.Context
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
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
import com.telekom.odsystem.componentstyles.ODSBannerStyle
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSBanner composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onFirstLinkClicked Callback triggered when action occurs.
 * @param onSecondLinkClicked Callback triggered when action occurs.
 * @param onDismiss Callback triggered when action occurs.
 */
@Composable
fun ODSBanner(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSBannerProps = ODSBannerProps(),
    onFirstLinkClicked: (() -> Unit)? = null,
    onSecondLinkClicked: (() -> Unit)? = null,
    onDismiss: () -> Unit = {}
) {

    val style = ODSBannerStyle().getStyle(scheme = scheme, props = props)
    ODSRow(
        modifier = modifier
            .fillMaxWidth(),
        cornerRadius = style.borderRadius,
        verticalAlignment = style.verticalAlignment,
        background = style.backgroundColor,
        effect = style.boxShadow,
        horizontalArrangement = style.horizontalArrangement
    ) {
        ODSRow(
            modifier = Modifier.widthIn(
                min = Dp.Unspecified,
                max = style.maxWidthWrapperMaxWidth ?: Dp.Unspecified
            ),
            padding = style.maxWidthWrapperPadding,
            horizontalArrangement = style.maxWidthWrapperHorizontalArrangement,
            verticalAlignment = style.maxWidthWrapperVerticalAlignment
        ) {
            ODSRow(
                modifier = Modifier.weight(1f),
                gap = style.notificationGap,
                padding = style.notificationPadding,
                horizontalAlignment = style.notificationHorizontalAlignment,
                verticalAlignment = style.notificationVerticalAlignment,
                horizontalArrangement = style.notificationHorizontalArrangement
            ) {
                ODSBannerTypeIcon(style = style, props = props)
                ODSBannerNotificationContentContainer(
                    style = style,
                    props = props,
                    scheme = scheme,
                    onFirstLinkClicked = onFirstLinkClicked,
                    onSecondLinkClicked = onSecondLinkClicked
                )
            }
            if (props.showCloseButton) {
                ODSBannerCrossIcon(
                    style = style,
                    scheme = scheme,
                    onDismiss = onDismiss,
                    context = LocalContext.current
                )
            }
        }
    }
}

@Composable
private fun ODSBannerCrossIcon(
    style: ODSBannerStyle,
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
private fun ODSBannerNotificationContentContainer(
    style: ODSBannerStyle,
    props: ODSBannerProps,
    scheme: ODSTheme,
    onFirstLinkClicked: (() -> Unit)?,
    onSecondLinkClicked: (() -> Unit)?
) {
    ODSColumn(
        padding = style.contentPadding,
        verticalAlignment = style.contentVerticalAlignment,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalArrangement = style.contentVerticalArrangement
    ) {

        ODSColumn(
            gap = style.titleTextGap,
            verticalAlignment = style.titleTextVerticalAlignment,
            horizontalAlignment = style.titleTextHorizontalAlignment,
            verticalArrangement = style.titleTextVerticalArrangement
        ) {
            if (!props.title.isNullOrEmpty()) {
                ODSText(
                    text = props.title,
                    style = style.titleTextStyle,
                    color = style.titleColor,
                    textAlign = style.titleTextAlign
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
private fun ODSBannerTypeIcon(style: ODSBannerStyle, props: ODSBannerProps) {
    val context = LocalContext.current
    when (props.mode) {
        ODSBannerMode.SUCCESS -> {
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

        ODSBannerMode.INFORMATIVE -> {
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

        ODSBannerMode.ERROR -> {
            ODSIcon(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = context.getString(R.string.semantic_error_alert)
                },
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.error_type_bold,
                    tint = style.errorColor
                ),
                height = style.errorHeight,
                width = style.errorWidth
            )
        }

        ODSBannerMode.WARNING -> {
            ODSIcon(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = context.getString(R.string.semantic_warning_alert)
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

package com.app.screentime.ntoificationstack

/** Import the package */
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.atoms.link.ODSLinkAlignment
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.link.ODSLinkType
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/** Import the required theme and scheme */

/**
 * Code generated with ODS RADD Code Generator Plugin
 * 2025-05-24 (v1.31.1) - uid: 3e4cb405
 * Figma link: https://figma.com/design/3MbZ8LOrBNBjTZX9J3t8Lu/OneApp ODS Library?node-id=657-1741
 */
/**
 * created for measuring height
 */

/**
 * Code generated with ODS RADD Code Generator
 * 2025-11-04 (v1.34.1) - uid: 5bee5665
 * Figma link: https://figma.com/design/3MbZ8LOrBNBjTZX9J3t8Lu/OneApp ODS Library?node-id=8940-4365
 */

@Composable
fun OAServiceNotificationSingle(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    colorModel: List<ODSColorModel>? = null,
    props: OAServiceNotificationSingleProps? = null,
    actionCallback: () -> Unit = {},
) {
    /** If the properties need to be modified within the UI snippet, use Mutable props */
    /** mutable props usage will trigger recomposition if modified */
    /** replace with "props: OAServiceNotificationSingleProps = OAServiceNotificationSingleProps()" if props don't need to be modified within this Composable*/
    if (props == null) return
    val style = OAServiceNotificationSingleStyle().getStyle(scheme = scheme)
    ODSBox(
        cornerRadius = style.cornerRadius,
        background = colorModel ?: style.cardBgBackground,
        modifier = modifier
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            padding = style.padding,
            cornerRadius = style.cornerRadius,
            verticalAlignment = style.verticalAlignment,
            horizontalArrangement = style.horizontalArrangement,
        ) {
            ODSRow(
                modifier = Modifier
                    .weight(1f),
                gap = style.notificationGap,
                padding = style.notificationPadding,
                horizontalAlignment = style.notificationHorizontalAlignment,
                verticalAlignment = style.notificationVerticalAlignment,
                horizontalArrangement = style.notificationHorizontalArrangement
            ) {
                if (props.iconDrawable != -1) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = props.iconDrawable),
                        tint = style.iconColor?.getColor(),
                        width = style.iconWidth,
                        height = style.iconHeight,
                        modifier = Modifier.clearAndSetSemantics { })
                }
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    padding = style.contentPadding,
                    verticalAlignment = style.contentVerticalAlignment,
                    horizontalAlignment = style.contentHorizontalAlignment,
                    verticalArrangement = style.contentVerticalArrangement
                ) {
                    if (!props.titleLabel.isNullOrEmpty()) {
                        ODSText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clearAndSetSemantics {},
                            text = props.titleLabel,
                            style = style.titleStyle,
                            color = style.titleColor,
                            textAlign = style.titleTextAlign,
                            overflow = style.titleOverflow
                        )
                    }
                    ODSLink(
                        modifier = Modifier.clearAndSetSemantics {},
                        scheme = scheme,
                        props = ODSLinkProps(
                            alignment = ODSLinkAlignment.LEFT,
                            label = props.actionText,
                            type = ODSLinkType.SECONDARY
                        ),
                        onClick = actionCallback // still functional but not announced
                    )

                }
            }

//            if (showDismissIcon) {
//                ODSRow(
//                    padding = style.spacerPadding,
//                    horizontalAlignment = style.spacerHorizontalAlignment,
//                    verticalAlignment = style.spacerVerticalAlignment,
//                    horizontalArrangement = style.spacerHorizontalArrangement
//                ) {
//                    ODSButton(
//                        scheme = scheme, modifier = Modifier.semantics {
//                            contentDescription = languageService.getTranslation(
//                                R.string.service__manage_service__landing_dismiss_icon_ax_label
//                            )
//                        }, props = ODSButtonProps(
//                            buttonIcon = ODSIconModel(drawableRes = R.drawable.close_type_bold_size_standard),
//                            buttonType = ODSButtonButtonType.ICON_ONLY,
//                            size = ODSButtonSize.SMALL,
//                            variant = ODSButtonVariant.GHOST,
//                            label = languageService.getTranslation(
//                                R.string.service__manage_service__landing_dismiss_icon_ax_label
//                            )
//                        ), onClick = onCloseCallback
//                    )
//                }
//            }
        }
    }
}


@Composable
internal fun OANotificationCardWithPaddingAsParent(
    model: ODSCardNotificationModel,
    showDismissIcon: Boolean,
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth(),
        padding = ODSPadding(horizontal = DSVariables.spacingComponent3)
    ) {
        OAServiceNotificationSingle(
            modifier = Modifier, props = model.notificationProps, colorModel = null
        )
    }
}
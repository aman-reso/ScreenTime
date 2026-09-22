package com.telekom.odsystem.molecules.listrownavigation

import ODSListRowNavigationProps
import ODSListRowNavigationVariant
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSListRowNavigation composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Suppress("complexity", "LongMethod")
@Composable
fun ODSListRowNavigation(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSListRowNavigationProps = ODSListRowNavigationProps(),
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val style =
        ODSListRowNavigationStyle().getStyle(
            scheme = scheme,
            props = props,
            state = when (true) {
                isPressed -> ODSActions.PRESSED
                isHovered -> ODSActions.HOVERED
                else -> ODSActions.DEFAULT
            }
        )
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .sizeWithinBounds(minHeight = style.minHeight ?: MIN_HEIGHT.dp)
            .customClickable(
                interactionSource = interactionSource,
                onClick = onClick,
                isPressed = { isPressed = it },
                role = Role.Button
            ),
        padding = style.padding,
        cornerRadius = style.cornerRadius,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        background = style.background,
    ) {
        ODSRow(
            gap = style.contentGap,
            horizontalArrangement = style.contentHorizontalArrangement,
            horizontalAlignment = style.contentHorizontalAlignment,
            verticalAlignment = style.contentVerticalAlignment
        ) {
            if (props.variant == ODSListRowNavigationVariant.ICON) {
                ODSRow(
                    horizontalArrangement = style.iconContainerHorizontalArrangement,
                    horizontalAlignment = style.iconContainerHorizontalAlignment,
                    verticalAlignment = style.iconContainerVerticalAlignment,
                    width = style.iconContainerWidth,
                    height = style.iconContainerHeight,
                    clipContent = style.iconContainerClipContent != false
                ) {
                    if (props.icon != null) {
                        ODSIcon(
                            iconModel = props.icon,
                            width = style.iconWidth,
                            height = style.iconHeight,
                            tint = style.iconColor?.getColor()
                        )
                    }
                }
            }
            if (props.variant == ODSListRowNavigationVariant.IMAGE) {
                ODSRow(
                    cornerRadius = style.imageCornerRadius,
                    horizontalArrangement = style.imageHorizontalArrangement,
                    horizontalAlignment = style.imageHorizontalAlignment,
                    verticalAlignment = style.imageVerticalAlignment,
                    clipContent = style.imageClipContent != false
                ) {
                    ODSImage(
                        width = style.image2Width,
                        height = style.image2Height,
                        imageModel = props.image,
                        cornerRadius = style.image2CornerRadius,
                        contentScale = style.image2ContentScale ?: ContentScale.Crop
                    )
                }
            }
            ODSRow(
                modifier = Modifier.weight(1f), // Not exported by plugin
                gap = style.textContentGap,
                padding = style.textContentPadding,
                horizontalArrangement = style.textContentHorizontalArrangement,
                horizontalAlignment = style.textContentHorizontalAlignment,
                verticalAlignment = style.textContentVerticalAlignment
            ) {
                ODSColumn(
                    modifier = Modifier.weight(1f), // Not exported by plugin
                    gap = style.labelTextContentGap,
                    verticalArrangement = style.labelTextContentVerticalArrangement,
                    verticalAlignment = style.labelTextContentVerticalAlignment,
                    horizontalAlignment = style.labelTextContentHorizontalAlignment
                ) {
                    if (!props.label.isNullOrEmpty()) {
                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = props.label,
                            style = style.labelStyle,
                            color = style.labelColor,
                            textAlign = style.labelTextAlign
                        )
                    }
                    if (!props.labelText.isNullOrEmpty()) {
                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = props.labelText,
                            style = style.labelTextStyle,
                            color = style.labelTextColor,
                            textAlign = style.labelTextTextAlign
                        )
                    }
                }
                if (props.showDescriptionTitle) {
                    ODSColumn(
                        modifier = Modifier.weight(1f), // Not exported by plugin
                        gap = style.descriptionTextContentGap,
                        verticalArrangement = style.descriptionTextContentVerticalArrangement,
                        verticalAlignment = style.descriptionTextContentVerticalAlignment,
                        horizontalAlignment = style.descriptionTextContentHorizontalAlignment
                    ) {
                        if (!props.descriptionTitle.isNullOrEmpty()) {
                            ODSText(
                                modifier = Modifier.fillMaxWidth(),
                                text = props.descriptionTitle,
                                style = style.descriptionStyle,
                                color = style.descriptionColor,
                                textAlign = style.descriptionTextAlign
                            )
                        }
                        if (!props.descriptionText.isNullOrEmpty()) {
                            ODSText(
                                modifier = Modifier.fillMaxWidth(),
                                text = props.descriptionText,
                                style = style.descriptionTextStyle,
                                color = style.descriptionTextColor,
                                textAlign = style.descriptionTextTextAlign
                            )
                        }
                    }
                }
            }
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = R.drawable.right_condensed_type_standard),
                width = style.rightCondensedWidth,
                height = style.rightCondensedHeight,
                tint = style.rightCondensedColor?.getColor()
            )
        }
    }
}

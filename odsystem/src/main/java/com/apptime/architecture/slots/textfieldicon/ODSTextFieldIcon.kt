package com.telekom.odsystem.slots.textfieldicon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-10-20 (v1.33.1) - uid: 929f428
 * Figma link: https://figma.com/design/hre4oCiCoYfStczE6GmCaA/Untitled?node-id=146-12440
 */

/**
 * A composable function that displays an icon or a button, used within a text field
 **
 * @param modifier The [Modifier] to be applied to the component.
 * @param scheme The [ODSTheme] to apply, which defines the color scheme. Defaults to [neutralScheme].
 * @param props The [ODSTextFieldIconProps] that configure the appearance and type of the icon container.
 *              This includes specifying whether it's an icon or a button, the icon resource, and button properties.
 * @param onClick An optional lambda to be executed when the component is a button and is clicked.
 */
@Composable
fun ODSTextFieldIcon(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTextFieldIconProps = ODSTextFieldIconProps(),
    onClick: (() -> Unit)? = null,
) {
    val style = ODSTextFieldIconStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        width = style.width,
        height = style.height
    ) {
        when (props.type) {
            ODSTextFieldIconType.ICON_CONTAINER -> {
                ODSIcon(
                    iconModel = props.icon,
                    tint = style.iconColor?.getColor(),
                    width = style.iconWidth,
                    height = style.iconHeight
                )
            }

            ODSTextFieldIconType.BUTTON_CONTAINER -> {
                props.buttonProps?.let {
                    ODSButton(
                        scheme = scheme,
                        props = it.toODSButtonProps(),
                        onClick = { onClick?.invoke() }
                    )
                }
            }
        }
    }
}

package com.telekom.odsystem.molecules.radiobuttongroup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButton
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSRadioButtonGroup composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSRadioButtonGroup(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSRadioButtonGroupProps = ODSRadioButtonGroupProps(),
    onClick: (Int) -> Unit
) {

    val style = ODSRadioButtonGroupStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier
            .sizeWithinBounds(minWidth = style.minWidth ?: Dp.Unspecified)
            .selectableGroup(),
        gap = style.gap,
        padding = style.padding,
        clipContent = style.clipContent != false,
        cornerRadius = style.borderRadius,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
    ) {
        if (!props.titleText.isNullOrEmpty()) {
            ODSTitleContainer(
                style = style,
                titleText = props.titleText ?: ""
            )
        }

        props.radioButtonProps?.let {
            ODSListContainer(
                radioButtonGroupRadioProps = it,
                style = style,
                scheme = scheme,
                props = props,
                onClick = onClick
            )
        }
        props.supportMessageProps?.let {
            ODSSupportMessageContainer(
                style = style,
                supportMessageProps = it,
                scheme = scheme
            )
        }
    }
}

@Composable
private fun ODSTitleContainer(style: ODSRadioButtonGroupStyle, titleText: String) {
    ODSColumn(
        gap = style.titleGap,
        padding = style.titlePadding,
        verticalArrangement = style.titleVerticalArrangement,
        verticalAlignment = style.titleVerticalAlignment,
        horizontalAlignment = style.titleHorizontalAlignment
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = titleText,
            style = style.titleTextStyle,
            color = style.titleColor,
            textAlign = style.titleTextAlign,
            overflow = style.titleTextOverflow
        )
    }
}

@Composable
private fun ODSListContainer(
    radioButtonGroupRadioProps: List<ODSRadioButtonGroupRadioButtonProps>,
    style: ODSRadioButtonGroupStyle,
    scheme: ODSTheme,
    props: ODSRadioButtonGroupProps,
    onClick: (Int) -> Unit
) {
    ODSColumn(
        gap = style.listContainerGap,
        verticalArrangement = style.listContainerVerticalArrangement,
        verticalAlignment = style.listContainerVerticalAlignment,
        horizontalAlignment = style.listContainerHorizontalAlignment
    ) {
        radioButtonGroupRadioProps.forEachIndexed { index, it ->
            ODSRadioButton(
                scheme = scheme,
                props = it.toODSRadioButtonProps(size = props.size),
                onClick = { onClick(index) }
            )
        }
    }
}

@Composable
private fun ODSSupportMessageContainer(
    style: ODSRadioButtonGroupStyle,
    supportMessageProps: ODSSupportMessageProps,
    scheme: ODSTheme
) {
    ODSColumn(
        padding = style.supportMessagePadding,
        verticalArrangement = style.supportMessageVerticalArrangement,
        verticalAlignment = style.supportMessageVerticalAlignment,
        horizontalAlignment = style.supportMessageHorizontalAlignment
    ) {
        ODSSupportMessage(
            scheme = scheme,
            props = supportMessageProps
        )
    }
}

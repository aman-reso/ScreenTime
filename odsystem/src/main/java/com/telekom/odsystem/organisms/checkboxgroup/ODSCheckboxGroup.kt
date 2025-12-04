package com.telekom.odsystem.organisms.checkboxgroup

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.checkbox.ODSCheckbox
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxProps
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSelected
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessage
import com.telekom.odsystem.atoms.supportmessage.ODSSupportMessageProps
import com.telekom.odsystem.molecules.checkboxlist.ODSCheckboxList
import com.telekom.odsystem.molecules.checkboxlist.ODSCheckboxListProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCheckboxGroup composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 * @param onListClick Callback triggered when action occurs.
 */
@Composable
fun ODSCheckboxGroup(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCheckboxGroupProps = ODSCheckboxGroupProps(),
    onClick: ((ODSCheckboxSelected) -> Unit)? = null,
    onListClick: ((ODSCheckboxSelected, Int) -> Unit)? = null
) {
    val style = ODSCheckboxGroupStyle().getStyle(scheme = scheme, props = props)
    ODSCheckboxGroupContainer(
        modifier = modifier,
        scheme = scheme,
        style = style,
        props = props,
        onClick = onClick,
        onListClick = onListClick
    )
}

@Composable
private fun ODSCheckboxGroupContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    style: ODSCheckboxGroupStyle,
    props: ODSCheckboxGroupProps = ODSCheckboxGroupProps(),
    onClick: ((ODSCheckboxSelected) -> Unit)? = null,
    onListClick: ((ODSCheckboxSelected, Int) -> Unit)? = null
) {
    ODSColumn(
        gap = style.gap,
        padding = style.padding,
        clipContent = style.clipContent != false,
        cornerRadius = style.borderRadius,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        modifier = modifier
    ) {
        if (!props.titleText.isNullOrEmpty()) {
            ODSCheckboxGroupTitleContainer(style = style, props = props)
        }
        if (props.type == ODSCheckboxGroupType.STANDARD) {
            val checkboxListProps = props.checkboxListProps?.toODSCheckboxListProps(props.size)
                ?: ODSCheckboxListProps()
            ODSCheckboxList(
                scheme = scheme,
                props = checkboxListProps,
                onClick = onListClick
            )
        }

        if (props.type == ODSCheckboxGroupType.NESTED) {
            ODSCheckboxGroupListContainer(
                scheme = scheme,
                props = props,
                style = style,
                onClick = onClick,
                onListClick = onListClick
            )
        }

        props.supportMessageProps?.let {
            ODSCheckboxGroupSupportMessageContainer(
                style = style,
                props = it,
                scheme = scheme
            )
        }
    }
}

@Composable
private fun ODSCheckboxGroupTitleContainer(
    style: ODSCheckboxGroupStyle,
    props: ODSCheckboxGroupProps
) {
    ODSColumn(
        gap = style.titleGap,
        padding = style.titlePadding,
        verticalArrangement = style.titleVerticalArrangement,
        verticalAlignment = style.titleVerticalAlignment,
        horizontalAlignment = style.titleHorizontalAlignment
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = props.titleText,
            style = style.titleTextStyle,
            color = style.titleColor,
            textAlign = style.titleTextAlign,
            overflow = style.titleTextOverflow
        )
    }
}

@Composable
private fun ODSCheckboxGroupListContainer(
    scheme: ODSTheme,
    props: ODSCheckboxGroupProps,
    style: ODSCheckboxGroupStyle,
    onClick: ((ODSCheckboxSelected) -> Unit)? = null,
    onListClick: ((ODSCheckboxSelected, Int) -> Unit)? = null
) {
    ODSColumn(
        gap = style.listContainerGap,
        padding = style.listContainerPadding,
        verticalArrangement = style.listContainerVerticalArrangement,
        verticalAlignment = style.listContainerVerticalAlignment,
        horizontalAlignment = style.listContainerHorizontalAlignment
    ) {
        val checkboxProps = props.checkboxProps?.toODSCheckboxProps(props.size) ?: ODSCheckboxProps()
        val checkboxListProps = props.checkboxListProps?.toODSCheckboxListProps(props.size) ?: ODSCheckboxListProps()

        ODSCheckbox(
            modifier = Modifier.applyNestedCheckboxSemantics(
                props = checkboxProps,
                context = LocalContext.current
            ),
            scheme = scheme,
            props = checkboxProps,
            onClick = onClick
        )
        ODSColumn(
            gap = style.secondLevelGap,
            padding = style.secondLevelPadding,
            verticalArrangement = style.secondLevelVerticalArrangement,
            verticalAlignment = style.secondLevelVerticalAlignment,
            horizontalAlignment = style.secondLevelHorizontalAlignment,
        ) {
            ODSCheckboxList(
                scheme = scheme,
                props = checkboxListProps,
                onClick = onListClick
            )
        }
    }
}

@Composable
private fun ODSCheckboxGroupSupportMessageContainer(
    style: ODSCheckboxGroupStyle,
    props: ODSSupportMessageProps,
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
            props = props
        )
    }
}

private fun Modifier.applyNestedCheckboxSemantics(
    props: ODSCheckboxProps,
    context: Context
): Modifier {
    val isEnabled = !props.disabled && !props.readOnly
    val isReadOnly = props.readOnly
    var contentDescription = context.getString(R.string.semantics_level_1)
    if (isEnabled && props.selected == ODSCheckboxSelected.INDETERMINATE) {
        contentDescription += "\n" + context.getString(R.string.semantics_not_all_options_selected)
    }
    if (isReadOnly) {
        contentDescription += "\n${context.getString(R.string.semantic_read_only)}"
    }
    return this.semantics {
        this.toggleableState = if (props.selected == ODSCheckboxSelected.INDETERMINATE) {
            ToggleableState.Indeterminate
        } else {
            ToggleableState(props.selected == ODSCheckboxSelected.SELECTED)
        }
        this.contentDescription = contentDescription
    }
}

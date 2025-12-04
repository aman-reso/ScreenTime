package com.telekom.odsystem.molecules.checkboxlist

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.checkbox.ODSCheckbox
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxProps
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSelected
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Displays a vertical list of ODS checkboxes.
 *
 * @param modifier Modifier applied to the column container.
 * @param scheme Color scheme used for styling.
 * @param props Visual configuration for the component.
 * @param onClick Callback invoked when an item is clicked.
 */
@Composable
fun ODSCheckboxList(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCheckboxListProps = ODSCheckboxListProps(),
    onClick: ((ODSCheckboxSelected, Int) -> Unit)? = null
) {
    val style = ODSCheckboxListStyle().getStyle(props = props)

    ODSColumn(
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        modifier = modifier
    ) {
        ODSColumn(
            gap = style.listContainerGap,
            verticalArrangement = style.listContainerVerticalArrangement,
            verticalAlignment = style.listContainerVerticalAlignment,
            horizontalAlignment = style.listContainerHorizontalAlignment
        ) {
            props.checkboxProps?.forEachIndexed { index, _ ->
                val checkboxProps = props.checkboxProps?.get(index)?.toODSCheckboxProps(props.size)
                    ?: ODSCheckboxProps()
                ODSCheckbox(
                    modifier = if (props.nested.not()) {
                        Modifier
                    } else {
                        Modifier.applyCheckboxSemantics(
                            props = checkboxProps,
                            context = LocalContext.current
                        )
                    },
                    scheme = scheme,
                    props = checkboxProps,
                    onClick = onClick?.let {
                        {
                            selected ->
                            it.invoke(selected, index)
                        }
                    }
                )
            }
        }
    }
}

private fun Modifier.applyCheckboxSemantics(
    props: ODSCheckboxProps,
    context: Context
): Modifier {
    val isReadOnly = props.readOnly
    var contentDescription = context.getString(R.string.semantics_level_2)
    if (isReadOnly) {
        contentDescription += "\n${context.getString(R.string.semantic_read_only)}"
    }
    return this.semantics {
        this.toggleableState = ToggleableState(props.selected == ODSCheckboxSelected.SELECTED)
        this.contentDescription = contentDescription
    }
}

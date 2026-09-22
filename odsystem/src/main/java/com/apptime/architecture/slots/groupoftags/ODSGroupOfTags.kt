package com.telekom.odsystem.slots.groupoftags

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSGroupOfTags composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ODSGroupOfTags(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSGroupOfTagsProps = ODSGroupOfTagsProps()
) {

    val style = ODSGroupOfTagsStyle().getStyle()

    ODSWrap(
        modifier = modifier.fillMaxWidth(),
        horizontalGap = style.gap,
        verticalGap = style.gap,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        props.leadingTagProps?.let {
            ODSTagStatic(scheme = scheme, props = it)
        }
        props.trailingTagProps?.let {
            ODSTagStatic(scheme = scheme, props = it)
        }
    }
}

package com.telekom.odsystem.atoms.tagstatic

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.SINGLE_LINE
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSTagStatic composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSTagStatic(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSTagStaticProps = ODSTagStaticProps()
) {

    val style = ODSTagStaticStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier.semantics(mergeDescendants = true) { /* Merge semantics for children */ },
        gap = style.gap,
        padding = style.padding,
        cornerRadius = style.borderRadius,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        background = style.backgroundColor,
    ) {
        ODSIcon(
            iconModel = props.icon,
            tint = style.iconColor?.getColor(),
            width = style.iconWidth,
            height = style.iconHeight
        )

        if (!props.label.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.weight(1f, fill = false),
                text = props.label,
                style = style.labelTextStyle,
                color = style.labelColor,
                textAlign = style.labelTextAlign,
                overflow = style.labelTextOverflow,
                maxLines = SINGLE_LINE
            )
        }
    }
}

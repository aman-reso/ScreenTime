package com.telekom.odsystem.slots.popoverpreferredactions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSPopoverPreferredActions composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onFistButtonClick Callback triggered when action occurs.
 * @param onSecondButtonClick Callback triggered when action occurs.
 * @param onFirstLinkClick Callback triggered when action occurs.
 * @param onSecondLinkClick Callback triggered when action occurs.
 */
@Composable
fun ODSPopoverPreferredActions(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSPopoverPreferredActionsProps = ODSPopoverPreferredActionsProps(),
    onFistButtonClick: () -> Unit = {},
    onSecondButtonClick: () -> Unit = {},
    onFirstLinkClick: (() -> Unit)? = null,
    onSecondLinkClick: (() -> Unit)? = null
) {

    val style = ODSPopoverPreferredActionsStyle().getStyle()

    if (props.type == ODSPopoverPreferredActionsType.TWO_BUTTONS) {
        ODSPopoverTwoButtonsType(
            modifier = modifier,
            scheme = scheme,
            style = style,
            props = props,
            onFistButtonClick = onFistButtonClick,
            onSecondButtonClick = onSecondButtonClick
        )
    } else {
        ODSPopoverTwoLinksType(
            modifier = modifier,
            scheme = scheme,
            style = style,
            props = props,
            onFirstLinkClick = onFirstLinkClick,
            onSecondLinkClick = onSecondLinkClick
        )
    }
}

@Composable
private fun ODSPopoverTwoButtonsType(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSPopoverPreferredActionsStyle,
    props: ODSPopoverPreferredActionsProps,
    onFistButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit
) {
    ODSRow(
        modifier = modifier,
        gap = style.gap,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        props.button1Props?.let { firstButtonProps ->
            ODSButton(
                scheme = scheme,
                props = firstButtonProps,
                onClick = onFistButtonClick
            )

            props.button2Props?.let { secondButtonProps ->
                ODSButton(
                    scheme = scheme,
                    props = secondButtonProps,
                    onClick = onSecondButtonClick
                )
            }
        }
    }
}

@Composable
private fun ODSPopoverTwoLinksType(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSPopoverPreferredActionsProps,
    style: ODSPopoverPreferredActionsStyle,
    onFirstLinkClick: (() -> Unit)?,
    onSecondLinkClick: (() -> Unit)?
) {
    ODSRow(
        modifier = modifier,
        gap = style.gap,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        props.link1Props?.let { firstLinkProps ->
            ODSLink(
                scheme = scheme,
                props = firstLinkProps,
                onClick = onFirstLinkClick
            )

            props.link2Props?.let { secondLinkProps ->
                ODSLink(
                    scheme = scheme,
                    props = secondLinkProps,
                    onClick = onSecondLinkClick
                )
            }
        }
    }
}

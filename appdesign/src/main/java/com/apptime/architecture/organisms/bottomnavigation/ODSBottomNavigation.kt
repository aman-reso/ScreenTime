package com.telekom.odsystem.organisms.bottomnavigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.navigationitem.ODSNavigationItem
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSBottomNavigation composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param selectedIndex Index of the selected item.
 * @param onIndexChanged Callback triggered when action occurs.
 */
@Composable
fun ODSBottomNavigation(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSBottomNavigationProps,
    selectedIndex: Int = 0,
    onIndexChanged: (index: Int) -> Unit,
) {
    val style = ODSBottomNavigationStyle().getStyle(scheme = scheme)
    ODSColumn(
        modifier = modifier.sizeWithinBounds(
            minWidth = Dp.Infinity,
            minHeight = style.minHeight ?: MIN_HEIGHT.dp
        ),
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        background = style.background
    ) {
        ODSLineTop(style = style)
        ODSActions(
            scheme = scheme,
            style = style,
            props = props,
            onIndexChanged = onIndexChanged,
            selectedIndex = selectedIndex
        )
    }
}

/**
 * ODSBottomNavigation composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onIndexChanged Callback triggered when action occurs.
 */
@Deprecated(
    "Use ODSBottomNavigation instead with selectedIndex",
    replaceWith = ReplaceWith("ODSBottomNavigation(modifier, scheme, props, selectedIndex, onIndexChanged)")
)
@Composable
fun ODSBottomNavigation(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSBottomNavigationProps,
    onIndexChanged: (index: Int) -> Unit,
) {
    val style = ODSBottomNavigationStyle().getStyle(scheme = scheme)
    ODSColumn(
        modifier = modifier.sizeWithinBounds(
            minWidth = Dp.Infinity,
            minHeight = style.minHeight ?: MIN_HEIGHT.dp
        ),
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        background = style.background
    ) {
        ODSLineTop(style = style)
        ODSActions(scheme = scheme, style = style, props = props, onIndexChanged = onIndexChanged)
    }
}

@Composable
private fun ODSLineTop(style: ODSBottomNavigationStyle) {
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        clipContent = style.lineTopClipContent ?: true,
        background = style.lineTopBackground,
        height = style.lineTopHeight
    ) {
    }
}

@Composable
private fun ODSActions(
    scheme: ODSTheme,
    style: ODSBottomNavigationStyle,
    props: ODSBottomNavigationProps,
    selectedIndex: Int,
    onIndexChanged: (index: Int) -> Unit,
) {
    ODSRow(
        horizontalArrangement = style.actionsHorizontalArrangement,
        horizontalAlignment = style.actionsHorizontalAlignment,
        verticalAlignment = style.actionsVerticalAlignment,
    ) {
        val items = props.items
        items?.forEachIndexed { index, odsNavigationItemProps ->
            ODSNavigationItem(
                scheme = scheme,
                props = odsNavigationItemProps.toODSNavigationItemProps(
                    showLabel = props.labels,
                    active = (selectedIndex == index && !odsNavigationItemProps.disabled)
                ),
                accessibilityIndex = index,
                accessibilityTotalCount = items.size,
                modifier = Modifier.weight(1f),
                onClick = {
                    onIndexChanged(index)
                }
            )
        }
    }
}

@Deprecated("Use ODSActions instead")
@Composable
private fun ODSActions(
    scheme: ODSTheme,
    style: ODSBottomNavigationStyle,
    props: ODSBottomNavigationProps,
    onIndexChanged: (index: Int) -> Unit,
) {
    ODSRow(
        horizontalArrangement = style.actionsHorizontalArrangement,
        horizontalAlignment = style.actionsHorizontalAlignment,
        verticalAlignment = style.actionsVerticalAlignment,
    ) {
        val items = props.items
        items?.forEachIndexed { index, odsNavigationItemProps ->
            ODSNavigationItem(
                scheme = scheme,
                props = odsNavigationItemProps.toODSNavigationItemProps(showLabel = false),
                accessibilityIndex = index,
                accessibilityTotalCount = items.size,
                modifier = Modifier.weight(1f),
                onClick = {
                    onIndexChanged(index)
                }
            )
        }
    }
}

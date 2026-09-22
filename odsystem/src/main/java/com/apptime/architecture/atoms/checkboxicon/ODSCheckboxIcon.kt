package com.telekom.odsystem.atoms.checkboxicon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.DEFAULT_ANIMATION_DURATION
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCheckboxIcon composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSCheckboxIcon(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCheckboxIconProps = ODSCheckboxIconProps()
) {
    val style = ODSCheckboxIconStyle().getStyle(
        scheme = scheme,
        props = props,
        state = props.state
    )

    ODSCheckboxIconContainer(
        modifier = modifier,
        style = style,
        props = props
    )
}

@Composable
private fun ODSCheckboxIconContainer(
    modifier: Modifier = Modifier,
    style: ODSCheckboxIconStyle,
    props: ODSCheckboxIconProps
) {
    val topPadding by animateDpAsState(style.padding?.top ?: 0.dp, label = "")
    val bottomPadding by animateDpAsState(style.padding?.bottom ?: 0.dp, label = "")
    val leftPadding by animateDpAsState(style.padding?.left ?: 0.dp, label = "")
    val rightPadding by animateDpAsState(style.padding?.right ?: 0.dp, label = "")
    ODSRow(
        modifier = modifier,
        padding = ODSPadding(
            top = topPadding,
            bottom = bottomPadding,
            left = leftPadding,
            right = rightPadding
        ),
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        width = style.width,
        height = style.height
    ) {
        ODSCheckBoxIconsType(
            style = style,
            props = props,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
    }
}

@Composable
private fun ODSCheckBoxIconsType(
    modifier: Modifier,
    style: ODSCheckboxIconStyle,
    props: ODSCheckboxIconProps
) {
    ODSRow(
        modifier = modifier,
        cornerRadius = style.iconCornerRadius,
        border = ODSBorder(width = style.iconBorder, colorList = style.iconBorderColor),
        horizontalArrangement = style.iconHorizontalArrangement,
        horizontalAlignment = style.iconHorizontalAlignment,
        verticalAlignment = style.iconVerticalAlignment,
        background = style.iconBackground
    ) {
        AnimatedVisibility(
            visible = props.selected != ODSCheckboxIconSelected.UNSELECTED,
            enter = scaleIn(
                initialScale = 0f,
                animationSpec = tween(DEFAULT_ANIMATION_DURATION)
            ),
            exit = scaleOut(targetScale = 0f, animationSpec = tween(DEFAULT_ANIMATION_DURATION))
        ) {
            if (props.selected == ODSCheckboxIconSelected.INDETERMINATE) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.minus_type_bold),
                    tint = style.minusColor?.getColor(),
                    width = style.minusWidth,
                    height = style.minusHeight
                )
            }
            if (props.selected == ODSCheckboxIconSelected.SELECTED) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.checkmark_type_bold),
                    tint = style.checkmarkColor?.getColor(),
                    width = style.checkmarkWidth,
                    height = style.checkmarkHeight
                )
            }
        }
    }
}

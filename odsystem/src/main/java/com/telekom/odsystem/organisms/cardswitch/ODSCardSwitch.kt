package com.telekom.odsystem.organisms.cardswitch

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIcon
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconProps
import com.telekom.odsystem.atoms.switchicon.ODSSwitchIconSize
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.extensions.invokeWith
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardSwitch composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSCardSwitch(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardSwitchProps = ODSCardSwitchProps(),
    onClick: ((Boolean) -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val state = when {
        isPressed -> ODSActions.PRESSED
        isHovered -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSCardSwitchStyle().getStyle(
        props = props,
        scheme = scheme,
        state = state
    )

    val scale by animateFloatAsState(
        targetValue = if (isHovered && !isPressed) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )

    ODSCardSwitchParentFrame(
        modifier = modifier,
        props = props,
        style = style,
        interactionSource = interactionSource,
        scheme = scheme,
        state = state,
        scale = scale,
        isPressed = { isPressed = it },
        onClick = onClick?.invokeWith { !props.selected }
    )
}

@Composable
private fun ODSCardSwitchParentFrame(
    modifier: Modifier,
    props: ODSCardSwitchProps,
    style: ODSCardSwitchStyle,
    interactionSource: MutableInteractionSource,
    scheme: ODSTheme,
    state: ODSActions,
    scale: Float,
    isPressed: (Boolean) -> Unit,
    onClick: (() -> Unit)? = null
) {
    ODSBox(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .semantics { this.toggleableState = ToggleableState(props.selected) }
            .customClickable(
                interactionSource = interactionSource,
                isPressed = isPressed,
                onClick = onClick,
                role = Role.Switch
            )
    ) {
        ODSColumn(
            modifier = Modifier
                .matchParentSize()
                .scale(scale),
            border = ODSBorder(
                width = style.cardBgBorder,
                colorList = style.cardBgBorderColor
            ),
            cornerRadius = style.cardBgBorderRadius,
            background = style.cardBgBackgroundColor,
            effect = style.boxShadow,
            horizontalAlignment = style.cardBgHorizontalAlignment,
            verticalAlignment = style.cardBgVerticalAlignment,
            verticalArrangement = style.cardBgVerticalArrangement,
            clipContent = style.cardBgClipContent != false
        ) {
        }

        ODSCardSwitchContainer(
            props = props,
            style = style,
            scheme = scheme,
            state = state
        )
    }
}

@Composable
private fun ODSCardSwitchContainer(
    props: ODSCardSwitchProps,
    style: ODSCardSwitchStyle,
    scheme: ODSTheme,
    state: ODSActions
) {
    ODSRow(
        modifier = Modifier.applySemantics(props),
        cornerRadius = style.borderRadius,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        effect = style.boxShadow
    ) {
        ODSContentContainer(
            props = props,
            style = style,
            scheme = scheme,
            state = state,
        )
    }
}

@Composable
private fun ODSContentContainer(
    props: ODSCardSwitchProps,
    style: ODSCardSwitchStyle,
    scheme: ODSTheme,
    state: ODSActions,
) {
    ODSRow(
        gap = style.contentGap,
        padding = style.contentPadding,
        horizontalArrangement = style.contentHorizontalArrangement,
        horizontalAlignment = style.contentHorizontalAlignment,
        verticalAlignment = style.contentVerticalAlignment
    ) {
        ODSCardContentContainer(
            modifier = Modifier.weight(1f),
            props = props,
            style = style,
            scheme = scheme
        )
        ODSSwitchIcon(
            scheme = scheme,
            props = ODSSwitchIconProps(
                selected = props.selected,
                size = ODSSwitchIconSize.SMALL,
                state = state
            )
        )
    }
}

@Composable
private fun ODSCardContentContainer(
    modifier: Modifier,
    props: ODSCardSwitchProps,
    style: ODSCardSwitchStyle,
    scheme: ODSTheme
) {
    ODSColumn(
        modifier = modifier,
        gap = style.cardContentGap,
        verticalAlignment = style.cardContentVerticalAlignment,
        horizontalAlignment = style.cardContentHorizontalAlignment,
        verticalArrangement = style.cardContentVerticalArrangement
    ) {
        ODSCopyContainer(
            props = props,
            style = style,
        )
        ODSTagsContainer(
            props = props,
            style = style,
            scheme = scheme
        )
    }
}

@Composable
private fun ODSCopyContainer(
    props: ODSCardSwitchProps,
    style: ODSCardSwitchStyle,
) {
    ODSColumn(
        gap = style.copyGap,
        verticalAlignment = style.copyVerticalAlignment,
        horizontalAlignment = style.copyHorizontalAlignment,
        verticalArrangement = style.copyVerticalArrangement
    ) {
        if (props.variant == ODSCardSwitchVariant.TITLE && !props.title.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.title,
                style = style.titleTextStyle,
                color = style.titleColor,
                textAlign = style.titleTextAlign
            )
        }
        if (props.variant == ODSCardSwitchVariant.BRAND && props.logo != null) {
            ODSColumn(
                height = style.logoContainerHeight,
                verticalArrangement = style.logoContainerVerticalArrangement,
                verticalAlignment = style.logoContainerVerticalAlignment,
                horizontalAlignment = style.logoContainerHorizontalAlignment,
                clipContent = style.logoContainerClipContent != false
            ) {
                ODSImage(
                    imageModel = props.logo,
                    height = style.logoContainerHeight,
                    contentScale = style.logoObjectFit ?: ContentScale.Fit
                )
            }
        }
        if (!props.subtitle.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.subtitle,
                style = style.subtitleTextStyle,
                color = style.subtitleColor,
                textAlign = style.subtitleTextAlign
            )
        }
    }
}

@Composable
private fun ODSTagsContainer(
    props: ODSCardSwitchProps,
    style: ODSCardSwitchStyle,
    scheme: ODSTheme
) {
    props.tag1Props?.let { tag1Props ->
        ODSRow(
            gap = style.tagsContainerGap,
            horizontalArrangement = style.tagsContainerHorizontalArrangement,
            horizontalAlignment = style.tagsContainerHorizontalAlignment,
            verticalAlignment = style.tagsContainerVerticalAlignment
        ) {
            ODSTagStatic(
                scheme = scheme,
                props = tag1Props
            )
            props.tag2Props?.let { tag2Props ->
                ODSTagStatic(
                    scheme = scheme,
                    props = tag2Props
                )
            }
        }
    }
}

private fun Modifier.applySemantics(
    props: ODSCardSwitchProps
): Modifier {
    var contentDescription = ""
    if (props.variant == ODSCardSwitchVariant.TITLE) {
        contentDescription += "\n ${props.title ?: ""}"
    }
    if (props.variant == ODSCardSwitchVariant.BRAND) {
        contentDescription += props.logo?.contentDescription ?: ""
    }
    props.subtitle?.let {
        contentDescription += "\n $it"
    }
    props.tag1Props?.label?.let {
        contentDescription += "\n $it"
    }
    props.tag2Props?.label?.let {
        contentDescription += "\n $it"
    }
    return clearAndSetSemantics {
        this.contentDescription = contentDescription
    }
}

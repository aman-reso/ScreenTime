package com.telekom.odsystem.organisms.cardbasic

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * ODSCardBasic composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param contentSlot Parameter for customization.
 * @param actionSlot Parameter for customization.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSCardBasic(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardBasicProps = ODSCardBasicProps(),
    contentSlot: (@Composable () -> Unit)? = null,
    actionSlot: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
    contentPadding: ODSPadding? = null
) {
    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState()
    val style = ODSCardBasicStyle().getStyle(
        scheme = scheme,
        props = props,
        state = if (pressed) ODSActions.PRESSED else if (isHovered.value) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    val scale by animateFloatAsState(
        targetValue = if (isHovered.value && !pressed) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )
    ODSColumn(
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        modifier = modifier
            .semantics(mergeDescendants = true) {
                isTraversalGroup = true
            }
            .customClickable(
                interactionSource = interactionSource,
                isPressed = { pressed = it },
                onClick = onClick,
                role = Role.Button
            )
    ) {
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = style.contentContentAlignment,
        ) {
            ODSColumn(
                cornerRadius = style.cardBgBorderRadius,
                clipContent = style.cardBgClipContent != false,
                verticalArrangement = style.cardBgVerticalArrangement,
                verticalAlignment = style.cardBgVerticalAlignment,
                horizontalAlignment = style.cardBgHorizontalAlignment,
                background = style.cardBgBackgroundColor,
                effect = style.cardBgBoxShadow,
                modifier = Modifier
                    .matchParentSize()
                    .scale(scale)
            ) { }
            ODSColumn(
                modifier = (if (props.isHorizontal) Modifier.fillMaxWidth() else Modifier),
                gap = style.contentGap,
                padding = contentPadding ?: style.contentPadding,
                verticalArrangement = style.contentVerticalArrangement,
                verticalAlignment = style.contentVerticalAlignment,
                horizontalAlignment = style.contentHorizontalAlignment,
            ) {
                contentSlot?.let {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = style.copyGap,
                        verticalAlignment = style.copyVerticalAlignment,
                        horizontalAlignment = style.copyHorizontalAlignment,
                        verticalArrangement = style.copyVerticalArrangement
                    ) {
                        it()
                    }
                }
                actionSlot?.let {
                    ODSColumn(
                        modifier = (if (props.isHorizontal) Modifier.weight(1f) else Modifier).fillMaxWidth(),
                        verticalArrangement = style.actionContainerVerticalArrangement,
                        verticalAlignment = style.actionContainerVerticalAlignment,
                        horizontalAlignment = style.actionContainerHorizontalAlignment,
                    ) {
                        it()
                    }
                }
            }
        }
    }
}

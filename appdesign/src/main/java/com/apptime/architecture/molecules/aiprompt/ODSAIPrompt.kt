package com.telekom.odsystem.molecules.aiprompt

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-22 (v1.32.2) - uid: 4863b618
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=16603-25672
 *
 */

/**
 * ODSAIPrompt is component that displays a prompt with an optional icon and title.
 * It suggests actions or provides information to the user in an AI context.
 *
 * @param modifier Modifier to be applied to the component.
 * @param scheme The ODSTheme to be used for styling the component. Defaults to neutralScheme.
 * @param props The ODSAIPromptProps to configure the component's appearance and content.
 * @param onClick A lambda function to be executed when the component is clicked. Can be null if no click action is needed.
 */
@Composable
fun ODSAIPrompt(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSAIPromptProps = ODSAIPromptProps(),
    onClick: (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val state = when {
        isPressed -> ODSActions.PRESSED
        isHovered -> ODSActions.HOVERED
        else -> ODSActions.DEFAULT
    }

    val style = ODSAIPromptStyle().getStyle(scheme = scheme, props = props, state = state)

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

    ODSRow(
        modifier = modifier
            .customClickable(
                interactionSource = interactionSource,
                role = Role.Button,
                onClick = onClick,
                isPressed = { isPressed = it }
            )
            .applyFillMaxWidthIfFillVariant(props = props)
            .width(IntrinsicSize.Max),
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        minWidth = style.minWidth,
        minHeight = style.minHeight) {

        ODSAIPromptContentContainer(
            props = props,
            style = style,
            scale = scale
        )
    }
}

@Composable
private fun ODSAIPromptContentContainer(
    props: ODSAIPromptProps,
    style: ODSAIPromptStyle,
    scale: Float
) {
    ODSBox(
        contentAlignment = style.contentZStackContentAlignment
    ) {
        ODSBox(
            modifier = Modifier
                .matchParentSize()
                .scale(scale),
            cornerRadius = style.promptBgCornerRadius,
            border = ODSBorder(width = style.promptBgBorder, colorList = style.promptBgBorderColor),
            background = style.promptBgBackground
        ) {
        }
        ODSRow(
            modifier = Modifier.applyFillMaxWidthIfFillVariant(props = props),
            gap = style.contentGap,
            padding = style.contentPadding,
            horizontalAlignment = style.contentHorizontalAlignment,
            verticalAlignment = style.contentVerticalAlignment,
            horizontalArrangement = style.contentHorizontalArrangement,
            minHeight = style.contentMinHeight
        ) {
            when (props.type) {
                ODSAIPromptType.TOP_ICON_TEXT -> ODSTopIconPlacement(
                    props = props,
                    style = style
                )

                ODSAIPromptType.LEFT_ICON_TEXT -> ODSLeftIconPlacement(
                    props = props,
                    style = style
                )

                ODSAIPromptType.TEXT_ONLY -> ODSNoneIconPlacement(
                    props = props,
                    style = style
                )
            }

            ODSIcon(
                iconModel = props.rightIcon,
                tint = style.rightIconColor?.getColor(),
                width = style.rightIconWidth,
                height = style.rightIconHeight
            )
        }
    }
}

@Composable
private fun RowScope.ODSLeftIconPlacement(
    props: ODSAIPromptProps,
    style: ODSAIPromptStyle
) {
    ODSRow(
        modifier = Modifier.weight(1f),
        gap = style.containerGap,
        verticalAlignment = style.containerVerticalAlignment,
        horizontalAlignment = style.containerHorizontalAlignment,
        horizontalArrangement = style.containerHorizontalArrangement
    ) {
        ODSContainer(
            props = props,
            style = style
        )
    }
}

@Composable
private fun RowScope.ODSTopIconPlacement(
    props: ODSAIPromptProps,
    style: ODSAIPromptStyle
) {
    ODSColumn(
        modifier = Modifier.weight(1f),
        gap = style.containerGap,
        verticalAlignment = style.containerVerticalAlignment,
        horizontalAlignment = style.containerHorizontalAlignment,
        verticalArrangement = style.containerVerticalArrangement
    ) {
        ODSContainer(
            props = props,
            style = style
        )
    }
}

@Composable
private fun RowScope.ODSNoneIconPlacement(
    props: ODSAIPromptProps,
    style: ODSAIPromptStyle
) {
    ODSColumn(
        modifier = Modifier.weight(1f),
        gap = style.containerGap,
        verticalAlignment = style.containerVerticalAlignment,
        horizontalAlignment = style.containerHorizontalAlignment,
        verticalArrangement = style.containerVerticalArrangement
    ) {
        ODSTitleOnlyContainer(
            props = props,
            style = style
        )
    }
}

@Composable
private fun ODSContainer(
    props: ODSAIPromptProps,
    style: ODSAIPromptStyle
) {
    props.icon?.let {
        ODSIcon(
            iconModel = props.icon,
            tint = style.iconColor?.getColor(),
            width = style.iconWidth,
            height = style.iconHeight
        )
    }
    ODSTitleContainer(props = props, style = style)
}

@Composable
private fun ODSTitleContainer(
    props: ODSAIPromptProps,
    style: ODSAIPromptStyle
) {
    ODSColumn(
        modifier = Modifier.applyFillMaxWidthIfFillVariant(props = props),
        gap = style.textContainerGap,
        verticalAlignment = style.textContainerVerticalAlignment,
        horizontalAlignment = style.textContainerHorizontalAlignment,
        verticalArrangement = style.textContainerVerticalArrangement
    ) {
        if (!props.title.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.applyFillMaxWidthIfFillVariant(props = props),
                text = props.title,
                style = style.titleStyle,
                color = style.titleColor,
                textAlign = style.titleTextAlign
            )
        }
        if (!props.description.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.applyFillMaxWidthIfFillVariant(props = props),
                text = props.description,
                style = style.descriptionTextStyle,
                color = style.descriptionTextColor,
                textAlign = style.descriptionTextTextAlign
            )
        }
    }
}

@Composable
private fun ODSTitleOnlyContainer(
    props: ODSAIPromptProps,
    style: ODSAIPromptStyle
) {
    if (!props.title.isNullOrEmpty()) {
        ODSText(
            modifier = Modifier.applyFillMaxWidthIfFillVariant(props = props),
            text = props.title,
            style = style.title2Style,
            color = style.title2Color,
            textAlign = style.title2TextAlign
        )
    }
    if (!props.description.isNullOrEmpty()) {
        ODSText(
            modifier = Modifier.applyFillMaxWidthIfFillVariant(props = props),
            text = props.description,
            style = style.descriptionText2Style,
            color = style.descriptionText2Color,
            textAlign = style.descriptionText2TextAlign
        )
    }
}

private fun Modifier.applyFillMaxWidthIfFillVariant(props: ODSAIPromptProps): Modifier =
    if (props.customWidth == ODSAIPromptCustomWidth.FILL) fillMaxWidth() else this

package com.telekom.odsystem.organisms.cardquickactiondeprecated

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.sparkline.ODSSparkline
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.DEFAULT_SCALE_DURATION
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSCardQuickActionDeprecated composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when action occurs.
 */
@Composable
fun ODSCardQuickActionDeprecated(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardQuickActionDeprecatedProps = ODSCardQuickActionDeprecatedProps(),
    onClick: () -> Unit,
) {
    var pressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val style = ODSCardQuickActionDeprecatedStyle().getStyle(
        props = props,
        scheme = scheme,
        state = if (pressed) ODSActions.PRESSED else if (isHovered) ODSActions.HOVERED else ODSActions.DEFAULT
    )

    val scale by animateFloatAsState(
        targetValue = if (isHovered && !pressed) {
            style.scaleFactor
                ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = tween(durationMillis = DEFAULT_SCALE_DURATION, easing = EaseInOut),
        label = ""
    )

    ODSBox(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .customClickable(
                interactionSource = interactionSource,
                isPressed = {
                    pressed = it
                },
                onClick = onClick,
                role = Role.Button
            )
    ) {
        ODSColumn(
            modifier = Modifier
                .matchParentSize()
                .scale(scale),
            cornerRadius = style.cardBgBorderRadius,
            background = style.cardBgBackgroundColor,
            effect = style.boxShadow,
            horizontalAlignment = style.cardBgHorizontalAlignment,
            verticalAlignment = style.cardBgVerticalAlignment,
            verticalArrangement = style.cardBgVerticalArrangement,
        ) { }
        ODSCardQuickActionContainer(props = props, style = style, scheme = scheme)
    }
}

@Composable
private fun ODSCardQuickActionContainer(
    props: ODSCardQuickActionDeprecatedProps,
    style: ODSCardQuickActionDeprecatedStyle,
    scheme: ODSTheme,
) {
    ODSRow(
        cornerRadius = style.borderRadius,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        ODSContentContainer(props = props, style = style, scheme = scheme)
    }
}

@Composable
private fun ODSContentContainer(
    props: ODSCardQuickActionDeprecatedProps,
    style: ODSCardQuickActionDeprecatedStyle,
    scheme: ODSTheme,
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
        ODSIcon(
            iconModel = ODSIconModel(drawableRes = R.drawable.arrow_right_type_bold),
            width = style.arrowRightWidth,
            height = style.arrowRightHeight,
            tint = style.arrowRightColor?.getColor()
        )
    }
}

@Composable
private fun ODSCardContentContainer(
    modifier: Modifier,
    props: ODSCardQuickActionDeprecatedProps,
    style: ODSCardQuickActionDeprecatedStyle,
    scheme: ODSTheme,
) {
    val context = LocalContext.current
    ODSColumn(
        modifier = modifier.applySemantics(context = context, props = props),
        gap = style.cardContentGap,
        verticalAlignment = style.cardContentVerticalAlignment,
        horizontalAlignment = style.cardContentHorizontalAlignment,
        verticalArrangement = style.cardContentVerticalArrangement
    ) {
        ODSCopyAndSparklineContainer(
            props = props,
            style = style,
            scheme = scheme
        )
        if (props.tag1Props != null || props.tag2Props != null) {
            ODSTagsContainer(
                props = props,
                style = style,
                scheme = scheme
            )
        }
    }
}

@Composable
private fun ODSCopyAndSparklineContainer(
    props: ODSCardQuickActionDeprecatedProps,
    style: ODSCardQuickActionDeprecatedStyle,
    scheme: ODSTheme,
) {
    ODSColumn(
        gap = style.copyAndSparklineGap,
        verticalAlignment = style.copyAndSparklineVerticalAlignment,
        horizontalAlignment = style.copyAndSparklineHorizontalAlignment,
        verticalArrangement = style.copyAndSparklineVerticalArrangement
    ) {
        if (props.variant == ODSCardQuickActionDeprecatedVariant.TITLE && !props.title.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.title,
                style = style.titleTextStyle,
                color = style.titleColor,
                textAlign = style.titleTextAlign
            )
        }
        if (props.variant == ODSCardQuickActionDeprecatedVariant.BRAND && props.logo != null) {
            ODSColumn(
                verticalArrangement = style.logoContainerVerticalArrangement,
                verticalAlignment = style.logoContainerVerticalAlignment,
                horizontalAlignment = style.logoContainerHorizontalAlignment,
                height = style.logoContainerHeight
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
        props.sparklineProps?.let {
            ODSSparkline(
                scheme = scheme,
                props = it
            )
        }
    }
}

@Composable
private fun ODSTagsContainer(
    props: ODSCardQuickActionDeprecatedProps,
    style: ODSCardQuickActionDeprecatedStyle,
    scheme: ODSTheme,
) {
    ODSRow(
        gap = style.tagsContainerGap,
        horizontalArrangement = style.tagsContainerHorizontalArrangement,
        horizontalAlignment = style.tagsContainerHorizontalAlignment,
        verticalAlignment = style.tagsContainerVerticalAlignment
    ) {
        props.tag1Props?.let { tag1Props ->
            ODSTagStatic(
                scheme = scheme,
                props = tag1Props
            )
        }
        props.tag2Props?.let { tag2Props ->
            ODSTagStatic(
                scheme = scheme,
                props = tag2Props
            )
        }
    }
}

private fun Modifier.applySemantics(
    context: Context,
    props: ODSCardQuickActionDeprecatedProps,
): Modifier {
    var contentDescription = ""
    if (props.variant == ODSCardQuickActionDeprecatedVariant.TITLE) {
        contentDescription += props.title ?: ""
    }
    if (props.variant == ODSCardQuickActionDeprecatedVariant.BRAND) {
        contentDescription += props.logo?.contentDescription ?: ""
    }
    props.subtitle?.let {
        contentDescription += "\n $it"
    }
    props.sparklineProps?.let {
        contentDescription += "\n ${
            context.getString(R.string.percent_progress, "${getSparkLineProgress(it.percentage)}")
        }"
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

@Suppress("MagicNumber")
private fun getSparkLineProgress(value: Float): Int {
    return when {
        value <= 1.0 -> 0
        value <= 25.0 -> 25
        value <= 50.0 -> 50
        value <= 75.0 -> 75
        else -> 100
    }
}

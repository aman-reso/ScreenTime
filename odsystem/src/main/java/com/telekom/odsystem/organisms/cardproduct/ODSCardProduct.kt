package com.telekom.odsystem.organisms.cardproduct

import androidx.compose.animation.core.animateFloatAsState
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
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.productcardcolors.ODSProductCardColors
import com.telekom.odsystem.atoms.productcardtag.ODSProductCardTag
import com.telekom.odsystem.atoms.productcardtag.ODSProductCardTagProps
import com.telekom.odsystem.foundations.DEFAULT_FACTOR
import com.telekom.odsystem.foundations.SCALE_FACTOR
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.foundations.scaleAnimationSpec
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * A composable function that displays a product card.
 *
 * @param modifier The modifier to be applied to the component.
 * @param scheme The ODSTheme to be used for styling the component.
 * @param props The ODSCardProductProps to be used for configuring the component.
 * @param actionSlot The composable function to be used for displaying the action.
 * @param contentSlot The composable function to be used for displaying the content.
 * @param featureSlot The composable function to be used for displaying the feature.
 * @param priceSlot The composable function to be used for displaying the price.
 * @param imageSlot The composable function to be used for displaying the image.
 * @param onClick The callback to be invoked when the component is clicked.
 */
@Composable
fun ODSCardProduct(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardProductProps = ODSCardProductProps(),
    actionSlot: @Composable (() -> Unit)? = null,
    contentSlot: @Composable (() -> Unit)? = null,
    featureSlot: @Composable (() -> Unit)? = null,
    priceSlot: @Composable (() -> Unit)? = null,
    imageSlot: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = { }
) {

    val style = ODSCardProductStyle().getStyle(scheme = scheme, props = props)

    if (props.size == ODSCardProductSize.SMALL_H) {
        ODSCardProductHorizontalContainer(
            modifier = modifier,
            scheme = scheme,
            style = style,
            props = props,
            contentSlot = contentSlot,
            featureSlot = featureSlot,
            priceSlot = priceSlot,
            actionSlot = actionSlot,
            imageSlot = imageSlot,
            onClick = onClick
        )
    } else {
        ODSCardProductVerticalContainer(
            modifier = modifier,
            scheme = scheme,
            style = style,
            props = props,
            contentSlot = contentSlot,
            featureSlot = featureSlot,
            priceSlot = priceSlot,
            actionSlot = actionSlot,
            imageSlot = imageSlot,
            onClick = onClick
        )
    }
}

@Composable
private fun ODSCardProductVerticalContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardProductStyle,
    props: ODSCardProductProps,
    contentSlot: @Composable (() -> Unit)?,
    featureSlot: @Composable (() -> Unit)?,
    priceSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?,
    imageSlot: @Composable (() -> Unit)?,
    onClick: () -> Unit
) {
    ODSColumn(
        modifier = modifier,
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement,
    ) {
        ODSCardContainer(
            scheme = scheme,
            props = props,
            style = style,
            imageSlot = imageSlot,
            onClick = onClick
        )
        ODSDescriptionContainer(
            style = style,
            contentSlot = contentSlot,
            featureSlot = featureSlot,
            priceSlot = priceSlot,
            actionSlot = actionSlot,
        )
    }
}

@Composable
private fun ODSCardProductHorizontalContainer(
    modifier: Modifier,
    scheme: ODSTheme,
    style: ODSCardProductStyle,
    props: ODSCardProductProps,
    contentSlot: @Composable (() -> Unit)?,
    featureSlot: @Composable (() -> Unit)?,
    priceSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?,
    imageSlot: @Composable (() -> Unit)?,
    onClick: () -> Unit
) {
    ODSRow(
        modifier = modifier,
        gap = style.gap,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
    ) {
        ODSCardContainer(
            modifier = Modifier.weight(1f),
            scheme = scheme,
            props = props,
            style = style,
            imageSlot = imageSlot,
            onClick = onClick
        )
        ODSDescriptionContainer(
            modifier = Modifier.weight(1f),
            style = style,
            contentSlot = contentSlot,
            featureSlot = featureSlot,
            priceSlot = priceSlot,
            actionSlot = actionSlot
        )
    }
}

@Composable
private fun ODSCardContainer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    props: ODSCardProductProps,
    style: ODSCardProductStyle,
    imageSlot: (@Composable () -> Unit)?,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scale by animateFloatAsState(
        targetValue = if (isHovered && !isPressed) {
            style.scaleFactor ?: SCALE_FACTOR
        } else {
            DEFAULT_FACTOR
        },
        animationSpec = scaleAnimationSpec,
        label = ""
    )
    ODSBox(
        modifier = modifier.customClickable(
            interactionSource = interactionSource,
            isPressed = { isPressed = it },
            onClick = onClick
        ),
        contentAlignment = style.cardZStackContentAlignment
    ) {
        ODSBox(
            modifier = Modifier
                .matchParentSize()
                .scale(scale),
            cornerRadius = style.cardBgCornerRadius,
            background = style.cardBgBackground
        ) { }
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = style.cardGap,
            padding = style.cardPadding,
            verticalAlignment = style.cardVerticalAlignment,
            horizontalAlignment = style.cardHorizontalAlignment,
            verticalArrangement = style.cardVerticalArrangement
        ) {
            props.productCardTagProps?.let {
                ODSTagContainer(scheme = scheme, style = style, props = it)
            }
            imageSlot?.let {
                ODSImageContainer(style = style, imageSlot = it)
            }
            ODSColourSwatchContainer(scheme = scheme, style = style, props = props)
        }
    }
}

@Composable
private fun ODSTagContainer(
    scheme: ODSTheme,
    style: ODSCardProductStyle,
    props: ODSProductCardTagProps
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        padding = style.tagContainerPadding,
        verticalAlignment = style.tagContainerVerticalAlignment,
        horizontalAlignment = style.tagContainerHorizontalAlignment,
        verticalArrangement = style.tagContainerVerticalArrangement
    ) {
        ODSProductCardTag(scheme = scheme, props = props)
    }
}

@Composable
fun ODSImageContainer(style: ODSCardProductStyle, imageSlot: @Composable () -> Unit) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = style.imageContainerHorizontalAlignment,
        verticalAlignment = style.imageContainerVerticalAlignment,
        horizontalArrangement = style.imageContainerHorizontalArrangement,
        height = style.imageContainerHeight
    ) {
        imageSlot()
    }
}

@Composable
private fun ODSColourSwatchContainer(
    scheme: ODSTheme,
    style: ODSCardProductStyle,
    props: ODSCardProductProps
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        padding = style.colourSwatchContainerPadding,
        verticalAlignment = style.colourSwatchContainerVerticalAlignment,
        horizontalAlignment = style.colourSwatchContainerHorizontalAlignment,
        verticalArrangement = style.colourSwatchContainerVerticalArrangement,
        minHeight = style.colourSwatchContainerMinHeight
    ) {
        props.productCardColorsProps?.let { colorProps ->
            val swatches = colorProps.colourSwatchProps
            if (!swatches.isNullOrEmpty() && swatches.size > 1) {
                ODSProductCardColors(scheme = scheme, props = colorProps)
            }
        }
    }
}

@Composable
private fun ODSDescriptionContainer(
    modifier: Modifier = Modifier,
    style: ODSCardProductStyle,
    contentSlot: @Composable (() -> Unit)?,
    featureSlot: @Composable (() -> Unit)?,
    priceSlot: @Composable (() -> Unit)?,
    actionSlot: @Composable (() -> Unit)?,
) {
    ODSColumn(
        modifier = modifier,
        gap = style.descriptionGap,
        padding = style.descriptionPadding,
        verticalAlignment = style.descriptionVerticalAlignment,
        horizontalAlignment = style.descriptionHorizontalAlignment,
        verticalArrangement = style.descriptionVerticalArrangement
    ) {
        contentSlot?.let {
            ODSContentSlotContainer(style = style, contentSlot = it)
        }
        featureSlot?.let {
            ODSFeatureSlotContainer(style = style, featureSlot = it)
        }
        priceSlot?.let {
            ODSPriceSlotContainer(style = style, priceSlot = it)
        }
        actionSlot?.let {
            ODSActionSlotContainer(style = style, actionSlot = it)
        }
    }
}

@Composable
private fun ODSContentSlotContainer(
    style: ODSCardProductStyle,
    contentSlot: @Composable (() -> Unit)
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = style.contentSlotContainerVerticalAlignment,
        horizontalAlignment = style.contentSlotContainerHorizontalAlignment,
        verticalArrangement = style.contentSlotContainerVerticalArrangement
    ) {
        contentSlot()
    }
}

@Composable
private fun ODSFeatureSlotContainer(
    style: ODSCardProductStyle,
    featureSlot: @Composable (() -> Unit)
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = style.featuresSlotContainerVerticalAlignment,
        horizontalAlignment = style.featuresSlotContainerHorizontalAlignment,
        verticalArrangement = style.featuresSlotContainerVerticalArrangement
    ) {
        featureSlot()
    }
}

@Composable
private fun ODSPriceSlotContainer(
    style: ODSCardProductStyle,
    priceSlot: @Composable (() -> Unit)
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = style.priceSlotContainerVerticalAlignment,
        horizontalAlignment = style.priceSlotContainerHorizontalAlignment,
        verticalArrangement = style.priceSlotContainerVerticalArrangement
    ) {
        priceSlot()
    }
}

@Composable
private fun ODSActionSlotContainer(
    style: ODSCardProductStyle,
    actionSlot: @Composable () -> Unit
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = style.actionSlotContainerHorizontalAlignment,
        verticalAlignment = style.actionSlotContainerVerticalAlignment,
        horizontalArrangement = style.actionSlotContainerHorizontalArrangement
    ) {
        actionSlot()
    }
}

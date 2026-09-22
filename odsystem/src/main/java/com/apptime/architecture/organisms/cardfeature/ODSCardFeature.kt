package com.telekom.odsystem.organisms.cardfeature

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-25 (v1.33.1) - uid: 12eefc0c
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=8864-15653
 */

/**
 * The ODSCardFeature composable is a UI component for showcasing prominent information.
 * It typically consists of an image and a content area.
 *
 * @param modifier Optional [Modifier] to be applied to the component.
 * @param scheme The [ODSTheme] to be used for styling the component. Defaults to [neutralScheme].
 * @param props The [ODSCardFeatureProps] that define the content and appearance of the card.
 * @param contentSlot An optional composable lambda that defines the content to be displayed within the card.
 * @param onClick A lambda function that is invoked when the button is clicked.
 */
@Composable
fun ODSCardFeature(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSCardFeatureProps = ODSCardFeatureProps(),
    contentSlot: (@Composable () -> Unit)? = null,
    onClick: () -> Unit,
) {
    val style = ODSCardFeatureStyle().getStyle(scheme = scheme)

    ODSCardFeatureContainer(
        style = style,
        scheme = scheme,
        props = props,
        onClick = onClick,
        contentSlot = contentSlot
    )
}

@Composable
private fun ODSCardFeatureContainer(
    modifier: Modifier = Modifier,
    style: ODSCardFeatureStyle,
    scheme: ODSTheme,
    props: ODSCardFeatureProps,
    onClick: () -> Unit,
    contentSlot: (@Composable () -> Unit)? = null,
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .semantics(mergeDescendants = true) {},
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        minWidth = style.minWidth
    ) {
        ODSImageContainer(
            style = style,
            props = props,
        )
        ODSContentContainer(
            style = style,
            scheme = scheme,
            props = props,
            onClick = onClick,
            contentSlot = contentSlot
        )
    }
}

@Composable
private fun ODSImageContainer(
    style: ODSCardFeatureStyle,
    props: ODSCardFeatureProps,
) {

    ODSBox(
        clipContent = style.imageZStackClipContent != false,
        contentAlignment = style.imageZStackContentAlignment,
        width = style.imageZStackWidth
    ) {
        ODSRow(
            modifier = Modifier
                .matchParentSize(),
            cornerRadius = style.imageBgCornerRadius,
            horizontalAlignment = style.imageBgHorizontalAlignment,
            verticalAlignment = style.imageBgVerticalAlignment,
            horizontalArrangement = style.imageBgHorizontalArrangement,
            background = style.imageBgBackground,
        ) {
        }
        ODSColumn(
            modifier = Modifier
                .fillMaxHeight(),
            cornerRadius = style.imageCornerRadius,
            clipContent = style.imageClipContent != false,
            verticalAlignment = style.imageVerticalAlignment,
            horizontalAlignment = style.imageHorizontalAlignment,
            verticalArrangement = style.imageVerticalArrangement,
            width = style.imageWidth
        ) {
            ODSImage(
                imageModel = props.image,
                height = style.image2Height,
                width = style.image2Width,
                contentScale = style.image2ContentScale ?: ContentScale.Fit
            )
        }
    }
}

@Composable
private fun RowScope.ODSContentContainer(
    style: ODSCardFeatureStyle,
    scheme: ODSTheme,
    props: ODSCardFeatureProps,
    onClick: () -> Unit,
    contentSlot: (@Composable () -> Unit)? = null,
) {
    ODSBox(
        modifier = Modifier
            .weight(1f),
        contentAlignment = style.contentZStackContentAlignment
    ) {
        ODSColumn(
            modifier = Modifier
                .matchParentSize(),
            cornerRadius = style.cardBgCornerRadius,
            verticalAlignment = style.cardBgVerticalAlignment,
            horizontalAlignment = style.cardBgHorizontalAlignment,
            verticalArrangement = style.cardBgVerticalArrangement,
            background = style.cardBgBackground,
        ) {
        }
        ODSRow(
            gap = style.contentGap,
            padding = style.contentPadding,
            horizontalAlignment = style.contentHorizontalAlignment,
            verticalAlignment = style.contentVerticalAlignment,
            horizontalArrangement = style.contentHorizontalArrangement,
            minHeight = style.contentMinHeight
        ) {
            ODSColumn(
                modifier = Modifier.weight(1f),
                gap = style.slotContainerGap,
                verticalAlignment = style.slotContainerVerticalAlignment,
                horizontalAlignment = style.slotContainerHorizontalAlignment,
                verticalArrangement = style.slotContainerVerticalArrangement
            ) {
                contentSlot?.invoke()
            }
            props.buttonProps?.let {
                ODSButton(scheme = scheme, props = it, onClick = onClick)
            }
        }
    }
}

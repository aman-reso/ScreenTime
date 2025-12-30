package com.telekom.odsystem.molecules.listrowstandard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * ODSListRowStandard composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSListRowStandard(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSListRowStandardProps = ODSListRowStandardProps()
) {
    val style = ODSListRowStandardStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .sizeWithinBounds(minHeight = style.minHeight ?: Dp.Unspecified),
        gap = style.gap,
        padding = style.padding,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment
    ) {
        if (props.variant == ODSListRowStandardVariant.IMAGE) {
            ODSRow(
                cornerRadius = style.imageCornerRadius,
                horizontalArrangement = style.imageHorizontalArrangement,
                horizontalAlignment = style.imageHorizontalAlignment,
                verticalAlignment = style.imageVerticalAlignment,
                clipContent = style.imageClipContent != false
            ) {
                ODSImage(
                    width = style.image2Width,
                    height = style.image2Height,
                    imageModel = props.image,
                    cornerRadius = style.image2CornerRadius,
                    contentScale = style.image2ContentScale ?: ContentScale.Crop
                )
            }
        }
        if (props.variant == ODSListRowStandardVariant.ICON) {
            ODSRow(
                horizontalArrangement = style.iconContainerHorizontalArrangement,
                horizontalAlignment = style.iconContainerHorizontalAlignment,
                verticalAlignment = style.iconContainerVerticalAlignment,
                width = style.iconContainerWidth,
                height = style.iconContainerHeight,
            ) {
                if (props.icon != null) {
                    ODSIcon(
                        iconModel = props.icon,
                        width = style.iconWidth,
                        height = style.iconHeight,
                        tint = style.iconColor?.getColor()
                    )
                }
            }
        }
        ODSRow(
            modifier = Modifier
                .weight(1f)
                .semantics {
                    isTraversalGroup = true
                }, // Not exported by plugin
            gap = style.textContentGap,
            horizontalArrangement = style.textContentHorizontalArrangement,
            horizontalAlignment = style.textContentHorizontalAlignment,
            verticalAlignment = style.textContentVerticalAlignment
        ) {
            val hasDescription = props.showDescriptionTitle && 
                (!props.descriptionTitle.isNullOrEmpty() || !props.descriptionText.isNullOrEmpty())
            
            ODSColumn(
                modifier = Modifier
                    .then(if (hasDescription) Modifier.weight(1f) else Modifier)
                    .semantics {
                        isTraversalGroup = true
                    }, // Not exported by plugin
                gap = style.labelTextContentGap,
                padding = style.labelTextContentPadding,
                verticalArrangement = style.labelTextContentVerticalArrangement,
                verticalAlignment = style.labelTextContentVerticalAlignment,
                horizontalAlignment = style.labelTextContentHorizontalAlignment
            ) {
                if (!props.label.isNullOrEmpty()) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = props.label,
                        style = style.labelStyle,
                        color = style.labelColor,
                        textAlign = style.labelTextAlign
                    )
                }
                if (!props.labelText.isNullOrEmpty()) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = props.labelText,
                        style = style.labelTextStyle,
                        color = style.labelTextColor,
                        textAlign = style.labelTextTextAlign
                    )
                }
            }
            if (hasDescription) {
                ODSColumn(
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            isTraversalGroup = true
                        }, // Not exported by plugin
                    gap = style.descriptionTextContentGap,
                    padding = style.descriptionTextContentPadding,
                    verticalArrangement = style.descriptionTextContentVerticalArrangement,
                    verticalAlignment = style.descriptionTextContentVerticalAlignment,
                    horizontalAlignment = style.descriptionTextContentHorizontalAlignment
                ) {
                    if (!props.descriptionTitle.isNullOrEmpty()) {
                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = props.descriptionTitle,
                            style = style.descriptionStyle,
                            color = style.descriptionColor,
                            textAlign = style.descriptionTextAlign
                        )
                    }
                    if (!props.descriptionText.isNullOrEmpty()) {
                        ODSText(
                            modifier = Modifier.fillMaxWidth(),
                            text = props.descriptionText,
                            style = style.descriptionTextStyle,
                            color = style.descriptionTextColor,
                            textAlign = style.descriptionTextTextAlign
                        )
                    }
                }
            }
        }
    }
}

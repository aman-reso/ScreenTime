package com.telekom.odsystem.atoms.thumbnail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-27 (v1.32.3) - uid: 1dc2b9ea
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=7904-10948
 */

/**
 * Displays an image or icon based on `ODSThumbnailProps`.
 *
 * @param modifier Modifier for the `ODSRow` container.
 * @param scheme ODSTheme to apply (defaults to `neutralScheme`).
 * @param props `ODSThumbnailProps` defining the visual type and its properties.
 */
@Composable
fun ODSThumbnail(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSThumbnailProps = ODSThumbnailProps()
) {

    val style = ODSThumbnailStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier,
        cornerRadius = style.cornerRadius,
        clipContent = style.clipContent != false,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        width = style.width,
        height = style.height
    ) {
        when (props.type) {
            ODSThumbnailType.IMAGE -> {
                ODSImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    imageModel = props.image,
                    contentScale = style.imageContentScale ?: ContentScale.Fit
                )
            }

            ODSThumbnailType.ICON -> {
                ODSIcon(
                    iconModel = props.icon,
                    tint = style.iconColor?.getColor(),
                    width = style.iconWidth,
                    height = style.iconHeight
                )
            }
        }
    }
}

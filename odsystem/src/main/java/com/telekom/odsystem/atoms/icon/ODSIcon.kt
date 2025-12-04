package com.telekom.odsystem.atoms.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.foundations.sizeWithinBounds

/**
 * Created by dmarinopoulos on 29/9/23
 */

/**
 * ODSIcon composable.
 *
 * Displays an icon using various image sources defined in [ODSIconModel], with support for
 * size, tinting, alignment, and content scaling customization.
 *
 * @param iconModel The model defining the source and configuration of the icon.
 * @param modifier Modifier applied to this component.
 * @param width Desired width of the icon.
 * @param height Desired height of the icon.
 * @param minWidth Parameter for customization.
 * @param minHeight Parameter for customization.
 * @param maxWidth Parameter for customization.
 * @param maxHeight Parameter for customization.
 * @param tint The tint color to apply to the icon.
 *             If [HexColor.None] is provided, no tint will be applied,
 *             and the icon will preserve its original colors.
 * @param alignment Alignment of the icon content within its bounds.
 * @param scale Parameter for customization.
 * @param opacity Parameter for customization.
 * @param rotate Parameter for customization.
 * @param aspectRatio Parameter for customization.
 * @param contentScale Defines how the icon content should be scaled within its bounds.
 */
@Suppress("LongParameterList", "LongMethod")
@Composable
fun ODSIcon(
    modifier: Modifier? = Modifier,
    iconModel: ODSIconModel?,
    width: Dp? = null,
    height: Dp? = null,
    minWidth: Dp? = null,
    minHeight: Dp? = null,
    maxWidth: Dp? = null,
    maxHeight: Dp? = null,
    tint: Color? = null,
    alignment: Alignment? = null,
    scale: Float? = null,
    opacity: Float? = null,
    rotate: Float? = null,
    aspectRatio: ODSAspectRatio? = null,
    contentScale: ContentScale? = null,
) {
    iconModel ?: return
    val imageTint = iconModel.tint?.getColor() ?: tint
    val colorFilter: ColorFilter? =
        if (imageTint != null && imageTint != HexColor.None.getColor()) ColorFilter.tint(imageTint) else null
    val contentDescription = iconModel.contentDescription
    val internalAlignment = alignment ?: Alignment.Center
    val internalContentScale = contentScale ?: ContentScale.Fit
    var internalModifier = modifier ?: Modifier

    scale?.let {
        internalModifier = internalModifier.then(Modifier.scale(it))
    }

    rotate?.let {
        internalModifier = internalModifier.then(Modifier.rotate(it))
    }

    opacity?.let {
        internalModifier = internalModifier.then(Modifier.alpha(it))
    }

    internalModifier = internalModifier.then(
        Modifier.sizeWithinBounds(
            minWidth = minWidth ?: Dp.Unspecified,
            minHeight = minHeight ?: Dp.Unspecified,
            maxWidth = maxWidth ?: Dp.Unspecified,
            maxHeight = maxHeight ?: Dp.Unspecified
        )
    )

    width?.let {
        internalModifier = internalModifier.then(Modifier.width(it))
    }

    height?.let {
        internalModifier = internalModifier.then(Modifier.height(it))
    }

    aspectRatio?.let {
        internalModifier = internalModifier.then(Modifier.aspectRatio(it.value))
    }

    if (iconModel.imageVector != null) {
        Icon(
            imageVector = iconModel.imageVector!!,
            contentDescription = contentDescription,
            modifier = internalModifier,
            tint = imageTint ?: Color.Unspecified
        )
    } else if (iconModel.drawableRes != null) {
        Image(
            painter = painterResource(id = iconModel.drawableRes!!),
            contentDescription = contentDescription,
            alignment = internalAlignment,
            contentScale = internalContentScale,
            colorFilter = colorFilter,
            modifier = internalModifier
        )
    }
}

/**
 * Represents a model for defining icon resources in the ODS design system.
 *
 * This class supports multiple types of icon representations including raster drawables,
 * vector images, and customizable tint colors. It is designed to offer flexibility
 * for UI components that display icons, while also supporting accessibility through
 * descriptive text.
 *
 * @property drawableRes A drawable resource ID for displaying a local raster image (e.g., PNG, JPG) as an icon.
 *                       Annotated with [@DrawableRes].
 * @property imageVector An [ImageVector] to be displayed, typically for vector graphics or scalable icons.
 * @property tint The tint color to apply to the icon.
 *                If [HexColor.None] is provided, no tint will be applied,
 *                and the icon will preserve its original colors.
 * @property contentDescription A textual description of the icon, used for accessibility purposes.
 */
class ODSIconModel {
    @DrawableRes
    var drawableRes: Int? = null

    var imageVector: ImageVector? = null

    var tint: HexColor? = null

    var contentDescription: String? = null

    constructor(
        drawableRes: Int,
        tint: HexColor? = null,
        contentDescription: String? = null
    ) {
        this.drawableRes = drawableRes
        this.tint = tint
        this.contentDescription = contentDescription
    }

    constructor(
        imageVector: ImageVector,
        tint: HexColor? = null,
        contentDescription: String? = null
    ) {
        this.imageVector = imageVector
        this.tint = tint
        this.contentDescription = contentDescription
    }
}

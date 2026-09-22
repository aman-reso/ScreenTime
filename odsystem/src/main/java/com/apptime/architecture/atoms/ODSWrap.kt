package com.telekom.odsystem.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.applyODSEffect
import com.telekom.odsystem.foundations.sizeWithinBounds

/**
 * Created by dmarinopoulos on 16/7/24
 */

/**
 * ODSWrap composable.
 *
 * @param modifier Modifier applied to this component.
 * @param background Parameter for customization.
 * @param effect Parameter for customization.
 * @param horizontalGap Parameter for customization.
 * @param verticalGap Parameter for customization.
 * @param padding Parameter for customization.
 * @param cornerRadius Parameter for customization.
 * @param border Parameter for customization.
 * @param horizontalArrangement Parameter for customization.
 * @param verticalArrangement Parameter for customization.
 * @param horizontalAlignment Parameter for customization.
 * @param verticalAlignment Parameter for customization.
 * @param width Parameter for customization.
 * @param height Parameter for customization.
 * @param minWidth Parameter for customization.
 * @param minHeight Parameter for customization.
 * @param maxWidth Parameter for customization.
 * @param maxHeight Parameter for customization.
 * @param scale Parameter for customization.
 * @param opacity Parameter for customization.
 * @param rotate Parameter for customization.
 * @param aspectRatio Parameter for customization.
 * @param clipContent Parameter for customization.
 * @param maxItemsInEachRow Parameter for customization.
 * @param maxLines Parameter for customization.
 * @param content Parameter for customization.
 */
@Suppress("LongParameterList", "LongMethod")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ODSWrap(
    modifier: Modifier? = Modifier,
    background: List<ODSColorModel>? = null,
    effect: ODSEffect? = null,
    horizontalGap: Dp? = null,
    verticalGap: Dp? = null,
    padding: ODSPadding? = null,
    cornerRadius: ODSCorners? = null,
    border: ODSBorder? = null,
    horizontalArrangement: Arrangement.Horizontal? = null,
    verticalArrangement: Arrangement.Vertical? = null,
    horizontalAlignment: Alignment.Horizontal? = null,
    verticalAlignment: Alignment.Vertical? = null,
    width: Dp? = null,
    height: Dp? = null,
    minWidth: Dp? = null,
    minHeight: Dp? = null,
    maxWidth: Dp? = null,
    maxHeight: Dp? = null,
    scale: Float? = null,
    opacity: Float? = null,
    rotate: Float? = null,
    aspectRatio: ODSAspectRatio? = null,
    clipContent: Boolean = false,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    content: @Composable RowScope.() -> Unit
) {
    var internalModifier = modifier ?: Modifier
    val internalShape = cornerRadius?.getRoundedCornerShape() ?: RoundedCornerShape(0.dp)
    val internalHorizontalGap = horizontalGap ?: 0.dp
    val internalVerticalGap = verticalGap ?: 0.dp
    val internalVerticalArrangement = verticalArrangement ?: Arrangement.Top
    val internalHorizontalArrangement = horizontalArrangement ?: Arrangement.Start
    val internalVerticalAlignment = verticalAlignment ?: Alignment.Top
    val internalHorizontalAlignment = horizontalAlignment ?: Alignment.Start

    scale?.let {
        internalModifier = internalModifier.then(Modifier.scale(it))
    }

    rotate?.let {
        internalModifier = internalModifier.then(Modifier.rotate(it))
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

    effect?.let {
        internalModifier = internalModifier.applyODSEffect(
            effect = it.copy(
                elevations = it.elevations.map { elevation ->
                    elevation.copy(
                        color = HexColor(
                            elevation.color.getHexColor(),
                            elevation.color.alpha * (opacity ?: 1f)
                        )
                    )
                }
            ),
            corners = cornerRadius,
            borderWidth = border?.width
        )
    }

    opacity?.let {
        internalModifier = internalModifier.then(Modifier.alpha(it))
    }

    internalModifier = internalModifier.then(
        Modifier.background(background, internalShape)
    )

    internalModifier = internalModifier.then(
        Modifier.border(
            width = border?.width ?: 0.dp,
            colorList = border?.colorList,
            shape = internalShape
        )
    )

    if (clipContent) {
        internalModifier = internalModifier.then(
            Modifier.clip(internalShape)
        )
    }

    internalModifier = internalModifier.then(
        Modifier.padding(padding?.getPaddingValues() ?: PaddingValues())
    )

    FlowRow(
        modifier = internalModifier,
        horizontalArrangement = if (internalHorizontalGap > 0.dp) {
            Arrangement.spacedBy(
                space = internalHorizontalGap,
                alignment = internalHorizontalAlignment
            )
        } else {
            internalHorizontalArrangement
        },
        verticalArrangement = if (internalVerticalGap > 0.dp) {
            Arrangement.spacedBy(
                space = internalVerticalGap,
                alignment = internalVerticalAlignment
            )
        } else {
            internalVerticalArrangement
        },
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines,
    ) {
        content()
    }
}

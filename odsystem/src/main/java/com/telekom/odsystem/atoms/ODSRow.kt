package com.telekom.odsystem.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.applyODSEffect
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme

/**
 * ODSRow composable.
 *
 * @param modifier Modifier applied to this component.
 * @param background Parameter for customization.
 * @param effect Parameter for customization.
 * @param gap Parameter for customization.
 * @param padding Parameter for customization.
 * @param cornerRadius Parameter for customization.
 * @param border Parameter for customization.
 * @param horizontalArrangement Parameter for customization.
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
 * @param content Parameter for customization.
 */
@Suppress("LongParameterList", "LongMethod")
@Composable
fun ODSRow(
    modifier: Modifier? = Modifier,
    background: List<ODSColorModel>? = null,
    effect: ODSEffect? = null,
    gap: Dp? = null,
    padding: ODSPadding? = null,
    cornerRadius: ODSCorners? = null,
    border: ODSBorder? = null,
    horizontalArrangement: Arrangement.Horizontal? = null,
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
    content: @Composable RowScope.() -> Unit
) {
    var internalModifier = modifier ?: Modifier
    val internalShape = cornerRadius?.getRoundedCornerShape() ?: RoundedCornerShape(0.dp)
    val internalGap = gap ?: 0.dp
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

    Row(
        modifier = internalModifier,
        horizontalArrangement = if (internalGap > 0.dp) {
            Arrangement.spacedBy(
                space = internalGap,
                alignment = internalHorizontalAlignment
            )
        } else {
            internalHorizontalArrangement
        },
        verticalAlignment = internalVerticalAlignment
    ) {
        content()
    }
}

// ODSRow Preview
@Preview
@Composable
fun ODSRowPreview() {
    ODSRow(
        gap = DSVariables.spacingComponent2,
        background = listOf(ODSColorModel(hexColor = neutralScheme.functionalWarningStandard)),
        cornerRadius = ODSCorners(DSVariables.radiusSmall),
        border = ODSBorder(
            width = 4.dp,
            colorList = listOf(ODSColorModel(neutralScheme.functionalWarningStandard))
        ),
        padding = ODSPadding(all = DSVariables.spacingComponent2)
    ) {
        Text(
            text = "ODSRow",
            style = DSTextStyles.bodyMRegular.toTextStyle(),
            modifier = Modifier.padding(DSVariables.spacingComponent2)
        )
        Text(
            text = "ODSRow2",
            style = DSTextStyles.bodyMRegular.toTextStyle(),
            modifier = Modifier.padding(DSVariables.spacingComponent2)
        )

        Text(
            text = "ODSRow3",
            style = DSTextStyles.bodyMRegular.toTextStyle(),
            modifier = Modifier.padding(DSVariables.spacingComponent2)
        )
    }
}

@Preview
@Composable
fun ODSRowPreview2() {
    ODSColumn(
        gap = 16.dp,
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        background = listOf(ODSColorModel(hexColor = HexColor("#010101")))
    ) {
        ODSRow(
            gap = 10.dp,
            padding = ODSPadding(all = 56.dp),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.Top,
            background = listOf(ODSColorModel(hexColor = HexColor("#C630D3"))),
            border = ODSBorder(width = 8.dp, colorList = listOf(ODSColorModel(HexColor("#28E125"))))
        ) {
            Text(
                text = "ODSRow",
                style = DSTextStyles.bodyMRegular.toTextStyle(),
                modifier = Modifier.padding(DSVariables.spacingComponent2)
            )
            Text(
                text = "ODSRow2",
                style = DSTextStyles.bodyMRegular.toTextStyle(),
                modifier = Modifier.padding(DSVariables.spacingComponent2)
            )
        }
    }
}

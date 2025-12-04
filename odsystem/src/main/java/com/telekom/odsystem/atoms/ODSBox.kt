package com.telekom.odsystem.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
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
 * ODSBox composable.
 *
 * @param modifier Modifier applied to this component.
 * @param background Parameter for customization.
 * @param effect Parameter for customization.
 * @param padding Parameter for customization.
 * @param cornerRadius Parameter for customization.
 * @param border Parameter for customization.
 * @param contentAlignment Parameter for customization.
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
fun ODSBox(
    modifier: Modifier? = Modifier,
    background: List<ODSColorModel>? = null,
    effect: ODSEffect? = null,
    padding: ODSPadding? = null,
    cornerRadius: ODSCorners? = null,
    border: ODSBorder? = null,
    contentAlignment: Alignment? = null,
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
    content: @Composable BoxScope.() -> Unit
) {
    var internalModifier = modifier ?: Modifier
    val internalShape = cornerRadius?.getRoundedCornerShape() ?: RoundedCornerShape(0.dp)
    val internalContentAlignment = contentAlignment ?: Alignment.TopStart

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

    Box(
        modifier = internalModifier,
        contentAlignment = internalContentAlignment
    ) {
        content()
    }
}

// ODSBox Preview
@Composable
@Preview
fun ODSBoxPreview() {
    ODSBox(
        modifier = Modifier
            .height(200.dp),
        background = listOf(ODSColorModel(hexColor = neutralScheme.functionalWarningStandard)),
        padding = ODSPadding(top = 16.dp, bottom = 16.dp, left = 16.dp, right = 16.dp),
        cornerRadius = ODSCorners(
            topLeft = 16.dp,
            topRight = 16.dp,
            bottomLeft = 16.dp,
            bottomRight = 16.dp
        ),
        border = ODSBorder(
            width = 2.dp,
            colorList = listOf(ODSColorModel(neutralScheme.basicBackground))
        ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "ODSBox",
            style = DSTextStyles.bodyMRegular.toTextStyle(),
            modifier = Modifier.padding(16.dp)
        )
    }
}

package com.telekom.odsystem.atoms

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.tokens.toTextStyleIgnoreScale

/**
 * Created by dmarinopoulos on 1/3/24
 */

/**
 * A customizable text component for displaying styled text.
 *
 * This composable function provides extensive flexibility in terms of text styling and layout,
 * making it suitable for diverse UI design requirements.
 *
 * @param modifier Optional [Modifier] for this text, providing layout information, padding, etc. Default is [Modifier].
 * @param text The content string to be displayed. It supports plain text. Default is null, indicating no text.
 * @param style Custom styling options such as font size, weight, and typeface encapsulated within an [ODSTextStyle]. Default is null.
 * @param color Custom color for the text, specified using [HexColor], which allows for precise color definitions beyond standard colors. Default is null.
 * @param textAlign Alignment of the text within its container, specified by [TextAlign]. Can control horizontal alignment like left, right, or center. Default is null.
 * @param textDecoration Text decoration to apply, such as underline or strikethrough, defined by [TextDecoration]. Default is null.
 * @param overflow How text should handle overflow, such as ellipsis or clipping, specified by [TextOverflow]. Default is null.
 * @param softWrap Boolean flag that indicates whether the text should wrap at soft line breaks (true) or single-line (false). Default is null.
 * @param maxLines The maximum number of lines for the text to span. If text exceeds this, it will be truncated according to the overflow rule. Default is null.
 * @param minLines The minimum number of lines for the text to span, which can affect the height of the text container. Default is null.
 * @param width Optional fixed width for the text component, specified in [Dp]. Default is null, allowing the component to size based on its content or other constraints.
 * @param height Optional fixed height for the text component, specified in [Dp]. Default is null, allowing the component to size based on its content or other constraints.
 * @param minWidth Optional minimum width constraint for the text component, specified in [Dp]. Default is null.
 * @param minHeight Optional minimum height constraint for the text component, specified in [Dp]. Default is null.
 * @param maxWidth Optional maximum width constraint for the text component, specified in [Dp]. Default is null.
 * @param maxHeight Optional maximum height constraint for the text component, specified in [Dp]. Default is null.
 * @param scale Optional scale factor to apply to the text component, affecting its size. A value of 1.0f means no scaling. Default is null.
 * @param opacity Optional opacity level for the text component. Values range from 0.0f (fully transparent) to 1.0f (fully opaque). Default is null.
 * @param rotate Optional rotation angle in degrees to apply to the text component. Positive values rotate clockwise. Default is null.
 * @param onTextLayout A callback that is triggered when a new text layout is computed. It provides a [TextLayoutResult], allowing for custom handling of the layout. Default is null.
 */

@Suppress("LongParameterList", "LongMethod")
@Composable
/**
 * ODSText composable.
 *
 * @param modifier Modifier applied to this component.
 * @param text Parameter for customization.
 * @param style Parameter for customization.
 * @param color Parameter for customization.
 * @param textAlign Parameter for customization.
 * @param textDecoration Parameter for customization.
 * @param overflow Parameter for customization.
 * @param softWrap Parameter for customization.
 * @param maxLines Parameter for customization.
 * @param minLines Parameter for customization.
 * @param onTextLayout Callback triggered when action occurs.
 */
fun ODSText(
    modifier: Modifier? = Modifier,
    text: String? = null,
    style: ODSTextStyle? = null,
    color: HexColor? = null,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow? = null,
    softWrap: Boolean? = null,
    maxLines: Int? = null,
    minLines: Int? = null,
    width: Dp? = null,
    height: Dp? = null,
    minWidth: Dp? = null,
    minHeight: Dp? = null,
    maxWidth: Dp? = null,
    maxHeight: Dp? = null,
    scale: Float? = null,
    opacity: Float? = null,
    rotate: Float? = null,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null
) {
    var internalModifier = modifier ?: Modifier
    val internalTextStyle = style?.toTextStyleIgnoreScale() ?: TextStyle()
    val internalTextColor = color?.getColor() ?: LocalContentColor.current
    val internalOverflow = overflow ?: TextOverflow.Clip
    val internalSoftWrap = softWrap != false

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

    Text(
        modifier = internalModifier,
        text = text ?: "",
        style = internalTextStyle.copy(color = internalTextColor),
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = internalOverflow,
        softWrap = internalSoftWrap,
        maxLines = maxLines ?: Int.MAX_VALUE,
        minLines = minLines ?: 1,
        onTextLayout = onTextLayout
    )
}

/**
 * A customizable text component for displaying styled text.
 *
 * This composable function provides extensive flexibility in terms of text styling and layout,
 * making it suitable for diverse UI design requirements.
 *
 * @param modifier The modifier to apply to this text component. Default is [Modifier].
 * @param text The text to be displayed. Can be an [AnnotatedString] that allows you to use annotated text for displaying styled text. Default is null.
 * @param style Custom text styles to apply. Could be font size, weight, etc. Default is null.
 * @param color The color to apply to the text. Use [HexColor] for custom color definitions. Default is null.
 * @param textAlign How to align the text horizontally. Default is null.
 * @param textDecoration Decoration (like underline, strikethrough) to apply to text. Default is null.
 * @param overflow Configuration for text overflow. How text should behave when it exceeds the space allocated to it. Default is null.
 * @param softWrap Whether the text should break at soft line breaks. If false, the glyphs in the text will be positioned as if there was unlimited horizontal space. Default is null.
 * @param maxLines The maximum number of lines to show in the text. Text exceeding this will be truncated. Default is null.
 * @param minLines The minimum number of lines to show in the text. This can affect the height of the text. Default is null.
 * @param width Optional fixed width for the text component, specified in [Dp]. Default is null, allowing the component to size based on its content or other constraints.
 * @param height Optional fixed height for the text component, specified in [Dp]. Default is null, allowing the component to size based on its content or other constraints.
 * @param minWidth Optional minimum width constraint for the text component, specified in [Dp]. Default is null.
 * @param minHeight Optional minimum height constraint for the text component, specified in [Dp]. Default is null.
 * @param maxWidth Optional maximum width constraint for the text component, specified in [Dp]. Default is null.
 * @param maxHeight Optional maximum height constraint for the text component, specified in [Dp]. Default is null.
 * @param scale Optional scale factor to apply to the text component, affecting its size. A value of 1.0f means no scaling. Default is null.
 * @param opacity Optional opacity level for the text component. Values range from 0.0f (fully transparent) to 1.0f (fully opaque). Default is null.
 * @param rotate Optional rotation angle in degrees to apply to the text component. Positive values rotate clockwise. Default is null.
 * @param onTextLayout A callback that is triggered when a new text layout is computed. It provides a [TextLayoutResult], allowing for custom handling of the layout. Default is null.
 * Example of use:
 * ODSText(text = buildAnnotatedString {
 *             withStyle(style = DSTextStyles.bodyMBold.toTextStyle().toSpanStyle()) {
 *                 append("Jetpack Compose ")
 *             }
 *
 *             withStyle(style = DSTextStyles.bodyMRegular.toTextStyle().toSpanStyle()) {
 *                 append("is awesome")
 *             }
 *
 *             withStyle(style = DSTextStyles.bodyL.toTextStyle().copy(color = scheme.basicAccent.getColor()).toSpanStyle()) {
 *                 append("!")
 *             }
 *         })
 */

@Suppress("LongParameterList", "LongMethod")
@Composable
fun ODSText(
    modifier: Modifier? = Modifier,
    text: AnnotatedString? = null,
    style: ODSTextStyle? = null,
    color: HexColor? = null,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
    overflow: TextOverflow? = null,
    softWrap: Boolean? = null,
    maxLines: Int? = null,
    minLines: Int? = null,
    width: Dp? = null,
    height: Dp? = null,
    minWidth: Dp? = null,
    maxWidth: Dp? = null,
    minHeight: Dp? = null,
    maxHeight: Dp? = null,
    scale: Float? = null,
    opacity: Float? = null,
    rotate: Float? = null,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null
) {
    var internalModifier = modifier ?: Modifier
    val internalTextStyle = style?.toTextStyle() ?: TextStyle()
    val internalTextColor = color?.getColor() ?: LocalContentColor.current
    val internalOverflow = overflow ?: TextOverflow.Clip
    val internalSoftWrap = softWrap != false
    val emptyAnnotatedString = AnnotatedString("")

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

    Text(
        text = text ?: emptyAnnotatedString,
        modifier = internalModifier,
        style = internalTextStyle,
        color = internalTextColor,
        textAlign = textAlign,
        textDecoration = textDecoration,
        overflow = internalOverflow,
        softWrap = internalSoftWrap,
        maxLines = maxLines ?: Int.MAX_VALUE,
        minLines = minLines ?: 1,
        onTextLayout = onTextLayout ?: {}
    )
}

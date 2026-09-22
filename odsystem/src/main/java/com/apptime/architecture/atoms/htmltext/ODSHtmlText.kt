package com.telekom.odsystem.atoms.htmltext

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BulletSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * A customizable text component for displaying styled text.
 ** @param modifier Optional [Modifier] for this text, providing layout information, padding, etc. Default is [Modifier].
 ** @param htmlText The content string to be displayed. It supports plain text. Default is null, indicating no text.
 * @param scheme The theme is used to style the component. Default is neutralScheme.
 * @param onLinkClick Callback function to handle link clicks. Returns the attributes of the <a> tag. Default is an empty lambda.
 */

@Composable
fun ODSHtmlText(
    modifier: Modifier = Modifier,
    htmlText: String? = null,
    scheme: ODSTheme = neutralScheme,
    style: ODSTextStyle? = null,
    onLinkClick: (Map<String, String>) -> Unit = { }
) {
    val density = LocalDensity.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                style?.applyTextStyle(this)
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { textView ->
            if (htmlText != null) {
                textView.setHtmlText(
                    density = density,
                    scheme = scheme,
                    htmlText = htmlText,
                    onLinkClick = onLinkClick
                )
            }
        }
    )
}

private fun TextView.setHtmlText(
    density: Density,
    scheme: ODSTheme,
    htmlText: String,
    onLinkClick: (Map<String, String>) -> Unit,
) {
    apply {
        val spanned = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)
        val spannable = SpannableStringBuilder(spanned)
        styleText(
            density = density,
            spannable = spannable,
            scheme = scheme,
            htmlText = htmlText,
            onLinkClick = onLinkClick
        )
        text = spannable
    }
}

private fun styleText(
    density: Density,
    spannable: SpannableStringBuilder,
    scheme: ODSTheme,
    htmlText: String,
    onLinkClick: (Map<String, String>) -> Unit,
) {
    spannable.setSpan(
        ForegroundColorSpan(scheme.basicText.getColor().toArgb()),
        0,
        spannable.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    styleBulletSpans(
        density = density,
        spannable = spannable,
        scheme = scheme,
    )
    styleAnchorTags(
        spannable = spannable,
        htmlText = htmlText,
        scheme = scheme,
        onLinkClick = onLinkClick
    )
}

private fun styleBulletSpans(
    density: Density,
    spannable: SpannableStringBuilder,
    scheme: ODSTheme,
) {
    val bulletSpacing =
        with(density) { DSVariables.spacingComponent3.roundToPx() }
    spannable.getSpans(0, spannable.length, BulletSpan::class.java).forEach { span ->
        val start = spannable.getSpanStart(span)
        val end = spannable.getSpanEnd(span)
        val flags = spannable.getSpanFlags(span)
        spannable.removeSpan(span)
        spannable.setSpan(
            BulletSpan(bulletSpacing, scheme.basicText.getColor().toArgb()),
            start,
            end,
            flags
        )
    }
}

private fun styleAnchorTags(
    spannable: SpannableStringBuilder,
    htmlText: String,
    scheme: ODSTheme,
    onLinkClick: (Map<String, String>) -> Unit
) {
    val anchorRegex = """<a\s+([^>]*)>(.*?)</a>""".toRegex()
    anchorRegex.findAll(htmlText).forEach { match ->
        val attributesString = match.groupValues[1]
        val anchorText = match.groupValues[2]
        val attributes = parseAttributes(attributesString)
        val start = spannable.indexOf(anchorText)
        if (start >= 0) {
            val end = start + anchorText.length
            spannable.getSpans(start, end, URLSpan::class.java).forEach { spannable.removeSpan(it) }
            spannable.setSpan(
                createClickableSpan(
                    scheme = scheme,
                    onClick = {
                        onLinkClick(attributes)
                    }
                ),
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}

private fun parseAttributes(attributesString: String): Map<String, String> {
    val attributes = mutableMapOf<String, String>()
    val attrRegex = """(\w+)=["'](.*?)["']""".toRegex()
    attrRegex.findAll(attributesString).forEach { attrMatch ->
        attributes[attrMatch.groupValues[1]] = attrMatch.groupValues[2]
    }
    return attributes
}

private fun createClickableSpan(
    scheme: ODSTheme,
    onClick: () -> Unit
): ClickableSpan {
    return object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = scheme.basicTextLink.getColor().toArgb()
            ds.isUnderlineText = true
        }
    }
}
